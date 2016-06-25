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
 * 用户管理，实现用户相关的操作
 * <p>
 * 以下情况获取一个海贝：
 * <p>
 * 注册成功奖励一个海贝，该奖励立即生效
 * <p>
 * 每参加意见反馈，经采纳后奖励一个海贝，该奖励采纳后立即生效
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
				logger.debug("用户不存在，userName = " + request.getUserName() + ", registered = " + request.isRegistered());
				response.setErrorCode(BaseResponse.ENOENT);
			}
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public RegisterResponse register(HttpSession httpSession, RegisterRequest request) {
		RegisterResponse response = new RegisterResponse();
		NameUser nameUser = null;
		InfoUser infoUser = request.getInfoUser();
		infoUser.setInviteCode(genInviteCode()); // 设置邀请码
		infoUser.setRegDate(new Timestamp(System.currentTimeMillis())); // 设置注册日期

		// 初始化数据库连接
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
				// 用设备号注册，先检查是否存在
				nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
				if (nameUser == null) {
					// 根据分区值，决定是共用，还是单独创建连接
					int userPartition = HibernateUtil.getPartition();
					if (userPartition != partition) {
						userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
						userT = userSession.beginTransaction();
					} else {
						userSession = nameSession;
					}

					// 被动注册用户等级为0
					infoUser.setLevel(0);

					userSession.save(infoUser);
					userSession.flush();

					// 保存名称与ID的关联关系
					nameUser = new NameUser();
					nameUser.setNameUserPK(nameUserPK);
					nameUser.setUserId(infoUser.getUserId());

					nameSession.save(nameUser);
					nameSession.flush();
				} else {
					// 根据分区值，决定是共用，还是单独创建连接
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
				// 用户主动注册，需要检查验证码
				if (!isValidVerification(httpSession, infoUser.getUserName(), request.getVerifyCode())) {
					response.setErrorCode(BaseResponse.EINVAL);
					return response;
				}

				// 根据分区值，决定是共用，还是单独创建连接
				int userPartition = HibernateUtil.getPartition();
				if (userPartition != partition) {
					userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
					userT = userSession.beginTransaction();
				} else {
					userSession = nameSession;
				}

				// 主动注册用户赠送一个等级
				infoUser.setLevel(1);

				userSession.save(infoUser);
				userSession.flush();

				// 保存名称与ID的关联关系
				nameUser = new NameUser();
				nameUser.setNameUserPK(nameUserPK);
				nameUser.setUserId(infoUser.getUserId());

				nameSession.save(nameUser);
				nameSession.flush();
			}

			// 更新用户的设备信息
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

			// 提交事务
			if (userT != null)
				userT.commit();
			nameT.commit();

			// 修改会话
			UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
			if (userStatSession == null) {
				userStatSession = new UserStatSession();
				httpSession.setAttribute(UserIfc.USER_STAT_ATTR, userStatSession);
			}
			userStatSession.setUserId(infoUser.getUserId());
			userStatSession.setDevId(infoUser.getDevId());

			// 设置应答
			response.setUserId(infoUser.getUserId());
			response.setUserName(infoUser.getUserName());
			response.setRegistered(infoUser.getRegistered());
			response.setPassword(infoUser.getPassword());
			response.setNickName(infoUser.getNickName());
			response.setPortrait(infoUser.getPortrait());
			response.setGender(infoUser.getGender());
			response.setLevel(Math.min(infoUser.getLevel(), ConstVariables.MAX_LEVEL));
			response.setMaxVideos(getMaxVideos(response.getLevel())); // 根据等级获取最大可观看视频数
			response.setInviteCode(infoUser.getInviteCode());
			response.setAsset(new Asset(userStatus));
			return response;
		} catch (ConstraintViolationException e) {
			logger.debug("主键冲突，用户信息重复");
			response.setErrorCode(BaseResponse.EDUPENT);
			return response;
		} catch (Exception e) {
			logger.error("系统异常，" + e);
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

		// 初始化数据库连接
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
				logger.debug("用户不存在，userName = " + request.getUserName() + ", registered = " + request.isRegistered());
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
				logger.debug("密码不正确，系统密码：" + infoUser.getPassword() + ", 请求密码：" + request.getPassword());
				response.setErrorCode(BaseResponse.EPERM);
				return response;
			}

			// 更新用户的设备信息
			UserStatus userStatus = (UserStatus) userSession.get(UserStatus.class, nameUser.getUserId());
			if (userStatus == null) {
				userStatus = new UserStatus();
				userStatus.setUserId(nameUser.getUserId());
				userStatus.setDevId(request.getDevId());
				userStatus.setBdUserId(request.getBdUserId());
				userStatus.setLoginTime(new Timestamp(System.currentTimeMillis()));

				userSession.save(userStatus);
			} else {
				// 如果用户在别的设备上已登录，则需要发送通知
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

			// 提交事务
			if (userT != null)
				userT.commit();
			nameT.commit();

			// 修改会话
			UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
			if (userStatSession == null) {
				userStatSession = new UserStatSession();
				httpSession.setAttribute(UserIfc.USER_STAT_ATTR, userStatSession);
			}
			userStatSession.setUserId(infoUser.getUserId());
			userStatSession.setDevId(infoUser.getDevId());

			// 设置应答
			response.setUserId(infoUser.getUserId());
			response.setUserName(infoUser.getUserName());
			response.setRegistered(infoUser.getRegistered());
			response.setPassword(infoUser.getPassword());
			response.setNickName(infoUser.getNickName());
			response.setPortrait(infoUser.getPortrait());
			response.setGender(infoUser.getGender());
			response.setLevel(Math.min(infoUser.getLevel(), ConstVariables.MAX_LEVEL));
			response.setMaxVideos(getMaxVideos(response.getLevel())); // 根据等级获取最大可观看视频数
			response.setInviteCode(infoUser.getInviteCode());
			response.setAsset(new Asset(userStatus));
			return response;
		} catch (Exception e) {
			logger.error("系统异常，" + e);
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

		// 删除会话
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

		// 获取userId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// 更新用户的设备信息
		UserStatus userStatus = userStatusManager.get(userId);
		if (userStatus == null) {
			logger.error("找不到用户对应的资产信息");
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

		// 如果修改用户名，则需要提供验证码
		if (requestInfoUser.getUserName() != null && !verified) {
			logger.error("修改注册手机号时提供的验证码不正确");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// 如果修改密码，则需要提供旧密码
		if (requestInfoUser.getPassword() != null && request.getOldPassword() == null && !verified) {
			logger.error("修改密码时提供的旧密码不正确");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// 获取userId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// 初始化数据库连接
		int partition = HibernateUtil.getPartition(userId);
		int namePartition = 0;
		Session userSession = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction userT = userSession.beginTransaction();
		Session nameSession = null;
		Transaction nameT = null;
		Session newNameSession = null;
		Transaction newNameT = null;

		// 获取用户信息
		InfoUser infoUser = (InfoUser) userSession.get(InfoUser.class, userId);

		// 实际上如果是注册，则赠送一个等级
		if (infoUser.getRegistered().booleanValue() == false && requestInfoUser.getRegistered() != null
				&& requestInfoUser.getRegistered().booleanValue() == true)
			infoUser.setLevel(infoUser.getLevel() + 1);

		try {
			// 检查是否需要修改用户名
			boolean userNameChanged = (requestInfoUser.getUserName() != null
					&& !requestInfoUser.getUserName().equals(infoUser.getUserName()) || requestInfoUser.getRegistered() != null
					&& !requestInfoUser.getRegistered().equals(infoUser.getRegistered()));

			if (userNameChanged) {
				// 初始化数据库
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

				// 查找用户名映射
				NameUser nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
				if (nameUser == null) {
					logger.debug("用户不存在，userName = " + requestInfoUser.getUserName());
					response.setErrorCode(BaseResponse.ENOENT);
					return response;
				}

				// 更改用户名时需要先删除
				nameSession.delete(nameUser);
			}

			// 合并请求数据
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

			// 更新用户信息
			userSession.update(infoUser);

			// 新增用户映射
			if (userNameChanged) {
				// 初始化数据库
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

			// 按照该提交顺序，可保证中间有任何异常时，可保证该用户能被找到
			if (newNameT != null)
				newNameT.commit();
			userT.commit();
			if (nameT != null)
				nameT.commit();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
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
			logger.error("重置密码时提供的验证码不正确");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		if (request.getPassword() == null) {
			logger.error("重置密码时提供的密码为空");
			response.setErrorCode(BaseResponse.EINVAL);
			return response;
		}

		// 初始化数据库连接
		NameUserPK nameUserPK = new NameUserPK();
		nameUserPK.setUserName(request.getUserName());
		nameUserPK.setRegistered(true);
		int partition = HibernateUtil.getPartition(nameUserPK.hashCode());
		Session nameSession = HibernateUtil.getSessionFactory(partition).openSession();
		Session userSession = null;
		Transaction userT = null;

		try {
			// 查找用户ID
			NameUser nameUser = (NameUser) nameSession.get(NameUser.class, nameUserPK);
			if (nameUser == null) {
				logger.debug("用户不用存在，userName = " + request.getUserName());
				response.setErrorCode(BaseResponse.EDUPENT);
				return response;
			}

			// 根据分区值，决定是共用，还是单独创建连接
			int userPartition = HibernateUtil.getPartition(nameUser.getUserId());
			if (userPartition != partition)
				userSession = HibernateUtil.getSessionFactory(userPartition).openSession();
			else
				userSession = nameSession;

			userT = userSession.beginTransaction();

			// 获取用户信息
			InfoUser infoUser = (InfoUser) userSession.get(InfoUser.class, nameUser.getUserId());
			infoUser.setPassword(request.getPassword());

			// 更新用户信息
			userSession.update(infoUser);

			// 提交事务
			userT.commit();
		} catch (Exception e) {
			logger.error("系统异常，" + e);
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

		// 获取userId
		UserStatSession userStatSession = (UserStatSession) httpSession.getAttribute(UserIfc.USER_STAT_ATTR);
		long userId = userStatSession.getUserId();

		// 设置用户ID
		LogFeedback logFeedback = new LogFeedback();
		logFeedback.setUserId(userId);
		logFeedback.setContent(request.getContent());
		logFeedback.setActionDate(new Timestamp(System.currentTimeMillis()));
		logFeedback.setStatus(ConstVariables.STATUS_PENDING);

		try {
			logFeedbackManager.save(logFeedback);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse addBehaviors(HttpSession httpSession, String request) {
		BaseResponse response = new BaseResponse();
		AuditManager auditManager = AuditManager.getInstance();

		// 获取userId
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
	 * 根据等级获取可看视频数
	 * 
	 * @param level
	 *            等级
	 * @return 视频数
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
