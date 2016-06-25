package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.AdBalance;

/**
 * ���������
 * 
 * @author xugq
 * 
 */
public class AdBalanceManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(AdBalanceManager.class);
	private static AdBalanceManager instance = new AdBalanceManager();

	/**
	 * ��ȡ������������ʵ��
	 * 
	 * @return ������������ʵ��
	 */
	public static AdBalanceManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private AdBalanceManager() {
	}

	/**
	 * ��ȡ������
	 * 
	 * @param adId
	 *            ���ID
	 * @return ������
	 */
	public AdBalance get(int adId) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			return (AdBalance) session.get(AdBalance.class, adId);
		} finally {
			session.close();
		}
	}

	/**
	 * �����������ݿ�
	 * 
	 * @param entity
	 *            ������
	 */
	public void save(AdBalance entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
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
	 * ���¹�������ݿ�
	 * 
	 * @param entity
	 *            ������
	 */
	public void update(AdBalance entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
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
	 * ����������
	 * 
	 * @param adId
	 *            ���ID
	 * @param locked
	 *            �������
	 * @return �Ƿ���³ɹ�
	 */
	public boolean lock(int adId, long locked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows = session
					.createQuery(
							"UPDATE AdBalance SET remain = remain - ?1, locked = locked + ?1 WHERE adId = ?2 AND remain >= ?1")
					.setLong("1", locked).setInteger("2", adId).executeUpdate();
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * ����������
	 * 
	 * @param adId
	 *            ���ID
	 * @param unlocked
	 *            �������
	 * @return �Ƿ���³ɹ�
	 */
	public boolean unlock(int adId, long unlocked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows = session
					.createQuery("UPDATE AdBalance SET remain = remain + ?1, locked = locked - ?1 WHERE adId = ?2")
					.setLong("1", unlocked).setInteger("2", adId).executeUpdate();
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * �Ʒ�
	 * 
	 * @param adId
	 *            ���ID
	 * @param remain
	 *            ������
	 * @param locked
	 *            ��������
	 * @return �Ƿ���³ɹ�
	 */
	public boolean charge(int adId, long remain, long locked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows;

			if (remain == 0) {
				rows = session.createQuery("UPDATE AdBalance SET locked = locked - ?1 WHERE adId = ?2")
						.setLong("1", locked).setInteger("2", adId).executeUpdate();
			} else if (locked == 0) {
				rows = session.createQuery("UPDATE AdBalance SET remain = remain - ?1 WHERE adId = ?2 AND remain > ?1")
						.setLong("1", remain).setInteger("2", adId).executeUpdate();
			} else {
				rows = session
						.createQuery(
								"UPDATE AdBalance SET remain = remain - ?1, locked = locked - ?2 WHERE adId = ?3 AND remain > ?1")
						.setLong("1", remain).setLong("2", locked).setInteger("3", adId).executeUpdate();
			}
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * �����ݿ�ɾ��������
	 * 
	 * @param entity
	 *            ������
	 */
	public void delete(AdBalance entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
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
	 * �޸ı�ṹ
	 */
	@Override
	public void alterTable() {
		logger.info("�ɹ��޸ı�ad_balance");
	}

}
