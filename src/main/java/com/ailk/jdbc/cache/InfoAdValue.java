package com.ailk.jdbc.cache;

import java.util.ArrayList;
import java.util.List;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.InfoQuizManager;
import com.ailk.jdbc.entity.InfoAd;

/**
 * �����Ϣֵ
 * 
 * @author xugq
 * 
 */
public class InfoAdValue {

	/**
	 * �����۸���
	 * 
	 * @author xugq
	 * 
	 */
	public static class PriceItem {
		private int price; // �۸�
		private Integer price2; // �۸�2
		private int times; // ����

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public Integer getPrice2() {
			return price2;
		}

		public void setPrice2(Integer price2) {
			this.price2 = price2;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	/**
	 * һ��ļ۸���
	 * 
	 * @author xugq
	 * 
	 */
	public static class PriceDayItem {
		private List<PriceItem> items; // �۸���
		private int days; // ����
		private long lockPrice; // �����۸�

		public List<PriceItem> getItems() {
			return items;
		}

		public void setItems(List<PriceItem> items) {
			this.items = items;
		}

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public long getLockPrice() {
			return lockPrice;
		}

		public void setLockPrice(long lockPrice) {
			this.lockPrice = lockPrice;
		}
	}

	private String adName; // �������
	private String adDesc; // �������
	private short adType; // �������
	private short adStyle; // �����ʽ
	private short deliveryType; // ��������
	private String officialUrl; // �ٷ���վ
	private List<PriceDayItem> prices; // �۸��б�
	private long budget; // Ԥ��
	private Short duration; // ��Ƶʱ��
	private List<Integer> quizKeys; // ������б�
	private List<InfoQuizValue> quizValues; // ����ֵ�б�
	private long custId; // �ͻ�ID
	private short status; // ״̬
	private String reason; // ԭ��
	private long effDate; // ��Ч����
	private long expDate; // ʧЧ����
	private String video; // ��ƵURL
	private String webView; // web view URL
	private String image; // ͼƬURL
	private String cycles; // �������
	private Long expire; // �����ʱ
	private boolean hasInvestigations; // �Ƿ��е����ʾ�

	/**
	 * ���ݹ����Ϣ���
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 * @param entity
	 *            �����Ϣ
	 */
	public void set(GlobalManager globalManager, InfoAd entity) {
		adName = entity.getAdName();
		adDesc = entity.getAdDesc();
		adType = entity.getAdType();
		adStyle = entity.getAdStyle();
		deliveryType = entity.getDeliveryType();
		officialUrl = entity.getOfficialUrl();

		// PricePlan�ĸ�ʽΪ��price1,times1,price?:price2,times2,price?$dayTimes1;price3,times3,price?:price4,times4,price?$dayTimes2
		// ������<=0��ʾ����ѭ��
		if (entity.getPricePlan() == null) {
			prices = null;
		} else {
			prices = new ArrayList<PriceDayItem>();
			String[] fields = entity.getPricePlan().split(";", -1);
			for (String field : fields) {
				long lockPrice = 0;
				PriceDayItem priceDayItem = new PriceDayItem();
				List<PriceItem> priceItems = new ArrayList<PriceItem>();
				priceDayItem.setItems(priceItems);

				String[] priceFields = field.split("\\$", -1);
				String[] dayFields = priceFields[0].split(":", -1);
				for (String dayField : dayFields) {
					PriceItem priceItem = new PriceItem();
					String[] itemFields = dayField.split(",", -1);

					priceItem.setPrice(Integer.parseInt(itemFields[0]));
					priceItem.setTimes(Integer.parseInt(itemFields[1]));
					if (priceItem.getTimes() <= 0) {
						priceItem.setTimes(-1);
						lockPrice += priceItem.getPrice();
					} else {
						lockPrice += priceItem.getPrice() * priceItem.getTimes();
					}
					// ���������������������ã�����һ��Ϊ�鿴����ķ���
					if (itemFields.length > 2)
						priceItem.setPrice2(Integer.parseInt(itemFields[2]));

					priceItems.add(priceItem);
				}

				priceDayItem.setDays(Integer.parseInt(priceFields[1]));
				if (priceDayItem.getDays() <= 0)
					priceDayItem.setDays(-1);
				priceDayItem.setLockPrice(lockPrice);
				prices.add(priceDayItem);
			}
		}

		budget = entity.getBudget();
		duration = entity.getDuration();
		hasInvestigations = false;

		if (entity.getQuizList() == null) {
			quizKeys = null;
			quizValues = null;
		} else {
			quizKeys = new ArrayList<Integer>();
			quizValues = new ArrayList<InfoQuizValue>();
			if (entity.getQuizList() != null) {
				String[] fields = entity.getQuizList().split(ConstVariables.FIELD_SEP, -1);
				for (String field : fields) {
					int quizId = Integer.parseInt(field);
					quizKeys.add(quizId);

					InfoQuizManager infoQuizManager = InfoQuizManager.getInstance(globalManager);
					InfoQuizValue infoQuizValue = infoQuizManager.get(quizId);
					quizValues.add(infoQuizValue);
					
					if (infoQuizValue.isSkip())
						hasInvestigations = true;
				}
			}
		}

		custId = entity.getCustId();
		status = entity.getStatus();
		reason = entity.getReason();
		effDate = entity.getEffDate().getTime();
		expDate = entity.getExpDate().getTime();
		video = entity.getVideo();
		webView = entity.getWebView();
		image = entity.getImage();
		cycles = entity.getCycles();
		if (entity.getExpire() != null)
			expire = entity.getExpire() * 1000;
	}

	/**
	 * ��ȡ����ļ۸�����
	 */
	public PriceDayItem getPriceDayItem(int day) {
		if (prices == null)
			return null;

		int cday = 0;
		for (PriceDayItem item : prices) {
			if (item.getDays() <= 0)
				return item;

			cday += item.getDays();
			// ���ܻ�Խ�磬��>0������
			if (cday > 0 && cday <= day)
				continue;

			return item;
		}

		return null;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getAdDesc() {
		return adDesc;
	}

	public void setAdDesc(String adDesc) {
		this.adDesc = adDesc;
	}

	public short getAdType() {
		return adType;
	}

	public void setAdType(short adType) {
		this.adType = adType;
	}

	public short getAdStyle() {
		return adStyle;
	}

	public void setAdStyle(short adStyle) {
		this.adStyle = adStyle;
	}

	public short getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(short deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getOfficialUrl() {
		return officialUrl;
	}

	public void setOfficialUrl(String officialUrl) {
		this.officialUrl = officialUrl;
	}

	public List<PriceDayItem> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceDayItem> prices) {
		this.prices = prices;
	}

	public long getBudget() {
		return budget;
	}

	public void setBudget(long budget) {
		this.budget = budget;
	}

	public Short getDuration() {
		return duration;
	}

	public void setDuration(Short duration) {
		this.duration = duration;
	}

	public List<Integer> getQuizKeys() {
		return quizKeys;
	}

	public void setQuizKeys(List<Integer> quizKeys) {
		this.quizKeys = quizKeys;
	}

	public List<InfoQuizValue> getQuizValues() {
		return quizValues;
	}

	public void setQuizValues(List<InfoQuizValue> quizValues) {
		this.quizValues = quizValues;
	}

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
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

	public long getEffDate() {
		return effDate;
	}

	public void setEffDate(long effDate) {
		this.effDate = effDate;
	}

	public long getExpDate() {
		return expDate;
	}

	public void setExpDate(long expDate) {
		this.expDate = expDate;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getWebView() {
		return webView;
	}

	public void setWebView(String webView) {
		this.webView = webView;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCycles() {
		return cycles;
	}

	public void setCycles(String cycles) {
		this.cycles = cycles;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public boolean isHasInvestigations() {
		return hasInvestigations;
	}

	public void setHasInvestigations(boolean hasInvestigations) {
		this.hasInvestigations = hasInvestigations;
	}

}
