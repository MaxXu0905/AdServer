package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.ailk.jdbc.cache.InfoCityValue;
import com.ailk.jdbc.entity.InfoCity;

/**
 * 城市信息管理
 * 
 * @author xugq
 * 
 */
public class InfoCityManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoCityManager.class);

	private List<InfoCityValue> cache;

	/**
	 * 获取操作InfoCity表的实例
	 * 
	 * @return 操作InfoCity表的实例
	 */
	public static InfoCityManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoCityManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	private InfoCityManager(GlobalManager globalManager) {
		cache = new ArrayList<InfoCityValue>();
	}

	/**
	 * 获取城市编码
	 * 
	 * @return 返回编码对象映射
	 */
	public List<InfoCityValue> get() {
		return cache;
	}

	/**
	 * 加载数据到缓存
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

		logger.info("城市信息表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoCityManager infoCityManager = new InfoCityManager(globalManager);

		infoCityManager.load();
		globalManager.setInfoCityManager(infoCityManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：info_city");
	}

}
