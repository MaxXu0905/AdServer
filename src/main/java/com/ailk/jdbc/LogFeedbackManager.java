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
 * ������־����
 * 
 * @author xugq
 * 
 */
public class LogFeedbackManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogFeedbackManager.class);
	private static LogFeedbackManager instance = new LogFeedbackManager();

	/**
	 * ��ȡ����LogFeedback���ʵ��
	 * 
	 * @return ����LogFeedback���ʵ��
	 */
	public static LogFeedbackManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private LogFeedbackManager() {
	}

	/**
	 * �����û�������¼�����ݿ�
	 * 
	 * @param entity
	 *            �û�������¼
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
	 * ��ȡ�û�������¼
	 * 
	 * @param id
	 *            ������¼ID
	 * @return �û�������¼
	 */
	public LogFeedback get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogFeedback) session.get(LogFeedback.class, id);
	}

	/**
	 * �����û�Id��ȡ�����б�
	 * 
	 * @param userId
	 *            �û�ID
	 * @param maxResults
	 *            ����¼��
	 * @return �û������б�
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
	 * ���·�����Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            ������Ϣ
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
	 * �޸ı�ṹ
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
		
		logger.info("�ɹ��޸ı�log_feedback");
	}

}
