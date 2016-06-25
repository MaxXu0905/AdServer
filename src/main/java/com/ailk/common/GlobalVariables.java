package com.ailk.common;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.ailk.api.impl.BaseResponse.SystemProperties;
import com.ailk.jdbc.AdParticipantManager;
import com.ailk.jdbc.CacheLoader;
import com.ailk.jdbc.GlobalManager;
import com.ailk.schedule.CheckLockExpire;

public class GlobalVariables {

	public static long sysdate; // 当前日期的毫秒

	public static final long MILLIS_PER_HOUR = 3600000; // 一小时的毫秒数
	public static final long MILLIS_PER_DAY = 86400000; // 一天的毫秒数

	private static GlobalManager preparedGlobalManager = null;

	private static final SystemProperties androidProperties = new SystemProperties(0); // 安卓客户端系统参数

	static {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		sysdate = calendar.getTimeInMillis();

		Timer timer = new Timer();
		long remainInDay = sysdate + MILLIS_PER_DAY - calendar.getTimeInMillis();

		// 在每天23点执行，自动加载缓存
		long delay = remainInDay - MILLIS_PER_HOUR;
		if (delay <= 0)
			delay += MILLIS_PER_DAY;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				GlobalManager globalManager = new GlobalManager();
				CacheLoader.inMemLoad(globalManager);
				preparedGlobalManager = globalManager;
			}
		}, delay, MILLIS_PER_DAY);

		// 在每天0点执行，自动切换系统日期，并切换缓存
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 如有需要，则切换缓存
				if (preparedGlobalManager != null) {
					GlobalManager.setInstance(preparedGlobalManager);
					preparedGlobalManager = null;
				}

				sysdate += MILLIS_PER_DAY;
			}
		}, remainInDay, MILLIS_PER_DAY);

		// 每十分钟执行一次广告参与人数同步
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				AdParticipantManager adParticipantManager = AdParticipantManager.getInstance();

				adParticipantManager.sync();
				adParticipantManager.load();
			}
		}, 0, 600000);

		// 每十分钟检查一次是否有超时的锁定记录
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				CheckLockExpire checkLockExpire = CheckLockExpire.getInstance();

				checkLockExpire.run();
			}
		}, 0, 600000);
	}

	public static SystemProperties getAndroidproperties() {
		return androidProperties;
	}

	// 判断请求的日期是否合法
	public static boolean validSysdate(long current, long request) {
		return (current == request || current - MILLIS_PER_DAY == request);
	}

}
