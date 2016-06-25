package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserUrl;
import com.ailk.jdbc.entity.UserUrlPK;

/**
 * 用户的查看广告详情的URL管理
 * 
 * @author xugq
 * 
 */
public class UserUrlManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserUrlManager.class);
	private static UserUrlManager instance = new UserUrlManager();

	/**
	 * 获取用户URL管理对象实例
	 * 
	 * @return
	 */
	public static UserUrlManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private UserUrlManager() {
	}

	/**
	 * 获取用户URL信息
	 * 
	 * @param userAdPK
	 *            用户URL信息键
	 * @return
	 */
	public UserUrl get(UserUrlPK userAdPK) {
		int partition = HibernateUtil.getPartition(userAdPK.getUserId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (UserUrl) session.get(UserUrl.class, userAdPK);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户URL信息
	 * 
	 * @param entity
	 *            用户URL信息对象
	 */
	public void save(UserUrl entity) {
		int partition = HibernateUtil.getPartition(entity.getUserUrlPK().getUserId());
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
	 * 更新用户URL信息
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(UserUrl entity) {
		int partition = HibernateUtil.getPartition(entity.getUserUrlPK().getUserId());
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
	 * 删除用户URL信息
	 * 
	 * @param entity
	 *            用户URL对象
	 */
	public void delete(UserUrl entity) {
		int partition = HibernateUtil.getPartition(entity.getUserUrlPK().getUserId());
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
		logger.info("成功修改表：user_url");
	}

}
