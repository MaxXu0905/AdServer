package com.ailk.api.impl;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import com.ailk.api.CustIfc;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.InfoContractManager;
import com.ailk.jdbc.InfoCustManager;
import com.ailk.jdbc.entity.InfoContract;
import com.ailk.jdbc.entity.InfoCust;

public class CustManager implements CustIfc {

	private static final Logger logger = Logger.getLogger(CustManager.class);

	public CustManager() {
	}

	@Override
	public BaseResponse exists(ExistsRequest request) {
		BaseResponse response = new BaseResponse();
		InfoCustManager infoCustManager = InfoCustManager.getInstance();

		try {
			InfoCust infoCust = infoCustManager.get(request.getName());
			if (infoCust == null) {
				logger.error("找不到客户信息，" + request.getName());
				response.setErrorCode(BaseResponse.ENOENT);
			}
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public RegisterResponse register(InfoCust infoCust) {
		RegisterResponse response = new RegisterResponse();
		InfoCustManager infoCustManager = InfoCustManager.getInstance();
		InfoContractManager infoContractManager = InfoContractManager.getInstance();

		try {
			Timestamp current = new Timestamp(System.currentTimeMillis());
			infoCust.setRegDate(current);
			infoCustManager.save(infoCust);

			InfoContract infoContract = new InfoContract();
			infoContract.setCustId(infoCust.getCustId());
			infoContract.setStatus(ConstVariables.STATUS_PENDING);
			infoContract.setOperId(infoCust.getCustId());
			infoContract.setOperDate(current);
			infoContract.setRegDate(current);
			infoContractManager.save(infoContract);

			response.setInfoCust(infoCust);
		} catch (ConstraintViolationException e) {
			logger.debug("主键冲突，重复的客户信息");
			response.setErrorCode(BaseResponse.EDUPENT);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		LoginResponse response = new LoginResponse();
		InfoCustManager infoCustManager = InfoCustManager.getInstance();

		try {
			InfoCust infoCust = infoCustManager.get(request.getName());
			if (infoCust == null) {
				logger.error("找不到客户信息，" + request.getName());
				response.setErrorCode(BaseResponse.ENOENT);
			} else {
				if (!infoCust.getPassword().equals(request.getPassword())) {
					logger.debug("密码不匹配，系统密码：" + infoCust.getPassword() + ", 请求密码：" + request.getPassword());
					response.setErrorCode(BaseResponse.EPERM);
				} else {
					response.setInfoCust(infoCust);
				}
			}
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse logout(long userId) {
		BaseResponse response = new BaseResponse();

		return response;
	}

	@Override
	public UpdateResponse update(InfoCust infoCust) {
		UpdateResponse response = new UpdateResponse();
		InfoCustManager infoCustManager = InfoCustManager.getInstance();

		infoCustManager.save(infoCust);
		response.setInfoCust(infoCust);
		return response;
	}

	@Override
	public ContractResponse addContract(InfoContract infoContract) {
		ContractResponse response = new ContractResponse();
		InfoContractManager infoContractManager = InfoContractManager.getInstance();

		try {
			infoContractManager.save(infoContract);
			response.setContractId(infoContract.getContractId());
		} catch (ConstraintViolationException e) {
			logger.debug("主键冲突，联系人信息重复");
			response.setErrorCode(BaseResponse.EDUPENT);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse accept(AcceptRequest request) {
		BaseResponse response = new BaseResponse();
		InfoContractManager infoContractManager = InfoContractManager.getInstance();
		InfoContract infoContract = new InfoContract();

		try {
			infoContract.setContractId(request.getContractId());
			infoContract.setStatus(ConstVariables.STATUS_PASS);
			infoContract.setOperId(request.getOperId());
			infoContract.setRegDate(new Timestamp(System.currentTimeMillis()));
			infoContractManager.update(infoContract);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public BaseResponse reject(RejectRequest request) {
		BaseResponse response = new BaseResponse();
		InfoContractManager infoContractManager = InfoContractManager.getInstance();
		InfoContract infoContract = new InfoContract();

		try {
			infoContract.setContractId(request.getContractId());
			infoContract.setStatus(ConstVariables.STATUS_REJECT);
			infoContract.setOperId(request.getOperId());
			infoContract.setReason(request.getReason());
			infoContract.setRegDate(new Timestamp(System.currentTimeMillis()));
			infoContractManager.update(infoContract);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

}
