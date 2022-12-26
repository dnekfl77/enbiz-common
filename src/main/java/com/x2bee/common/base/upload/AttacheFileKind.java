package com.x2bee.common.base.upload;

public enum AttacheFileKind {
	CUSTOMER("10","customer"),
	CUSTOMER_SERVICE("20","customerservice"),
	DELIVERY("30","delivery"),
	DISPLAY("40","display"),
	GOODS("50","goods"),
	MARKETING("60","marketing"),
	ORDER("70","order"),
	PAYMENT("80","payment"),
	SYSTEM("90","system"),
	VENDOR("100","vendor"),
	EDITOR("110","editor");

    private final String cd;
    private final String cdNm;

    AttacheFileKind(String cd, String cdNm){
        this.cd = cd;
        this.cdNm = cdNm;
    }

    public String getCd() {
        return cd;
    }
    public String getNm() {
        return cdNm;
    }

    public boolean isEquals(String cd){
        return cd.equals(this.getCd());
    }
}
