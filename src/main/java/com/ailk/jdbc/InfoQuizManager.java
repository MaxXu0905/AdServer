package com.ailk.jdbc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.cache.InfoQuizKey;
import com.ailk.jdbc.cache.InfoQuizValue;
import com.ailk.jdbc.entity.InfoQuiz;

/**
 * �����б���������б����ص�˽���ڴ��У��ù�������ǵ������ڵ�
 * 
 * @author xugq
 * 
 */
@LoadPriority(LoadPriority.PRIORITY_MIN)
public class InfoQuizManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoQuizManager.class);

	private Map<InfoQuizKey, InfoQuizValue> cache;

	/**
	 * ��ȡ����InfoQuiz���ʵ������ʵ��Ϊ���̼�����
	 * 
	 * @return ����InfoQuiz���ʵ��
	 */
	public static InfoQuizManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoQuizManager();
	}

	/**
	 * ���캯��
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	private InfoQuizManager(GlobalManager globalManager) {
		cache = new HashMap<InfoQuizKey, InfoQuizValue>();
	}

	/**
	 * ��ȡ������Ϣ����
	 * 
	 * @return ������Ϣ����
	 */
	public Map<InfoQuizKey, InfoQuizValue> getCache() {
		return cache;
	}

	/**
	 * �ӻ����ѯ������Ϣ
	 * 
	 * @param quizId
	 *            ����ID
	 * @return ���ز�ѯ���
	 */
	public InfoQuizValue get(int quizId) {
		InfoQuizKey key = new InfoQuizKey();
		key.setQuizId(quizId);

		return cache.get(key);
	}

	/**
	 * ����������Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            ������Ϣ
	 */
	public void save(InfoQuiz entity) {
		int partition = HibernateUtil.getPartition();
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
	 * ����������Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            ������Ϣ
	 */
	public void update(InfoQuiz entity) {
		int partition = HibernateUtil.getPartition(entity.getQuizId());
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
	 * �����ݿ�ɾ��������Ϣ
	 * 
	 * @param entity
	 *            ������Ϣ
	 */
	public void delete(InfoQuiz entity) {
		int partition = HibernateUtil.getPartition(entity.getQuizId());
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
	 * �������ݵ�����
	 */
	@Override
	public void load() {
		synchronized (InfoQuizManager.class) {
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				Iterator<?> iter = session.createQuery("FROM InfoQuiz").iterate();

				while (iter.hasNext()) {
					InfoQuiz entity = (InfoQuiz) iter.next();
					InfoQuizKey key = new InfoQuizKey();
					InfoQuizValue value = new InfoQuizValue();

					key.set(entity);
					value.set(entity);

					cache.put(key, value);
				}

				session.close();
			}
		}

		logger.info("������Ϣ�������ϣ���" + cache.size() + "����¼");
	}

	/**
	 * �������ݵ�����
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	public static void load(GlobalManager globalManager) {
		InfoQuizManager infoQuizManager = new InfoQuizManager(globalManager);

		infoQuizManager.load();
		globalManager.setInfoQuizManager(infoQuizManager);
	}

	/**
	 * �޸ı�ṹ
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_quiz AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("�ɹ��޸ı�info_quiz");
	}

}
