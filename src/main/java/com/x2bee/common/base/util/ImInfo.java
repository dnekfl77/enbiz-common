package com.x2bee.common.base.util;

import org.springframework.stereotype.Component;
@Component
public class ImInfo {
    private String imBaseUrl1;
    private String imBaseUrl2;
    
    private boolean usePrimary = true;
    
    public ImInfo() {
        this(null, null);
    }
    
    public ImInfo(String imBaseUrl1, String imBaseUrl2) {
        this.imBaseUrl1 = imBaseUrl1;
        this.imBaseUrl2 = imBaseUrl2;
    }
    
    /**
     * @return the imBaseUrl1
     */
    public String getImBaseUrl1() {
        return imBaseUrl1;
    }
    /**
     * @param imBaseUrl1 the imBaseUrl1 to set
     */
    public void setImBaseUrl1(String imBaseUrl1) {
        this.imBaseUrl1 = imBaseUrl1;
    }
    /**
     * @return the imBaseUrl2
     */
    public String getImBaseUrl2() {
        return imBaseUrl2;
    }
    /**
     * @param imBaseUrl2 the imBaseUrl2 to set
     */
    public void setImBaseUrl2(String imBaseUrl2) {
        this.imBaseUrl2 = imBaseUrl2;
    }
    
     public String getImBaseUrl(){
         return usePrimary ? this.imBaseUrl1 : this.imBaseUrl2;
     }
     
     public void switchUrl(){
         this.usePrimary = !this.usePrimary;
     }
}
