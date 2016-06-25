package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoUserExt;

/**
 * 用户信息扩展表管理
 * 
 * @author xugq
 * 
 */
public class InfoUserExtManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoUserExtManager.class);
	private static InfoUserExtManager instance = new InfoUserExtManager();

	/**
	 * 获取操作用户信息扩展表的实例
	 * 
	 * @return 操作用户信息扩展表表的实例
	 */
	public static InfoUserExtManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private InfoUserExtManager() {
	}

	/**
	 * 保存用户扩展信息到数据库
	 * 
	 * @param entity
	 *            用户信息对象
	 */
	public void save(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
	 * 更新用户扩展信息到数据库
	 * 
	 * @param entity
	 *            用户信息对象
	 */
	public void update(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
	 * 删除用户扩展信息
	 * 
	 * @param entity
	 *            用户扩展信息对象
	 */
	public void delete(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
		logger.info("成功修改表：info_user_ext");
	}
	
}
