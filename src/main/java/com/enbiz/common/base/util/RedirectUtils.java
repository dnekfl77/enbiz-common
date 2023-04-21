package com.enbiz.common.base.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.enbiz.common.base.constant.BaseConstants;

public class RedirectUtils {

    private RedirectUtils(){
        
    }
    
    /**
     * returns ModelAndView instance for commons/redirect.jsp
     * @param showMessage - Message to be shown in the javascript alert dialog box
     * @param redirectUrl - argument url for javascript : window.location.replace
     * @return
     */
    public static ModelAndView getRedirectMAV(String showMessage, String redirectUrl) {
        ModelAndView modelAndView = new ModelAndView(BaseConstants.JSP_FOR_REDIRECT);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put(BaseConstants.SHOW_MESSAGE, showMessage);
        result.put(BaseConstants.REDIRECT_URL, redirectUrl);

        modelAndView.addObject("result", result);

        return modelAndView;
    }
    
    public static ModelAndView getRedirectMAV(String redirectUrl) {
        return getRedirectMAV(null, redirectUrl);
    }

}
