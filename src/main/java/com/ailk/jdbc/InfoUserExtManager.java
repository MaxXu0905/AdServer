package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoUserExt;

/**
 * �û���Ϣ��չ�����
 * 
 * @author xugq
 * 
 */
public class InfoUserExtManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoUserExtManager.class);
	private static InfoUserExtManager instance = new InfoUserExtManager();

	/**
	 * ��ȡ�����û���Ϣ��չ���ʵ��
	 * 
	 * @return �����û���Ϣ��չ����ʵ��
	 */
	public static InfoUserExtManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private InfoUserExtManager() {
	}

	/**
	 * �����û���չ��Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �û���Ϣ����
	 */
	public void save(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
	 * �����û���չ��Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �û���Ϣ����
	 */
	public void update(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
	 * ɾ���û���չ��Ϣ
	 * 
	 * @param entity
	 *            �û���չ��Ϣ����
	 */
	public void delete(InfoUserExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoUserExtPK().getUserId());
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
		logger.info("�ɹ��޸ı�info_user_ext");
	}
	
}
