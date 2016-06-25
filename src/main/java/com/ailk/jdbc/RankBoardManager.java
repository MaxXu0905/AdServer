package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.cache.RankBoardValue;
import com.ailk.jdbc.entity.RankBoard;

/**
 * 排行榜管理
 * 
 * @author xugq
 * 
 */
public class RankBoardManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(RankBoardManager.class);

	private List<RankBoardValue> cache; // 排行榜缓存对象

	/**
	 * 获取操作排行榜表的实例
	 * 
	 * @return 操作排行榜表的实例
	 */
	public static RankBoardManager getInstance(GlobalManager globalManager) {
		return globalManager.getRankBoardManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	private RankBoardManager(GlobalManager globalManager) {
		cache = new ArrayList<RankBoardValue>();
	}

	/**
	 * 获取排行榜缓存
	 * 
	 * @return 排行榜缓存
	 */
	public List<RankBoardValue> getCache() {
		return cache;
	}

	/**
	 * 获取排行榜
	 * 
	 * @param rank
	 *            排名
	 * @return 返回结果
	 */
	public RankBoard get(int rank) {
		Session session = HibernateUtil.getSessionFactory(0).openSession();
		try {
			return (RankBoard) session.get(RankBoard.class, rank);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存排行榜
	 * 
	 * @param entity
	 *            排行榜对象
	 */
	public void save(RankBoard entity) {
		Session session = HibernateUtil.getSessionFactory(0).openSession();
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
	 * 更新排行榜
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(RankBoard entity) {
		Session session = HibernateUtil.getSessionFactory(0).openSession();
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
	 * 删除排行榜
	 * 
	 * @param entity
	 *            排行榜对象
	 */
	public void delete(RankBoard entity) {
		Session session = HibernateUtil.getSessionFactory(0).openSession();
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
		synchronized (RankBoardManager.class) {
			Session session = HibernateUtil.getSessionFactory(0).openSession();
			Iterator<?> iter = session.createQuery("FROM RankBoard").iterate();

			while (iter.hasNext()) {
				RankBoard entity = (RankBoard) iter.next();

				RankBoardValue value = new RankBoardValue();
				value.set(entity);
				cache.add(value);
			}

			session.close();
		}

		logger.info("排行榜表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		RankBoardManager rankBoardManager = new RankBoardManager(globalManager);

		rankBoardManager.load();
		globalManager.setRankBoardManager(rankBoardManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：rank_board");
	}

}
