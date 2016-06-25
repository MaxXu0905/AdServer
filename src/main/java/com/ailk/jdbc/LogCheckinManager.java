package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.LogCheckin;

/**
 * ǩ������
 * 
 * @author xugq
 * 
 */
public class LogCheckinManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogCheckinManager.class);
	private static LogCheckinManager instance = new LogCheckinManager();

	/**
	 * ��ȡ����LogCheckin���ʵ��
	 * 
	 * @return ����LogCheckin���ʵ��
	 */
	public static LogCheckinManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private LogCheckinManager() {
	}

	/**
	 * ����ǩ����¼�����ݿ�
	 * 
	 * @param entity
	 *            ǩ����¼
	 */
	public void save(LogCheckin entity) {
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
	 * ��ȡǩ����¼
	 * 
	 * @param userId
	 *           �û�ID
	 * @return ǩ����¼
	 */
	public LogCheckin get(long userId) {
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogCheckin) session.get(LogCheckin.class, userId);
	}

	/**
	 * ����ǩ����¼�����ݿ�
	 * 
	 * @param entity
	 *            ǩ����¼
	 */
	public void update(LogCheckin entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
		logger.info("�ɹ��޸ı�log_checkin");
	}

}
