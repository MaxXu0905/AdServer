package com.ailk.api;

import com.ailk.api.impl.BaseResponse;
import com.ailk.jdbc.entity.InfoContract;
import com.ailk.jdbc.entity.InfoCust;

public interface CustIfc {

	public static final String CUST_ATTR = "CustIfc.infoCust";

	static class ExistsRequest {
		String name; // �����绰

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * ����û��Ƿ����
	 * 
	 * @param request
	 *            ����
	 * @return �����򷵻���ȷ������ʧ��
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
	 * �û�ע��
	 * 
	 * @param infoCust
	 *            �ͻ���Ϣ
	 * @return ע��ɹ������ؿͻ�ID
	 */
	public RegisterResponse register(InfoCust infoCust);

	static class LoginRequest {
		String name; // �����绰
		String password; // ����

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
	 * �ͻ���¼
	 * 
	 * @param request
	 *            ����
	 * @return ��¼�ɹ������ؿͻ�ID
	 */
	public LoginResponse login(LoginRequest request);

	/**
	 * �ͻ�ע��
	 * 
	 * @param custId
	 *            �ͻ�ID
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
	 * ���¿ͻ���Ϣ
	 * 
	 * @param infoCust
	 *            �ͻ���Ϣ
	 * @return �Ƿ�ɹ�
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
	 * ���Ӻ�ͬ
	 * 
	 * @param infoContract
	 *            ��ͬ��Ϣ
	 * @return ��ͬID
	 */
	public ContractResponse addContract(InfoContract infoContract);

	class AcceptRequest {
		int contractId; // ��ͬID
		long operId; // ����ԱID

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
	 * ��ͬ����ͨ��
	 * 
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse accept(AcceptRequest request);

	class RejectRequest {
		int contractId; // ��ͬID
		String reason; // �ܾ�ԭ��
		long operId; // ����ԱID

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
	 * ��ͬ�����ܾ�
	 * 
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse reject(RejectRequest request);

}
