package com.ailk.api;

import com.ailk.api.impl.BaseResponse;
import com.ailk.jdbc.entity.InfoContract;
import com.ailk.jdbc.entity.InfoCust;

public interface CustIfc {

	public static final String CUST_ATTR = "CustIfc.infoCust";

	static class ExistsRequest {
		String name; // 邮箱或电话

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 检查用户是否存在
	 * 
	 * @param request
	 *            请求
	 * @return 存在则返回正确，否则失败
	 */
	public BaseResponse exists(ExistsRequest request);

	static class RegisterResponse extends BaseResponse {
		private InfoCust infoCust;

		public InfoCust getInfoCust() {
			return infoCust;
		}

		public void setInfoCust(InfoCust infoCust) {
			this.infoCust = infoCust;
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param infoCust
	 *            客户信息
	 * @return 注册成功，返回客户ID
	 */
	public RegisterResponse register(InfoCust infoCust);

	static class LoginRequest {
		String name; // 邮箱或电话
		String password; // 密码

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	static class LoginResponse extends BaseResponse {
		private InfoCust infoCust;
		private String url;

		public InfoCust getInfoCust() {
			return infoCust;
		}

		public void setInfoCust(InfoCust infoCust) {
			this.infoCust = infoCust;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	/**
	 * 客户登录
	 * 
	 * @param request
	 *            请求
	 * @return 登录成功，返回客户ID
	 */
	public LoginResponse login(LoginRequest request);

	/**
	 * 客户注销
	 * 
	 * @param custId
	 *            客户ID
	 */
	public BaseResponse logout(long custId);

	static class UpdateResponse extends BaseResponse {
		private InfoCust infoCust;

		public InfoCust getInfoCust() {
			return infoCust;
		}

		public void setInfoCust(InfoCust infoCust) {
			this.infoCust = infoCust;
		}
	}

	/**
	 * 更新客户信息
	 * 
	 * @param infoCust
	 *            客户信息
	 * @return 是否成功
	 */
	public UpdateResponse update(InfoCust infoCust);

	static class ContractResponse extends BaseResponse {
		int contractId;

		public int getContractId() {
			return contractId;
		}

		public void setContractId(int contractId) {
			this.contractId = contractId;
		}
	}

	/**
	 * 增加合同
	 * 
	 * @param infoContract
	 *            合同信息
	 * @return 合同ID
	 */
	public ContractResponse addContract(InfoContract infoContract);

	class AcceptRequest {
		int contractId; // 合同ID
		long operId; // 操作员ID

		public int getContractId() {
			return contractId;
		}

		public void setContractId(int contractId) {
			this.contractId = contractId;
		}

		public long getOperId() {
			return operId;
		}

		public void setOperId(long operId) {
			this.operId = operId;
		}
	}

	/**
	 * 合同审批通过
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse accept(AcceptRequest request);

	class RejectRequest {
		int contractId; // 合同ID
		String reason; // 拒绝原因
		long operId; // 操作员ID

		public int getContractId() {
			return contractId;
		}

		public void setContractId(int contractId) {
			this.contractId = contractId;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public long getOperId() {
			return operId;
		}

		public void setOperId(long operId) {
			this.operId = operId;
		}
	}

	/**
	 * 合同审批拒绝
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse reject(RejectRequest request);

}
