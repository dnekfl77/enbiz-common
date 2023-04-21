package com.enbiz.common.base.masking;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MaskingUtils {
    public static final char DEFAULT_REPLACE = '*';
    
	/**
	* Obtained desensitization value
	*
	* @param value
	* @param type
	* @return {@link String}
	*/
	public static String getValue(String value, MaskingType type) {
		if(StringUtils.isEmpty(value) || "NULL".equalsIgnoreCase(value)) {
			return "";
		}

		switch (type) {

			/**
			 * DEFAULT
			 * 전체 마스킹
			 */
			case DEFAULT:
				value = masking(value, 0);	//전체 마스킹
				break;

			/**
			 * 성명_한글
			 * 뒤 1자리 마스킹
			 */
			case NAME_KR:
				// 공백제거
				value = value.replaceAll(" ", "");

				//뒤 1자리 마스킹
				if(value.length()>=2) {
					value = masking(value, 1, 1);
				}
				break;

			/**
			 * 성명_영문
			 * -3자 이상 : 첫번째와 마지막 알파베 제외하고 마스킹
			 * -2자 이하: 뒤 1자리 마스킹
			 */
			case NAME_EN:
				//9자 이상: 9자 부터 절사
				if(value.length()>8) {
					value = value.substring(0, 8);
				}

				//4자 이하: 뒤 1자리 마스킹
				if(value.length()<=2) {
					value = masking(value, value.length()-1);
				} else {
					//3자 이상 : 앞, 뒤 한 글자씩 남기고 가운데 마스킹
					value = masking(value, 1, value.length()-2);
				}
				break;

			/**
			 * 생년월일
			 * - 일자리 마스킹
			 */
			case BIRTH:
				if(value.length()==8) {
					value = masking(value, 6);
				}
				else {
					value = masking(value, 4);
				}
				break;

			/**
			 * 주민등록번호
			 * - 앞 7자리 이하 마스킹 (공백 포함)
			 */
			case RRN:
				value = masking(value, 6);
				break;

			/**
			 * 전화번호
			 * - 중간번호 전체 마스킹
			 */
			case PHONE_NUM:
				value = masking(value, 3, 4);
				break;

			/**
			 * 핸드폰
			 * - 중간번호 전체 마스킹
			 */
			case MOBILE_NUM:
				if(value.length()==11) {
					value = masking(value, 3, 4);
				} else {
					value = masking(value, 3, 3);
				}
				break;

			/**
			 * 주소
			 * - 앞 7자리 이하 마스킹 (공백 포함)
			 */
			case ADDRESS://주소
				value = masking(value, 6);
				break;

			/**
			 * 상세 주소
			 * - 전체 마스킹
			 */
			case ADDRESS_DTL:
				value = masking(value, 0);
				break;

			/**
			 * IP ADDRESS
			 * IPv4 경우 17~24비트, IPv6 경우 113~128비트 영역
			 * 123.123.***.123
			 */
			case IP:
				String[] arryIP = value.split("\\.");

				//IPv4
				if(arryIP.length==4) {
					value = arryIP[0]+"."+arryIP[1]+"."+masking(arryIP[2], 0)+"."+arryIP[3];
					break;
				}

				//IPv6
				arryIP = value.split("\\:");
				if(arryIP.length==8) {
					value = arryIP[0]+":"+arryIP[1]+":"+arryIP[2]+":"+arryIP[3]+":"+arryIP[4]+":"+arryIP[5]+":"+arryIP[6]+":"+masking(arryIP[7], 0);
					break;
				}
				break;

			/**
			 * 이메일
			 * - 앞 4자리 노출, 이후 @제외하고 전체 마스킹
			 */
			case EMAIL:
				if(value.indexOf("@") < 0 || value.length() <= 4) {
					break;
				}

				String[] emails = value.split("\\@");

				//앞 4자리 노출, 이후 @제외하고 전체 마스킹
				value = masking(emails[0], 4) + "@" + masking(emails[1], 0);
				break;

			/**
			 * ID
			 * - 4자리부터 끝까지 마스킹
			 */
			case ID:
				if(value.length() <= 4) {
					break;
				}

				value = masking(value, 3);
				break;

			/**
			 * 계좌번호
			 * - 6번째부터 끝까지 마스킹
			 */
			case ACTN:
				if(value.length() <= 6) {
					break;
				}

				value = masking(value, 5);
			break;

			/**
			 * 신용카드 번호
			 * - 2, 3번째 영역 전체 마스킹
			 * -16자리인 경우: 1234********5678
			 * -15자리인 경우: 1234********567
			 */
			case CARD:
				if(value.length()==11) {
					value = masking(value, 6, 4);
				}
				else {
					value = masking(value, 8, 4);
				}
				break;

			/**
			 * 개인정보 메모
			 * - 전체 마스킹
			 */
			case MEMO://계좌번호
				value = masking(value, 0);
				break;

			/**
			 * 운전면허번호
			 * - 5번째부터 6자리이상 마스킹
			 *   서울 95-******-61 
			 *   또는 11-95-******-61
			 */
			case LICENSE:
				value = masking(value, 4, 6);
				break;
				
			case PASSPORT:
				value = masking(value, 5, 4);
				break;
				
			case BNO:
				value = masking(value, 2, 4);
				break;
				
			case QRCODE:
				value = masking(value, 4, 4);
				break;
				
			
			default:
				value = masking(value, 0);
				break;
		}

		return value;
	}

    /**
     * 문자열 마스킹
     *
     * @param src      원본
     * @param startIdx 시작위치
     * @return 마스킹 적용된 문자열
     */
    public static String masking(String src, int startIdx) {
        return masking(src, DEFAULT_REPLACE, null, startIdx, src.length());
    }

    /**
     * 문자열 마스킹
     *
     * @param src      원본
     * @param startIdx 시작위치
     * @param length   길이
     * @return 마스킹 적용된 문자열
     */
    public static String masking(String src, int startIdx, int length) {
        return masking(src, DEFAULT_REPLACE, null, startIdx, length);
    }

    /**
     * 문자열 마스킹
     *
     * @param src      원본
     * @param replace  대치문자
     * @param startIdx 시작위치
     * @return 마스킹 적용된 문자열
     */
    public static String masking(String src, char replace, int startIdx) {
        return masking(src, replace, null, startIdx, src.length());
    }

    /**
     * 문자열 마스킹
     *
     * @param src      원본
     * @param replace  대치문자
     * @param startIdx 시작위치
     * @param length   길이
     * @return 마스킹 적용된 문자열
     */
    public static String masking(String src, char replace, int startIdx,
                                 int length) {
        return masking(src, replace, null, startIdx, length);
    }

    /**
     * 문자열 마스킹
     *
     * @param src      원본
     * @param replace  대치문자
     * @param exclude  제외문자
     * @param startIdx 시작위치
     * @param length   길이
     * @return 마스킹 적용된 문자열
     */
    public static String masking(String src, char replace, char[] exclude,
                                 int startIdx, int length) {
		if(StringUtils.isEmpty(src) || "NULL".equalsIgnoreCase(src)) {
			return "";
		}
        StringBuffer sb = new StringBuffer(src);

        // 종료 인덱스
        int endIdx = startIdx + length;
        if (sb.length() < endIdx)
            endIdx = sb.length();

        // 치환
        for (int i = startIdx; i < endIdx; i++) {
            boolean isExclude = false;
            // 제외 문자처리
            if (exclude != null && 0 < exclude.length) {
                char currentChar = sb.charAt(i);

                for (char excludeChar : exclude) {
                    if (currentChar == excludeChar)
                        isExclude = true;
                }
            }

            if (!isExclude)
                sb.setCharAt(i, replace);
            // sb.replace(i, i + 1, replace);
        }

        return sb.toString();
    }

}