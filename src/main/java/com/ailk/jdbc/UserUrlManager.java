package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserUrl;
import com.ailk.jdbc.entity.UserUrlPK;

/**
 * �û��Ĳ鿴��������URL����
 * 
 * @author xugq
 * 
 */
public class UserUrlManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserUrlManager.class);
	private static UserUrlManager instance = new UserUrlManager();

	/**
	 * ��ȡ�û�URL�������ʵ��
	 * 
	 * @return
	 */
	public static UserUrlManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private UserUrlManager() {
	}

	/**
	 * ��ȡ�û�URL��Ϣ
	 * 
	 * @param userAdPK
	 *            �û�URL��Ϣ��
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
	 * �����û�URL��Ϣ
	 * 
	 * @param entity
	 *            �û�URL��Ϣ����
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
	 * �����û�URL��Ϣ
	 * 
	 * @param entity
	 *            �û�����
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
	 * ɾ���û�URL��Ϣ
	 * 
	 * @param entity
	 *            �û�URL����
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
		logger.info("�ɹ��޸ı�user_url");
	}

}
