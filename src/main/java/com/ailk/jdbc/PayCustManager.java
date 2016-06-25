package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayCust;

/**
 * 客户支付管理
 * 
 * @author xugq
 * 
 */
public class PayCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayCustManager.class);
	private static PayCustManager instance = new PayCustManager();

	/**
	 * 获取客户支付管理对象
	 * 
	 * @return 客户支付管理对象
	 */
	public static PayCustManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private PayCustManager() {
	}

	/**
	 * 从数据库查询指定用户的支付记录
	 * 
	 * @param custId
	 *            客户ID
	 * @param maxResults
	 *            最大返回记录数
	 * @return 返回查询结果
	 */
	public List<PayCust> get(long custId, int maxResults) {
		List<PayCust> result = new ArrayList<PayCust>();

		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM PayCust WHERE custId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", custId).iterate();
		while (iter.hasNext()) {
			PayCust entity = (PayCust) iter.next();
			PayCust item = entity.clone();

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
	public void save(PayCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
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
	public void update(PayCust entity) {
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
	public void delete(PayCust entity) {
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
				String sql = "alter table pay_cust AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_cust_cust_id on pay_cust(cust_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("成功修改表：pay_cust");
	}

}
