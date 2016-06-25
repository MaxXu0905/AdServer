package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.cache.InfoAdExtValue;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.entity.InfoAdExt;

/**
 * �����չ��Ϣ�����ñ����ص�˽���ڴ�
 * 
 * @author xugq
 * 
 */
public class InfoAdExtManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoAdExtManager.class);

	private Map<InfoAdKey, List<InfoAdExtValue>> cache;

	/**
	 * ��ȡ����InfoAdExt���ʵ��
	 * 
	 * @return ����InfoAdExt���ʵ��
	 */
	public static InfoAdExtManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoAdExtManager();
	}

	/**
	 * ���캯��
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	private InfoAdExtManager(GlobalManager globalManager) {
		cache = new HashMap<InfoAdKey, List<InfoAdExtValue>>();
	}

	/**
	 * ��ȡ�����չ��Ϣ����
	 * 
	 * @return �������չϢ����
	 */
	public Map<InfoAdKey, List<InfoAdExtValue>> getCache() {
		return cache;
	}

	/**
	 * �ӻ����ѯ�����Ϣ
	 * 
	 * @param adId
	 *            ���ID
	 * @return ���ز�ѯ���
	 */
	public List<InfoAdExtValue> get(int adId) {
		InfoAdKey key = new InfoAdKey();
		key.setAdId(adId);
		return cache.get(key);
	}

	/**
	 * ��������չ��Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �����չ��Ϣ
	 */
	public void save(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
	 * ���¹����չ��Ϣ�����ݿ�
	 * 
	 * @param entity
	 *            �����չ��Ϣ
	 */
	public void update(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
	 * �����ݿ�ɾ�������չ��Ϣ
	 * 
	 * @param entity
	 *            �����չ��Ϣ
	 */
	public void delete(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
		synchronized (InfoAdExtManager.class) {
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				Iterator<?> iter = session.createQuery("FROM InfoAdExt ORDER BY infoAdExtPK.adId, infoAdExtPK.attrId")
						.iterate();

				while (iter.hasNext()) {
					InfoAdExt entity = (InfoAdExt) iter.next();
					InfoAdKey key = new InfoAdKey();
					key.setAdId(entity.getInfoAdExtPK().getAdId());

					List<InfoAdExtValue> items = cache.get(key);
					if (items == null) {
						items = new ArrayList<InfoAdExtValue>();
						cache.put(key, items);
					}

					InfoAdExtValue value = new InfoAdExtValue();
					value.setAttrId(entity.getInfoAdExtPK().getAttrId());
					value.setAttrValue(entity.getAttrValues());
					items.add(value);
				}

				session.close();
			}
		}

		logger.info("�����չ��Ϣ�������ϣ���" + cache.size() + "����¼");
	}

	/**
	 * �������ݵ�����
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	public static void load(GlobalManager globalManager) {
		InfoAdExtManager infoAdExtManager = new InfoAdExtManager(globalManager);

		infoAdExtManager.load();
		globalManager.setInfoAdExtManager(infoAdExtManager);
	}

	/**
	 * �޸ı�ṹ
	 */
	@Override
	public void alterTable() {
		logger.info("�ɹ��޸ı�info_ad_ext");
	}

}
