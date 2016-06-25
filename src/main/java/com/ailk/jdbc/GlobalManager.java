package com.ailk.jdbc;

/**
 * 全局变量管理，用于保证加载过程的一致性
 * 
 * @author xugq
 * 
 */
public class GlobalManager {

	private static boolean inited = false;
	private static GlobalManager instance = new GlobalManager();

	private SystemPropertiesManager systemPropertiesManager;
	private InfoAdExtManager infoAdExtManager;
	private InfoAdManager infoAdManager;
	private InfoCityManager infoCityManager;
	private InfoCodeManager infoCodeManager;
	private InfoPeriodManager infoPeriodManager;
	private InfoQuizManager infoQuizManager;
	private RankBoardManager rankBoardManager;

	public static GlobalManager getInstance() {
		return instance;
	}

	public static void setInstance(GlobalManager instance) {
		GlobalManager.instance = instance;
	}

	/**
	 * 构造函数，第一次自动初始化
	 */
	public GlobalManager() {
		if (inited)
			return;

		synchronized (GlobalManager.class) {
			if (!inited) {
				CacheLoader.inMemLoad(this);
				inited = true;
			}
		}
	}
	
	public SystemPropertiesManager getSystemPropertiesManager() {
		return systemPropertiesManager;
	}

	public void setSystemPropertiesManager(SystemPropertiesManager systemPropertiesManager) {
		this.systemPropertiesManager = systemPropertiesManager;
	}

	public InfoAdExtManager getInfoAdExtManager() {
		return infoAdExtManager;
	}

	public void setInfoAdExtManager(InfoAdExtManager infoAdExtManager) {
		this.infoAdExtManager = infoAdExtManager;
	}

	public InfoAdManager getInfoAdManager() {
		return infoAdManager;
	}

	public void setInfoAdManager(InfoAdManager infoAdManager) {
		this.infoAdManager = infoAdManager;
	}

	public InfoCityManager getInfoCityManager() {
		return infoCityManager;
	}

	public void setInfoCityManager(InfoCityManager infoCityManager) {
		this.infoCityManager = infoCityManager;
	}

	public InfoCodeManager getInfoCodeManager() {
		return infoCodeManager;
	}

	public void setInfoCodeManager(InfoCodeManager infoCodeManager) {
		this.infoCodeManager = infoCodeManager;
	}

	public InfoPeriodManager getInfoPeriodManager() {
		return infoPeriodManager;
	}

	public void setInfoPeriodManager(InfoPeriodManager infoPeriodManager) {
		this.infoPeriodManager = infoPeriodManager;
	}

	public InfoQuizManager getInfoQuizManager() {
		return infoQuizManager;
	}

	public void setInfoQuizManager(InfoQuizManager infoQuizManager) {
		this.infoQuizManager = infoQuizManager;
	}

	public RankBoardManager getRankBoardManager() {
		return rankBoardManager;
	}

	public void setRankBoardManager(RankBoardManager rankBoardManager) {
		this.rankBoardManager = rankBoardManager;
	}

}
