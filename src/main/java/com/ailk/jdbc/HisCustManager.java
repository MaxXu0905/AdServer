package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.HisCust;

/**
 * �ͻ���ʷ����
 * 
 * @author xugq
 * 
 */
public class HisCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(HisCustManager.class);
	private static HisCustManager instance = new HisCustManager();

	/**
	 * ��ȡ����HisCust���ʵ��
	 * 
	 * @return ����HisCust���ʵ��
	 */
	public static HisCustManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private HisCustManager() {
	}

	/**
	 * ���ݱ����ˮ��ȡ�ͻ���ʷ��Ϣ
	 * 
	 * @param id
	 *            �����ˮ
	 * @return �ͻ���ʷ��Ϣ
	 */
	public HisCust get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		return (HisCust) session.get(HisCust.class, id);
	}

	/**
	 * ����ͻ���Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �ͻ���Ϣ��Ϣ����
	 */
	public void save(HisCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
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
	 * �����ݿ�ɾ���ͻ������Ϣ
	 * 
	 * @param entity
	 *            �ͻ��������
	 */
	public void delete(HisCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
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
