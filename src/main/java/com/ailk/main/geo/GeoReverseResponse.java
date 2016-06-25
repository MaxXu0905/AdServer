package com.ailk.main.geo;

public class GeoReverseResponse {

	static class GeoReverseResult {
		
		static class AddressComponent {
			String city;
			String district;
			String province;
			String street;
			String street_number;

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public String getDistrict() {
				return district;
			}

			public void setDistrict(String district) {
				this.district = district;
			}

			public String getProvince() {
				return province;
			}

			public void setProvince(String province) {
				this.province = province;
			}

			public String getStreet() {
				return street;
			}

			public void setStreet(String street) {
				this.street = street;
			}

			public String getStreet_number() {
				return street_number;
			}

			public void setStreet_number(String street_number) {
				this.street_number = street_number;
			}
		}

		GeoLocation location;
		String formatted_address;
		String business;
		AddressComponent addressComponent;
		int cityCode;

		public GeoLocation getLocation() {
			return location;
		}

		public void setLocation(GeoLocation location) {
			this.location = location;
		}

		public String getFormatted_address() {
			return formatted_address;
		}

		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}

		public String getBusiness() {
			return business;
		}

		public void setBusiness(String business) {
			this.business = business;
		}

		public AddressComponent getAddressComponent() {
			return addressComponent;
		}

		public void setAddressComponent(AddressComponent addressComponent) {
			this.addressComponent = addressComponent;
		}

		public int getCityCode() {
			return cityCode;
		}

		public void setCityCode(int cityCode) {
			this.cityCode = cityCode;
		}
	}

	int status;
	GeoReverseResult result;

	public GeoReverseResponse() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GeoReverseResult getResult() {
		return result;
	}

	public void setResult(GeoReverseResult result) {
		this.result = result;
	}
}
