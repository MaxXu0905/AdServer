package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.ailk.common.ServerProperties;

/**
 * 数据库管理，如果分区数为1，则使用hibernate.cfg.xml；否则使用hibernate-[i].cfg.xml
 * 
 * @author xugq
 * 
 */
public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	private static final List<SessionFactory> sessionFactories = new ArrayList<SessionFactory>();

	private static final int PARTITION_BITS = 8; // 分区位
	private static final String PARTITIONS = "cluster.partitions";
	private static final String PARTITION = "cluster.partition";
	private static int partitions; // 总分区数
	private static int partition; // 当前分区

	static {
		Properties properties = ServerProperties.getInstance().getProperties();
		partitions = Integer.parseInt(properties.getProperty(PARTITIONS, "1"));
		partition = Integer.parseInt(properties.getProperty(PARTITION, "0"));

		for (int i = 0; i < partitions; i++) {
			try {
				// 从hibernate.cfg.xml创建SessionFactory
				String configFile;

				if (partitions == 1)
					configFile = "hibernate.cfg.xml";
				else
					configFile = "hibernate-" + i + ".cfg.xml";

				Configuration configuration = new Configuration().configure(configFile);
				ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
						configuration.getProperties()).buildServiceRegistry();
				sessionFactories.add(configuration.buildSessionFactory(serviceRegistry));
			} catch (Throwable ex) {
				logger.fatal(ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
	}

	/**
	 * 根据分区获取会话工厂
	 * 
	 * @param partition
	 *            分区
	 * @return 会话工厂
	 */
	public static SessionFactory getSessionFactory(int partition) {
		return sessionFactories.get(partition);
	}

	/**
	 * 获取分区数
	 * 
	 * @return 分区数
	 */
	public static int getPartitions() {
		return partitions;
	}

	/**
	 * 获取当前分区
	 * 
	 * @return 当前分区
	 */
	public static int getPartition() {
		return partition;
	}

	/**
	 * 根据id获取数据库分区
	 * 
	 * @param id
	 *            ID
	 * @return 数据库分区
	 */
	public static int getPartition(long id) {
		return ((int) ((id >> (64 - PARTITION_BITS)) & ((1 << PARTITION_BITS) - 1))) % partitions;
	}

}