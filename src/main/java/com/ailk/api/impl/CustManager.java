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
				logger.error("�Ҳ����ͻ���Ϣ��" + request.getName());
				response.setErrorCode(BaseResponse.ENOENT);
			}
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
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
			logger.debug("������ͻ���ظ��Ŀͻ���Ϣ");
			response.setErrorCode(BaseResponse.EDUPENT);
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
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
				logger.error("�Ҳ����ͻ���Ϣ��" + request.getName());
				response.setErrorCode(BaseResponse.ENOENT);
			} else {
				if (!infoCust.getPassword().equals(request.getPassword())) {
					logger.debug("���벻ƥ�䣬ϵͳ���룺" + infoCust.getPassword() + ", �������룺" + request.getPassword());
					response.setErrorCode(BaseResponse.EPERM);
				} else {
					response.setInfoCust(infoCust);
				}
			}
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
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
			logger.debug("������ͻ����ϵ����Ϣ�ظ�");
			response.setErrorCode(BaseResponse.EDUPENT);
		} catch (Exception e) {
			logger.error("ϵͳ�쳣��" + e);
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
			logger.error("ϵͳ�쳣��" + e);
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
			logger.error("ϵͳ�쳣��" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

}
