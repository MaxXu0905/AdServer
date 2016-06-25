package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayRqst;

/**
 * ֧����������ɻ���status��ѯ��Ҳ�ɻ���userId��ѯ�������Ҫ������Ӧ������
 * 
 * @author xugq
 * 
 */
public class PayRqstManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayRqstManager.class);
	private static PayRqstManager instance = new PayRqstManager();

	/**
	 * ����֧������ʵ��
	 * 
	 * @return ֧������ʵ��
	 */
	public static PayRqstManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private PayRqstManager() {
	}
	
	public PayRqst get(long id) {
		int partition = HibernateUtil.getPartition(id);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (PayRqst) session.get(PayRqst.class, id);
		} finally {
			session.close();
		}
	}

	/**
	 * �����ݿ��ѯָ��״̬��֧������
	 * 
	 * @param status
	 *            ֧������״̬
	 * @param maxResults
	 *            ��󷵻ؼ�¼��
	 * @return ���ز�ѯ���
	 */
	public List<PayRqst> get(short status, int maxResults) {
		List<PayRqst> result = new ArrayList<PayRqst>();

		for (int partition = 0; partition < HibernateUtil.getPartitions(); ++partition) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Query query = session.createQuery("FROM PayRqst WHERE status = ?1");
			if (maxResults > 0)
				query.setMaxResults(maxResults);

			Iterator<?> iter = query.setShort("1", status).iterate();
			while (iter.hasNext()) {
				PayRqst entity = (PayRqst) iter.next();
				PayRqst item = entity.clone();

				result.add(item);
				--maxResults;
			}

			session.close();

			if (maxResults <= 0)
				break;
		}

		return result;
	}

	/**
	 * �����ݿ��ѯָ���û���֧������
	 * 
	 * @param userId
	 *            �û�ID
	 * @param maxId
	 *            ���ID
	 * @param maxResults
	 *            ��󷵻ؼ�¼��
	 * @return ���ز�ѯ���
	 */
	public List<PayRqst> get(long userId, long maxId, int maxResults) {
		List<PayRqst> result = new ArrayList<PayRqst>();

		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query;
		if (maxId >= 0) {
			query = session.createQuery("FROM PayRqst WHERE userId = ?1 and id <= ?2 ORDER BY id desc")
					.setLong("1", userId)
					.setLong("2", maxId);
		} else {
			query = session.createQuery("FROM PayRqst WHERE userId = ?1 ORDER BY id desc")
					.setLong("1", userId);
		}
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.iterate();
		while (iter.hasNext()) {
			PayRqst entity = (PayRqst) iter.next();
			PayRqst item = entity.clone();

			result.add(item);
		}

		session.close();
		return result;
	}

	/**
	 * ����֧������
	 * 
	 * @param entity
	 *            �������֧������
	 */
	public void save(PayRqst entity) {
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
	 * ����֧������
	 * 
	 * @param entity
	 *            �����µ�֧������
	 */
	public void update(PayRqst entity) {
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
	 * ɾ��֧������
	 * 
	 * @param entity
	 *            ��ɾ����֧������
	 */
	public void delete(PayRqst entity) {
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
				String sql = "alter table pay_rqst AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_rqst_status on pay_rqst(status)";
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_rqst_user_id_id on pay_rqst(user_id, id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("�ɹ��޸ı�pay_rqst");
	}

}
