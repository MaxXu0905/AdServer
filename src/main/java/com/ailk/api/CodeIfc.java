package com.ailk.api;

import java.util.List;

import com.ailk.api.impl.BaseResponse;
import com.ailk.jdbc.cache.InfoCityValue;
import com.ailk.jdbc.cache.InfoCodeExtValue;
import com.ailk.jdbc.cache.InfoGroupValue;
import com.ailk.jdbc.cache.InfoPeriodValue;

public interface CodeIfc {

	static class GroupResponse extends BaseResponse {
		private List<InfoGroupValue> items;

		public List<InfoGroupValue> getItems() {
			return items;
		}

		public void setItems(List<InfoGroupValue> items) {
			this.items = items;
		}
	}

	/**
	 * 获取广告信息列表
	 * 
	 * @param request
	 *            请求
	 * @return 广告信息列表
	 */
	public GroupResponse getGroup(String groupName);

	static class GroupRequest {
		private String groupName;
		private List<InfoGroupValue> items;

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public List<InfoGroupValue> getItems() {
			return items;
		}

		public void setItems(List<InfoGroupValue> items) {
			this.items = items;
		}
	}

	/**
	 * 设置编码组
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse setGroup(GroupRequest request);

	static class CodeResponse extends BaseResponse {
		private String desc;
		private List<InfoCodeExtValue> items;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public List<InfoCodeExtValue> getItems() {
			return items;
		}

		public void setItems(List<InfoCodeExtValue> items) {
			this.items = items;
		}
	}

	static class CodeRequest {
		String groupName;
		String codeName;

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getCodeName() {
			return codeName;
		}

		public void setCodeName(String codeName) {
			this.codeName = codeName;
		}
	}

	/**
	 * 获取编码
	 * 
	 * @param request
	 *            请求
	 * @return 编码
	 */
	public CodeResponse getCode(CodeRequest request);

	static class CitiesResponse extends BaseResponse {
		private List<InfoCityValue> items;

		public List<InfoCityValue> getItems() {
			return items;
		}

		public void setItems(List<InfoCityValue> items) {
			this.items = items;
		}
	}

	/**
	 * 获取支持的城市列表
	 * 
	 * @return 支持的城市列表
	 */
	public CitiesResponse getCities();

	static class PeriodsResponse extends BaseResponse {
		private List<InfoPeriodValue> items;

		public List<InfoPeriodValue> getItems() {
			return items;
		}

		public void setItems(List<InfoPeriodValue> items) {
			this.items = items;
		}
	}

	/**
	 * 获取时段列表
	 * 
	 * @return 时段列表
	 */
	public PeriodsResponse getPeriods();

}
