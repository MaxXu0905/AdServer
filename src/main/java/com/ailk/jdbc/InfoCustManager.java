package com.ailk.jdbc;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoCust;

/**
 * �ͻ���Ϣ�����
 * @author xugq
 *
 */
public class InfoCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoCustManager.class);
	private static InfoCustManager instance = new InfoCustManager();
	
	/**
	 * ��ȡ����InfoCust���ʵ��
	 * 
	 * @return ����InfoCust���ʵ��
	 */
	public static InfoCustManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private InfoCustManager() {
	}
	
	/**
	 * ���ݿͻ�ID��ȡ�ͻ���Ϣ
	 * @param custId �ͻ�ID
	 * @return �ͻ���Ϣ
	 */
	public InfoCust get(long custId) {
		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		return (InfoCust) session.get(InfoCust.class, custId);
	}
	
	/**
	 * ���ݿͻ������ȡ�ͻ���Ϣ
	 * @param name �ͻ�����/�绰
	 * @return �ͻ���Ϣ
	 */
	public InfoCust get(String name) {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); ++partition) {
			Session session = HibernateUtil.getSessionFactory(partition)
					.openSession();
			Iterator<?> iter = session.createQuery("FROM InfoCust WHERE email = ?1 or phoneNo = ?2")
					.setString("1", name)
					.setString("2", name)
					.iterate();
			if (iter.hasNext()) {
				InfoCust infoCust = (InfoCust) iter.next();
				return infoCust.clone();
			}
		}
		
		return null;
	}

	/**
	 * ����ͻ���Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �ͻ���Ϣ����
	 */
	public void save(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * ���¿ͻ���Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �ͻ���Ϣ����
	 */
	public void update(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * �����ݿ�ɾ���ͻ���Ϣ
	 * 
	 * @param entity
	 *            �ͻ���Ϣ����
	 */
	public void delete(InfoCust entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_cust AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create unique index idx_info_cust_email on info_cust(email)";
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create unique index idx_info_cust_phone_no on info_cust(phone_no)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("�ɹ��޸ı�info_cust");
	}

}
