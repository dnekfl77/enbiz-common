package com.x2bee.common.base.util;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.x2bee.common.base.constant.X2Constants;

/**
 * 문자열 처리를 위한 Utility class
 * @author User
 *
 */
public class TextUtil {
    /**
     * 파일 경로중 방지해야 할 문자열.
     */
    private static String[] BLOCK_DIR = {"//", "./", "../", "\\\\", ".\\", "..\\", ".."};

	public static final int CHARSET_UTF8 = 1;
	public static final int CHARSET_NON_UTF8 = 0;

	private TextUtil(){
	    
	}
	
    /**
     * 주어진 시작지점으로부터 주어진 바이트길이만큼 문자열을 바이트단위로 잘른 문자열을 리턴 
     * @param str
     * @param iStartPos
     * @param iByteLength
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String subStringByte(String str, int iStartPos, int iByteLength) throws UnsupportedEncodingException {
        return subStringByte(str, iStartPos, iByteLength, "euc-kr");
    }

    /**
     * 주어진 시작지점으로부터 문자열을 바이트단위로 잘른 문자열을 리턴
     * @param str
     * @param iStartPos
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String subStringByte(String str, int iStartPos) throws UnsupportedEncodingException {
        return subStringByte(str, iStartPos, "euc-kr");
    }
    /**
     *  주어진 시작지점으로부터 주어진 바이트길이만큼 문자열을 바이트단위로 잘른 문자열을 리턴
     * @param str
     * @param iStartPos
     * @param iByteLength
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String subStringByte(String str, int iStartPos, int iByteLength, String encoding) throws UnsupportedEncodingException {
        StringBuffer sbStr = new StringBuffer(str.getBytes(encoding).length);
        try {
            if (str.getBytes(encoding).length <= iByteLength) {
                return str;
            }
            int iTotal = 0;
            
            for (char c : str.toCharArray()) {

                iTotal += String.valueOf(c).getBytes(encoding).length;
                
                if (iTotal > iByteLength + iStartPos) {
                    break;
                }
                if(iTotal > iStartPos){
                    sbStr.append(c);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
        return sbStr.toString();
    }
    
    /**
     * 주어진 시작지점으로부터 문자열을 바이트단위로 잘른 문자열을 리턴
     * @param str
     * @param iStartPos
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String subStringByte(String str, int iStartPos,String encoding) throws UnsupportedEncodingException {
        StringBuffer sbStr = new StringBuffer(str.getBytes(encoding).length);
        try {
            int iTotal = 0;
            
            for (char c : str.toCharArray()) {

                iTotal += String.valueOf(c).getBytes(encoding).length;
                
                if(iTotal > iStartPos){
                    sbStr.append(c);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
        return sbStr.toString();
    }
	/**
	 * 문자열의 바이트수를 리턴한다.
	 * TextUtil.CHARSET_UTF8 : 한글을 3바이트로 처리
	 * TextUtil.CHARSET_NON_UTF8 : 한글을 2바이트로 처리
	 * @param value
	 * @return
	 */
	public static long getLength(String value) {
		return getLength(value, TextUtil.CHARSET_UTF8);
	}

	public static long getLength(String value, int charset) {

		long length = 0;

		for (int i = 0 ; i < value.length() ; i++) {
			char chr = value.charAt(i);

			if (chr >= 0x0001 && chr <= 0x007F) {
				length++;
			} else if (chr > 0x07FF) {
				if (charset == TextUtil.CHARSET_UTF8) {	//한글 UTF8에서 3바이트
					length += 3;
				} else {
					length += 2;
				}
			} else {
				length += 2;
			}
		}

		return length;
	}

	/**
	 * 주어진 문자열을 처음위치부터 주어진 바이트 수만큼 자른다.
	 * @param value
	 * @param length
	 * @return
	 */
	public static String substring(String value, long length) {

		long count = 0;

		if(value == null) {
			return null;
		}

		try (CharArrayWriter writer = new CharArrayWriter()){

			for (int i = 0 ; i < value.length() ; i++) {

				char chr = value.charAt(i);

				if (chr >= 0x0001 && chr <= 0x007F) {
					count++;
				} else {
					count += 2;
				}

				if (count > length) {
					break;
				}

				writer.append(chr);
			}

			return writer.toString();

		}

	}

	/**
	 * 원본 문자열의 앞에 패딩 문자열을 반복하여 붇여 전체 크기가 totalLength인 문자열로 반환한다. 패딩 문자열은 전체 크기에
	 * 맞게 앞에서 부터 잘려서 들어가고 원본 문자열보다 전체 크기가 작을 수 없다.
	 * 예)	lpad("abcd", 8, "XY") --> XYXYabcd,
	 * 		lpad("abcd", 7, "XY") --> XYXabcd
	 *
	 * @param originalString
	 * 				원본문자열
	 * @param totalLength
	 * 				전체크기
	 * @param padString
	 * 				패딩문자열
	 * @return string
	 * @throws IllegalArgumentException
	 */
	public static String lpad(String originalString, int totalLength, String padString) {
		if (originalString == null || padString == null || totalLength <= 0) {
			throw new IllegalArgumentException("Target string and padding String can't be null and totalLength must be positive number");
		}

		int originalLength = originalString.length();
		int padLength = padString.length();

		if (totalLength == originalLength) {
			return originalString;
		} else if (totalLength < originalLength) {
			throw new IllegalArgumentException("Total length must not be smaller than original string length");
		}

		int moduloLength = (totalLength - originalLength) % padLength;
        StringBuilder pad = new StringBuilder(X2Constants.EMPTY);

		for (int i = originalLength; i < totalLength - moduloLength; i += padLength) {
			pad.append(padString);
		}

		if (moduloLength != 0) {
			pad.append(padString.substring(0, moduloLength));
		}

		return pad.append(originalString).toString();
	}

	/**
	 * 원본 문자열의 뒤에 패딩 문자열을 반복하여 붇여 전체 크기가 totalLength인 문자열로 반환한다. 패딩 문자열은 전체 크기에
	 * 맞게 앞에서 부터 잘려서 들어가고 원본 문자열보다 전체 크기가 작을 수 없다.
	 * 예) 	rpad("abcd", 8, "XY") --> abcdXYXY,
	 * 		rpad("abcd", 7, "XY") --> abcdXYX
	 *
	 * @param originalString
	 * @param totalLength
	 * @param padString
	 * @return string
	 * @throws IllegalArgumentException
	 */
	public static String rpad(String originalString, int totalLength, String padString) {
		if (originalString == null || padString == null || totalLength <= 0) {
			throw new IllegalArgumentException("Target string and padding String can't be null and totalLength must be positive number");
		}

		int originalLength = originalString.length();
		int padLength = padString.length();

		if (totalLength == originalLength) {
			return originalString;
		} else if (totalLength < originalLength) {
			throw new IllegalArgumentException("Total length must not be smaller than original string length");
		}

		int moduloLength = (totalLength - originalLength) % padLength;
        StringBuilder pad = new StringBuilder(X2Constants.EMPTY);

		for (int i = originalLength; i < totalLength - moduloLength; i += padLength) {
			pad.append(padString);
		}
		if (moduloLength != 0) {
			pad.append(padString.substring(0, moduloLength));
		}

		return originalString + pad.toString();
	}

	/**
	 * 작성자:박현진
	 * 개요:전문등의 통신용이나 DB저장시 사용. 바이트수 기준으로 오른쪽을 fillerh 채운다. filler가 없으면 " "로 채워진다.
	 */
	public static String rpadbyte(String value, int length, String orgFiller) {

	    String filler = orgFiller;
		if ( filler == null ) {
			filler = " ";	//기본값 스페이스
		}

		int count = 0;

		for (int i = 0 ; i < value.length() ; i++) {
			char chr = value.charAt(i);
			if (chr >= 0x0001 && chr <= 0x007F) {
				count++;
			} else {
				count += 2;
			}
		}

		StringBuilder result = new StringBuilder (value);
		for ( int i = count ; i < length ; i ++ ) {
			result.append (filler);
		}
		return result.toString ();
	}

	/**
	 * 작성자:박현진
	 * 개요:전문등의 통신용이나 DB저장시 사용. 바이트수 기준으로 왼쪽을 fillerh 채운다. filler가 없으면 " "로 채워진다.
	 */
	public static String lpadbyte(String value, int length, String orgFiller) {

	    String filler = orgFiller;
		if ( filler == null ) {
			filler = " ";	//기본값 스페이스
		}

		int count = 0;

		for (int i = 0 ; i < value.length() ; i++) {
			char chr = value.charAt(i);
			if (chr >= 0x0001 && chr <= 0x007F) {
				count++;
			} else {
				count += 2;
			}
		}

		StringBuilder result = new StringBuilder ();
		for ( int i = count ; i < length ; i ++ ) {
			result.append (filler);
		}
		result.append(value);
		return result.toString ();
	}

	/**
	 * 주어진 문자열을 구분자(delimiter)로 나눈 문자열 배열(<code>String[]</code>)을 반환한다.
	 *
	 * @return string배열
	 */
	public static String[] split(String str, String delimiter) {
		return split(str, delimiter, true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] split(String str, String delimiter, boolean bEmpty) {
		if (str == null) {
			return new String[0];
		}

        if (delimiter == null || delimiter.isEmpty()) {
			String[] ret = new String[1];
			ret[0] = str;
			return ret;
		}

		String token;
		if (bEmpty) {
			List list = new ArrayList();

			int start = 0;
			int end;
			while ((end = str.indexOf(delimiter, start)) != -1) {
				token = str.substring(start, end);
				list.add(token);
				// start = end + 1; // delimiter가 "#;"인 경우는?
				start = end + delimiter.length();
			}
			list.add(str.substring(start)); // !!!

			return (String[]) list.toArray(new String[list.size()]);
		} else { // 공백 무시
			if(str.length() < 1) {
				return new String[0];
			}

			List list = new ArrayList();

			int start = 0;
			int end;
			while ((end = str.indexOf(delimiter, start)) != -1) {
				token = str.substring(start, end);
                if (token != null && !token.isEmpty()) {
					list.add(token);
				}
				// start = end + 1; // delimiter가 "#;"인 경우는?
				start = end + delimiter.length();
			}
			token = str.substring(start);
            if (token != null && !token.isEmpty()) {
				list.add(token);
			}

			return (String[]) list.toArray(new String[list.size()]);
		}
	}

	/**
	 * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다.
	 * 대소문자를 구분하지 않고 변환한다.
	 *
	 * @param word
	 * @param regex
	 * 				자바 정규식 표현
	 * @param replacement
	 * @return string
	 */
	public static String replaceString(String word, String regex, String replacement) {
		return replaceString(word, regex, replacement, true);
	}

	/**
	 * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다.
	 * caseSensitive:true -> 대소문자를 구분하지 않는다.
	 * caseSensitive:false -> 대소문자를 구분한다.
	 *
	 * @param word
	 * @param regex
	 * 				자바 정규식 표현
	 * @param replacement
	 * @param caseSensitive
	 * @return string
	 */
	public static String replaceString(String word, String regex, String replacement, boolean caseSensitive) {

		if (word == null) {
			throw new IllegalArgumentException("Parameter 'word' can not be null.");
		}

        if (word.isEmpty()) {
            return X2Constants.EMPTY;
		}

		if (regex == null) {
			throw new IllegalArgumentException("Parameter 'regex' can not be null.");
		}

		if (replacement == null) {
			throw new IllegalArgumentException("Parameter 'replacement' can not be null.");
		}

		Pattern pattern;
		if ( caseSensitive ) {
			pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(regex);
		}

		return pattern.matcher(word).replaceAll(replacement);
	}

	/**
	 * 문자열을 지정된 갯수를 원하는 문자열로 변환한다.
	 * 예)	replaceString("supermam", -3, "*") -> super***
	 * 		replaceString("supermam", 3, "*") -> ***erman
	 * 실제 문자열 길이를 초과하면 모든 문자를 치환한다.
	 *
	 * @param word
	 * @param length
	 * 			치환할 문자갯수(양수이면 앞에서 부터, 음수이면 뒤에서부터 치환함)
	 * @param replacement
	 * 			치환할 문자열
	 * @return string
	 */
	public static String replaceString(String word, int length, String replacement) {

		String regexp;
        int real_length = Math.abs(length) > word.length() ? word.length() : Math.abs(length);

		if (length >= 0) {
			regexp = "^.{" + real_length + "}";
		} else {
			regexp = ".{" + real_length + "}$";
		}

		StringBuilder buf = new StringBuilder();
		for ( int i = 0 ; i < real_length ; i++ ) {
			buf.append(replacement);
		}
		Pattern pattern = Pattern.compile(regexp);

		return pattern.matcher(word).replaceAll(buf.toString());
	}

	public static String convertToPrintableHtml(String str) {
        if (str == null || str.isEmpty()) {
            return X2Constants.EMPTY;
		}

		String ret = str;

		ret = TextUtil.replaceString(ret, "<", "&lt;");
		ret = TextUtil.replaceString(ret, ">", "&gt;");
		ret = TextUtil.replaceString(ret, "\t", "&nbsp;&nbsp;&nbsp;");
		ret = TextUtil.replaceString(ret, "\"", "&#34;");

		ret = convertToEnterKey(ret);
		ret = ret.trim();
		return ret;
	}

	public static String convertToEnterKey(String str){
        if (str == null || str.isEmpty()) {
            return X2Constants.EMPTY;
		}

		String ret = str;

		// "\n" -> <br>
		ret = TextUtil.replaceString(ret, "\n", "<br>");
		// "\r\n" -> <br>
		ret = TextUtil.replaceString(ret, "\r\n", "<br>");
		return ret;
	}

	public static String splitToEnterKey(String str){
	    if (str == null || str.isEmpty()) {
	        return X2Constants.EMPTY;
	    }
	    
	    String ret = str;
	    
	    Pattern patten = Pattern.compile("\n");
	    Matcher matcher = patten.matcher(ret);
	    if(matcher.find()){
	        ret = ret.split("\n")[0];
	    }
	    patten = Pattern.compile("\r\n");
        matcher = patten.matcher(ret);
        if(matcher.find()){
            ret = ret.split("\r\n")[0];
	    }
	    return ret;
	}

	public static boolean isEmpty(String word) {
		if (word == null) {
			return true;
		}
		for ( int i = 0 ; i < word.length() ; i++ ) {
			if ( !Character.isWhitespace(word.charAt(i)) ) {
				return false;
			}
		}
		return true;
	}

    /**
     * 정규식을 이용한 html태그(태그내용) 제거
     */
	public static String removeTag(String s) {
        return s.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", X2Constants.EMPTY);
	}

	/**
	 * 작성자:박현진
	 * 개요:XSS(크로스 사이트 스크립팅 방지)
	 */
	public static String xss(String value) {

        if (value == null || value.isEmpty()) {
            return X2Constants.EMPTY;
		}

		String ret = value;

		ret = TextUtil.replaceString(ret, "<", "&lt;");
		ret = TextUtil.replaceString(ret, ">", "&gt;");
		ret = TextUtil.replaceString(ret, "\t", "&nbsp;&nbsp;&nbsp;");
		ret = TextUtil.replaceString(ret, "\"", "&#34;");

		return ret.trim();
	}

	/**
	 * xss 방지가 되어있는 문자열을 원상복귀한다.
	 * @param value
	 * @return
	 * @see TextUtil.xss(value)
	 */
	public static String removeXss(String value){
        if (value == null || value.isEmpty()) {
            return X2Constants.EMPTY;
        }

        String ret = value;

        ret = TextUtil.replaceString(ret, "&lt;", "<");
        ret = TextUtil.replaceString(ret, "&gt;", ">");
        ret = TextUtil.replaceString(ret, "&nbsp;&nbsp;&nbsp;", "\t");
        ret = TextUtil.replaceString(ret, "&#34;", "\"");

        return ret.trim();
	}

	/**
	 * 작성자:박현진
	 * 개요:자바스크립트 태그 무력화
	 */
	public static String removeScript(String s) {

        if (s == null || s.isEmpty()) {
            return X2Constants.EMPTY;
		}

		String replaced = s.replaceAll("<[sS][cC][rR][iI][pP][tT]>", "&lt;script&gt;");
		replaced = replaced.replaceAll("</[sS][cC][rR][iI][pP][tT]>", "&lt;/script&gt;");

		return replaced;
	}


    /**
     * Cross Script 방어.
     * @param source
     * @return
     */
    public static String cleanXSS(String source) {
        if (source == null || source.isEmpty() || source.trim().isEmpty()) {
        	return X2Constants.EMPTY;
        }

        String str = source;
        str = str.trim();
        str = xss(str);
        str = removeScript(str);
        str = str.replaceAll("eval\\((.*?)\\)", "");
        str = str.replaceAll("expression\\((.*?)\\)", "");
        str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        str = str.replaceAll("[\\\"\\\'][\\s]*vbscript:(.*)[\\\"\\\']", "\"\"");
        //str = str.replaceAll("([\\\"\\s])on([a-z]+)", "$1on-$2");
        return str;
    }

    /**
     * Cross Script 방어(JS 제거).
     * @param source
     * @return
     */
    public static String cleanXSS2(String source) {
        if (source == null || source.isEmpty()) {
        	return X2Constants.EMPTY;
        }

        String str = source;
        str = cleanXSS(str);

        str = str.replaceAll("cookie", "coo-kie");
        str = str.replaceAll("document", "doc-ument");
        str = str.replaceAll("([a-z]+)script:", "$1-script:");
        str = str.replaceAll("(<layer[^>]+)src", "$1x-src");
        str = str.replaceAll("(i*)input(.*?)", "$1x-input$2");
        str = str.replaceAll("(a*)action(.*?)", "$1x-action$2");
        str = str.replaceAll("function", "");
        str = str.replaceAll("alert", "");
        return str;
    }

    /**
     * Cross Script 방어 (HTML 제거 및 JS 제거).
     * @param source
     * @return
     */
    public static String cleanXSS3(String source) {
        if (source == null || source.isEmpty()) {
        	return X2Constants.EMPTY;
        }

        String str = source;
        str = removeTag(str);
        str = cleanXSS2(str);

        return str;
    }


    /**
     * decodeXSS
     * @param source
     * @return
     */
    public static String decodeXSS(String source) {
    	if (source == null || source.isEmpty() || source.trim().isEmpty()) {
    		return X2Constants.EMPTY;
        }

        String str = source;
        str = str.trim();
        str = removeXss(str);
        str = str.replaceAll("&#x2F;", "/");
        return str;
    }

    /**
     * 부적절한 디렉토리 경로 확인.
     * @param source
     * @return
     */
    public static boolean checkDir(String source) {
        if (source == null || source.isEmpty() || source.trim().isEmpty()) {
        	return true;
        }

        String str	= source;
        Boolean chk	= true;

        for (int i = 0; i < BLOCK_DIR.length; i++) {
        	if (str.indexOf(BLOCK_DIR[i]) > -1) {
        		chk = false;
        	}
        	if (!chk) break;
        }
        return chk;
    }

    /**
     * 부적절한 디렉토리 경로 제거.
     * @param source
     * @return
     */
    public static String cleanDir(String source) {

    	if (checkDir(source)) {
    		return source;
    	}

        String str = source;
        str = cleanXSS(str);
        for (int r = 0; r < 2; r++) {
	        for (int i = 0; i < BLOCK_DIR.length; i++) {
	        	str = str.replace(BLOCK_DIR[i], "");
	        }
        }
        return str;
    }

    /**
     * 문자열의 오른쪽에서 부터 Substring을 진행하여 반환.
     * @param source
     * @param right
     * @return
     */
    public static String substringRight(String source, int right) {
    	String str = source;
    	if (str == null) {
    		return X2Constants.EMPTY;
    	}

    	str = str.trim();
    	if (str.isEmpty() || str.length() <= right) {
    		return str;
    	}

    	str = str.substring(str.length() - right, str.length());

    	return str;
    }

    /**
     * 문자열이 변경되었는지 검사.
     * @param source
     * @param dest
     * @return
     */
    public static boolean isUpdated(String source, String dest) {
        if (source == null) {
            return !(dest == null);
        }

        return !source.equals(dest);
    }

    /**
     * 일자 형태의 문자열에서 숫제를 제외한 내용을 제거하고 반환.
     * @param source
     * @return
     */
    public static String cleanDatetime(String source) {
    	if (source == null || source.isEmpty()) {
            return source;
        }

        return source.replaceAll("-","").replaceAll(":","").replaceAll(" ","");
    }
    
    /**
     * str1의 값이 null이거나 공백이면 str2의 값으로 대체한다.<br>
     * str2의 값도 null이거나 공백이면 공백을 반환한다. 만약 str1이 값이 있다면 str1의 값을 반환한다. <br>
     * <br>
     * 작성일자 : 2009. 02. 18 <br>
     * 변경이력 :
     * 
     * @param str1
     *            체크할 문자열
     * @param str2
     *            대체할 문자열
     * @return str1의 값, str2의 값, 공백 중 하나
     */
    public static String nvl(String str1, String str2) {
        String returnValue;

        if (str1 == null || "".equals(str1)) {
            if (str2 != null && !"".equals(str2)) {
                returnValue = str2.trim();
            } else {
                returnValue = "";
            }
        } else {
            returnValue = str1.trim();
        }

        return returnValue;
    }

    /**
     * null을 공백으로 치환한다. <br>
     * <br>
     * 작성일자 : 2009. 02. 18 <br>
     * 변경이력 :
     * 
     * @param str1
     * @return String
     */
    public static String nullToSpace(String str1) {
        String returnValue;

        if (str1 == null) {
            returnValue = "";
        } else {
            returnValue = str1.trim();
        }

        return returnValue;
    }

    /**
     * null을 "0"으로 치환한다. <br>
     * <br>
     * 작성일자 : 2009. 02. 18 <br>
     * 변경이력 :
     * 
     * @param str1
     * @return String
     */
    public static String nullToZero(String str1) {
        String returnValue;

        if (str1 == null) {
            returnValue = "0";
        } else {
            returnValue = str1.trim();
        }

        return returnValue;
    }
    
    /**
     * [오쇼핑]String에 CDATA tag 붙이기
     * @param str
     * @return
     */
    public static String setStringCDATA(String str) {
        String returnValue;
        if (str == null || str.isEmpty()) {
            return X2Constants.EMPTY;
        }

        returnValue = "<![CDATA["+str+"]]>"; 
        return returnValue;
    }
    
    public static String stringReplaceCDATA(String str) {
        String returnValue;
        if (str == null || str.isEmpty()) {
            return X2Constants.EMPTY;
        }
        
        returnValue = str.replace("<![CDATA[","").replace("]]>","") ; 
        return returnValue;
    }
}
