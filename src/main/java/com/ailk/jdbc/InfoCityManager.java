package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.ailk.jdbc.cache.InfoCityValue;
import com.ailk.jdbc.entity.InfoCity;

/**
 * ������Ϣ����
 * 
 * @author xugq
 * 
 */
public class InfoCityManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoCityManager.class);

	private List<InfoCityValue> cache;

	/**
	 * ��ȡ����InfoCity���ʵ��
	 * 
	 * @return ����InfoCity���ʵ��
	 */
	public static InfoCityManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoCityManager();
	}

	/**
	 * ���캯��
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	private InfoCityManager(GlobalManager globalManager) {
		cache = new ArrayList<InfoCityValue>();
	}

	/**
	 * ��ȡ���б���
	 * 
	 * @return ���ر������ӳ��
	 */
	public List<InfoCityValue> get() {
		return cache;
	}

	/**
	 * �������ݵ�����
	 */
	@Override
	public void load() {
		synchronized (InfoCityManager.class) {
			InfoCityValue infoCityValue = null;

			int partition = HibernateUtil.getPartition(0);
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Iterator<?> iter = session.createQuery("FROM InfoCity ORDER BY provinceName, cityCode").iterate();

			while (iter.hasNext()) {
				InfoCity entity = (InfoCity) iter.next();
				if (infoCityValue == null) {
					infoCityValue = new InfoCityValue();
					infoCityValue.setItemName(entity.getProvinceName());
				} else if (!infoCityValue.getItemName().equals(entity.getProvinceName())) {
					cache.add(infoCityValue);
					infoCityValue = new InfoCityValue();
					infoCityValue.setItemName(entity.getProvinceName());
				}

				List<InfoCityValue.InfoCityItem> items = infoCityValue.getItems();
				if (items == null) {
					items = new ArrayList<InfoCityValue.InfoCityItem>();
					infoCityValue.setItems(items);
				}

				InfoCityValue.InfoCityItem item = new InfoCityValue.InfoCityItem();
				item.setItemCode(entity.getCityCode());
				item.setItemName(entity.getCityName());
				items.add(item);
			}

			if (infoCityValue != null)
				cache.add(infoCityValue);
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
		InfoCityManager infoCityManager = new InfoCityManager(globalManager);

		infoCityManager.load();
		globalManager.setInfoCityManager(infoCityManager);
	}

	/**
	 * �޸ı�ṹ
	 */
	@Override
	public void alterTable() {
		logger.info("�ɹ��޸ı�info_city");
	}

}
