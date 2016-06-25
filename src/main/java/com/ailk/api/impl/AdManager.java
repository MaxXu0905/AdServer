package com.ailk.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ailk.api.AdIfc;
import com.ailk.api.UserIfc;
import com.ailk.api.UserIfc.UserStatSession;
import com.ailk.common.ConstVariables;
import com.ailk.common.GlobalVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.LogPromotionManager;
import com.ailk.jdbc.UserAdManager;
import com.ailk.jdbc.UserAdStatusManager;
import com.ailk.jdbc.UserStatusManager;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.LogPromotion;
import com.ailk.jdbc.entity.UserAd;
import com.ailk.jdbc.entity.UserAdPK;
import com.ailk.jdbc.entity.UserAdStatus;
import com.ailk.jdbc.entity.UserAdStatusPK;
import com.ailk.jdbc.entity.UserStatus;

/**
 * 广告管理，实现广告相关的操作
 * 
 * @author xugq
 * 
 */
public class AdManager implements AdIfc {

	private static final Logger logger = Logger.getLogger(AdManager.class);

	public AdManager() {
	}

	@Override
	public GetAdsResponse getAds(HttpSession httpSession, GetAdsRequest request) {
		GetAdsResponse response = new GetAdsResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		List<InfoAdItem> items = new ArrayList<InfoAdItem>();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		// 需要预先保留，保证在处理过程中不会更改，如果给的时间无效，则以最新时间为准
		long sysdate = GlobalVariables.sysdate;
		if (!GlobalVariables.validSysdate(sysdate, request.getSysdate()))
			request.setBatchId((short) 0);
		else
			sysdate = request.getSysdate();

		// 如果是活动，需要设置置顶选项
		if (request.getBatchId() == 0 && request.getAdStyle() == ConstVariables.AD_STYLE_PROMOTION)
			response.setFlags(AdIfc.FLAG_PROMOTION);

		// 获取广告ID列表管理对象
		UserAdManager userAdManager = UserAdManager.getInstance();

		// 获取userId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		Map<Short, List<UserIfc.AdItem>> adMap = userStatSession.getAdMap();
		List<UserIfc.AdItem> adItems = null;
		boolean found = false;

		// 如果存在缓存
		if (adMap != null) {
			// 如果不属于同一天，则使缓存失效
			if (userStatSession.getSysdate() != sysdate)
				adMap.clear();

			// 缓存中有值
			if (adMap.containsKey(request.getAdStyle())) {
				adItems = adMap.get(request.getAdStyle());
				found = true;
			}
		}

		if (!found) {
			// 设置查询参数
			UserAdPK userAdPK = new UserAdPK();
			userAdPK.setUserId(userId);
			userAdPK.setAdStyle(request.getAdStyle());

			// 获取用户广告列表
			UserAd userAd = userAdManager.get(userAdPK);
			if (userAd == null) {
				adItems = infoAdManager.getDefaultAds(request.getAdStyle());
			} else {
				// 根据请求的时间，决定是用今天的还是昨天的
				boolean recalculate = false;
				String ads;

				if (sysdate >= userAd.getSysdate()) {
					ads = userAd.getAds();

					// 如果请求今天的数据，需要判断生成的数据是否已过时
					if (ads != null) {
						UserStatusManager userStatusManager = UserStatusManager.getInstance();
						UserStatus userStatus = userStatusManager.get(userId);
						if (userStatus.getLastUpdate() != null
								&& userAd.getLastUpdate().before(userStatus.getLastUpdate()))
							recalculate = true;
					}
				} else {
					ads = userAd.getOldAds();
				}

				if (ads == null) {
					adItems = null;
				} else {
					adItems = new ArrayList<UserIfc.AdItem>();
					String[] fields = ads.split(ConstVariables.FIELD_SEP, -1);

					for (int i = 0; i < fields.length; i += 3) {
						UserIfc.AdItem adItem = new UserIfc.AdItem();

						adItem.setAdId(Integer.parseInt(fields[i]));

						// 检查是否有广告存在，如果不存在，则跳过
						if (!infoAdManager.contains(adItem.getAdId()))
							continue;

						if (!recalculate) {
							// 不需要重新计算
							adItem.setDays(Integer.parseInt(fields[i + 1]));
							adItem.setTimes(Integer.parseInt(fields[i + 2]));
						} else {
							// 需要重新计算
							adItem.setDays(-1);
							adItem.setTimes(-1);
						}

						adItems.add(adItem);
					}
				}
			}

			// 保存到会话中
			userStatSession.setSysdate(sysdate);
			if (adMap == null) {
				adMap = new HashMap<Short, List<UserIfc.AdItem>>();
				userStatSession.setAdMap(adMap);
			}
			adMap.put(request.getAdStyle(), adItems);
		}

		// 没有广告列表
		if (adItems == null || adItems.isEmpty()) {
			response.setSysdate(sysdate);
			response.setEnd(true);
			return response;
		}

		// 查询广告详情
		Map<Integer, InfoAdItem> modifyMap = new HashMap<Integer, InfoAdItem>();
		List<Integer> adIds = new ArrayList<Integer>();
		for (int i = request.getBatchId() * ConstVariables.USER_ADS_PER_BATCH; i < adItems.size(); i++) {
			UserIfc.AdItem adItem = adItems.get(i);
			InfoAdItem item = new InfoAdItem();

			if (adItem.getDays() == -1) {
				modifyMap.put(adItem.getAdId(), item);
				adIds.add(adItem.getAdId());
			} else {
				InfoAdValue infoAdValue = infoAdManager.get(adItem.getAdId());
				item.set(adItem.getAdId(), infoAdValue, adItem.getDays(), adItem.getTimes());
			}

			items.add(item);
			if (items.size() == ConstVariables.USER_ADS_PER_BATCH)
				break;
		}

		// 修正用户状态值
		if (!adIds.isEmpty()) {
			UserAdStatusManager userAdStatusManager = UserAdStatusManager.getInstance();
			Map<UserAdStatusPK, UserAdStatus> userAdStatuses = userAdStatusManager.get(userId, adIds);

			UserAdStatusPK userAdStatusPK = new UserAdStatusPK();
			userAdStatusPK.setUserId(userId);

			for (Entry<Integer, InfoAdItem> entry : modifyMap.entrySet()) {
				int adId = entry.getKey();
				InfoAdItem item = entry.getValue();

				userAdStatusPK.setAdId(adId);
				UserAdStatus userAdStatus = userAdStatuses.get(userAdStatusPK);
				InfoAdValue infoAdValue = infoAdManager.get(adId);
				item.set(adId, infoAdValue, userAdStatus.getDays(), userAdStatus.getTimes());
			}
		}

		// 应答中需要设置这批数据的系统时间
		response.setSysdate(sysdate);
		response.setEnd(items.size() < ConstVariables.USER_ADS_PER_BATCH);
		response.setItems(items);
		return response;
	}

	@Override
	public GetAdsResponse getSplashAds(GetAdsRequest request) {
		GetAdsResponse response = new GetAdsResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		List<InfoAdItem> items = new ArrayList<InfoAdItem>();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		// 需要预先保留，保证在处理过程中不会更改，如果给的时间无效，则以最新时间为准
		long sysdate = GlobalVariables.sysdate;

		List<UserIfc.AdItem> adItems = infoAdManager.getDefaultAds(request.getAdStyle());

		// 没有广告列表
		if (adItems == null || adItems.isEmpty()) {
			response.setSysdate(sysdate);
			response.setEnd(true);
			return response;
		}

		for (UserIfc.AdItem adItem : adItems) {
			InfoAdItem item = new InfoAdItem();

			InfoAdValue infoAdValue = infoAdManager.get(adItem.getAdId());
			item.set(adItem.getAdId(), infoAdValue, adItem.getDays(), adItem.getTimes());
			items.add(item);
		}

		// 应答中需要设置这批数据的系统时间
		response.setSysdate(sysdate);
		response.setEnd(true);
		response.setItems(items);
		return response;
	}

	@Override
	public GetPromotionsResponse getPromotions(HttpSession httpSession, GetPromotionsRequest request) {
		GetPromotionsResponse response = new GetPromotionsResponse();
		LogPromotionManager logPromotionManager = LogPromotionManager.getInstance();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		// 获取userId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		try {
			List<LogPromotion> logPromotions = logPromotionManager.get(userId, request.getMaxId(), request.getStatus(),
					ConstVariables.PROMOTIONS_PER_BATCH);
			if (logPromotions == null) {
				response.setEnd(true);
				return response;
			}

			List<GetPromotionsItem> items = new ArrayList<GetPromotionsItem>();
			for (LogPromotion logPromotion : logPromotions) {
				GetPromotionsItem item = new GetPromotionsItem();

				InfoAdValue infoAdValue = infoAdManager.get(logPromotion.getAdId());
				if (infoAdValue == null) {
					item.setCycles("未知");
					item.setTitle("已下线");
				} else {
					item.setCycles(infoAdValue.getCycles());
					item.setTitle(infoAdValue.getAdName());
				}

				item.setId(logPromotion.getId());
				item.setAdId(logPromotion.getAdId());
				item.setActionDate(logPromotion.getActionDate().getTime());
				item.setProfit(logPromotion.getProfit());
				item.setReason(logPromotion.getReason());
				item.setLastUpdate(logPromotion.getLastUpdate().getTime());
				items.add(item);
			}

			// 设置应答
			response.setEnd(items.size() < ConstVariables.PROMOTIONS_PER_BATCH);
			response.setItems(items);
		} catch (Exception e) {
			response.setErrorCode(BaseResponse.ESYSTEM);
			logger.error("系统异常，" + e);
		}

		return response;
	}

}
