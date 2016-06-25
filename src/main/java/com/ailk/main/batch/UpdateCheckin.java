package com.ailk.main.batch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.api.impl.AuditManager;
import com.ailk.common.ConstVariables;
import com.ailk.common.GlobalVariables;
import com.ailk.common.ServerProperties;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.LogCheckin;
import com.ailk.jdbc.entity.UserStatus;

/**
 * 更新用户签到，该进程需要在每个节点执行，并且需在每晚12点后执行
 * <p>
 * 以下情况获取一个海贝：
 * <p>
 * 每连续10天签到可兑换一个海贝
 * 
 * @author xugq
 * 
 */
public class UpdateCheckin {

	public static void main(String[] args) {
		try {
			UpdateCheckin instance = new UpdateCheckin();

			instance.loadUser();
			instance.loadLogCheckin();
			instance.loadAudit();
			instance.updateUserStatus();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
		}
	}

	private final static Logger logger = Logger.getLogger(UpdateCheckin.class);

	private long operDate;
	private List<Long> users = new ArrayList<Long>();
	private Map<Long, LogCheckin> logCheckinMap = new HashMap<Long, LogCheckin>();
	private Set<Long> auditSet = new HashSet<Long>();

	private UpdateCheckin() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		operDate = calendar.getTimeInMillis();
	}

	/**
	 * 加载用户
	 */
	private void loadUser() {
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			long nextDate = operDate + GlobalVariables.MILLIS_PER_DAY;
			Iterator<?> iter = session.createQuery("FROM InfoUser").iterate();
			while (iter.hasNext()) {
				InfoUser infoUser = (InfoUser) iter.next();

				if (infoUser.getRegDate().getTime() >= nextDate)
					continue;

				users.add(infoUser.getUserId());
			}
		} finally {
			session.close();
		}
	}

	/**
	 * 加载用户签到日志
	 */
	private void loadLogCheckin() {
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			Iterator<?> iter = session.createQuery("FROM LogCheckin").iterate();
			while (iter.hasNext()) {
				LogCheckin logCheckin = (LogCheckin) iter.next();

				logCheckinMap.put(logCheckin.getUserId(), logCheckin);
			}
		} finally {
			session.close();
		}
	}

	/**
	 * 获取日志
	 * 
	 * @throws IOException
	 */
	private void loadAudit() throws IOException {
		Properties properties = ServerProperties.getInstance().getProperties();
		String auditDir = properties.getProperty(AuditManager.AUDIT_DIR);
		if (!auditDir.endsWith("/"))
			auditDir += "/";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(new Date(operDate - GlobalVariables.MILLIS_PER_DAY));

		byte[] bytes = new byte[8];
		String filename = auditDir + "audit/" + dateStr;
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		while (true) {
			if (is.read(bytes, 0, 8) != 8)
				break;
			long userId = AuditManager.parseLong(bytes);

			if (is.skip(8) != 8)
				break;

			if (is.read(bytes, 0, 4) != 4)
				break;
			int len = AuditManager.parseInt(bytes);

			if (is.skip(len) != len)
				break;

			auditSet.add(userId);
		}
	}

	/**
	 * 更新用户状态
	 */
	private void updateUserStatus() {
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			for (long userId : users) {
				Transaction t = session.beginTransaction();

				try {
					LogCheckin logCheckin = logCheckinMap.get(userId);
					if (logCheckin == null) {
						// 新发展的用户
						logCheckin = new LogCheckin();

						logCheckin.setUserId(userId);
						logCheckin.setLastUpdate(operDate);
						session.save(logCheckin);
					} else {
						// 如果今天已更新，则不需操作
						if (logCheckin.getLastUpdate() == operDate)
							continue;

						logCheckin.setLastUpdate(operDate);
						session.update(logCheckin);
					}

					UserStatus userStatus = (UserStatus) session.get(UserStatus.class, userId);
					if (userStatus == null) {
						logger.error("找不到用户状态，" + userId);
						continue;
					}

					// 首先锁定用户，保证同一用户的请求不会同时处理
					session.buildLockRequest(LockOptions.UPGRADE).lock(userStatus);

					if (!auditSet.contains(userId)) {
						// 没有请求
						userStatus.setCheckin(0);
					} else {
						// 有请求
						userStatus.setCheckin(userStatus.getCheckin() + 1);

						if ((userStatus.getCheckin() % ConstVariables.CHECKINS_PER_LEVEL) == 0) {
							// 如果达到了数量，则更新等级
							InfoUser infoUser = (InfoUser) session.get(InfoUser.class, userId);
							if (infoUser == null) {
								logger.error("找不到用户信息");
								continue;
							}

							infoUser.setLevel(infoUser.getLevel() + 1);
							session.update(infoUser);
						}
					}

					// 更新用户状态
					session.update(userStatus);

					t.commit();
				} catch (Exception e) {
					// 忽略异常
				} finally {
					if (t.isActive())
						t.rollback();
				}
			}
		} finally {
			session.close();
		}
	}

}
