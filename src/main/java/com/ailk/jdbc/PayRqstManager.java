package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayRqst;

/**
 * 支付请求管理，可基于status查询，也可基于userId查询，因此需要创建相应的索引
 * 
 * @author xugq
 * 
 */
public class PayRqstManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayRqstManager.class);
	private static PayRqstManager instance = new PayRqstManager();

	/**
	 * 返回支付请求实例
	 * 
	 * @return 支付请求实例
	 */
	public static PayRqstManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private PayRqstManager() {
	}
	
	public PayRqst get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (PayRqst) session.get(PayRqst.class, id);
		} finally {
			session.close();
		}
	}

	/**
	 * 从数据库查询指定状态的支付请求
	 * 
	 * @param status
	 *            支付请求状态
	 * @param maxResults
	 *            最大返回记录数
	 * @return 返回查询结果
	 */
	public List<PayRqst> get(short status, int maxResults) {
		List<PayRqst> result = new ArrayList<PayRqst>();

		for (int partition = 0; partition < HibernateUtil.getPartitions(); ++partition) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Query query = session.createQuery("FROM PayRqst WHERE status = ?1");
			if (maxResults > 0)
				query.setMaxResults(maxResults);

			Iterator<?> iter = query.setShort("1", status).iterate();
			while (iter.hasNext()) {
				PayRqst entity = (PayRqst) iter.next();
				PayRqst item = entity.clone();

				result.add(item);
				--maxResults;
			}

			session.close();

			if (maxResults <= 0)
				break;
		}

		return result;
	}

	/**
	 * 从数据库查询指定用户的支付请求
	 * 
	 * @param userId
	 *            用户ID
	 * @param maxId
	 *            最大ID
	 * @param maxResults
	 *            最大返回记录数
	 * @return 返回查询结果
	 */
	public List<PayRqst> get(long userId, long maxId, int maxResults) {
		List<PayRqst> result = new ArrayList<PayRqst>();

		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query;
		if (maxId >= 0) {
			query = session.createQuery("FROM PayRqst WHERE userId = ?1 and id <= ?2 ORDER BY id desc")
					.setLong("1", userId)
					.setLong("2", maxId);
		} else {
			query = session.createQuery("FROM PayRqst WHERE userId = ?1 ORDER BY id desc")
					.setLong("1", userId);
		}
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.iterate();
		while (iter.hasNext()) {
			PayRqst entity = (PayRqst) iter.next();
			PayRqst item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}

	/**
	 * 保存支付请求
	 * 
	 * @param entity
	 *            待保存的支付请求
	 */
	public void save(PayRqst entity) {
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

	/**
	 * 更新支付请求
	 * 
	 * @param entity
	 *            待更新的支付请求
	 */
	public void update(PayRqst entity) {
		int partition = HibernateUtil.getPartition(entity.getId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 删除支付请求
	 * 
	 * @param entity
	 *            待删除的支付请求
	 */
	public void delete(PayRqst entity) {
		int partition = HibernateUtil.getPartition(entity.getId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.delete(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table pay_rqst AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_rqst_status on pay_rqst(status)";
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_rqst_user_id_id on pay_rqst(user_id, id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：pay_rqst");
	}

}
