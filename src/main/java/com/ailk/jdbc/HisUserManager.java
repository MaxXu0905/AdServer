package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.HisUser;

/**
 * 用户历史管理
 * 
 * @author xugq
 * 
 */
public class HisUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(HisUserManager.class);
	private static HisUserManager instance = new HisUserManager();

	/**
	 * 获取操作HisUser表的实例
	 * 
	 * @return 操作HisUser表的实例
	 */
	public static HisUserManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private HisUserManager() {
	}

	/**
	 * 根据变更流水获取用户历史信息
	 * 
	 * @param id
	 *            变更流水
	 * @return 用户历史信息
	 */
	public HisUser get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		return (HisUser) session.get(HisUser.class, id);
	}

	/**
	 * 保存用户信息到数据库
	 * 
	 * @param entity
	 *            用户信息信息对象
	 */
	public void save(HisUser entity) {
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
	 * 从数据库删除用户变更信息
	 * 
	 * @param entity
	 *            用户变更对象
	 */
	public void delete(HisUser entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
