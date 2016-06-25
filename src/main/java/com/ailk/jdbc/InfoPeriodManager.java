package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.cache.InfoPeriodValue;
import com.ailk.jdbc.entity.InfoPeriod;

/**
 * 时段信息管理，数据将缓存到私有内存中
 * 
 * @author xugq
 * 
 */
public class InfoPeriodManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoPeriodManager.class);

	private List<InfoPeriodValue> cache;

	/**
	 * 获取操作InfoPeriod表的实例
	 * 
	 * @return 操作InfoPeriod表的实例
	 */
	public static InfoPeriodManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoPeriodManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *             全局管理对象
	 */
	private InfoPeriodManager(GlobalManager globalManager) {
		cache = new ArrayList<InfoPeriodValue>();
	}

	/**
	 * 获取城市编码
	 * 
	 * @return 返回编码对象映射
	 */
	public List<InfoPeriodValue> get() {
		return cache;
	}

	/**
	 * 判断是否匹配
	 * 
	 * @param priceValueSet
	 *            价格值集合
	 * @param calendar
	 *            日历
	 * @return 是否匹配
	 */
	public boolean match(Set<Integer> priceValueSet, Calendar calendar) {
		if (priceValueSet.isEmpty())
			return true;

		short periodType;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
			periodType = ConstVariables.PERIOD_TYPE_HOLIDAY;
		else
			periodType = ConstVariables.PERIOD_TYPE_WORKING;

		long time = calendar.getTime().getTime();

		for (InfoPeriodValue value : cache) {
			if (value.getPeriodType() != periodType)
				continue;

			if (!priceValueSet.contains(value.getPeriodId()))
				continue;

			if (time >= value.getBeginTime() && time < value.getEndTime())
				return true;
		}

		return false;
	}

	/**
	 * 加载数据到缓存
	 */
	@Override
	public void load() {
		synchronized (InfoPeriodManager.class) {
			for (int partition = 0; partition < HibernateUtil.getPartitions(); ++partition) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				Iterator<?> iter = session.createQuery("FROM InfoPeriod ORDER BY periodId").iterate();

				while (iter.hasNext()) {
					InfoPeriod entity = (InfoPeriod) iter.next();

					InfoPeriodValue value = new InfoPeriodValue();
					value.set(entity);
					cache.add(value);
				}
			}
		}

		logger.info("时段信息表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoPeriodManager infoPeriodManager = new InfoPeriodManager(globalManager);

		infoPeriodManager.load();
		globalManager.setInfoPeriodManager(infoPeriodManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_period AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：info_period");
	}

}
