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
@Table(name = "info_cust")
public class InfoCust implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "cust_id")
	private long custId;

	@Column(name = "cust_type", nullable = false)
	private short custType;

	@Column(name = "cust_name", nullable = false)
	private String custName;

	@Column(name = "lic_type", nullable = false)
	private short licType;

	@Column(name = "lic_loc")
	private String licLoc;

	@Column(name = "contact")
	private String contact;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "telephone_no")
	private String telephoneNo;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "reg_date", nullable = false)
	private Timestamp regDate;

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public short getCustType() {
		return custType;
	}

	public void setCustType(short custType) {
		this.custType = custType;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public short getLicType() {
		return licType;
	}

	public void setLicType(short licType) {
		this.licType = licType;
	}

	public String getLicLoc() {
		return licLoc;
	}

	public void setLicLoc(String licLoc) {
		this.licLoc = licLoc;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + (int) (custId ^ (custId >>> 32));
		result = prime * result + ((custName == null) ? 0 : custName.hashCode());
		result = prime * result + custType;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((licLoc == null) ? 0 : licLoc.hashCode());
		result = prime * result + licType;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + ((telephoneNo == null) ? 0 : telephoneNo.hashCode());
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
		InfoCust other = (InfoCust) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (custId != other.custId)
			return false;
		if (custName == null) {
			if (other.custName != null)
				return false;
		} else if (!custName.equals(other.custName))
			return false;
		if (custType != other.custType)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (licLoc == null) {
			if (other.licLoc != null)
				return false;
		} else if (!licLoc.equals(other.licLoc))
			return false;
		if (licType != other.licType)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNo == null) {
			if (other.phoneNo != null)
				return false;
		} else if (!phoneNo.equals(other.phoneNo))
			return false;
		if (regDate == null) {
			if (other.regDate != null)
				return false;
		} else if (!regDate.equals(other.regDate))
			return false;
		if (telephoneNo == null) {
			if (other.telephoneNo != null)
				return false;
		} else if (!telephoneNo.equals(other.telephoneNo))
			return false;
		return true;
	}

	@Override
	public InfoCust clone() {
		try {
			return (InfoCust) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
