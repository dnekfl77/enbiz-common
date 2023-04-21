package com.enbiz.common.base.util.masking;

public class CommonAddrMasker implements Masker {
    
    @Override
    public String mask(String address) {
            String addr = address.trim();
            
            int cnt = 0;
            int blankPosition = 0;
            
            for(int i = addr.length(); i > 0 ; i--) {
                if(" ".equals(addr.substring(i-1 , i) ) ) {
                    cnt++;
                }
                
                if(cnt == 2) {
                    blankPosition = i;
                    break;
                }
            }
            
            String maskingValue = addr.substring(blankPosition , addr.length() );
            String[] maskingBlankDivi = maskingValue.split(" ");
            
            StringBuilder sb = new StringBuilder();
            sb.append(addr.substring(0, blankPosition - 1) );
            sb.append(" ");
           
            for(int i = 0; i < maskingValue.length(); i++) {
                if(i == maskingBlankDivi[0].length() ) {
                    sb.append(" ");
                } else {
                    sb.append("*");
                }
            }
            
        return sb.toString();
    }
}