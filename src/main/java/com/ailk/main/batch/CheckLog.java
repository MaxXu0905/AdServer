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
 * �û���Ϊ��־���ý�����Ҫ��ÿ���ڵ�ִ�У���������ÿ��12���ִ��
 * 
 * <p>
 * ���������ȡһ��������
 * <p>
 * ÿ�鿴50�β�ͬ�������飨�����������󻬡���Ƶ�鿴��������ǻ�鿴���飩
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
			logger.error("ϵͳ�쳣��" + e);
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
	 * ��ȡ��־
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

						// �����¼
						session.save(userUrl);
						reviews++;
						break;
					}
				}

				if (reviews > 0) {
					// ����ͳ����
					UserStatus userStatus = (UserStatus) session.get(UserStatus.class, userId);
					if (userStatus == null) {
						logger.error("�Ҳ����û�״̬��¼");
						return;
					}

					// ���������û�����֤ͬһ�û������󲻻�ͬʱ����
					session.buildLockRequest(LockOptions.UPGRADE).lock(userStatus);

					int level = (userStatus.getReviews() + reviews) / ConstVariables.REVIEWS_PER_LEVEL
							- userStatus.getReviews() / ConstVariables.REVIEWS_PER_LEVEL;
					userStatus.setReviews(userStatus.getReviews() + reviews);
					session.update(userStatus);

					// ����ﵽ������������µȼ�
					if (level > 0) {
						InfoUser infoUser = (InfoUser) session.get(InfoUser.class, userId);
						if (infoUser == null) {
							logger.error("�Ҳ����û���Ϣ");
							return;
						}

						infoUser.setLevel(infoUser.getLevel() + level);
						session.update(infoUser);
					}
				}

				// �ύ����
				t.commit();
			} catch (Exception e) {
				logger.error("ϵͳ�쳣��" + e);
			} finally {
				if (t.isActive())
					t.rollback();
				session.close();
			}
		}
	}

}
