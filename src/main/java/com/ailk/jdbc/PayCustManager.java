package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayCust;

/**
 * �ͻ�֧������
 * 
 * @author xugq
 * 
 */
public class PayCustManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayCustManager.class);
	private static PayCustManager instance = new PayCustManager();

	/**
	 * ��ȡ�ͻ�֧���������
	 * 
	 * @return �ͻ�֧���������
	 */
	public static PayCustManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private PayCustManager() {
	}

	/**
	 * �����ݿ��ѯָ���û���֧����¼
	 * 
	 * @param custId
	 *            �ͻ�ID
	 * @param maxResults
	 *            ��󷵻ؼ�¼��
	 * @return ���ز�ѯ���
	 */
	public List<PayCust> get(long custId, int maxResults) {
		List<PayCust> result = new ArrayList<PayCust>();

		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM PayCust WHERE custId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", custId).iterate();
		while (iter.hasNext()) {
			PayCust entity = (PayCust) iter.next();
			PayCust item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}

	/**
	 * ����֧����¼
	 * 
	 * @param entity
	 *            �������֧������
	 */
	public void save(PayCust entity) {
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
	 * ����֧����¼
	 * 
	 * @param entity
	 *            �����µ�֧����¼
	 */
	public void update(PayCust entity) {
		int partition = HibernateUtil.getPartition(entity.getId());
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
	 * ɾ��֧����¼
	 * 
	 * @param entity
	 *            ��ɾ����֧����¼
	 */
	public void delete(PayCust entity) {
		int partition = HibernateUtil.getPartition(entity.getId());
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
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table pay_cust AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_cust_cust_id on pay_cust(cust_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("�ɹ��޸ı�pay_cust");
	}

}
