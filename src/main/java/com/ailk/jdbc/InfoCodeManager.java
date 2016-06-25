package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.cache.InfoCodeExtValue;
import com.ailk.jdbc.cache.InfoCodeKey;
import com.ailk.jdbc.cache.InfoCodeValue;
import com.ailk.jdbc.cache.InfoGroupKey;
import com.ailk.jdbc.cache.InfoGroupValue;
import com.ailk.jdbc.entity.InfoCode;
import com.ailk.jdbc.entity.InfoCodeExt;
import com.ailk.jdbc.entity.InfoCodeExtPK;
import com.ailk.jdbc.entity.InfoCodePK;

/**
 * 编码管理，包括扩展信息
 * 
 * @author xugq
 * 
 */
public class InfoCodeManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoCodeManager.class);

	private Map<InfoGroupKey, List<InfoGroupValue>> groupCache;
	private Map<InfoCodeKey, InfoCodeValue> codeCache;

	/**
	 * 获取操作InfoCode表的实例
	 * 
	 * @return 操作InfoCode表的实例
	 */
	public static InfoCodeManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoCodeManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *             全局管理对象
	 */
	private InfoCodeManager(GlobalManager globalManager) {
		groupCache = new HashMap<InfoGroupKey, List<InfoGroupValue>>();
		codeCache = new HashMap<InfoCodeKey, InfoCodeValue>();
	}

	/**
	 * 获取编码对象映射列表
	 * 
	 * @Param groupName 分组名
	 * @return 返回编码对象映射列表
	 */
	public List<InfoGroupValue> get(String groupName) {
		InfoGroupKey key = new InfoGroupKey();
		key.setGroupName(groupName);

		return groupCache.get(key);
	}

	/**
	 * 获取编码映射
	 * 
	 * @param groupName
	 *            分组名
	 * @param codeName
	 *            编码名
	 * @return
	 */
	public InfoCodeValue get(String groupName, String codeName) {
		InfoCodeKey key = new InfoCodeKey();
		key.setGroupName(groupName);
		key.setCodeName(codeName);

		return codeCache.get(key);
	}

	/**
	 * 保存编码信息到数据库
	 * 
	 * @param entity
	 *            编码信息信息对象
	 */
	public void save(InfoCode entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 保存编码信息到数据库
	 * 
	 * @param entity
	 *            编码信息对象
	 */
	public void save(InfoCodeExt entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 更新编码信息到数据库
	 * 
	 * @param entity
	 *            编码信息对象
	 */
	public void update(InfoCode entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 更新编码扩展信息到数据库
	 * 
	 * @param entity
	 *            编码扩展信息对象
	 */
	public void update(InfoCodeExt entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 从数据库删除编码信息
	 * 
	 * @param entity
	 *            代码信息对象
	 */
	public void delete(InfoCode entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 从数据库删除编码扩展信息
	 * 
	 * @param entity
	 *            代码信息对象
	 */
	public void delete(InfoCodeExt entity) {
		int partition = HibernateUtil.getPartition(0);
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
	 * 设置编码组
	 * 
	 * @param groupName
	 *            分组名
	 * @param items
	 *            编码列表
	 */
	public void setGroup(String groupName, List<InfoGroupValue> items) {
		int partition = HibernateUtil.getPartition(0);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			session.createQuery("DELETE FROM InfoCode WHERE groupName = ?1").setString("1", groupName).executeUpdate();

			session.createQuery("DELETE FROM InfoCodeExt WHERE infoCodeExtPK.groupName = ?1").setString("1", groupName)
					.executeUpdate();

			InfoCode infoCode = new InfoCode();
			InfoCodePK infoCodePK = new InfoCodePK();
			infoCodePK.setGroupName(groupName);
			infoCode.setInfoCodePK(infoCodePK);

			InfoCodeExt infoCodeExt = new InfoCodeExt();
			InfoCodeExtPK infoCodeExtPK = new InfoCodeExtPK();
			infoCodeExtPK.setGroupName(groupName);
			infoCodeExt.setInfoCodeExtPK(infoCodeExtPK);

			for (InfoGroupValue infoCodeValue : items) {
				infoCodePK.setCodeName(infoCodeValue.getCodeName());
				infoCode.setDesc(infoCodeValue.getDesc());
				session.save(infoCode);

				infoCodeExtPK.setCodeName(infoCodeValue.getCodeName());

				for (InfoCodeExtValue item : infoCodeValue.getItems()) {
					infoCodeExt.getInfoCodeExtPK().setCodeValue(item.getCodeValue());
					infoCodeExt.setDesc(item.getDesc());
					session.save(infoCodeExt);
				}
			}

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
		Map<InfoGroupKey, List<InfoGroupValue>> tmpGroupCache = new HashMap<InfoGroupKey, List<InfoGroupValue>>();
		Map<InfoCodeKey, InfoCodeValue> tmpCodeCache = new HashMap<InfoCodeKey, InfoCodeValue>();
		int partition = HibernateUtil.getPartition(0);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			Iterator<?> iter = session.createQuery("FROM InfoCode").iterate();
			while (iter.hasNext()) {
				InfoCode entity = (InfoCode) iter.next();

				InfoGroupKey key = new InfoGroupKey();
				key.setGroupName(entity.getInfoCodePK().getGroupName());

				List<InfoGroupValue> value = tmpGroupCache.get(key);
				if (value == null) {
					value = new ArrayList<InfoGroupValue>();
					tmpGroupCache.put(key, value);
				}

				InfoGroupValue infoCodeValue = new InfoGroupValue();
				infoCodeValue.setCodeName(entity.getInfoCodePK().getCodeName());
				infoCodeValue.setDesc(entity.getDesc());
				infoCodeValue.setExtraInfo(entity.getExtraInfo());
				infoCodeValue.setWidth(entity.getWidth());
				value.add(infoCodeValue);
			}

			iter = session.createQuery("FROM InfoCodeExt ORDER BY infoCodeExtPK.codeValue").iterate();
			while (iter.hasNext()) {
				InfoCodeExt entity = (InfoCodeExt) iter.next();

				InfoGroupKey key = new InfoGroupKey();
				key.setGroupName(entity.getInfoCodeExtPK().getGroupName());

				List<InfoGroupValue> value = tmpGroupCache.get(key);
				if (value == null)
					continue;

				for (InfoGroupValue infoCodeValue : value) {
					if (infoCodeValue.getCodeName().equals(entity.getInfoCodeExtPK().getCodeName())) {
						List<InfoCodeExtValue> items = infoCodeValue.getItems();
						InfoCodeExtValue infoCodeExtValue = new InfoCodeExtValue();

						infoCodeExtValue.setCodeValue(entity.getInfoCodeExtPK().getCodeValue());
						infoCodeExtValue.setDesc(entity.getDesc());
						items.add(infoCodeExtValue);
						break;
					}
				}
			}
		} finally {
			session.close();
		}

		for (Entry<InfoGroupKey, List<InfoGroupValue>> entry : tmpGroupCache.entrySet()) {
			for (InfoGroupValue value : entry.getValue()) {
				InfoCodeKey codeKey = new InfoCodeKey();
				codeKey.setGroupName(entry.getKey().getGroupName());
				codeKey.setCodeName(value.getCodeName());

				InfoCodeValue codeValue = new InfoCodeValue();
				codeValue.setDesc(value.getDesc());
				codeValue.setExtraInfo(value.getExtraInfo());
				codeValue.setWidth(value.getWidth());
				codeValue.setItems(value.getItems());

				tmpCodeCache.put(codeKey, codeValue);
			}
		}

		groupCache = tmpGroupCache;
		codeCache = tmpCodeCache;
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoCodeManager infoCodeManager = new InfoCodeManager(globalManager);

		infoCodeManager.load();
		globalManager.setInfoCodeManager(infoCodeManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：info_code");
		logger.info("成功修改表：info_code_ext");
	}

}
