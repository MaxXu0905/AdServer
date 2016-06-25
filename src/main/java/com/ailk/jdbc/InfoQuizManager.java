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
 * 问题列表管理，问题列表将加载到私有内存中，该管理对象是单例存在的
 * 
 * @author xugq
 * 
 */
@LoadPriority(LoadPriority.PRIORITY_MIN)
public class InfoQuizManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoQuizManager.class);

	private Map<InfoQuizKey, InfoQuizValue> cache;

	/**
	 * 获取操作InfoQuiz表的实例，该实例为进程级单例
	 * 
	 * @return 操作InfoQuiz表的实例
	 */
	public static InfoQuizManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoQuizManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	private InfoQuizManager(GlobalManager globalManager) {
		cache = new HashMap<InfoQuizKey, InfoQuizValue>();
	}

	/**
	 * 获取问题信息缓存
	 * 
	 * @return 问题信息缓存
	 */
	public Map<InfoQuizKey, InfoQuizValue> getCache() {
		return cache;
	}

	/**
	 * 从缓存查询问题信息
	 * 
	 * @param quizId
	 *            问题ID
	 * @return 返回查询结果
	 */
	public InfoQuizValue get(int quizId) {
		InfoQuizKey key = new InfoQuizKey();
		key.setQuizId(quizId);

		return cache.get(key);
	}

	/**
	 * 保存问题信息到数据库
	 * 
	 * @param entity
	 *            问题信息
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
	 * 更新问题信息到数据库
	 * 
	 * @param entity
	 *            问题信息
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
	 * 从数据库删除问题信息
	 * 
	 * @param entity
	 *            问题信息
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
	 * 加载数据到缓存
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

		logger.info("问题信息表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoQuizManager infoQuizManager = new InfoQuizManager(globalManager);

		infoQuizManager.load();
		globalManager.setInfoQuizManager(infoQuizManager);
	}

	/**
	 * 修改表结构
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

		logger.info("成功修改表：info_quiz");
	}

}
