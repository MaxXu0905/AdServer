package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.HisUser;

/**
 * �û���ʷ����
 * 
 * @author xugq
 * 
 */
public class HisUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(HisUserManager.class);
	private static HisUserManager instance = new HisUserManager();

	/**
	 * ��ȡ����HisUser���ʵ��
	 * 
	 * @return ����HisUser���ʵ��
	 */
	public static HisUserManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private HisUserManager() {
	}

	/**
	 * ���ݱ����ˮ��ȡ�û���ʷ��Ϣ
	 * 
	 * @param id
	 *            �����ˮ
	 * @return �û���ʷ��Ϣ
	 */
	public HisUser get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		return (HisUser) session.get(HisUser.class, id);
	}

	/**
	 * �����û���Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �û���Ϣ��Ϣ����
	 */
	public void save(HisUser entity) {
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
	 * �����ݿ�ɾ���û������Ϣ
	 * 
	 * @param entity
	 *            �û��������
	 */
	public void delete(HisUser entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
		logger.info("�ɹ��޸ı�his_cust");
	}

}
