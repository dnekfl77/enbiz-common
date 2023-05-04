package com.enbiz.common.base.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseCommonEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 3654058779853559877L;

	private String sysRegDtm;
	private String sysRegId;
	private String sysModDtm;
	private String sysModId;

	private int rowsPerPage;
	private int pageIdx;
	private int pageCalc;
	private String systemDefaultLanguage;
	private String dbLocaleLanguage;
	private String state;
	private String defaultCntryCd;
	private String sysRegrNm;
	private String sysModrNm;
	private String sysRegMenuId = "EMPTY";
	private String sysRegIpAddr = "EMPTY";
	private String sysModMenuId = "EMPTY";
	private String sysModIpAddr = "EMPTY";
	
	public int getOffset() {
		return (pageIdx - 1) * rowsPerPage;
	}	

}
