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
 * �û���Ϊ����
 * 
 * @author xugq
 * 
 */
public class LogBehaviorManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogBehaviorManager.class);
	private static LogBehaviorManager instance = new LogBehaviorManager();

	/**
	 * ��ȡ����LogBehavior���ʵ������ʵ��Ϊ���̼�����
	 * 
	 * @return ����LogBehavior���ʵ��
	 */
	public static LogBehaviorManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private LogBehaviorManager() {
	}

	/**
	 * �����û���Ϊ��¼�����ݿ�
	 * 
	 * @param entity
	 *            �û���Ϊ��¼
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
	 * �����û���Ϊ��¼�б����ݿ⣬���뱣֤�����û�ID��һ�µ�
	 * 
	 * @param entities
	 *            �û���Ϊ��¼�б�
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
	 * ��ȡ�û���Ϊ��¼
	 * 
	 * @param id
	 *            ��Ϊ��¼ID
	 * @return �û���Ϊ��¼
	 */
	public LogBehavior get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogBehavior) session.get(LogBehavior.class, id);
	}

	/**
	 * �����û�Id��ȡ��Ϊ�б�
	 * 
	 * @param userId
	 *            �û�ID
	 * @param maxResults
	 *            ����¼��
	 * @return �û���Ϊ�б�
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
	 * �޸ı�ṹ
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
		
		logger.info("�ɹ��޸ı�log_behavior");
	}

}
