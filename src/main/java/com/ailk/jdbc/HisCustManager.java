package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.HisCust;

/**
 * 客户历史管理
 * 
 * @author xugq
 * 
 */
public class HisCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(HisCustManager.class);
	private static HisCustManager instance = new HisCustManager();

	/**
	 * 获取操作HisCust表的实例
	 * 
	 * @return 操作HisCust表的实例
	 */
	public static HisCustManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private HisCustManager() {
	}

	/**
	 * 根据变更流水获取客户历史信息
	 * 
	 * @param id
	 *            变更流水
	 * @return 客户历史信息
	 */
	public HisCust get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		return (HisCust) session.get(HisCust.class, id);
	}

	/**
	 * 保存客户信息到数据库
	 * 
	 * @param entity
	 *            客户信息信息对象
	 */
	public void save(HisCust entity) {
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
	 * 从数据库删除客户变更信息
	 * 
	 * @param entity
	 *            客户变更对象
	 */
	public void delete(HisCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
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
		logger.info("成功修改表：his_cust");
	}

}
