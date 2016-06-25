package com.ailk.api.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.ailk.api.CheckIfc;
import com.ailk.api.CustIfc;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.InfoUserManager;
import com.ailk.jdbc.LogPromotionManager;
import com.ailk.jdbc.NameUserManager;
import com.ailk.jdbc.PayRqstManager;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.InfoAd;
import com.ailk.jdbc.entity.InfoCust;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.LogFeedback;
import com.ailk.jdbc.entity.LogPromotion;
import com.ailk.jdbc.entity.NameUser;
import com.ailk.jdbc.entity.PayRqst;
import com.ailk.jdbc.entity.UserStatus;

/**
 * 审核管理，实现审核相关的操作
 * 
 * @author xugq
 * 
 */
public class CheckManager implements CheckIfc {

	private static final Logger logger = Logger.getLogger(CheckManager.class);

	public CheckManager() {
	}

	@Override
	public BaseResponse saveAd(HttpSession httpSession, SaveAdRequest request) {
		BaseResponse response = new BaseResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		// 获取客户ID
		InfoCust infoCust = (InfoCust) httpSession.getAttribute(CustIfc.CUST_ATTR);
		long custId = infoCust.getCustId();

		InfoAd infoAd = request.getInfoAd();
		infoAd.setCustId(custId);
		infoAd.setOperId(custId);
		infoAd.setOperDate(new Timestamp(System.currentTimeMillis()));

		try {
			infoAdManager.save(request.getInfoAd(), request.getQuizList(), request.getExtValues());
		} catch (ConstraintViolationException e) {
			logger.debug("主键冲突，重复的广告信息");
			response.setErrorCode(BaseResponse.EDUPENT);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public GetAdsResponse getAds(HttpSession httpSession, GetAdsRequest request) {
		GetAdsResponse response = new GetAdsResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		List<GetAdsItem> items = new ArrayList<GetAdsItem>();

		try {
			if (request.getAdId() != null) {
				InfoAdValue infoAdValue = infoAdManager.get(request.getAdId());
				if (infoAdValue == null || infoAdValue.getAdStyle() != request.getAdStyle()) {
					logger.error("找不到广告信息，adId=" + request.getAdId());
					response.setErrorCode(BaseResponse.ENOENT);
					return response;
				}

				GetAdsItem item = new GetAdsItem();
				item.setAdId(request.getAdId());
				item.setAdName(infoAdValue.getAdName());
				item.setWebView(infoAdValue.getWebView());
				items.add(item);
			} else {
				TreeMap<InfoAdKey, InfoAdValue> cache = infoAdManager.getCache();
				for (Entry<InfoAdKey, InfoAdValue> entry : cache.entrySet()) {
					InfoAdKey key = entry.getKey();
					InfoAdValue value = entry.getValue();

					if (value.getAdStyle() != request.getAdStyle())
						continue;

					if (request.getAdName() != null && !value.getAdName().contains(request.getAdName()))
						continue;

					GetAdsItem item = new GetAdsItem();
					item.setAdId(key.getAdId());
					item.setAdName(value.getAdName());
					item.setWebView(value.getWebView());
					items.add(item);
				}
			}

			if (!items.isEmpty())
				response.setItems(items);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public GetChecksResponse getChecks(HttpSession httpSession, GetChecksRequest request) {
		GetChecksResponse response = new GetChecksResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		NameUserManager nameUserManager = NameUserManager.getInstance();
		InfoUserManager infoUserManager = InfoUserManager.getInstance();
		long userId;

		try {
			StringBuilder builder = new StringBuilder();
			builder.append("FROM LogPromotion WHERE adId = ?1 AND status = ?2 AND id >= ?3");
			if (request.getUserName() != null && !request.getUserName().isEmpty()) {
				NameUser nameUser = nameUserManager.get(request.getUserName(), true);
				if (nameUser == null) {
					logger.error("找不到用户ID，userName=" + request.getUserName());
					response.setErrorCode(BaseResponse.ENOENT);
					return response;
				}

				userId = nameUser.getUserId();
				builder.append(" AND userId = ?4");
			} else {
				userId = -1;
			}

			builder.append(" ORDER BY id");

			// 初始化断点
			List<Long> saver = request.getSaver();
			if (saver == null) {
				saver = new ArrayList<Long>();
				for (int i = 0; i < HibernateUtil.getPartitions(); i++)
					saver.add(0L);
			} else if (saver.size() < HibernateUtil.getPartitions()) {
				int count = HibernateUtil.getPartitions() - saver.size();
				for (int i = 0; i < count; i++)
					saver.add(0L);
			}

			// 初始化变量
			TreeMap<Long, LogPromotion> treeMap = new TreeMap<Long, LogPromotion>();

			// 获取数据
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				try {
					long id = saver.get(partition);

					Query query = session.createQuery(builder.toString()).setInteger("1", request.getAdId())
							.setShort("2", request.getStatus()).setLong("3", id);
					if (userId != -1)
						query.setLong("4", userId);

					query.setMaxResults(request.getPageSize());

					int count = 0;
					Iterator<?> iter = query.iterate();
					while (iter.hasNext()) {
						LogPromotion entity = (LogPromotion) iter.next();

						treeMap.put(entity.getId(), entity);
						count++;
					}

					// 已经到达末尾
					if (count < request.getPageSize())
						saver.set(partition, Long.MAX_VALUE);
				} finally {
					session.close();
				}
			}

			List<GetChecksItem> items = new ArrayList<GetChecksItem>();
			for (LogPromotion entity : treeMap.values()) {
				// 设置结果
				GetChecksItem item = new GetChecksItem();

				InfoUser infoUser = infoUserManager.get(entity.getUserId());
				if (infoUser != null)
					item.setUserName(infoUser.getUserName());

				item.setId(entity.getId());
				item.setAdId(entity.getAdId());
				item.setStatus(entity.getStatus());
				item.setLastUpdate(entity.getLastUpdate().getTime());
				item.setInvestigations(entity.getInvestigations());
				item.setAnswers(entity.getAnswers());
				item.setReason(entity.getReason());
				item.setProfit(entity.getProfit());
				items.add(item);

				// 设置断点
				int partition = HibernateUtil.getPartition(entity.getId());
				saver.set(partition, entity.getId() + 1);
			}

			response.setSaver(saver);

			InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
			InfoAdValue infoAdValue = infoAdManager.get(request.getAdId());
			if (infoAdValue != null)
				response.setQuizValues(infoAdValue.getQuizValues());

			if (!items.isEmpty())
				response.setItems(items);
		} catch (Exception e) {
			logger.error("系统异常，" + e.getStackTrace().toString());
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse acceptPromotion(HttpSession httpSession, AcceptPromotionRequest request) {
		BaseResponse response = new BaseResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		LogPromotionManager logPromotionManager = LogPromotionManager.getInstance();

		try {
			LogPromotion logPromotion = logPromotionManager.get(request.getId());
			if (logPromotion == null) {
				logger.error("找不到活动日志，id=" + request.getId());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}
			
			logPromotion.setStatus(ConstVariables.STATUS_PASS);
			logPromotion.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			logPromotionManager.update(globalManager, logPromotion);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse rejectPromotion(HttpSession httpSession, RejectPromotionRequest request) {
		BaseResponse response = new BaseResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		LogPromotionManager logPromotionManager = LogPromotionManager.getInstance();

		try {
			LogPromotion logPromotion = logPromotionManager.get(request.getId());
			if (logPromotion == null) {
				logger.error("找不到活动信息，id=" + request.getId());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			logPromotion.setStatus(ConstVariables.STATUS_REJECT);
			logPromotion.setReason(request.getReason());
			logPromotion.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			logPromotionManager.update(globalManager, logPromotion);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public GetPayResponse getPayRqst(HttpSession httpSession, GetPayRequest request) {
		GetPayResponse response = new GetPayResponse();
		NameUserManager nameUserManager = NameUserManager.getInstance();
		InfoUserManager infoUserManager = InfoUserManager.getInstance();
		long userId;

		try {
			StringBuilder builder = new StringBuilder();
			builder.append("FROM PayRqst WHERE payType = ?1 AND status = ?2 AND id >= ?3");
			if (request.getUserName() != null && !request.getUserName().isEmpty()) {
				NameUser nameUser = nameUserManager.get(request.getUserName(), true);
				if (nameUser == null) {
					logger.error("找不到用户ID，userName=" + request.getUserName());
					response.setErrorCode(BaseResponse.ENOENT);
					return response;
				}

				userId = nameUser.getUserId();
				builder.append(" AND userId = ?4");
			} else {
				userId = -1;
			}

			builder.append(" ORDER BY id");

			// 初始化断点
			List<Long> saver = request.getSaver();
			if (saver == null) {
				saver = new ArrayList<Long>();
				for (int i = 0; i < HibernateUtil.getPartitions(); i++)
					saver.add(0L);
			} else if (saver.size() < HibernateUtil.getPartitions()) {
				int count = HibernateUtil.getPartitions() - saver.size();
				for (int i = 0; i < count; i++)
					saver.add(0L);
			}

			// 初始化变量
			TreeMap<Long, PayRqst> treeMap = new TreeMap<Long, PayRqst>();

			// 获取数据
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				try {
					long id = saver.get(partition);

					Query query = session.createQuery(builder.toString()).setShort("1", request.getPayType())
							.setShort("2", request.getStatus()).setLong("3", id);
					if (userId != -1)
						query.setLong("4", userId);

					query.setMaxResults(request.getPageSize());

					int count = 0;
					Iterator<?> iter = query.iterate();
					while (iter.hasNext()) {
						PayRqst entity = (PayRqst) iter.next();

						treeMap.put(entity.getId(), entity);
						count++;
					}

					// 已经到达末尾
					if (count < request.getPageSize())
						saver.set(partition, Long.MAX_VALUE);
				} finally {
					session.close();
				}
			}

			List<GetPayItem> items = new ArrayList<GetPayItem>();
			for (PayRqst entity : treeMap.values()) {
				// 设置结果
				GetPayItem item = new GetPayItem();

				item.setId(entity.getId());

				InfoUser infoUser = infoUserManager.get(entity.getUserId());
				if (infoUser != null)
					item.setUserName(infoUser.getUserName());

				item.setPayType(entity.getPayType());
				item.setAmount(entity.getAmount());
				item.setStatus(entity.getStatus());
				item.setReason(entity.getReason());
				item.setLastUpdate(entity.getLastUpdate().getTime());
				items.add(item);

				// 设置断点
				int partition = HibernateUtil.getPartition(entity.getId());
				saver.set(partition, entity.getId() + 1);
			}

			response.setSaver(saver);
			if (!items.isEmpty())
				response.setItems(items);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse acceptPayRqst(HttpSession httpSession, AcceptPayRequest request) {
		BaseResponse response = new BaseResponse();
		PayRqstManager payRqstManager = PayRqstManager.getInstance();

		try {
			PayRqst payRqst = payRqstManager.get(request.getId());
			if (payRqst == null) {
				logger.error("找不到支付请求，id=" + request.getId());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			payRqst.setStatus(ConstVariables.STATUS_PASS);
			payRqst.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			payRqstManager.update(payRqst);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse rejectPayRqst(HttpSession httpSession, RejectPayRequest request) {
		BaseResponse response = new BaseResponse();
		int partition = HibernateUtil.getPartition(request.getId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			PayRqst payRqst = (PayRqst) session.get(PayRqst.class, request.getId());
			if (payRqst == null) {
				logger.error("找不到支付请求，id=" + request.getId());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			payRqst.setStatus(ConstVariables.STATUS_REJECT);
			payRqst.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			session.update(payRqst);

			// 回退费用
			UserStatus userStatus = (UserStatus) session
					.get(UserStatus.class, payRqst.getUserId(), LockOptions.UPGRADE);
			if (userStatus == null) {
				logger.error("用户信息不存在，id=" + payRqst.getUserId());
			} else {
				userStatus.setCashed(userStatus.getCashed() - payRqst.getAmount());

				session.update(userStatus);
			}

			t.commit();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

	@Override
	public GetFeedbackResponse getFeedback(HttpSession httpSession, GetFeedbackRequest request) {
		GetFeedbackResponse response = new GetFeedbackResponse();
		InfoUserManager infoUserManager = InfoUserManager.getInstance();

		try {
			StringBuilder builder = new StringBuilder();
			builder.append("FROM LogFeedback WHERE status = ?1 AND id >= ?2 ORDER BY id");

			// 初始化断点
			List<Long> saver = request.getSaver();
			if (saver == null) {
				saver = new ArrayList<Long>();
				for (int i = 0; i < HibernateUtil.getPartitions(); i++)
					saver.add(0L);
			} else if (saver.size() < HibernateUtil.getPartitions()) {
				int count = HibernateUtil.getPartitions() - saver.size();
				for (int i = 0; i < count; i++)
					saver.add(0L);
			}

			// 初始化变量
			TreeMap<Long, LogFeedback> treeMap = new TreeMap<Long, LogFeedback>();

			// 获取数据
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				try {
					long id = saver.get(partition);

					Query query = session.createQuery(builder.toString()).setShort("1", request.getStatus())
							.setLong("2", id);

					query.setMaxResults(request.getPageSize());

					int count = 0;
					Iterator<?> iter = query.iterate();
					while (iter.hasNext()) {
						LogFeedback entity = (LogFeedback) iter.next();

						treeMap.put(entity.getId(), entity);
						count++;
					}

					// 已经到达末尾
					if (count < request.getPageSize())
						saver.set(partition, Long.MAX_VALUE);
				} finally {
					session.close();
				}
			}

			List<GetFeedbackItem> items = new ArrayList<GetFeedbackItem>();
			for (LogFeedback entity : treeMap.values()) {
				// 设置结果
				GetFeedbackItem item = new GetFeedbackItem();

				InfoUser infoUser = infoUserManager.get(entity.getUserId());
				if (infoUser != null)
					item.setUserName(infoUser.getUserName());

				item.setId(entity.getId());
				item.setContent(entity.getContent());
				item.setActionDate(entity.getActionDate().getTime());
				items.add(item);

				// 设置断点
				int partition = HibernateUtil.getPartition(entity.getId());
				saver.set(partition, entity.getId() + 1);
			}

			response.setSaver(saver);
			if (!items.isEmpty())
				response.setItems(items);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse handleFeedback(HttpSession httpSession, HandleFeedbackRequest request) {
		BaseResponse response = new BaseResponse();
		int partition = HibernateUtil.getPartition(request.getId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		// 获取客户ID
		InfoCust infoCust = (InfoCust) httpSession.getAttribute(CustIfc.CUST_ATTR);
		long custId = infoCust.getCustId();

		try {
			LogFeedback logFeedback = (LogFeedback) session.get(LogFeedback.class, request.getId());
			if (logFeedback == null) {
				logger.error("找不到反馈信息，id=" + request.getId());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			if (request.getStatus() == ConstVariables.STATUS_PASS && request.getLevel() > 0) {
				InfoUser infoUser = (InfoUser) session
						.get(InfoUser.class, logFeedback.getUserId(), LockOptions.UPGRADE);
				if (infoUser == null) {
					logger.error("用户不存在，userId=" + logFeedback.getUserId());

					logFeedback.setReason("用户不存在");
				} else {
					infoUser.setLevel(infoUser.getLevel() + request.getLevel());
					session.update(infoUser);

					logFeedback.setReason("增加海贝数：" + infoUser.getLevel());
				}
			}

			logFeedback.setStatus(request.getStatus());
			logFeedback.setOperDate(new Timestamp(System.currentTimeMillis()));
			logFeedback.setOperId(custId);
			session.update(logFeedback);

			t.commit();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

}
