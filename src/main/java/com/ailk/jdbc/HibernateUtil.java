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
 * ���ݿ�������������Ϊ1����ʹ��hibernate.cfg.xml������ʹ��hibernate-[i].cfg.xml
 * 
 * @author xugq
 * 
 */
public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	private static final List<SessionFactory> sessionFactories = new ArrayList<SessionFactory>();

	private static final int PARTITION_BITS = 8; // ����λ
	private static final String PARTITIONS = "cluster.partitions";
	private static final String PARTITION = "cluster.partition";
	private static int partitions; // �ܷ�����
	private static int partition; // ��ǰ����

	static {
		Properties properties = ServerProperties.getInstance().getProperties();
		partitions = Integer.parseInt(properties.getProperty(PARTITIONS, "1"));
		partition = Integer.parseInt(properties.getProperty(PARTITION, "0"));

		for (int i = 0; i < partitions; i++) {
			try {
				// ��hibernate.cfg.xml����SessionFactory
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
	 * ���ݷ�����ȡ�Ự����
	 * 
	 * @param partition
	 *            ����
	 * @return �Ự����
	 */
	public static SessionFactory getSessionFactory(int partition) {
		return sessionFactories.get(partition);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public static int getPartitions() {
		return partitions;
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return ��ǰ����
	 */
	public static int getPartition() {
		return partition;
	}

	/**
	 * ����id��ȡ���ݿ����
	 * 
	 * @param id
	 *            ID
	 * @return ���ݿ����
	 */
	public static int getPartition(long id) {
		return ((int) ((id >> (64 - PARTITION_BITS)) & ((1 << PARTITION_BITS) - 1))) % partitions;
	}

}