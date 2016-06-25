package com.ailk.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserAdStatus;
import com.ailk.jdbc.entity.UserAdStatusPK;

/**
 * �û����״̬����
 * 
 * @author xugq
 * 
 */
public class UserAdStatusManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserAdStatusManager.class);
	private static UserAdStatusManager instance = new UserAdStatusManager();
	private static Map<Integer, String> sqlMap = new ConcurrentHashMap<Integer, String>();

	/**
	 * ��ȡ�û����״̬����
	 * 
	 * @return �û����״̬����
	 */
	public static UserAdStatusManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private UserAdStatusManager() {
	}

	/**
	 * ��ȡ�û������Ϣ
	 * 
	 * @param userId
	 *            �û�ID
	 * @param adIds
	 *            ����б�
	 * @return �����Ϣ�б�
	 */
	public Map<UserAdStatusPK, UserAdStatus> get(long userId, List<Integer> adIds) {
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return get(session, userId, adIds);
		} finally {
			session.close();
		}
	}

	/**
	 * ��ȡ�û������Ϣ
	 * 
	 * @param session
	 *            �Ự
	 * @param userId
	 *            �û�ID
	 * @param adIds
	 *            ����б�
	 * @return �����Ϣ�б�
	 */
	public Map<UserAdStatusPK, UserAdStatus> get(Session session, long userId, List<Integer> adIds) {
		String sql = sqlMap.get(adIds.size());
		if (sql == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("FROM UserAdStatus WHERE userAdStatusPK.userId = ?1 AND userAdStatusPK.adId IN (");
			for (int i = 0; i < adIds.size(); i++) {
				if (i != 0)
					builder.append(", ");
				builder.append("?");
				builder.append(i + 2);
			}
			builder.append(")");

			sql = builder.toString();
			sqlMap.put(adIds.size(), sql);
		}

		Query query = session.createQuery(sql);
		query.setLong("1", userId);
		for (int i = 0; i < adIds.size(); i++)
			query.setInteger(Integer.toString(i + 2), adIds.get(i));

		Map<UserAdStatusPK, UserAdStatus> result = new HashMap<UserAdStatusPK, UserAdStatus>();
		for (Object obj : query.list()) {
			UserAdStatus entity = (UserAdStatus) obj;

			result.put(entity.getUserAdStatusPK(), entity);
		}

		return result;
	}

	/**
	 * �����û����״̬
	 * 
	 * @param entity
	 *            �û����״̬����
	 */
	public void save(UserAdStatus entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdStatusPK().getUserId());
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
	 * �����û����״̬
	 * 
	 * @param entity
	 *            �û�����
	 */
	public void update(UserAdStatus entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdStatusPK().getUserId());
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
	 * ɾ���û����״̬
	 * 
	 * @param entity
	 *            �û����״̬����
	 */
	public void delete(UserAdStatus entity) {
		int partition = HibernateUtil.getPartition(entity.getUserAdStatusPK().getUserId());
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
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "create index idx_user_ad_status_expire_date on user_ad_status(expire_date)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("�ɹ��޸ı�user_ad_status");
	}

}
