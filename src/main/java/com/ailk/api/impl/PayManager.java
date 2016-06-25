package com.ailk.api.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.api.PayIfc;
import com.ailk.api.UserIfc;
import com.ailk.api.UserIfc.Asset;
import com.ailk.api.UserIfc.UserStatSession;
import com.ailk.common.ConstVariables;
import com.ailk.common.GlobalVariables;
import com.ailk.jdbc.AdBalanceManager;
import com.ailk.jdbc.AdParticipantManager;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.PayRqstManager;
import com.ailk.jdbc.UserAdStatusManager;
import com.ailk.jdbc.UserStatusManager;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.cache.InfoAdValue.PriceDayItem;
import com.ailk.jdbc.cache.InfoAdValue.PriceItem;
import com.ailk.jdbc.entity.AdBalance;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.LogInvestigation;
import com.ailk.jdbc.entity.LogInvestigationPK;
import com.ailk.jdbc.entity.LogPromotion;
import com.ailk.jdbc.entity.PayRqst;
import com.ailk.jdbc.entity.UserAdStatus;
import com.ailk.jdbc.entity.UserAdStatusPK;
import com.ailk.jdbc.entity.UserStatus;
import com.ailk.main.batch.UserAdManager;

/**
 * ֧������ʵ��֧����صĲ���
 * <p>
 * ���������ȡһ��������
 * <p>
 * ÿ30����Ƶ�ɶһ�һ���������ý���ÿ������һ��
 * <p>
 * ÿ���ͨ��һ����ɶһ�һ���������Ƽ��Լ������ʾ���⣬�ý��������ͨ��ʱ��Ч
 * 
 * @author xugq
 * 
 */
public class PayManager implements PayIfc {

	private static final Logger logger = Logger.getLogger(PayManager.class);

	public PayManager() {
	}

	@Override
	public LockResponse lock(HttpSession httpSession, LockRequest request) {
		LockResponse response = new LockResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		List<Integer> adIds = request.getItems();
		List<Integer> fails = null;
		List<Short> statuses = null;

		// ��ҪԤ�ȱ�������֤�ڴ�������в������
		long sysdate = GlobalVariables.sysdate;
		if (!GlobalVariables.validSysdate(sysdate, request.getSysdate())) {
			response.setErrorCode(BaseResponse.EEXPIRE);
			return response;
		}
		sysdate = request.getSysdate();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			// ��ȡ�������
			UserStatusManager userStatusManager = UserStatusManager.getInstance();
			InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
			UserAdStatusManager userAdStatusManager = UserAdStatusManager.getInstance();
			AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();

			UserStatus userStatus = userStatusManager.get(session, userId, LockOptions.UPGRADE);
			if (userStatus == null) {
				logger.error("�û�״̬���ݲ����ڣ�userId=" + userId);
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			// ��ȡ�����û�������б�
			Map<UserAdStatusPK, UserAdStatus> userAdStatuses = userAdStatusManager.get(session, userId, adIds);

			// ���ò��ҹؼ���
			UserAdStatusPK userAdStatusPK = new UserAdStatusPK();
			userAdStatusPK.setUserId(userId);

			for (int i = 0; i < adIds.size(); i++) {
				int adId = adIds.get(i);

				// ��ȡ�û����״̬
				userAdStatusPK.setAdId(adId);
				UserAdStatus userAdStatus = userAdStatuses.get(userAdStatusPK);

				// ��ȡ�����Ϣ
				InfoAdValue infoAdValue = infoAdManager.get(adId);
				if (infoAdValue == null || infoAdValue.getExpDate() < System.currentTimeMillis()) {
					if (fails == null) {
						fails = new ArrayList<Integer>();
						statuses = new ArrayList<Short>();
					}

					fails.add(adId);
					statuses.add(ConstVariables.STATUS_OFFLINE);
					continue;
				}

				// �Ѿ�����������������
				if (userAdStatus != null && userAdStatus.getLockAmount() > 0) {
					if (infoAdValue.getAdStyle() == ConstVariables.AD_STYLE_VIDEO) {
						// �����Ƶ�������������ã�����������
						if (userAdStatus.getSysdate() == request.getSysdate())
							continue;
					} else {
						// ��Ҫ��������ʱ��
						userAdStatus.setExpireDate(new Timestamp(System.currentTimeMillis()));
						userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));
						session.update(userAdStatus);
						continue;
					}
				}

				// �������ڻ�ȡ�������
				int days;
				if (userAdStatus == null)
					days = 0;
				else
					days = userAdStatus.getDays();
				PriceDayItem priceDayItem = infoAdValue.getPriceDayItem(days);

				// ��������Ҫ����ԭ�������Ĳ���
				long locked = priceDayItem.getLockPrice();
				if (userAdStatus != null)
					locked -= userAdStatus.getLockAmount();

				if (!adBalanceManager.lock(adId, locked)) {
					if (fails == null) {
						fails = new ArrayList<Integer>();
						statuses = new ArrayList<Short>();
					}

					fails.add(adId);
					statuses.add(ConstVariables.STATUS_EXCEED);
					continue;
				}

				// ��¼�������
				if (userAdStatus == null) {
					userAdStatus = new UserAdStatus();

					UserAdStatusPK pk = userAdStatus.getUserAdStatusPK();
					pk.setUserId(userId);
					pk.setAdId(adId);
					userAdStatus.setSysdate(sysdate);
					userAdStatus.setDays(0);
					userAdStatus.setTimes(0);
					userAdStatus.setLockAmount(locked);
					if (infoAdValue.getExpire() != null)
						userAdStatus.setExpireDate(new Timestamp(System.currentTimeMillis() + infoAdValue.getExpire()));
					userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));

					session.save(userAdStatus);
				} else {
					userAdStatus.setLockAmount(locked);
					if (infoAdValue.getExpire() != null)
						userAdStatus.setExpireDate(new Timestamp(System.currentTimeMillis() + infoAdValue.getExpire()));
					userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));

					session.update(userAdStatus);
				}
			}

			// ����ʧ�ܼ�¼
			if (fails != null) {
				response.setFails(fails);
				response.setStatuses(statuses);
			}

			t.commit();
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

	@Override
	public BaseResponse unlock(HttpSession httpSession, UnlockRequest request) {
		BaseResponse response = new BaseResponse();
		List<PayItem> items = request.getItems();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			// ��ȡ�������
			UserStatusManager userStatusManager = UserStatusManager.getInstance();
			UserAdStatusManager userAdStatusManager = UserAdStatusManager.getInstance();
			AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();

			UserStatus userStatus = userStatusManager.get(session, userId, LockOptions.UPGRADE);
			if (userStatus == null) {
				logger.error("�û�״̬���ݲ����ڣ�userId=" + userId);
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			// ��ȡ�����û�������б�
			List<Integer> adIds = new ArrayList<Integer>();
			for (PayItem item : items) {
				adIds.add(item.getAdId());
			}
			Map<UserAdStatusPK, UserAdStatus> userAdStatuses = userAdStatusManager.get(session, userId, adIds);

			// ���ò��ҹؼ���
			UserAdStatusPK userAdStatusPK = new UserAdStatusPK();
			userAdStatusPK.setUserId(userId);

			for (int i = 0; i < items.size(); i++) {
				PayItem item = items.get(i);

				// ��ȡ�û����״̬
				userAdStatusPK.setAdId(item.getAdId());
				UserAdStatus userAdStatus = userAdStatuses.get(userAdStatusPK);

				// δ����������
				if (userAdStatus == null || userAdStatus.getLockAmount() == 0)
					continue;

				// �������
				if (!adBalanceManager.unlock(item.getAdId(), userAdStatus.getLockAmount())) {
					logger.error("����ʧ�ܣ�adId=" + item.getAdId() + ", amount=" + userAdStatus.getLockAmount());
				}

				// ��¼�������
				userAdStatus.setLockAmount(0);
				userAdStatus.setExpireDate(null);
				userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));
				session.update(userAdStatus);
			}

			t.commit();
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

	@Override
	public ChargeResponse charge(HttpSession httpSession, ChargeRequest request) {
		ChargeResponse response = new ChargeResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		boolean isLockDetail = (request.getLockDetail() == null ? false : request.getLockDetail().booleanValue());
		List<PayItem> items = request.getItems();
		List<ChargeResponseItem> responseItems = new ArrayList<ChargeResponseItem>();

		// ��ҪԤ�ȱ�������֤�ڴ�������в������
		long sysdate = GlobalVariables.sysdate;
		if (request.getSysdate() != 0) {
			if (!GlobalVariables.validSysdate(sysdate, request.getSysdate())) {
				response.setErrorCode(BaseResponse.EEXPIRE);
				return response;
			}
			sysdate = request.getSysdate();
		}

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			// ��ȡ�������
			InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
			UserStatusManager userStatusManager = UserStatusManager.getInstance();
			UserAdStatusManager userAdStatusManager = UserAdStatusManager.getInstance();
			AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
			boolean userStatusChanged = false;
			int level = 0;

			UserStatus userStatus = userStatusManager.get(session, userId, LockOptions.UPGRADE);
			if (userStatus == null) {
				logger.error("�û�״̬���ݲ����ڣ�userId=" + userId);
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			// ����豸���Ƿ�һ��
			if (request.getDevId() == null || !request.getDevId().equals(userStatus.getDevId())) {
				response.setErrorCode(BaseResponse.EDEVID);
				return response;
			}

			// ��ȡ�����û�������б�
			List<Integer> adIds = new ArrayList<Integer>();
			for (PayItem item : items) {
				adIds.add(item.getAdId());
			}
			Map<UserAdStatusPK, UserAdStatus> userAdStatuses = userAdStatusManager.get(session, userId, adIds);

			// ���ò��ҹؼ���
			UserAdStatusPK userAdStatusPK = new UserAdStatusPK();
			userAdStatusPK.setUserId(userId);

			for (int i = 0; i < items.size(); i++) {
				PayItem item = items.get(i);
				int adId = item.getAdId();

				ChargeResponseItem responseItem = new ChargeResponseItem();
				responseItems.add(responseItem);

				// ��ȡ�û����״̬
				userAdStatusPK.setAdId(adId);
				UserAdStatus userAdStatus = userAdStatuses.get(userAdStatusPK);

				// ��ȡ�����Ϣ
				InfoAdValue infoValue = infoAdManager.get(item.getAdId());

				// �������ڻ�ȡ�������
				int days;
				int times;
				if (userAdStatus == null) {
					days = 0;
					times = 0;
				} else {
					// �������Ƶ��Ҫ����ͳ��
					if (infoValue.getAdStyle() == ConstVariables.AD_STYLE_VIDEO && userAdStatus.getSysdate() != sysdate) {
						days = userAdStatus.getDays() + 1;
						times = 0;
					} else {
						if (isLockDetail && userAdStatus.getTimes2() > 0) {
							// �����鿴�����У��������鿴һ��
							responseItem.setTimes(1);
							responseItem.setStatus(ConstVariables.STATUS_NO_MONEY);
							continue;
						}

						days = userAdStatus.getDays();
						times = userAdStatus.getTimes();
					}
				}

				if (infoValue.getAdStyle() == ConstVariables.AD_STYLE_LOCK && !isLockDetail) {
					LockAdSession lockAdSession = (LockAdSession) httpSession.getAttribute(LOCK_AD_ATTR);
					if (lockAdSession == null) {
						lockAdSession = new LockAdSession();
						httpSession.setAttribute(LOCK_AD_ATTR, lockAdSession);
					}

					long validMillis = System.currentTimeMillis() - MILLIS_PER_HOUR;
					List<Long> timestamps = lockAdSession.getTimestamps();
					if (timestamps.size() < VALIDS_PER_HOUR) {
						lockAdSession.setMinIndex(timestamps.size());
						timestamps.add(validMillis + MILLIS_PER_HOUR);
					} else {
						int minIndex = lockAdSession.getMinIndex();
						long minTimestamp = timestamps.get(minIndex);
						if (minTimestamp > validMillis) {
							// �Ѿ��Ʒѣ��ظ��ύ
							logger.error("�û�" + userId + "��һСʱ���ύ��" + VALIDS_PER_HOUR + "����棬�ͻ��˳����쳣���ܵ��˹���");
							responseItem.setTimes(times);
							responseItem.setStatus(ConstVariables.STATUS_NO_MONEY);
							continue;
						}

						timestamps.set(lockAdSession.getMinIndex(), validMillis + MILLIS_PER_HOUR);
						lockAdSession.setMinIndex((minIndex + 1) % VALIDS_PER_HOUR);
					}
				} else if (times > item.getSeq()) {
					// �Ѿ��Ʒѣ��ظ��ύ
					responseItem.setTimes(times);
					responseItem.setStatus(ConstVariables.STATUS_NO_MONEY);
					continue;
				}

				PriceDayItem priceDayItem = infoValue.getPriceDayItem(days);
				int ctimes = 0;
				long chargeAmount = 0;
				for (PriceItem priceItem : priceDayItem.getItems()) {
					if (priceItem.getTimes() <= 0) {
						chargeAmount = priceItem.getPrice();
						break;
					}

					ctimes += priceItem.getTimes();
					// ����Խ�磬��>0������
					if (ctimes > 0 && ctimes <= times) {
						// �Ѿ��Ʒѣ��ظ��ύ
						responseItem.setTimes(times);
						responseItem.setStatus(ConstVariables.STATUS_NO_MONEY);
						continue;
					}

					chargeAmount = priceItem.getPrice();
					break;
				}

				if (chargeAmount <= 0) {
					// �Ʒ�Ϊ0������Ʒ�
					// �Ѿ��Ʒѣ��ظ��ύ
					responseItem.setTimes(times);
					responseItem.setStatus(ConstVariables.STATUS_NO_MONEY);
					continue;
				}

				boolean status;
				long lockAmount;
				boolean locked = (userAdStatus != null && userAdStatus.getLockAmount() > 0);

				// �Ʒ�
				if (locked) { // �Ѿ�����
					lockAmount = userAdStatus.getLockAmount() - chargeAmount;
					status = adBalanceManager.charge(item.getAdId(), 0, chargeAmount);
				} else { // δ����
					if (priceDayItem.getLockPrice() == 0 || priceDayItem.getLockPrice() <= chargeAmount) { // ����Ҫ����
						lockAmount = 0;
						status = adBalanceManager.charge(item.getAdId(), chargeAmount, 0);
					} else { // ��Ҫ����
						lockAmount = priceDayItem.getLockPrice() - chargeAmount;
						status = adBalanceManager.charge(item.getAdId(), priceDayItem.getLockPrice(), lockAmount);
					}
				}

				if (!status) {
					responseItem.setTimes(times + 1);
					responseItem.setStatus(ConstVariables.STATUS_EXCEED);
					continue;
				}

				// ��Ҫ����ͳ�Ƽ�¼
				if (userAdStatus == null) {
					userAdStatus = new UserAdStatus();

					UserAdStatusPK pk = userAdStatus.getUserAdStatusPK();
					pk.setUserId(userId);
					pk.setAdId(adId);
					userAdStatus.setDays(0);
					if (!isLockDetail) {
						userAdStatus.setTimes(1);
						userAdStatus.setTimes2(0);
					} else {
						userAdStatus.setTimes(0);
						userAdStatus.setTimes2(1);
					}
					userAdStatus.setSysdate(sysdate);
					userAdStatus.setLockAmount(lockAmount);
					if (infoValue.getExpire() != null)
						userAdStatus.setExpireDate(new Timestamp(System.currentTimeMillis() + infoValue.getExpire()));
					userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));

					session.save(userAdStatus);
				} else {
					userAdStatus.setSysdate(sysdate);
					userAdStatus.setDays(days);
					if (!isLockDetail)
						userAdStatus.setTimes(times + 1);
					else
						userAdStatus.setTimes2(times + 1);
					userAdStatus.setLockAmount(lockAmount);
					if (infoValue.getExpire() != null)
						userAdStatus.setExpireDate(new Timestamp(System.currentTimeMillis() + infoValue.getExpire()));
					userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));

					session.update(userAdStatus);
				}

				// �����û�����
				switch (infoValue.getAdStyle()) {
				case ConstVariables.AD_STYLE_LOCK:
					userStatus.setLockTimes(userStatus.getLockTimes() + 1);
					userStatus.setLockProfit(userStatus.getLockProfit() + chargeAmount);
					userStatusChanged = true;
					break;
				case ConstVariables.AD_STYLE_SPLASH:
					userStatus.setSlashTimes(userStatus.getSlashTimes() + 1);
					userStatus.setSlashProfit(userStatus.getSlashProfit() + chargeAmount);
					userStatusChanged = true;
					break;
				case ConstVariables.AD_STYLE_VIDEO:
					userStatus.setVideoTimes(userStatus.getVideoTimes() + 1);
					userStatus.setVideoProfit(userStatus.getVideoProfit() + chargeAmount);
					userStatusChanged = true;
					if ((userStatus.getVideoTimes() % ConstVariables.VIDEOS_PER_LEVEL) == 0)
						level++;
					break;
				case ConstVariables.AD_STYLE_PROMOTION:
					userStatus.setPromotionTimes(userStatus.getPromotionTimes() + 1);
					userStatus.setPromotionProfit(userStatus.getPromotionProfit() + chargeAmount);
					userStatusChanged = true;
					if ((userStatus.getVideoTimes() % ConstVariables.PROMOTIONS_PER_LEVEL) == 0)
						level++;
					break;
				}

				// ���üƷѽ��
				responseItem.setProfit(chargeAmount);
				// ���õ�ǰ�Ĵ���
				responseItem.setTimes(times + 1);
			}

			// �����û�״̬
			if (userStatusChanged)
				session.update(userStatus);

			// ���µȼ�
			if (level > 0) {
				InfoUser infoUser = (InfoUser) session.get(InfoUser.class, userId);
				infoUser.setLevel(infoUser.getLevel() + level);
				session.update(infoUser);
			}

			// �ύ����
			t.commit();

			// �����ʲ�
			response.setItems(responseItems);
			response.setAsset(new Asset(userStatus));
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		if (sysdate != GlobalVariables.sysdate) {
			// �û��ύ��������ϵͳʱ�����л�������Ҫ���������б�
			try {
				UserAdManager userAdManager = new UserAdManager(session, new Timestamp(System.currentTimeMillis()),
						GlobalVariables.sysdate);
				userAdManager.update(globalManager, userId);
			} catch (Exception e) {
			}
		}

		return response;
	}

	@Override
	public CommitPromotionResponse commitPromotion(HttpSession httpSession, CommitPromotionRequest request) {
		CommitPromotionResponse response = new CommitPromotionResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		AdParticipantManager adParticipantManager = AdParticipantManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			UserStatusManager userStatusManager = UserStatusManager.getInstance();
			UserStatus userStatus = userStatusManager.get(session, userId, LockOptions.UPGRADE);
			if (userStatus == null) {
				logger.error("�û�״̬���ݲ����ڣ�userId=" + userId);
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			if (request.getInvestigations() == null) {
				// û�е����ʾ�
				InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
				InfoAdValue infoAdValue = infoAdManager.get(request.getAdId());

				if (infoAdValue != null && infoAdValue.isHasInvestigations()) {
					// ��Ҫ�����ʾ�������ݿ��ѯ����ȫ
					LogInvestigationPK logInvestigationPK = new LogInvestigationPK();
					logInvestigationPK.setUserId(userId);
					logInvestigationPK.setAdId(request.getAdId());
					LogInvestigation logInvestigation = (LogInvestigation) session.get(LogInvestigation.class,
							logInvestigationPK);
					if (logInvestigation != null)
						request.setInvestigations(logInvestigation.getInvestigations());
				}
			} else {
				// �е����ʾ������
				LogInvestigationPK logInvestigationPK = new LogInvestigationPK();
				logInvestigationPK.setUserId(userId);
				logInvestigationPK.setAdId(request.getAdId());

				LogInvestigation logInvestigation = new LogInvestigation();
				logInvestigation.setLogInvestigationPK(logInvestigationPK);
				logInvestigation.setInvestigations(request.getInvestigations());
				session.saveOrUpdate(logInvestigation);
			}

			LogPromotion logPromotion = new LogPromotion();
			logPromotion.setUserId(userId);
			logPromotion.setAdId(request.getAdId());
			logPromotion.setActionDate(new Timestamp(request.getActionDate()));
			logPromotion.setInvestigations(request.getInvestigations());
			logPromotion.setAnswers(request.getAnswers());
			logPromotion.setLastUpdate(new Timestamp(System.currentTimeMillis()));

			// ��ȡ�����û�������б�
			UserAdStatusPK userAdStatusPK = new UserAdStatusPK();
			userAdStatusPK.setUserId(userId);
			userAdStatusPK.setAdId(request.getAdId());

			UserAdStatus userAdStatus = (UserAdStatus) session.get(UserAdStatus.class, userAdStatusPK);
			while (true) {
				if (userAdStatus == null) {
					logPromotion.setStatus(ConstVariables.STATUS_REJECT);
					logPromotion.setReason("���ʧ�ܣ�ϵͳ�쳣");
					break;
				}

				if (userAdStatus.isRejected()) {
					logPromotion.setStatus(ConstVariables.STATUS_FORBIDDEN);
					logPromotion.setReason("���ʧ�ܣ���Ѿܾ�");
					break;
				}

				AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
				InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

				// ��ȡ�����Ϣ
				InfoAdValue infoAdValue = infoAdManager.get(request.getAdId());
				if (infoAdValue == null) {
					logPromotion.setStatus(ConstVariables.STATUS_OFFLINE);
					logPromotion.setReason("���ʧ�ܣ��������");
					break;
				}

				// �������
				PriceDayItem priceDayItem = infoAdValue.getPriceDayItem(0);
				int times = 0;
				long chargeAmount = 0;
				for (PriceItem priceItem : priceDayItem.getItems()) {
					if (priceItem.getTimes() <= 0) {
						times = Integer.MAX_VALUE;
						chargeAmount = priceItem.getPrice();
						break;
					}

					times += priceItem.getTimes();
					// ����Խ��
					if (times <= 0) {
						times = Integer.MAX_VALUE;
						chargeAmount = priceItem.getPrice();
						break;
					}
				}

				// ����������Ƿ񳬹�
				if (times <= userAdStatus.getTimes()) {
					logPromotion.setStatus(ConstVariables.STATUS_ELIMIT);
					logPromotion.setReason("���ʧ�ܣ�������μӴ���");
					break;
				}

				if (request.isComplete()) {
					boolean status;
					long lockAmount;
					boolean locked = (userAdStatus != null && userAdStatus.getLockAmount() > 0);

					// �Ʒ�
					if (locked) { // �Ѿ�����
						lockAmount = userAdStatus.getLockAmount() - chargeAmount;
						status = adBalanceManager.charge(request.getAdId(), 0, chargeAmount);
					} else { // δ����
						if (priceDayItem.getLockPrice() == 0 || priceDayItem.getLockPrice() <= chargeAmount) { // ����Ҫ����
							lockAmount = 0;
							status = adBalanceManager.charge(request.getAdId(), chargeAmount, 0);
						} else { // ��Ҫ����
							lockAmount = priceDayItem.getLockPrice() - chargeAmount;
							status = adBalanceManager
									.charge(request.getAdId(), priceDayItem.getLockPrice(), lockAmount);
						}
					}

					// �Ʒ�
					if (!status) {
						logPromotion.setStatus(ConstVariables.STATUS_REJECT);
						logPromotion.setReason("���ʧ�ܣ���������");
						break;
					}

					logPromotion.setProfit(chargeAmount);
					logPromotion.setStatus(ConstVariables.STATUS_PENDING);

					// ���²�������
					adParticipantManager.update(request.getAdId());
				} else {
					// ����
					if (!adBalanceManager.unlock(request.getAdId(), userAdStatus.getLockAmount()))
						logger.error("����ʧ�ܣ�adId=" + request.getAdId());

					logPromotion.setStatus(ConstVariables.STATUS_FORBIDDEN);
					logPromotion.setReason("���ʧ�ܣ���Ѿܾ�");
					userAdStatus.setRejected(true);
				}

				// �����û����״̬��
				userAdStatus.setTimes(userAdStatus.getTimes() + 1);
				userAdStatus.setLockAmount(0L);
				userAdStatus.setExpireDate(null);
				userAdStatus.setLastUpdate(new Timestamp(request.getTimestamp()));
				session.update(userAdStatus);
				break;
			}

			// ������־
			session.save(logPromotion);

			// �ύ����
			t.commit();

			// ����Ӧ��
			response.setId(logPromotion.getId());
			response.setStatus(logPromotion.getStatus());
			response.setReason(logPromotion.getReason());
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

	@Override
	public BalanceResponse getBalance(int adId) {
		BalanceResponse response = new BalanceResponse();
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();

		AdBalance adBalance = adBalanceManager.get(adId);
		if (adBalance == null) {
			logger.error("���������Ϣ������");
			response.setErrorCode(BaseResponse.ENOENT);
			return response;
		}

		response.setBudget(adBalance.getBudget());
		response.setRemain(adBalance.getRemain());
		response.setLocked(adBalance.getLocked());
		return response;
	}

	@Override
	public RqstPayResponse rqstPay(HttpSession httpSession, RqstPayRequest request) {
		RqstPayResponse response = new RqstPayResponse();
		PayRqstManager payRqstManager = PayRqstManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			// �������
			InfoUser infoUser = (InfoUser) session.get(InfoUser.class, userId);
			if (infoUser == null || infoUser.getPassword() == null
					|| !infoUser.getPassword().equals(request.getPassword())) {
				response.setErrorCode(BaseResponse.EPERM);
				return response;
			}

			// ������
			UserStatus userStatus = (UserStatus) session.get(UserStatus.class, userId);
			if (userStatus == null) {
				response.setErrorCode(BaseResponse.ELIMIT);
				return response;
			}

			// ���������û�����֤ͬһ�û������󲻻�ͬʱ����
			session.buildLockRequest(LockOptions.UPGRADE).lock(userStatus);

			// �������Ƿ����
			long profit = userStatus.getLockProfit() + userStatus.getVideoProfit() + userStatus.getPromotionProfit()
					+ userStatus.getRecomProfit() - userStatus.getCashed();
			if (profit < request.getAmount()) {
				response.setErrorCode(BaseResponse.ELIMIT);
				return response;
			}

			// ��¼֧������
			PayRqst payRqst = new PayRqst();
			payRqst.setUserId(userId);
			payRqst.setPayType(request.getPayType());
			payRqst.setPayName(request.getPayName());
			payRqst.setPayId(request.getPayId());
			payRqst.setAmount(request.getAmount());
			payRqst.setStatus(ConstVariables.STATUS_PENDING);
			payRqst.setRqstDate(new Timestamp(System.currentTimeMillis()));
			payRqst.setLastUpdate(payRqst.getRqstDate());

			try {
				payRqstManager.save(payRqst);
			} catch (Exception e) {
				logger.error("ϵͳ�쳣��" + e);
				response.setErrorCode(BaseResponse.ESYSTEM);
			}

			// �������
			userStatus.setCashed(userStatus.getCashed() + request.getAmount());
			session.save(userStatus);
			t.commit();

			// �����ʲ�
			response.setAsset(new Asset(userStatus));
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}

		return response;
	}

	@Override
	public GetPayResponse getPayRqst(HttpSession httpSession, GetPayRequest request) {
		GetPayResponse response = new GetPayResponse();
		PayRqstManager payRqstManager = PayRqstManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		try {
			List<PayRqst> payRqsts = payRqstManager.get(userId, request.getMaxId(), ConstVariables.PAYS_PER_BATCH);
			if (payRqsts == null || payRqsts.isEmpty()) {
				response.setEnd(true);
				return response;
			}

			List<GetPayItem> items = new ArrayList<GetPayItem>();
			for (PayRqst payRqst : payRqsts) {
				GetPayItem item = new GetPayItem();

				item.setId(payRqst.getId());
				item.setPayType(payRqst.getPayType());
				item.setAmount(payRqst.getAmount());
				item.setStatus(payRqst.getStatus());
				item.setReason(payRqst.getReason());
				item.setRqstDate(payRqst.getRqstDate().getTime());
				items.add(item);
			}

			// ����Ӧ��
			response.setEnd(items.size() < ConstVariables.PAYS_PER_BATCH);
			response.setItems(items);
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

}