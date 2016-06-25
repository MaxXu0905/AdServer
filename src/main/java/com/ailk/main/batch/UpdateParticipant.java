package com.ailk.main.batch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.AdParticipantManager;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.cache.InfoAdValue;

/**
 * 更新活动参与人数，必须在系统停止的状态下执行，用于矫正参与人数，一般情况下不需执行
 * 
 * @author xugq
 * 
 */
public class UpdateParticipant {

	public static void main(String[] args) {
		try {
			UpdateParticipant instance = new UpdateParticipant();

			instance.loadParticipants();
			instance.updateParticipants();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
		}
	}

	private final static Logger logger = Logger.getLogger(UpdateParticipant.class);

	private InfoAdManager infoAdManager;
	private Map<Integer, Integer> participantMap = new HashMap<Integer, Integer>();

	private UpdateParticipant() {
		GlobalManager globalManager = GlobalManager.getInstance();
		infoAdManager = InfoAdManager.getInstance(globalManager);
	}

	/**
	 * 加载参与人数
	 */
	private void loadParticipants() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Iterator<?> iter = session.createQuery(
					"SELECT userAdStatusPK.adId, sum(times) FROM UserAdStatus GROUP BY userAdStatusPK.adId").iterate();

			while (iter.hasNext()) {
				Object[] objs = (Object[]) iter.next();

				int adId = (Integer) objs[0];
				InfoAdValue infoAdValue = infoAdManager.get(adId);
				if (infoAdValue == null || infoAdValue.getAdStyle() != ConstVariables.AD_STYLE_PROMOTION)
					continue;

				int participants = ((Long) objs[1]).intValue();

				Integer value = participantMap.get(adId);
				if (value == null)
					participantMap.put(adId, participants);
				else
					value += participants;
			}
		}
	}

	/**
	 * 更新参与人数
	 */
	private void updateParticipants() {
		// 先删除原记录
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Transaction t = session.beginTransaction();

			try {
				session.createQuery("DELETE FROM AdParticipant").executeUpdate();
				t.commit();
			} finally {
				if (t.isActive())
					t.rollback();
				session.close();
			}
		}

		// 同步记录
		AdParticipantManager adParticipantManager = AdParticipantManager.getInstance();
		adParticipantManager.sync(participantMap);
	}

}
