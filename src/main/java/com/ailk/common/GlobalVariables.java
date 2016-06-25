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

	public static long sysdate; // ��ǰ���ڵĺ���

	public static final long MILLIS_PER_HOUR = 3600000; // һСʱ�ĺ�����
	public static final long MILLIS_PER_DAY = 86400000; // һ��ĺ�����

	private static GlobalManager preparedGlobalManager = null;

	private static final SystemProperties androidProperties = new SystemProperties(0); // ��׿�ͻ���ϵͳ����

	static {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		sysdate = calendar.getTimeInMillis();

		Timer timer = new Timer();
		long remainInDay = sysdate + MILLIS_PER_DAY - calendar.getTimeInMillis();

		// ��ÿ��23��ִ�У��Զ����ػ���
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

		// ��ÿ��0��ִ�У��Զ��л�ϵͳ���ڣ����л�����
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// ������Ҫ�����л�����
				if (preparedGlobalManager != null) {
					GlobalManager.setInstance(preparedGlobalManager);
					preparedGlobalManager = null;
				}

				sysdate += MILLIS_PER_DAY;
			}
		}, remainInDay, MILLIS_PER_DAY);

		// ÿʮ����ִ��һ�ι���������ͬ��
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				AdParticipantManager adParticipantManager = AdParticipantManager.getInstance();

				adParticipantManager.sync();
				adParticipantManager.load();
			}
		}, 0, 600000);

		// ÿʮ���Ӽ��һ���Ƿ��г�ʱ��������¼
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

	// �ж�����������Ƿ�Ϸ�
	public static boolean validSysdate(long current, long request) {
		return (current == request || current - MILLIS_PER_DAY == request);
	}

}
