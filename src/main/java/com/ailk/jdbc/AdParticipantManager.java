package com.ailk.jdbc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.ailk.jdbc.entity.AdParticipant;

/**
 * 广告参与人数管理
 * 
 * @author xugq
 * 
 */
public class AdParticipantManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(AdParticipantManager.class);
	private static AdParticipantManager instance = new AdParticipantManager();

	private HashMap<Integer, Integer> newParticipantsMap = new HashMap<Integer, Integer>(); // 新增参与人数
	private HashMap<Integer, Integer> participantsMap = new HashMap<Integer, Integer>(); // 总计参与人数

	/**
	 * 获取广告参与人数管理对象实例
	 * 
	 * @return
	 */
	public static AdParticipantManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private AdParticipantManager() {
	}

	public int getParticipants(int adId) {
		Integer participants = participantsMap.get(adId);
		if (participants == null)
			return 0;

		return participants;
	}

	/**
	 * 获取广告参与人数信息
	 * 
	 * @param adId
	 *            广告ID
	 * @return
	 */
	public AdParticipant get(int adId) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (AdParticipant) session.get(AdParticipant.class, adId);
		} finally {
			session.close();
		}
	}

	/**
	 * 更新广告参与人数
	 * 
	 * @param adId
	 *            广告ID
	 */
	public void update(int adId) {
		synchronized (AdParticipantManager.class) {
			Integer participants = newParticipantsMap.get(adId);
			if (participants == null)
				newParticipantsMap.put(adId, 1);
			else
				participants++;
		}
	}

	/**
	 * 同步参与人数到数据库
	 */
	public void sync() {
		HashMap<Integer, Integer> map = newParticipantsMap;
		synchronized (AdParticipantManager.class) {
			newParticipantsMap = new HashMap<Integer, Integer>();
		}

		sync(map);
	}

	/**
	 * 同步参与人数到数据库
	 * 
	 * @param map
	 *            待同步数据
	 */
	public void sync(Map<Integer, Integer> map) {
		Session[] sessions = new Session[HibernateUtil.getPartitions()];
		Query[] queries = new Query[HibernateUtil.getPartitions()];

		for (Entry<Integer, Integer> entry : map.entrySet()) {
			int adId = entry.getKey();
			int participants = entry.getValue();

			// 重置统计值
			entry.setValue(0);

			int partition = HibernateUtil.getPartition(adId);
			Session session = sessions[partition];
			Query query;

			// 获取会话
			if (session == null) {
				session = HibernateUtil.getSessionFactory(partition).openSession();
				sessions[partition] = session;

				query = session
						.createQuery("UPDATE AdParticipant SET participants = participants + ?1 WHERE adId = ?2");
				queries[partition] = query;
			} else {
				query = queries[partition];
			}

			Transaction t = session.beginTransaction();

			try {
				// 更新
				int rows = query.setInteger("1", participants).setInteger("2", adId).executeUpdate();
				if (rows <= 0) {
					// 更新失败则插入
					AdParticipant entity = new AdParticipant();
					entity.setAdId(adId);
					entity.setParticipants(participants);

					try {
						session.save(entity);
					} catch (ConstraintViolationException e) {
						// 主键冲突则再更新
						if (query.setParameter("1", adId).executeUpdate() <= 0) {
							logger.error("更新广告参与人数失败");
							return;
						}
					} catch (Exception e) {
						logger.error("系统异常，" + e);
						return;
					}
				}

				t.commit();
			} finally {
				if (t.isActive())
					t.rollback();
			}
		}

		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			if (sessions[partition] != null)
				sessions[partition].close();
		}
	}

	/**
	 * 从数据库加载参与人数
	 */
	@Override
	public void load() {
		synchronized (AdParticipantManager.class) {
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();

				try {
					Iterator<?> iter = session.createQuery("FROM AdParticipant").iterate();
					while (iter.hasNext()) {
						AdParticipant entity = (AdParticipant) iter.next();

						Integer participants = map.get(entity.getAdId());
						if (participants == null)
							map.put(entity.getAdId(), entity.getParticipants());
						else
							participants += entity.getParticipants();
					}
				} finally {
					session.close();
				}
			}

			participantsMap = map;
		}
		
		logger.info("广告参与人数表加载完毕，共" + participantsMap.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		instance.load();
	}

	@Override
	public void alterTable() {
		logger.info("成功修改表：ad_participant");
	}

}
