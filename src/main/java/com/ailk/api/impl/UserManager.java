package com.ailk.api.impl;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.ailk.api.UserIfc;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.LogFeedbackManager;
import com.ailk.jdbc.NameUserManager;
import com.ailk.jdbc.RankBoardManager;
import com.ailk.jdbc.UserStatusManager;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.LogFeedback;
import com.ailk.jdbc.entity.NameUser;
import com.ailk.jdbc.entity.NameUserPK;
import com.ailk.jdbc.entity.UserStatus;
import com.ailk.open.PushManager;
import com.ailk.open.SmsSend;

/**
 * �û�����ʵ���û���صĲ���
 * <p>
 * ���������ȡһ��������
 * <p>
 * ע��ɹ�����һ���������ý���������Ч
 * <p>
 * ÿ�μ���������������ɺ���һ���������ý������ɺ�������Ч
 * 
 * @author xugq
 * 
 */
public class UserManager implements UserIfc {

	private static final Logger logger = Logger.getLogger(UserManager.class);
	private static AtomicInteger inviteCode;

	static {
		int seed = (int) (System.currentTimeMillis() / 1000);
		seed = seed / HibernateUtil.getPartitions() * HibernateUtil.getPartitions();
		seed += HibernateUtil.getPartition();

		inviteCode = new AtomicInteger(seed);
	}

	public UserManager() {
	}

	@Override
	public BaseResponse exists(ExistsRequest request) {
		BaseResponse response = new BaseResponse();
		NameUserManager nameUserManager = NameUserManager.getInstance();

		try {
			NameUser nameUser = nameUserManager.get(request.getUserName(), request.isRegistered());
			if (nameUser == null) {
				logger.debug("�û������ڣ�userName = " + request.getUserName() + ", registered = " + request.isRegistered());
				response.setErrorCode(BaseResponse.ENOENT);
			}
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public RegisterResponse register(HttpSession httpSession, RegisterRequest request) {
		RegisterResponse response = new RegisterResponse();
		NameUser nameUser = null;
		InfoUser infoUser = request.getInfoUser();
		infoUser.setInviteCode(genInviteCode()); // ����������
		infoUser.setRegDate(new Timestamp(System.currentTimeMillis())); // ����ע������

		// ��ʼ�����ݿ�����
		NameUserPK nameUserPK = new NameUserPK();
		nameUserPK.setUserName(infoUser.getUserName());
		nameUserPK.setRegistered(infoUser.getRegistered());
		int partition = HibernateUtil.getPartition(nameUserPK.hashCode());
		Session nameSession = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction nameT = nameSession.beginTransaction();
		Session userSession = null;
		Transaction userT = null;

		try {
			if (!infoUser.getRegistered()) {
				// ���豸��ע�ᣬ�ȼ���Ƿ����
				nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
				if (nameUser == null) {
					// ���ݷ���ֵ�������ǹ��ã����ǵ�����������
					int userPartition = HibernateUtil.getPartition();
					if (userPartition != partition) {
						userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
						userT = userSession.beginTransaction();
					} else {
						userSession = nameSession;
					}

					// ����ע���û��ȼ�Ϊ0
					infoUser.setLevel(0);

					userSession.save(infoUser);
					userSession.flush();

					// ����������ID�Ĺ�����ϵ
					nameUser = new NameUser();
					nameUser.setNameUserPK(nameUserPK);
					nameUser.setUserId(infoUser.getUserId());

					nameSession.save(nameUser);
					nameSession.flush();
				} else {
					// ���ݷ���ֵ�������ǹ��ã����ǵ�����������
					int userPartition = HibernateUtil.getPartition(nameUser.getUserId());
					if (userPartition != partition) {
						userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
						userT = userSession.beginTransaction();
					} else {
						userSession = nameSession;
					}

					infoUser = (InfoUser) userSession.get(InfoUser.class, nameUser.getUserId());
				}
			} else {
				// �û�����ע�ᣬ��Ҫ�����֤��
				if (!isValidVerification(httpSession, infoUser.getUserName(), request.getVerifyCode())) {
					response.setErrorCode(BaseResponse.EINVAL);
					return response;
				}

				// ���ݷ���ֵ�������ǹ��ã����ǵ�����������
				int userPartition = HibernateUtil.getPartition();
				if (userPartition != partition) {
					userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
					userT = userSession.beginTransaction();
				} else {
					userSession = nameSession;
				}

				// ����ע���û�����һ���ȼ�
				infoUser.setLevel(1);

				userSession.save(infoUser);
				userSession.flush();

				// ����������ID�Ĺ�����ϵ
				nameUser = new NameUser();
				nameUser.setNameUserPK(nameUserPK);
				nameUser.setUserId(infoUser.getUserId());

				nameSession.save(nameUser);
				nameSession.flush();
			}

			// �����û����豸��Ϣ
			UserStatus userStatus = (UserStatus) userSession.get(UserStatus.class, nameUser.getUserId());
			if (userStatus == null) {
				userStatus = new UserStatus();
				userStatus.setUserId(nameUser.getUserId());
				userStatus.setDevId(infoUser.getDevId());
				userStatus.setBdUserId(request.getBdUserId());
				userStatus.setLoginTime(new Timestamp(System.currentTimeMillis()));

				userSession.save(userStatus);
			} else {
				userStatus.setDevId(infoUser.getDevId());
				userStatus.setBdUserId(request.getBdUserId());
				userStatus.setLoginTime(new Timestamp(System.currentTimeMillis()));

				userSession.update(userStatus);
			}

			// �ύ����
			if (userT != null)
				userT.commit();
			nameT.commit();

			// �޸ĻỰ
			UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
			if (userStatSession == null) {
				userStatSession = new UserStatSession();
				httpSession.setAttribute(UserIfc.USER_STAT_ATTR, userStatSession);
			}
			userStatSession.setUserId(infoUser.getUserId());
			userStatSession.setDevId(infoUser.getDevId());

			// ����Ӧ��
			response.setUserId(infoUser.getUserId());
			response.setUserName(infoUser.getUserName());
			response.setRegistered(infoUser.getRegistered());
			response.setPassword(infoUser.getPassword());
			response.setNickName(infoUser.getNickName());
			response.setPortrait(infoUser.getPortrait());
			response.setGender(infoUser.getGender());
			response.setLevel(Math.min(infoUser.getLevel(), ConstVariables.MAX_LEVEL));
			response.setMaxVideos(getMaxVideos(response.getLevel())); // ���ݵȼ���ȡ���ɹۿ���Ƶ��
			response.setInviteCode(infoUser.getInviteCode());
			response.setAsset(new Asset(userStatus));
			return response;
		} catch (ConstraintViolationException e) {
			logger.debug("������ͻ���û���Ϣ�ظ�");
			response.setErrorCode(BaseResponse.EDUPENT);
			return response;
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
			return response;
		} finally {
			if (userT != null) {
				if (userT.isActive())
					userT.rollback();
				userSession.close();
			}

			if (nameT.isActive())
				nameT.rollback();
			nameSession.close();
		}
	}

	@Override
	public LoginResponse login(HttpSession httpSession, LoginRequest request) {
		LoginResponse response = new LoginResponse();

		// ��ʼ�����ݿ�����
		NameUserPK nameUserPK = new NameUserPK();
		nameUserPK.setUserName(request.getUserName());
		nameUserPK.setRegistered(request.isRegistered());
		int partition = HibernateUtil.getPartition(nameUserPK.hashCode());
		Session nameSession = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction nameT = nameSession.beginTransaction();
		Session userSession = null;
		Transaction userT = null;

		try {
			NameUser nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
			if (nameUser == null) {
				logger.debug("�û������ڣ�userName = " + request.getUserName() + ", registered = " + request.isRegistered());
				response.setErrorCode(BaseResponse.ENOENT);
				return response;
			}

			int userPartition = HibernateUtil.getPartition(nameUser.getUserId());
			if (userPartition != partition) {
				userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
				userT = userSession.beginTransaction();
			} else {
				userSession = nameSession;
			}

			InfoUser infoUser = (InfoUser) userSession.get(InfoUser.class, nameUser.getUserId());
			if (infoUser.getRegistered().booleanValue() && !infoUser.getPassword().equals(request.getPassword())) {
				logger.debug("���벻��ȷ��ϵͳ���룺" + infoUser.getPassword() + ", �������룺" + request.getPassword());
				response.setErrorCode(BaseResponse.EPERM);
				return response;
			}

			// �����û����豸��Ϣ
			UserStatus userStatus = (UserStatus) userSession.get(UserStatus.class, nameUser.getUserId());
			if (userStatus == null) {
				userStatus = new UserStatus();
				userStatus.setUserId(nameUser.getUserId());
				userStatus.setDevId(request.getDevId());
				userStatus.setBdUserId(request.getBdUserId());
				userStatus.setLoginTime(new Timestamp(System.currentTimeMillis()));

				userSession.save(userStatus);
			} else {
				// ����û��ڱ���豸���ѵ�¼������Ҫ����֪ͨ
				if (userStatus.getDevId() != null && !userStatus.getDevId().equals(request.getDevId())
						&& userStatus.getBdUserId() != null) {
					PushManager pushManager = PushManager.getInstance();
					PushManager.KickUserResponse kickUserResponse = new PushManager.KickUserResponse();

					kickUserResponse.setUserId(infoUser.getUserId());
					kickUserResponse.setLastUpdate(System.currentTimeMillis());
					pushManager.sendMessage(userStatus.getBdUserId(), kickUserResponse);
				}

				userStatus.setDevId(request.getDevId());
				userStatus.setBdUserId(request.getBdUserId());
				userStatus.setLoginTime(new Timestamp(System.currentTimeMillis()));

				userSession.update(userStatus);
			}

			// �ύ����
			if (userT != null)
				userT.commit();
			nameT.commit();

			// �޸ĻỰ
			UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
			if (userStatSession == null) {
				userStatSession = new UserStatSession();
				httpSession.setAttribute(UserIfc.USER_STAT_ATTR, userStatSession);
			}
			userStatSession.setUserId(infoUser.getUserId());
			userStatSession.setDevId(infoUser.getDevId());

			// ����Ӧ��
			response.setUserId(infoUser.getUserId());
			response.setUserName(infoUser.getUserName());
			response.setRegistered(infoUser.getRegistered());
			response.setPassword(infoUser.getPassword());
			response.setNickName(infoUser.getNickName());
			response.setPortrait(infoUser.getPortrait());
			response.setGender(infoUser.getGender());
			response.setLevel(Math.min(infoUser.getLevel(), ConstVariables.MAX_LEVEL));
			response.setMaxVideos(getMaxVideos(response.getLevel())); // ���ݵȼ���ȡ���ɹۿ���Ƶ��
			response.setInviteCode(infoUser.getInviteCode());
			response.setAsset(new Asset(userStatus));
			return response;
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
			return response;
		} finally {
			if (userT != null) {
				if (userT.isActive())
					userT.rollback();
				userSession.close();
			}

			if (nameT.isActive())
				nameT.rollback();
			nameSession.close();
		}
	}

	@Override
	public BaseResponse logout(HttpSession httpSession, LogoutRequest request) {
		BaseResponse response = new BaseResponse();

		// ɾ���Ự
		httpSession.removeAttribute(UserIfc.USER_STAT_ATTR);

		return response;
	}

	@Override
	public BaseResponse sendVerify(HttpSession httpSession, SendVerifyRequest request) {
		BaseResponse response = new BaseResponse();

		String verifyCode = SmsSend.sendSms(request.getUserName());
		if (verifyCode != null) {
			VerifyCodeSession verifyCodeSession = new VerifyCodeSession();
			verifyCodeSession.setUserName(request.getUserName());
			verifyCodeSession.setVerifyCode(verifyCode);
			httpSession.setAttribute(VERIFY_CODE_ATTR, verifyCodeSession);
		} else {
			response.setErrorCode(BaseResponse.EVERIFY);
		}

		return response;
	}

	@Override
	public GetAssetResponse getAsset(HttpSession httpSession, GetAssetRequest request) {
		GetAssetResponse response = new GetAssetResponse();
		UserStatusManager userStatusManager = UserStatusManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// �����û����豸��Ϣ
		UserStatus userStatus = userStatusManager.get(userId);
		if (userStatus == null) {
			logger.error("�Ҳ����û���Ӧ���ʲ���Ϣ");
			response.setErrorCode(BaseResponse.ENOENT);
			return response;
		}

		response.setAsset(new Asset(userStatus));
		return response;
	}

	@Override
	public BaseResponse update(HttpSession httpSession, UpdateRequest request) {
		BaseResponse response = new BaseResponse();
		InfoUser requestInfoUser = request.getInfoUser();
		boolean verified = isValidVerification(httpSession, requestInfoUser.getUserName(), request.getVerifyCode());

		// ����޸��û���������Ҫ�ṩ��֤��
		if (requestInfoUser.getUserName() != null && !verified) {
			logger.error("�޸�ע���ֻ���ʱ�ṩ����֤�벻��ȷ");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// ����޸����룬����Ҫ�ṩ������
		if (requestInfoUser.getPassword() != null && request.getOldPassword() == null && !verified) {
			logger.error("�޸�����ʱ�ṩ�ľ����벻��ȷ");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// ��ʼ�����ݿ�����
		int partition = HibernateUtil.getPartition(userId);
		int namePartition = 0;
		Session userSession = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction userT = userSession.beginTransaction();
		Session nameSession = null;
		Transaction nameT = null;
		Session newNameSession = null;
		Transaction newNameT = null;

		// ��ȡ�û���Ϣ
		InfoUser infoUser = (InfoUser) userSession.get(InfoUser.class, userId);

		// ʵ���������ע�ᣬ������һ���ȼ�
		if (infoUser.getRegistered().booleanValue() == false && requestInfoUser.getRegistered() != null
				&& requestInfoUser.getRegistered().booleanValue() == true)
			infoUser.setLevel(infoUser.getLevel() + 1);

		try {
			// ����Ƿ���Ҫ�޸��û���
			boolean userNameChanged = (requestInfoUser.getUserName() != null
					&& !requestInfoUser.getUserName().equals(infoUser.getUserName()) || requestInfoUser.getRegistered() != null
					&& !requestInfoUser.getRegistered().equals(infoUser.getRegistered()));

			if (userNameChanged) {
				// ��ʼ�����ݿ�
				NameUserPK nameUserPK = new NameUserPK();
				nameUserPK.setUserName(infoUser.getUserName());
				nameUserPK.setRegistered(infoUser.getRegistered());

				namePartition = HibernateUtil.getPartition(nameUserPK.hashCode());
				if (namePartition != partition) {
					nameSession = HibernateUtil.getSessionFactory(namePartition).openSession();
					nameT = nameSession.beginTransaction();
				} else {
					nameSession = userSession;
				}

				// �����û���ӳ��
				NameUser nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
				if (nameUser == null) {
					logger.debug("�û������ڣ�userName = " + requestInfoUser.getUserName());
					response.setErrorCode(BaseResponse.ENOENT);
					return response;
				}

				// �����û���ʱ��Ҫ��ɾ��
				nameSession.delete(nameUser);
			}

			// �ϲ���������
			if (requestInfoUser.getUserName() != null)
				infoUser.setUserName(requestInfoUser.getUserName());
			if (requestInfoUser.getRegistered() != null)
				infoUser.setRegistered(requestInfoUser.getRegistered());
			if (requestInfoUser.getPassword() != null)
				infoUser.setPassword(requestInfoUser.getPassword());
			if (requestInfoUser.getNickName() != null)
				infoUser.setNickName(requestInfoUser.getNickName());
			if (requestInfoUser.getPortrait() != null)
				infoUser.setPortrait(requestInfoUser.getPortrait());
			if (requestInfoUser.getGender() != null)
				infoUser.setGender(requestInfoUser.getGender());
			if (requestInfoUser.getAge() != null)
				infoUser.setAge(requestInfoUser.getAge());
			if (requestInfoUser.getLocation() != null)
				infoUser.setLocation(requestInfoUser.getLocation());

			// �����û���Ϣ
			userSession.update(infoUser);

			// �����û�ӳ��
			if (userNameChanged) {
				// ��ʼ�����ݿ�
				NameUser nameUser = new NameUser();
				NameUserPK nameUserPK = nameUser.getNameUserPK();
				nameUserPK.setUserName(infoUser.getUserName());
				nameUserPK.setRegistered(infoUser.getRegistered());
				nameUser.setUserId(infoUser.getUserId());

				int newNamePartition = HibernateUtil.getPartition(nameUserPK.hashCode());
				if (newNamePartition == partition) {
					newNameSession = userSession;
				} else if (newNamePartition == namePartition) {
					newNameSession = nameSession;
				} else {
					newNameSession = HibernateUtil.getSessionFactory(newNamePartition).openSession();
					newNameT = newNameSession.beginTransaction();
				}

				newNameSession.save(nameUser);
			}

			// ���ո��ύ˳�򣬿ɱ�֤�м����κ��쳣ʱ���ɱ�֤���û��ܱ��ҵ�
			if (newNameT != null)
				newNameT.commit();
			userT.commit();
			if (nameT != null)
				nameT.commit();
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (newNameT != null) {
				if (newNameT.isActive())
					newNameT.rollback();
				newNameSession.close();
			}

			if (nameT != null) {
				if (nameT.isActive())
					nameT.rollback();
				nameSession.close();
			}

			if (userT.isActive())
				userT.rollback();
			userSession.close();
		}

		return response;
	}

	@Override
	public BaseResponse resetPassword(HttpSession httpSession, ResetPasswordRequest request) {
		BaseResponse response = new BaseResponse();
		boolean verified = isValidVerification(httpSession, request.getUserName(), request.getVerifyCode());

		if (!verified) {
			logger.error("��������ʱ�ṩ����֤�벻��ȷ");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		if (request.getPassword() == null) {
			logger.error("��������ʱ�ṩ������Ϊ��");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// ��ʼ�����ݿ�����
		NameUserPK nameUserPK = new NameUserPK();
		nameUserPK.setUserName(request.getUserName());
		nameUserPK.setRegistered(true);
		int partition = HibernateUtil.getPartition(nameUserPK.hashCode());
		Session nameSession = HibernateUtil.getSessionFactory(partition).openSession();
		Session userSession = null;
		Transaction userT = null;

		try {
			// �����û�ID
			NameUser nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
			if (nameUser == null) {
				logger.debug("�û����ô��ڣ�userName = " + request.getUserName());
				response.setErrorCode(BaseResponse.EDUPENT);
				return response;
			}

			// ���ݷ���ֵ�������ǹ��ã����ǵ�����������
			int userPartition = HibernateUtil.getPartition(nameUser.getUserId());
			if (userPartition != partition)
				userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
			else
				userSession = nameSession;

			userT = userSession.beginTransaction();

			// ��ȡ�û���Ϣ
			InfoUser infoUser = (InfoUser) userSession.get(InfoUser.class, nameUser.getUserId());
			infoUser.setPassword(request.getPassword());

			// �����û���Ϣ
			userSession.update(infoUser);

			// �ύ����
			userT.commit();
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		} finally {
			if (userT != null) {
				if (userT.isActive())
					userT.rollback();
				userSession.close();
			}

			nameSession.close();
		}

		return response;
	}

	@Override
	public GetRankBoardResponse getRankBoard(GetRankBoardRequest request) {
		GetRankBoardResponse response = new GetRankBoardResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		RankBoardManager rankBoardManager = RankBoardManager.getInstance(globalManager);

		response.setItems(rankBoardManager.getCache());
		return response;
	}

	@Override
	public BaseResponse addFeedback(HttpSession httpSession, AddFeedbackRequest request) {
		BaseResponse response = new BaseResponse();
		LogFeedbackManager logFeedbackManager = LogFeedbackManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// �����û�ID
		LogFeedback logFeedback = new LogFeedback();
		logFeedback.setUserId(userId);
		logFeedback.setContent(request.getContent());
		logFeedback.setActionDate(new Timestamp(System.currentTimeMillis()));
		logFeedback.setStatus(ConstVariables.STATUS_PENDING);

		try {
			logFeedbackManager.save(logFeedback);
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse addBehaviors(HttpSession httpSession, String request) {
		BaseResponse response = new BaseResponse();
		AuditManager auditManager = AuditManager.getInstance();

		// ��ȡuserId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		auditManager.syslog(userId, request);

		return response;
	}

	private boolean isValidVerification(HttpSession httpSession, String userName, String verifyCode) {
		return true;
		/*
		 * VerifyCodeSession verifyCodeSession = (VerifyCodeSession)
		 * httpSession.getAttribute(VERIFY_CODE_ATTR); if (verifyCodeSession ==
		 * null) return false;
		 * 
		 * if (verifyCodeSession.getUserName() == null) return false;
		 * 
		 * if (verifyCodeSession.getVerifyCode() == null) return false;
		 * 
		 * return verifyCodeSession.getUserName().equals(userName) &&
		 * verifyCodeSession.getVerifyCode().equals(verifyCode);
		 */
	}

	private String genInviteCode() {
		int localInviteCode = inviteCode.getAndAdd(HibernateUtil.getPartitions());
		String inviteCodeStr = "";

		for (int i = 0; i < 6; i++) {
			int remain = localInviteCode % 36;
			localInviteCode /= 36;

			if (remain < 10)
				inviteCodeStr += (char) ('0' + remain);
			else
				inviteCodeStr += (char) ('A' + remain - 10);
		}

		return inviteCodeStr;
	}

	/**
	 * ���ݵȼ���ȡ�ɿ���Ƶ��
	 * 
	 * @param level
	 *            �ȼ�
	 * @return ��Ƶ��
	 */
	private int getMaxVideos(int level) {
		int realLevel = level / 4;

		if (realLevel < 4)
			return 6;
		else if (realLevel < 8)
			return 7;
		else if (realLevel < 12)
			return 8;
		else if (realLevel < 16)
			return 9;
		else if (realLevel < 32)
			return 10;
		else if (realLevel < 48)
			return 11;
		else if (realLevel < 64)
			return 12;
		else if (realLevel < 128)
			return 13;
		else if (realLevel < 192)
			return 14;
		else
			return 15;
	}

}
