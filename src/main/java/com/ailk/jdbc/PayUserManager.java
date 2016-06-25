package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.PayUser;

/**
 * �û�֧������
 * 
 * @author xugq
 * 
 */
public class PayUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(PayUserManager.class);
	private static PayUserManager instance = new PayUserManager();

	/**
	 * �����û�֧���������
	 * 
	 * @return �û�֧���������
	 */
	public static PayUserManager getInstance() {
		return instance;
	}

	/**
	 * �����캯��
	 */
	private PayUserManager() {
	}

	/**
	 * �����ݿ��ѯָ���û���֧����¼
	 * 
	 * @param userId
	 *            �û�ID
	 * @param maxResults
	 *            ��󷵻ؼ�¼��
	 * @return ���ز�ѯ���
	 */
	public List<PayUser> get(long userId, int maxResults) {
		List<PayUser> result = new ArrayList<PayUser>();

		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Query query = session.createQuery("FROM PayUser WHERE userId = ?1");
		if (maxResults > 0)
			query.setMaxResults(maxResults);

		Iterator<?> iter = query.setLong("1", userId).iterate();
		while (iter.hasNext()) {
			PayUser entity = (PayUser) iter.next();
			PayUser item = entity.clone();

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
	public void save(PayUser entity) {
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
	 * ����֧����¼
	 * 
	 * @param entity
	 *            �����µ�֧����¼
	 */
	public void update(PayUser entity) {
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
	public void delete(PayUser entity) {
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
				String sql = "alter table pay_user AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_pay_user_user_id on pay_user(user_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("�ɹ��޸ı�pay_user");
	}

}
