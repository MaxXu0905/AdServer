package com.ailk.schedule;

import java.sql.Timestamp;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.AdBalanceManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.UserStatusManager;
import com.ailk.jdbc.entity.UserAdStatus;
import com.ailk.jdbc.entity.UserStatus;

/**
 * 检查是否有锁定记录超时，每个一定时间执行一次，需要在每个节点都部署
 * 
 * @author xugq
 * 
 */
public class CheckLockExpire {

	private static final Logger logger = Logger.getLogger(CheckLockExpire.class);
	private static CheckLockExpire instance = new CheckLockExpire();

	public static CheckLockExpire getInstance() {
		return instance;
	}

	public void run() {
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		UserStatusManager userStatusManager = UserStatusManager.getInstance();
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();

		try {
			Iterator<?> iter = session.createQuery("FROM UserAdStatus WHERE expireDate <= ?1")
					.setTimestamp("1", new Timestamp(System.currentTimeMillis())).iterate();
			while (iter.hasNext()) {
				UserAdStatus userAdStatus = (UserAdStatus) iter.next();

				if (userAdStatus.getLockAmount() <= 0)
					continue;

				Transaction t = session.beginTransaction();

				try {
					UserStatus userStatus = userStatusManager.get(session,
							userAdStatus.getUserAdStatusPK().getUserId(), LockOptions.UPGRADE);
					if (userStatus == null)
						continue;

					// 锁定余额
					if (!adBalanceManager.unlock(userAdStatus.getUserAdStatusPK().getAdId(),
							userAdStatus.getLockAmount())) {
						logger.error("解锁失败，adId=" + userAdStatus.getUserAdStatusPK().getAdId() + ", amount="
								+ userAdStatus.getLockAmount());
					}

					// 记录锁定情况
					userAdStatus.setLockAmount(0);
					userAdStatus.setExpireDate(null);
					userAdStatus.setLastUpdate(new Timestamp(System.currentTimeMillis()));
					session.update(userAdStatus);

					t.commit();
				} catch (Exception e) {
					logger.error("系统异常，" + e);
				} finally {
					if (t.isActive())
						t.rollback();
				}
			}
			
			logger.info("检查超时锁定记录完毕");
		} finally {
			session.close();
		}
	}

}
