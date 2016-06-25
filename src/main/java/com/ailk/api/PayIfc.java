package com.ailk.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.api.UserIfc.Asset;
import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;
import com.ailk.common.ConstVariables;

/**
 * ֧�����������ͣ� 1. ���� a. �û������֧������ b. �������������� c. �û�������ֱ�ӷ��𣬻����ۼƵ�һ��ֵ����ƷѴ��� d.
 * ���ʧ�ܣ���ֱ�Ӻ��ԣ����û���һ��Ӱ�� 2. ���� a. �û������֧������ b. �������������� c.
 * �û��ۿ���ֱ�ӷ���������������£�����״̬������������ d. ���ʧ�ܣ���ֱ�Ӻ��ԣ����û���Ӱ�졣 3. ��Ƶ a. �û������֧������ b.
 * ������������� c. �û��ۿ�ǰ����Ҫ������һ��ֻ����һ�Σ���Ч��һ�죻��������������£����������������̿��ܻᲿ��ʧ�ܣ���Ҫ�����ύ d.
 * ��������Ϻ󣬿������ύ���ύ����Ƶ����������Ҳ����δ��������Ҫ�жϾ��� e.
 * ���δ������������һ���л����ܲ������룬����ҪԤ����������֤�ִθ��ѵ���Ƶ���Ի������ 4. � a. �û������֧������ b. ������������� c.
 * �û�����ǰ����Ҫ������ͬһ����棬δ�ύǰ�������ظ�������������Ч��4Сʱ�����������߲��� d. ����Ч����δ�ύ������ϵͳ�Զ�ȡ�� e.
 * �û�Ҳ��������ȡ�����ȡ���������ķ����Զ��ͷ� f. ����������벻�������㣬��Ҫ�ȵ����ͨ������� 5. �Ƽ� a. ϵͳ�����֧������ b.
 * �������������� c. ÿһ����Ч���Ƽ�����������Ӧ�����룬����������ķ�ʽ���У����ṩʵʱ��ʽ d.
 * ���Ƽ�������ʱ���Ƽ���Ҳ������Ӧ�����룬����������ķ�ʽ���У����ṩʵʱ��ʽ
 * 
 * @author xugq
 * 
 */
public interface PayIfc {

	public static final String LOCK_AD_ATTR = "PayIfc.lockAd";
	public static final long MILLIS_PER_HOUR = 3600000;
	public static final int VALIDS_PER_HOUR = 3;

	static class LockAdSession {
		private List<Long> timestamps; // �������ʱ���
		private int minIndex; // ��Сʱ�������

		public LockAdSession() {
			timestamps = new ArrayList<Long>();
			minIndex = -1;
		}

		public List<Long> getTimestamps() {
			return timestamps;
		}

		public void setTimestamps(List<Long> timestamps) {
			this.timestamps = timestamps;
		}

		public int getMinIndex() {
			return minIndex;
		}

		public void setMinIndex(int minIndex) {
			this.minIndex = minIndex;
		}
	}

	static class PayItem {
		private int adId; // ���ID
		private int seq; // �ۿ�����
		private long lastCommit; // �ۿ�ʱ�����������

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}

		public long getLastCommit() {
			return lastCommit;
		}

		public void setLastCommit(long lastCommit) {
			this.lastCommit = lastCommit;
		}
	}

	static class LockRequest extends BaseRequest {
		private long sysdate; // ϵͳ����
		private List<Integer> items; // ������¼�б�

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public List<Integer> getItems() {
			return items;
		}

		public void setItems(List<Integer> items) {
			this.items = items;
		}
	}

	static class LockResponse extends BaseResponse {
		private List<Integer> fails; // ʧ�ܵĹ��˳���
		private List<Short> statuses; // ʧ�ܵĹ��ԭ��

		public List<Integer> getFails() {
			return fails;
		}

		public void setFails(List<Integer> fails) {
			this.fails = fails;
		}

		public List<Short> getStatuses() {
			return statuses;
		}

		public void setStatuses(List<Short> statuses) {
			this.statuses = statuses;
		}
	}

	/**
	 * ��������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ����״̬���쳣ʱ���ش�������в���ʧ�ܣ��򷵻�ʧ���б�
	 */
	public LockResponse lock(HttpSession httpSession, LockRequest request);

	static class UnlockRequest extends BaseRequest {
		private List<PayItem> items;

		public List<PayItem> getItems() {
			return items;
		}

		public void setItems(List<PayItem> items) {
			this.items = items;
		}
	}

	/**
	 * ��������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ����״̬
	 */
	public BaseResponse unlock(HttpSession httpSession, UnlockRequest request);

	static class ChargeRequest extends BaseRequest {
		private long sysdate; // ��ǰ����
		private String devId; // �豸ID
		private Boolean lockDetail; // �Ƿ�Ϊ��������
		private List<PayItem> items; // �ƷѼ�¼�б�

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public String getDevId() {
			return devId;
		}

		public void setDevId(String devId) {
			this.devId = devId;
		}

		public Boolean getLockDetail() {
			return lockDetail;
		}

		public void setLockDetail(Boolean lockDetail) {
			this.lockDetail = lockDetail;
		}

		public List<PayItem> getItems() {
			return items;
		}

		public void setItems(List<PayItem> items) {
			this.items = items;
		}
	}

	static class ChargeResponseItem {
		private short status; // ���״̬
		private long profit; // ����
		private int times; // ������-1��ʾ�������
		
		public ChargeResponseItem () {
			status = ConstVariables.STATUS_PASS;
			profit = 0;
			times = -1;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	static class ChargeResponse extends BaseResponse {
		private List<ChargeResponseItem> items; // ���Ӧ��
		private Asset asset; // �ʲ�

		public List<ChargeResponseItem> getItems() {
			return items;
		}

		public void setItems(List<ChargeResponseItem> items) {
			this.items = items;
		}

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * �Ʒѣ��÷��ÿ����Ѿ�������Ҳ����δ���������δ�������������ҪԤ����������֤�ִθ��ѵ���Ƶ���ں����ۿ�ʱ�ܻ�����롣�漰���˻������������
	 * �û�ͳ�Ʊ��������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public ChargeResponse charge(HttpSession httpSession, ChargeRequest request);

	static class BalanceResponse extends BaseResponse {
		private long budget; // Ԥ��
		private long remain; // �������
		private long locked; // �������

		public long getBudget() {
			return budget;
		}

		public void setBudget(long budget) {
			this.budget = budget;
		}

		public long getRemain() {
			return remain;
		}

		public void setRemain(long remain) {
			this.remain = remain;
		}

		public long getLocked() {
			return locked;
		}

		public void setLocked(long locked) {
			this.locked = locked;
		}
	}

	static class CommitPromotionRequest extends BaseRequest {
		private int adId; // ���ID
		private long actionDate; // ����ʱ��
		private String investigations; // �����ʾ��
		private String answers; // ÿ�α����
		private boolean complete; // �Ƿ�����

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public long getActionDate() {
			return actionDate;
		}

		public void setActionDate(long actionDate) {
			this.actionDate = actionDate;
		}

		public String getInvestigations() {
			return investigations;
		}

		public void setInvestigations(String investigations) {
			this.investigations = investigations;
		}

		public String getAnswers() {
			return answers;
		}

		public void setAnswers(String answers) {
			this.answers = answers;
		}

		public boolean isComplete() {
			return complete;
		}

		public void setComplete(boolean complete) {
			this.complete = complete;
		}
	}

	static class CommitPromotionResponse extends BaseResponse {
		private long id; // ���־ID
		private short status; // ״̬
		private String reason; // �ܾ�ԭ��

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}

	/**
	 * �ύ�
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public CommitPromotionResponse commitPromotion(HttpSession httpSession, CommitPromotionRequest request);

	/**
	 * ��ȡ�˻����
	 * 
	 * @param adId
	 *            ���ID
	 * @return �Ƿ�ɹ�
	 */
	public BalanceResponse getBalance(int adId);

	static class RqstPayRequest extends BaseRequest {
		private String password; // ����
		private int payType; // ֧����ʽ
		private String payName; // �˺�����
		private String payId; // �˺�
		private int amount; // ���

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public String getPayName() {
			return payName;
		}

		public void setPayName(String payName) {
			this.payName = payName;
		}

		public String getPayId() {
			return payId;
		}

		public void setPayId(String payId) {
			this.payId = payId;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
	}

	static class RqstPayResponse extends BaseResponse {
		private Asset asset; // �ʲ�

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * ��������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public RqstPayResponse rqstPay(HttpSession httpSession, RqstPayRequest request);

	static class GetPayRequest extends BaseRequest {
		private long maxId; // ���ID

		public long getMaxId() {
			return maxId;
		}

		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}
	}

	static class GetPayItem {
		private long id; // ID
		private int payType; // ֧������
		private int amount; // ���
		private short status; // ״̬
		private String reason; // ԭ��
		private long rqstDate; // ��������

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public long getRqstDate() {
			return rqstDate;
		}

		public void setRqstDate(long rqstDate) {
			this.rqstDate = rqstDate;
		}
	}

	static class GetPayResponse extends BaseResponse {
		private boolean end; // �Ƿ����
		private List<GetPayItem> items; // ֧���б�

		public boolean isEnd() {
			return end;
		}

		public void setEnd(boolean end) {
			this.end = end;
		}

		public List<GetPayItem> getItems() {
			return items;
		}

		public void setItems(List<GetPayItem> items) {
			this.items = items;
		}
	}

	/**
	 * ��ȡ֧������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ֧����������
	 */
	public GetPayResponse getPayRqst(HttpSession httpSession, GetPayRequest request);

}
