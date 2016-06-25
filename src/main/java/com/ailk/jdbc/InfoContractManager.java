package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoContract;

/**
 * ��ͬ��Ϣ����
 * @author xugq
 *
 */
public class InfoContractManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoContractManager.class);
	private static InfoContractManager instance = new InfoContractManager();
	
	/**
	 * ��ȡ������ͬ��Ϣ���ʵ��
	 * 
	 * @return ������ͬ��Ϣ���ʵ��
	 */
	public static InfoContractManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private InfoContractManager() {
	}
	
	/**
	 * ���ݺ�ͬID��ȡ��ͬ��Ϣ
	 * @param contractId ��ͬID
	 * @return ��ͬ��Ϣ
	 */
	public InfoContract get(long contractId) {
		int partition = HibernateUtil.getPartition(contractId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		return (InfoContract) session.get(InfoContract.class, contractId);
	}
	
	/**
	 * ���ݿ͑�ID��ȡ��ͬ��Ϣ�б�
	 * @param custId �ͻ�ID
	 * @param maxResults ��󷵻ؼ�¼��
	 * @return �ͻ���Ϣ�б�
	 */
	public List<InfoContract> getByCustId(long custId, int maxResults) {
		List<InfoContract> result = new ArrayList<InfoContract>();
		
		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		Iterator<?> iter = session.createQuery("FROM InfoContract WHERE custId = ?1")
				.setMaxResults(maxResults)
				.setLong("1", custId)
				.iterate();
		if (iter.hasNext()) {
			InfoContract infoContract = (InfoContract) iter.next();
			result.add(infoContract.clone());
		}
		
		return result;
	}

	/**
	 * �����ͬ��Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            ��ͬ��Ϣ����
	 */
	public void save(InfoContract entity) {
		int partition = HibernateUtil.getPartition();
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
	public void update(InfoContract entity) {
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
	public void delete(InfoContract entity) {
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
				String sql = "alter table info_contract AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create index idx_info_contract_cust_id on info_contract(cust_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("�ɹ��޸ı�info_contract");
	}
	
}
