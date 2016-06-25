package com.ailk.main.batch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.api.UserIfc;
import com.ailk.api.impl.AuditManager;
import com.ailk.common.ConstVariables;
import com.ailk.common.GlobalVariables;
import com.ailk.common.ServerProperties;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.UserStatus;
import com.ailk.jdbc.entity.UserUrl;
import com.ailk.jdbc.entity.UserUrlPK;
import com.google.gson.Gson;

/**
 * 用户行为日志，该进程需要在每个节点执行，并且需在每晚12点后执行
 * 
 * <p>
 * 以下情况获取一个海贝：
 * <p>
 * 每查看50次不同广告的详情（不论是锁屏左滑、视频查看详情或者是活动查看详情）
 * 
 * @author xugq
 * 
 */
public class CheckLog {

	public static void main(String[] args) {
		try {
			CheckLog instance = new CheckLog();

			instance.loadSyslog();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
		}
	}

	private final static Logger logger = Logger.getLogger(CheckLog.class);

	private long operDate;
	private Gson gson;

	private CheckLog() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		operDate = calendar.getTimeInMillis();
		gson = new Gson();
	}

	/**
	 * 获取日志
	 * 
	 * @throws IOException
	 */
	private void loadSyslog() throws IOException {
		Properties properties = ServerProperties.getInstance().getProperties();
		String auditDir = properties.getProperty(AuditManager.AUDIT_DIR);
		if (!auditDir.endsWith("/"))
			auditDir += "/";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(new Date(operDate - GlobalVariables.MILLIS_PER_DAY));

		byte[] bytes = new byte[8];
		int bytesLen = 8;
		String filename = auditDir + "syslog/" + dateStr;
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

			if (bytesLen < len) {
				bytesLen = len;
				bytes = new byte[bytesLen];
			}
			if (is.read(bytes, 0, len) != len)
				break;

			UserIfc.BehaviorsRequest request = gson.fromJson(bytes.toString(), UserIfc.BehaviorsRequest.class);
			if (request.getItems() == null)
				continue;

			int reviews = 0;
			int partition = HibernateUtil.getPartition(userId);
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Transaction t = session.beginTransaction();

			try {
				for (UserIfc.Behavior behavior : request.getItems()) {
					switch (behavior.getBehavior()) {
					case ConstVariables.BEHAVIOR_REVIEW:
						UserUrlPK userUrlPK = new UserUrlPK();
						userUrlPK.setUserId(userId);
						userUrlPK.setUrl(behavior.getContent());

						UserUrl userUrl = (UserUrl) session.get(UserUrlPK.class, userUrlPK);
						if (userUrl != null)
							continue;

						// 保存记录
						session.save(userUrl);
						reviews++;
						break;
					}
				}

				if (reviews > 0) {
					// 更新统计数
					UserStatus userStatus = (UserStatus) session.get(UserStatus.class, userId);
					if (userStatus == null) {
						logger.error("找不到用户状态记录");
						return;
					}

					// 首先锁定用户，保证同一用户的请求不会同时处理
					session.buildLockRequest(LockOptions.UPGRADE).lock(userStatus);

					int level = (userStatus.getReviews() + reviews) / ConstVariables.REVIEWS_PER_LEVEL
							- userStatus.getReviews() / ConstVariables.REVIEWS_PER_LEVEL;
					userStatus.setReviews(userStatus.getReviews() + reviews);
					session.update(userStatus);

					// 如果达到了数量，则更新等级
					if (level > 0) {
						InfoUser infoUser = (InfoUser) session.get(InfoUser.class, userId);
						if (infoUser == null) {
							logger.error("找不到用户信息");
							return;
						}

						infoUser.setLevel(infoUser.getLevel() + level);
						session.update(infoUser);
					}
				}

				// 提交事务
				t.commit();
			} catch (Exception e) {
				logger.error("系统异常，" + e);
			} finally {
				if (t.isActive())
					t.rollback();
				session.close();
			}
		}
	}

}
