package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserAd;
import com.ailk.jdbc.entity.UserAdPK;

/**
 * 用户的广告ID列表
 * 
 * @author xugq
 * 
 */
public class UserAdManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserAdManager.class);
	private static UserAdManager instance = new UserAdManager();

	/**
	 * 获取用户的广告ID列表管理对象实例
	 * 
	 * @return
	 */
	public static UserAdManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private UserAdManager() {
	}

	/**
	 * 获取用户广告信息
	 * 
	 * @param userAdPK
	 *            用户广告信息键
	 * @return
	 */
	public UserAd get(UserAdPK userAdPK) {
		int partition = HibernateUtil.getPartition(userAdPK.getUserId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (UserAd) session.get(UserAd.class, userAdPK);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户广告信息
	 * 
	 * @param entity
	 *            用户广告信息对象
	 */
	public void save(UserAd entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdPK().getUserId());
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
	 * 更新用户广告信息
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(UserAd entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdPK().getUserId());
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
	 * 删除用户广告信息
	 * 
	 * @param entity
	 *            用户广告信息对象
	 */
	public void delete(UserAd entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdPK().getUserId());
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

	@Override
	public void alterTable() {
		logger.info("成功修改表：user_ad");
	}

}
