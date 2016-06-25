package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayUser;

/**
 * 用户支付管理
 * 
 * @author xugq
 * 
 */
public class PayUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayUserManager.class);
	private static PayUserManager instance = new PayUserManager();

	/**
	 * 返回用户支付管理对象
	 * 
	 * @return 用户支付管理对象
	 */
	public static PayUserManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private PayUserManager() {
	}

	/**
	 * 从数据库查询指定用户的支付记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param maxResults
	 *            最大返回记录数
	 * @return 返回查询结果
	 */
	public List<PayUser> get(long userId, int maxResults) {
		List<PayUser> result = new ArrayList<PayUser>();

		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM PayUser WHERE userId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", userId).iterate();
		while (iter.hasNext()) {
			PayUser entity = (PayUser) iter.next();
			PayUser item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}

	/**
	 * 保存支付记录
	 * 
	 * @param entity
	 *            待保存的支付请求
	 */
	public void save(PayUser entity) {
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
	 * 更新支付记录
	 * 
	 * @param entity
	 *            待更新的支付记录
	 */
	public void update(PayUser entity) {
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
	 * 删除支付记录
	 * 
	 * @param entity
	 *            待删除的支付记录
	 */
	public void delete(PayUser entity) {
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
				String sql = "alter table pay_user AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_user_user_id on pay_user(user_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：pay_user");
	}

}
