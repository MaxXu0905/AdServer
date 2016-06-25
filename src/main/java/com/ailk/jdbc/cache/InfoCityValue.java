package com.ailk.jdbc.cache;

import java.util.List;

public class InfoCityValue {

	public static class InfoCityItem {
		private int itemCode;
		private String itemName;

		public int getItemCode() {
			return itemCode;
		}

		public void setItemCode(int itemCode) {
			this.itemCode = itemCode;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
	}

	private String itemName;
	private List<InfoCityItem> items;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<InfoCityItem> getItems() {
		return items;
	}

	public void setItems(List<InfoCityItem> items) {
		this.items = items;
	}

}
