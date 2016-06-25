package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.LogBehavior;

/**
 * 用户行为管理
 * 
 * @author xugq
 * 
 */
public class LogBehaviorManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogBehaviorManager.class);
	private static LogBehaviorManager instance = new LogBehaviorManager();

	/**
	 * 获取操作LogBehavior表的实例，该实例为进程级单例
	 * 
	 * @return 操作LogBehavior表的实例
	 */
	public static LogBehaviorManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private LogBehaviorManager() {
	}

	/**
	 * 保存用户行为记录到数据库
	 * 
	 * @param entity
	 *            用户行为记录
	 */
	public void save(LogBehavior entity) {
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
	 * 保存用户行为记录列表到数据库，必须保证所有用户ID是一致的
	 * 
	 * @param entities
	 *            用户行为记录列表
	 */
	public void save(List<LogBehavior> entities) {
		if (entities.isEmpty())
			return;

		int partition = HibernateUtil.getPartition(entities.get(0).getUserId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			for (LogBehavior entity : entities)
				session.save(entity);

			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 获取用户行为记录
	 * 
	 * @param id
	 *            行为记录ID
	 * @return 用户行为记录
	 */
	public LogBehavior get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogBehavior) session.get(LogBehavior.class, id);
	}

	/**
	 * 根据用户Id获取行为列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param maxResults
	 *            最大记录数
	 * @return 用户行为列表
	 */
	public List<LogBehavior> getByUserId(long userId, int maxResults) {
		List<LogBehavior> result = new ArrayList<LogBehavior>();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM LogBehavior WHERE userId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", userId).iterate();
		while (iter.hasNext()) {
			LogBehavior entity = (LogBehavior) iter.next();
			LogBehavior item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table log_behavior AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_log_behavior_user_id on log_behavior(user_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("成功修改表：log_behavior");
	}

}
