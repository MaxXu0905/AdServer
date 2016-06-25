package com.ailk.jdbc.cache;

import com.ailk.jdbc.entity.InfoAd;

public class InfoAdKey implements Comparable<InfoAdKey> {

	private int adId; // ¹ã¸æID

	public void set(InfoAd entity) {
		adId = entity.getAdId();
	}

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (adId ^ (adId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoAdKey other = (InfoAdKey) obj;
		if (adId != other.adId)
			return false;
		return true;
	}

	@Override
	public int compareTo(InfoAdKey o) {
		if (adId < o.adId)
			return -1;
		else if (adId > o.adId)
			return 1;
		else
			return 0;
	}

}
