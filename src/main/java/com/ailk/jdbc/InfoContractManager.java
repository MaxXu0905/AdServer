package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoContract;

/**
 * 合同信息管理
 * @author xugq
 *
 */
public class InfoContractManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoContractManager.class);
	private static InfoContractManager instance = new InfoContractManager();
	
	/**
	 * 获取操作合同信息表的实例
	 * 
	 * @return 操作合同信息表的实例
	 */
	public static InfoContractManager getInstance() {
		return instance;
	}

	/**
	 * 对象构造函数
	 */
	private InfoContractManager() {
	}
	
	/**
	 * 根据合同ID获取合同信息
	 * @param contractId 合同ID
	 * @return 合同信息
	 */
	public InfoContract get(long contractId) {
		int partition = HibernateUtil.getPartition(contractId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		return (InfoContract) session.get(InfoContract.class, contractId);
	}
	
	/**
	 * 根据客戶ID获取合同信息列表
	 * @param custId 客户ID
	 * @param maxResults 最大返回记录数
	 * @return 客户信息列表
	 */
	public List<InfoContract> getByCustId(long custId, int maxResults) {
		List<InfoContract> result = new ArrayList<InfoContract>();
		
		int partition = HibernateUtil.getPartition(custId);
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
		Iterator<?> iter = session.createQuery("FROM InfoContract WHERE custId = ?1")
				.setMaxResults(maxResults)
				.setLong("1", custId)
				.iterate();
		if (iter.hasNext()) {
			InfoContract infoContract = (InfoContract) iter.next();
			result.add(infoContract.clone());
		}
		
		return result;
	}

	/**
	 * 保存合同信息到数据库
	 * 
	 * @param entity
	 *            合同信息对象
	 */
	public void save(InfoContract entity) {
		int partition = HibernateUtil.getPartition();
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * 更新客户信息到数据库
	 * 
	 * @param entity
	 *            客户信息对象
	 */
	public void update(InfoContract entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * 从数据库删除客户信息
	 * 
	 * @param entity
	 *            客户信息对象
	 */
	public void delete(InfoContract entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition)
				.openSession();
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
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_contract AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
				
				sql = "create index idx_info_contract_cust_id on info_contract(cust_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		logger.info("成功修改表：info_contract");
	}
	
}
