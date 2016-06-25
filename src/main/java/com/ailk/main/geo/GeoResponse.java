package com.ailk.main.geo;

public class GeoResponse {
	
	static class GeoResult {
		GeoLocation location;
		int precise;
		int confidence;
		String level;

		public GeoResult() {
		}

		public GeoLocation getLocation() {
			return location;
		}

		public void setLocation(GeoLocation location) {
			this.location = location;
		}

		public int getPrecise() {
			return precise;
		}

		public void setPrecise(int precise) {
			this.precise = precise;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}
	}
	
	int status;
	GeoResponse result;

	public GeoResponse() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GeoResponse getResult() {
		return result;
	}

	public void setResult(GeoResponse result) {
		this.result = result;
	}
}
