package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.api.UserIfc.Asset;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.LogPromotion;
import com.ailk.jdbc.entity.UserPush;
import com.ailk.jdbc.entity.UserStatus;
import com.ailk.open.PushManager;

/**
 * 活动日志管理，记录问题答案
 * 
 * @author xugq
 * 
 */
public class LogPromotionManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogPromotionManager.class);
	private static LogPromotionManager instance = new LogPromotionManager();

	/**
	 * 获取操作LogPromotion表的实例
	 * 
	 * @return 操作LogPromotion表的实例
	 */
	public static LogPromotionManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private LogPromotionManager() {
	}

	/**
	 * 保存用户活动记录到数据库
	 * 
	 * @param entity
	 *            用户活动记录
	 */
	public void save(LogPromotion entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	public LogPromotion get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			return (LogPromotion) session.get(LogPromotion.class, id);
		} finally {
			session.close();
		}
	}

	/**
	 * 根据用户Id获取待审核活动列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户活动列表
	 */
	public List<LogPromotion> getByUserId(long userId) {
		List<LogPromotion> result = new ArrayList<LogPromotion>();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM LogPromotion WHERE userId = ?1 AND status = "
				+ ConstVariables.STATUS_PENDING);

		try {
			Iterator<?> iter = query.setLong("1", userId).iterate();
			while (iter.hasNext()) {
				LogPromotion entity = (LogPromotion) iter.next();
				LogPromotion item = entity.clone();

				result.add(item);
			}
		} finally {
			session.close();
		}

		return result;
	}

	/**
	 * 获取用户的活动列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param maxId
	 *            最大ID
	 * @param status
	 *            状态
	 * @maxResults 最大返回记录数
	 * @return 用户活动列表
	 */
	public List<LogPromotion> get(long userId, long maxId, short status, int maxResults) {
		List<LogPromotion> result = new ArrayList<LogPromotion>();
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query;

		if (maxId >= 0) {
			query = session
					.createQuery("FROM LogPromotion WHERE userId = ?1 AND status = ?2 AND id <= ?3 ORDER BY id desc")
					.setLong("1", userId).setShort("2", status).setLong("3", maxId);
		} else {
			query = session.createQuery("FROM LogPromotion WHERE userId = ?1 AND status = ?2 ORDER BY id desc")
					.setLong("1", userId).setShort("2", status);
		}

		try {
			Iterator<?> iter = query.setMaxResults(maxResults).iterate();
			while (iter.hasNext()) {
				LogPromotion entity = (LogPromotion) iter.next();
				LogPromotion item = entity.clone();

				result.add(item);
			}
		} finally {
			session.close();
		}

		return result;
	}

	/**
	 * 更新活动状态，并发通知给用户
	 * 
	 * @param globalManager
	 *            全局管理对象
	 * @param logPromotion
	 *            活动记录
	 */
	public void update(GlobalManager globalManager, LogPromotion logPromotion) {
		UserPushManager userPushManager = UserPushManager.getInstance();
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
		int partition = HibernateUtil.getPartition(logPromotion.getUserId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			// 提交变更
			session.update(logPromotion);

			// 查找设备号
			UserStatus userStatus = (UserStatus) session.get(UserStatus.class, logPromotion.getUserId(),
					LockOptions.UPGRADE);
			if (userStatus == null) {
				t.commit();
				return;
			}

			if (logPromotion.getStatus() == ConstVariables.STATUS_PASS) {
				userStatus.setPromotionProfit(userStatus.getPromotionProfit() + logPromotion.getProfit());
				session.update(userStatus);
			} else {
				// 把预扣的钱退回
				adBalanceManager.charge(logPromotion.getAdId(), -logPromotion.getProfit(), 0);
			}

			t.commit();

			String bdUserId = userStatus.getBdUserId();
			if (bdUserId == null) {
				// 查找用户的推送记录
				if (userStatus.getDevId() == null)
					return;

				UserPush userPush = userPushManager.get(userStatus.getDevId());
				if (userPush == null)
					return;

				bdUserId = userPush.getBdUserId();
			}

			// 推送准备
			PushManager pushManager = PushManager.getInstance();
			PushManager.StatusChangeResponse response = new PushManager.StatusChangeResponse();
			response.setId(logPromotion.getId());
			response.setUserId(logPromotion.getUserId());
			response.setAdId(logPromotion.getAdId());
			response.setStatus(logPromotion.getStatus());
			response.setProfit(logPromotion.getProfit());
			response.setReason(logPromotion.getReason());
			response.setLastUpdate(logPromotion.getLastUpdate().getTime());
			response.setAsset(new Asset(userStatus));

			InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
			InfoAdValue infoAdValue = infoAdManager.get(logPromotion.getAdId());
			if (infoAdValue != null)
				response.setTitle(infoAdValue.getAdName());

			// 推送通知
			pushManager.sendMessage(bdUserId, response);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table log_promotion AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_log_promotion_status_user_id on log_promotion(status, user_id)";
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_log_promotion_u_ser_id_id on log_promotion(user_id, id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：log_promotion");
	}

}
