package com.x2bee.common.base.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x2bee.common.base.entity.UserAgent;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 브라우저 User Agent 유틸 Mozilla/[version] ([system and browser information])
 * [platform] ([platform details]) [extensions]. 브라우저별로 매우 다양한 User agent가 존재하며
 * 계속해서 새로운 형태의 단말이 나올때마다 User Agent 형태 또한 추가되는 것으로 지속적인 관리가 필요하다. 해당 클래스에서는
 * well known browser의 헤더에 대해서만 파싱하며 모바일의 경우 단말 정보에 대해서는 따로 파싱하지 않는다.
 *
 * @author User
 * @since 20160923
 * @version 20160923 jwlim 최초 작성
 */
@Data
@ToString
@Slf4j
public class UserAgentUtil {

    private static final Logger log = LoggerFactory.getLogger(UserAgentUtil.class);

    // user agent
    private String userAgent = null;

    private String applicationName = null;
    private String applicationVer = null;
    private String osName = null;
    private String osVer = null;
    private String osType = null;
    private String browserName = null;
    private String browserVer = null;
    private String engineName = null;
    private String engineVer = null;

    private boolean isMobile = false;

//    private UserAgent userAgentVo = null;

    private static final Pattern ptnMobileInfo = Pattern
            .compile("(iphone os|ipad;|android|windows phone|blackberry)\\s([\\w\\._-]+)");
    private static final Pattern ptnIOSType = Pattern
            .compile("(iphone os|ipad;)");
    private static final Pattern ptnAndroidType = Pattern
            .compile("(android)");
    private static final Pattern ptnPcOs = Pattern
            .compile("(windows|linux|macintosh|mac)(\\s)?((nt|arm)?(\\s)?([\\s\\w\\._-]+)?)?");
    private static final Pattern ptnBrowserType = Pattern.compile("(msie|trident|chrome|safari|firefox)");
    private static final Pattern ptnBrowserMsieVer = Pattern.compile("(msie\\s([\\w\\._-]+;))|(trident/([\\d._-]+);)");
    private static final Pattern ptnBrowserWebkitVer = Pattern
            .compile("(chrome|version)/([\\w\\._-]+)\\s(mobile(/[\\w]+)?\\s)?(safari)/([\\w\\._-]+)");
    private static final Pattern ptnBrowserFirefoxVer = Pattern
            .compile("(rv:)([\\w\\._-]+)\\)\\s(gecko)/([\\w\\._-]+)\\s(firefox)/([\\w\\._-]+)");
    private static final Pattern ptnEngineInfo = Pattern.compile("(webkit|trident|presto|netfront|gecko)/([\\w._-]+)");

    public static final String SEPARATOR_SLASH = "/";
    public static final String SEPARATOR_SPACEBAR = " ";
    /**
     * 생성자
     *
     * @param userAgent
     *            http user agent
     */
    public UserAgentUtil(String userAgent) {
        log.debug("### User Agent Util [userAgent] : " + userAgent);

        this.userAgent = userAgent;

        if(this.userAgent != null){
            this.parse();
        }
    }

    /**
     * 생성자
     *
     * @param request
     *            httpServletRequest 객체
     */
    public UserAgentUtil(HttpServletRequest request) {
        this(request.getHeader("User-Agent"));
    }

    /**
     * User Agent 파싱
     */
    private void parse() {
        this.userAgent = this.userAgent.toLowerCase();
        String[] tmpSplitArr = this.userAgent.split("[()]");

        setApplicationInfo(tmpSplitArr);

        setMobileOrPC();

        setBrowserInfo();

        setEngineInfo();

    }

    private void setApplicationInfo(String[] tmpSplitArr) {
        if(tmpSplitArr.length <= 0){
            return;
        }

        String[] tmpArr = tmpSplitArr[0].trim().split("/");
        this.setApplicationName(tmpArr[0]);
        if (tmpArr.length > 1) {
            this.setApplicationVer(tmpArr[1]);
        }
    }

    private void setEngineInfo() {
        if(!(this.getEngineName() == null && this.getEngineVer() == null)){
            return;
        }

        Matcher match = UserAgentUtil.ptnEngineInfo.matcher(this.userAgent);
        if (match.find()) {
            this.setEngineName(match.group(1));
            this.setEngineVer(match.group(2).replaceAll("_", "."));
        }
    }

    /**
     * 브라우저 정보 설정
     */
    private void setBrowserInfo() {
        Matcher match = UserAgentUtil.ptnBrowserType.matcher(this.userAgent);
        // 정의된 브라우저 타입일 경우 (msie / chrome / safari / firefox)

        if (!match.find()) {
            return;
        }

        if ("msie".equals(match.group(1)) || "trident".equals(match.group(1))) {
            match = UserAgentUtil.ptnBrowserMsieVer.matcher(this.userAgent);
            this.setMSIEInfo(match);

        } else if ("firefox".equals(match.group(1))) {
            match = UserAgentUtil.ptnBrowserFirefoxVer.matcher(this.userAgent);
            this.setFireFoxInfo(match);

        } else {
            match = UserAgentUtil.ptnBrowserWebkitVer.matcher(this.userAgent);
            this.setWebkitInfo(match);

        }
    }

    /**
     * MSIE 셋팅
     * @param match
     */
    private void setMSIEInfo(Matcher match) {
        if (match.matches() && match.group(1) != null) {
            this.setBrowserName(match.group(1));
            this.setBrowserVer(match.group(2).replaceAll("_", "."));
        } else if (match.matches() && match.group(4) != null && match.group(4).startsWith("7.")) {
            // msie가 없고 trident만 있으며, trident 엔진 버전이 7.x인 경우 익스플로러 11로 간주함.
            this.setBrowserName("msie");
            this.setBrowserVer("11");
        }
    }

    /**
     * 파이어폭스 설정
     * @param match
     */
    private void setFireFoxInfo(Matcher match) {
        if (match.find()) {
            this.setBrowserName(match.group(5));
            this.setBrowserVer(match.group(6).replaceAll("_", "."));

            this.setEngineName(match.group(3));
            this.setEngineVer(match.group(2).replaceAll("_", "."));
        }
    }

    /**
     * 웹킷 설정
     * @param match
     */
    private void setWebkitInfo(Matcher match) {
        if (match.find()) {
            if ("chrome".equals(match.group(1))) {
                this.setBrowserName(match.group(1));
            } else if (match.group(3) != null && match.group(4) == null) {
                this.setBrowserName(match.group(3) + " " + match.group(5));
            } else {
                this.setBrowserName(match.group(5));
            }
            this.setBrowserVer(match.group(2).replaceAll("_", "."));
        }
    }

    private void setMobileOrPC() {
        Matcher match = UserAgentUtil.ptnMobileInfo.matcher(this.userAgent);
        // 모바일일 경우
        if (match.find()) {
            this.setMobile(true);

            this.setOsName(match.group(1));

            Matcher matchA = UserAgentUtil.ptnAndroidType.matcher(match.group(1));

            if(matchA.find()){
                this.setOsType(UserAgentConsts.OS_TYPE_A);
            }else{
                Matcher matchI = UserAgentUtil.ptnIOSType.matcher(match.group(1));

                if(matchI.find()){
                    this.setOsType(UserAgentConsts.OS_TYPE_I);
                }
            }

            this.setOsVer(match.group(2).replaceAll("_", "."));

        } else {
            // pc 버전 처리
            match = UserAgentUtil.ptnPcOs.matcher(this.userAgent);

            if (match.find()) {
                this.setOsName(match.group(1));
                this.setOsVer(match.group(3).replaceAll("_", "."));
            }
        }
    }

    /**
     * UserAgent 파싱
     *
     * @param userAgent
     * @return
     * @throws Exception
     */
    public static UserAgent getAppUserAgent(String userAgentStr) throws Exception{
        log.debug(">>> UserAgent 파싱 시작 : " + userAgentStr);

//        CryptoUtil cryptoUtil = new CryptoUtil(Config.getStatically("encryptKey"));
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        StringBuilder deviceIdstr = new StringBuilder();

        if(!"".equals(TextUtil.nullToSpace(userAgentStr))){
            String[] userAgentArray = TextUtil.split(userAgentStr, SEPARATOR_SPACEBAR);
            for(String item : userAgentArray){
                item = TextUtil.nullToSpace(item);
                String[] itemArray = TextUtil.split(item, SEPARATOR_SLASH);
                if(itemArray!=null && itemArray.length==2){
                    paramMap.put(itemArray[0], itemArray[1]);
                }else if(itemArray!=null && itemArray.length > 2){
                    for (int i = 0; i < itemArray.length; i++) {
                        if(i != 0){
                            deviceIdstr.append(itemArray[i]);
                            if(i != itemArray.length -1){
                                deviceIdstr.append(SEPARATOR_SLASH);
                            }
                        }
                    }
                    paramMap.put(itemArray[0], deviceIdstr.toString());
                }
            }
        }

//        log.debug(">>> UserAgent 파싱 결과 : " + paramMap);

        // EtMbrBaseEx 객체 설정
        UserAgent userAgent = new UserAgent();
        userAgent.setAppVer(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.APP_VER)));
        userAgent.setOsType(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.OS_TYPE)));
        userAgent.setOsVer(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.OS_VER)));
        userAgent.setDeviceModel(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.DEVICE_MODEL)));
        userAgent.setDeviceId(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.DEVICE_ID)));
        userAgent.setMode(TextUtil.nullToSpace((String)paramMap.get(UserAgentConsts.MODE)));
        // 디바이스아이디와 앱버전 정보가 있으면 APP으로 판단
        if(!"".equals(userAgent.getAppVer()) && !"".equals(userAgent.getDeviceId())){
            userAgent.setIsApp(true);
        }else{
            userAgent.setIsApp(false);
        }
//        // 단말 아이디 복호화
//        if(!"".equals(userAgent.getDeviceId())){
//            userAgent.setDeviceId(cryptoUtil.decrypt(userAgent.getDeviceId()));
//        }

//        log.debug(">>> UserAgent 파싱 종료 : " + userAgent);
        return userAgent;
    }

    public static String getClaimMediaCd() {
    	return "20";
    }

}
