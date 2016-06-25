package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_user")
public class InfoUser implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long userId;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "registered", nullable = false)
	private Boolean registered;

	@Column(name = "password")
	private String password;

	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "portrait")
	private String portrait;

	@Column(name = "dev_id", nullable = false)
	private String devId;

	// 发起邀请时使用的代码，由系统自动生成
	@Column(name = "invite_code")
	private String inviteCode;

	// 注册时推荐用户的邀请码
	@Column(name = "invited_code")
	private String invitedCode;

	@Column(name = "level", nullable = false)
	private Integer level;

	@Column(name = "phone_type")
	private Short phoneType;

	@Column(name = "gender")
	private Short gender;

	@Column(name = "age")
	private Short age;

	@Column(name = "location")
	private String location;

	@Column(name = "product")
	private String product;

	@Column(name = "cpu")
	private String cpu;

	@Column(name = "model")
	private String model;

	@Column(name = "os_type")
	private Short osType;

	@Column(name = "os_version")
	private String osVersion;

	@Column(name = "sdk")
	private Integer sdk;

	@Column(name = "apps")
	private String apps;

	@Column(name = "favor")
	private String favor;

	@Column(name = "pay_type")
	private Short payType;

	// 取现账号
	@Column(name = "account")
	private String account;

	@Column(name = "reg_date", nullable = false)
	private Timestamp regDate;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getInvitedCode() {
		return invitedCode;
	}

	public void setInvitedCode(String invitedCode) {
		this.invitedCode = invitedCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Short getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(Short phoneType) {
		this.phoneType = phoneType;
	}

	public Short getGender() {
		return gender;
	}

	public void setGender(Short gender) {
		this.gender = gender;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Short getOsType() {
		return osType;
	}

	public void setOsType(Short osType) {
		this.osType = osType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public Integer getSdk() {
		return sdk;
	}

	public void setSdk(Integer sdk) {
		this.sdk = sdk;
	}

	public String getApps() {
		return apps;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}

	public String getFavor() {
		return favor;
	}

	public void setFavor(String favor) {
		this.favor = favor;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((apps == null) ? 0 : apps.hashCode());
		result = prime * result + ((cpu == null) ? 0 : cpu.hashCode());
		result = prime * result + ((devId == null) ? 0 : devId.hashCode());
		result = prime * result + ((favor == null) ? 0 : favor.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((inviteCode == null) ? 0 : inviteCode.hashCode());
		result = prime * result + ((invitedCode == null) ? 0 : invitedCode.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((osType == null) ? 0 : osType.hashCode());
		result = prime * result + ((osVersion == null) ? 0 : osVersion.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((payType == null) ? 0 : payType.hashCode());
		result = prime * result + ((phoneType == null) ? 0 : phoneType.hashCode());
		result = prime * result + ((portrait == null) ? 0 : portrait.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((registered == null) ? 0 : registered.hashCode());
		result = prime * result + ((sdk == null) ? 0 : sdk.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		InfoUser other = (InfoUser) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (apps == null) {
			if (other.apps != null)
				return false;
		} else if (!apps.equals(other.apps))
			return false;
		if (cpu == null) {
			if (other.cpu != null)
				return false;
		} else if (!cpu.equals(other.cpu))
			return false;
		if (devId == null) {
			if (other.devId != null)
				return false;
		} else if (!devId.equals(other.devId))
			return false;
		if (favor == null) {
			if (other.favor != null)
				return false;
		} else if (!favor.equals(other.favor))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (inviteCode == null) {
			if (other.inviteCode != null)
				return false;
		} else if (!inviteCode.equals(other.inviteCode))
			return false;
		if (invitedCode == null) {
			if (other.invitedCode != null)
				return false;
		} else if (!invitedCode.equals(other.invitedCode))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (osType == null) {
			if (other.osType != null)
				return false;
		} else if (!osType.equals(other.osType))
			return false;
		if (osVersion == null) {
			if (other.osVersion != null)
				return false;
		} else if (!osVersion.equals(other.osVersion))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (payType == null) {
			if (other.payType != null)
				return false;
		} else if (!payType.equals(other.payType))
			return false;
		if (phoneType == null) {
			if (other.phoneType != null)
				return false;
		} else if (!phoneType.equals(other.phoneType))
			return false;
		if (portrait == null) {
			if (other.portrait != null)
				return false;
		} else if (!portrait.equals(other.portrait))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (regDate == null) {
			if (other.regDate != null)
				return false;
		} else if (!regDate.equals(other.regDate))
			return false;
		if (registered == null) {
			if (other.registered != null)
				return false;
		} else if (!registered.equals(other.registered))
			return false;
		if (sdk == null) {
			if (other.sdk != null)
				return false;
		} else if (!sdk.equals(other.sdk))
			return false;
		if (userId != other.userId)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public InfoUser clone() {
		try {
			return (InfoUser) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
