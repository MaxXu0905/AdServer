package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserAd;
import com.ailk.jdbc.entity.UserAdPK;

/**
 * �û��Ĺ��ID�б�
 * 
 * @author xugq
 * 
 */
public class UserAdManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserAdManager.class);
	private static UserAdManager instance = new UserAdManager();

	/**
	 * ��ȡ�û��Ĺ��ID�б�������ʵ��
	 * 
	 * @return
	 */
	public static UserAdManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private UserAdManager() {
	}

	/**
	 * ��ȡ�û������Ϣ
	 * 
	 * @param userAdPK
	 *            �û������Ϣ��
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
	 * �����û������Ϣ
	 * 
	 * @param entity
	 *            �û������Ϣ����
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
	 * �����û������Ϣ
	 * 
	 * @param entity
	 *            �û�����
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
	 * ɾ���û������Ϣ
	 * 
	 * @param entity
	 *            �û������Ϣ����
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
		logger.info("�ɹ��޸ı�user_ad");
	}

}
