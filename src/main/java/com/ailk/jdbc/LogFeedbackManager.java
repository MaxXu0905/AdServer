package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.LogFeedback;

/**
 * 反馈日志管理
 * 
 * @author xugq
 * 
 */
public class LogFeedbackManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogFeedbackManager.class);
	private static LogFeedbackManager instance = new LogFeedbackManager();

	/**
	 * 获取操作LogFeedback表的实例
	 * 
	 * @return 操作LogFeedback表的实例
	 */
	public static LogFeedbackManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private LogFeedbackManager() {
	}

	/**
	 * 保存用户反馈记录到数据库
	 * 
	 * @param entity
	 *            用户反馈记录
	 */
	public void save(LogFeedback entity) {
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
	 * 获取用户反馈记录
	 * 
	 * @param id
	 *            反馈记录ID
	 * @return 用户反馈记录
	 */
	public LogFeedback get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogFeedback) session.get(LogFeedback.class, id);
	}

	/**
	 * 根据用户Id获取反馈列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param maxResults
	 *            最大记录数
	 * @return 用户反馈列表
	 */
	public List<LogFeedback> getByUserId(long userId, int maxResults) {
		List<LogFeedback> result = new ArrayList<LogFeedback>();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM LogFeedback WHERE userId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", userId).iterate();
		while (iter.hasNext()) {
			LogFeedback entity = (LogFeedback) iter.next();
			LogFeedback item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}
	
	/**
	 * 更新反馈信息到数据库
	 * 
	 * @param entity
	 *            反馈信息
	 */
	public void update(LogFeedback entity) {
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
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table log_feedback AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_log_feedback_user_id on log_feedback(user_id)";
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create index idx_log_feedback_status on log_feedback(status)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("成功修改表：log_feedback");
	}

}
