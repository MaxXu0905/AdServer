package com.ailk.main.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.InfoUserManager;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.RankBoard;
import com.ailk.jdbc.entity.UserStatus;

/**
 * 更新用户排名，该进程只能在分区0上执行，需在每晚11点前完成
 * 
 * @author xugq
 * 
 */
public class UpdateRank {

	public static void main(String[] args) {
		if (HibernateUtil.getPartition() != 0) {
			System.out.println("错误：该任务只能在分区0上执行");
			System.exit(1);
		}

		UpdateRank instance = new UpdateRank();
		instance.loadUserStatus();
		instance.updateUserStatus();
		instance.updateRankBoard();
	}

	private static final int MAX_RANKS = 50; // 最大排名榜数量
	private TreeMap<Long, List<Long>> rankMap = new TreeMap<Long, List<Long>>();

	/**
	 * 加载用户状态
	 */
	private void loadUserStatus() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				Iterator<?> iter = session.createQuery("FROM UserStatus").iterate();
				while (iter.hasNext()) {
					UserStatus userStatus = (UserStatus) iter.next();

					long profit = userStatus.getLockProfit() + userStatus.getVideoProfit()
							+ userStatus.getPromotionProfit() + userStatus.getRecomProfit();

					List<Long> userIds = rankMap.get(profit);
					if (userIds == null) {
						userIds = new ArrayList<Long>();
						rankMap.put(profit, userIds);
					}
					userIds.add(userStatus.getUserId());
				}
			} finally {
				session.close();
			}
		}
	}

	/**
	 * 更新用户状态
	 */
	private void updateUserStatus() {
		Session[] sessions = new Session[HibernateUtil.getPartitions()];
		Query[] queries = new Query[HibernateUtil.getPartitions()];
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			sessions[partition] = HibernateUtil.getSessionFactory(partition).openSession();
			queries[partition] = sessions[partition].createQuery("UPDATE UserStatus SET rank = ?1 WHERE userId = ?2");
		}

		try {
			int rank = 1;
			for (List<Long> values : rankMap.descendingMap().values()) {
				for (long userId : values) {
					int partition = HibernateUtil.getPartition(userId);
					Session session = sessions[partition];
					Query query = queries[partition];
					Transaction t = session.beginTransaction();

					try {
						query.setInteger("1", rank).setLong("2", userId).executeUpdate();
						t.commit();
					} finally {
						if (t.isActive())
							t.rollback();
					}
				}

				rank += values.size();
			}
		} finally {
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++)
				sessions[partition].close();
		}
	}

	private void updateRankBoard() {
		InfoUserManager infoUserManager = InfoUserManager.getInstance();
		Session session = HibernateUtil.getSessionFactory(0).openSession();
		Transaction t = session.beginTransaction();
		session.createQuery("DELETE FROM RankBoard").executeUpdate();

		try {
			int rank = 0;
			for (Entry<Long, List<Long>> entry : rankMap.descendingMap().entrySet()) {
				long profit = entry.getKey();
				List<Long> values = entry.getValue();

				for (long userId : values) {
					++rank;

					// 找不到用户，或者用户未注册，则不进入排行榜
					InfoUser infoUser = infoUserManager.get(userId);
					if (infoUser == null || infoUser.getRegistered() == null
							|| !infoUser.getRegistered().booleanValue())
						continue;

					RankBoard rankBoard = new RankBoard();
					rankBoard.setRank(rank);
					rankBoard.setPortrait(infoUser.getPortrait());
					rankBoard.setNickName(infoUser.getNickName());
					rankBoard.setLevel(Math.min(infoUser.getLevel(), ConstVariables.MAX_LEVEL));
					rankBoard.setProfit(profit);
					rankBoard.setUserId(userId);
					session.save(rankBoard);

					if (rank == MAX_RANKS)
						break;
				}
			}

			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

}
