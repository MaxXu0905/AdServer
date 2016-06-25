package com.ailk.jdbc;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoCust;

/**
 * 客户信息表管理
 * @author xugq
 *
 */
public class InfoCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoCustManager.class);
	private static InfoCustManager instance = new InfoCustManager();
	
	/**
	 * 获取操作InfoCust表的实例
	 * 
	 * @return 操作InfoCust表的实例
	 */
	public static InfoCustManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private InfoCustManager() {
	}
	
	/**
	 * 根据客户ID获取客户信息
	 * @param custId 客户ID
	 * @return 客户信息
	 */
	public InfoCust get(long custId) {
		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		return (InfoCust) session.get(InfoCust.class, custId);
	}
	
	/**
	 * 根据客户邮箱获取客户信息
	 * @param name 客户邮箱/电话
	 * @return 客户信息
	 */
	public InfoCust get(String name) {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); ++partition) {
			Session session = HibernateUtil.getSessionFactory(partition)
					.openSession();
			Iterator<?> iter = session.createQuery("FROM InfoCust WHERE email = ?1 or phoneNo = ?2")
					.setString("1", name)
					.setString("2", name)
					.iterate();
			if (iter.hasNext()) {
				InfoCust infoCust = (InfoCust) iter.next();
				return infoCust.clone();
			}
		}
		
		return null;
	}

	/**
	 * 保存客户信息到数据库
	 * 
	 * @param entity
	 *            客户信息对象
	 */
	public void save(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * 更新客户信息到数据库
	 * 
	 * @param entity
	 *            客户信息对象
	 */
	public void update(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * 从数据库删除客户信息
	 * 
	 * @param entity
	 *            客户信息对象
	 */
	public void delete(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
				String sql = "alter table info_cust AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create unique index idx_info_cust_email on info_cust(email)";
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create unique index idx_info_cust_phone_no on info_cust(phone_no)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("成功修改表：info_cust");
	}

}
