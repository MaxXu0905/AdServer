package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.NameUser;
import com.ailk.jdbc.entity.NameUserPK;

/**
 * 用户名与用户ID映射管理
 * 
 * @author xugq
 * 
 */
public class NameUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(NameUserManager.class);
	private static NameUserManager instance = new NameUserManager();

	/**
	 * 获取操作用户名映射表的实例
	 * 
	 * @return 操作用户名映射表的实例
	 */
	public static NameUserManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private NameUserManager() {
	}

	/**
	 * 获取用户名映射
	 * 
	 * @param userName
	 *            用户名
	 * @param registered
	 *            是否注册
	 * @return 映射结果
	 */
	public NameUser get(String userName, boolean registered) {
		NameUserPK nameUserPK = new NameUserPK();
		nameUserPK.setUserName(userName);
		nameUserPK.setRegistered(registered);

		return get(nameUserPK);
	}

	/**
	 * 获取用户名映射
	 * 
	 * @param nameUserPK
	 *            用户名映射键
	 * @return 映射结果
	 */
	public NameUser get(NameUserPK nameUserPK) {
		int partition = HibernateUtil.getPartition(nameUserPK.hashCode());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (NameUser) session.get(NameUser.class, nameUserPK);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户名映射
	 * 
	 * @param entity
	 *            用户名映射对象
	 */
	public void save(NameUser entity) {
		int partition = HibernateUtil.getPartition(entity.getNameUserPK().hashCode());
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
	 * 更新用户名映射
	 * 
	 * @param entity
	 *            用户名映射对象
	 */
	public void update(NameUser entity) {
		int partition = HibernateUtil.getPartition(entity.getNameUserPK().hashCode());
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
	 * 删除用户名映射
	 * 
	 * @param entity
	 *            用户名映射对象
	 */
	public void delete(NameUser entity) {
		int partition = HibernateUtil.getPartition(entity.getNameUserPK().hashCode());
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
		logger.info("成功修改表：name_user");
	}

}
