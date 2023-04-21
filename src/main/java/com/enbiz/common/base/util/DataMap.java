/*
 * Copyright 2009-2011, SYS4U INC. All Rights Reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     1. Redistributions of source code must retain the above copyright 
 *        notice, this list of conditions and the following disclaimer. 
 *     
 *     2. Redistributions in binary form must reproduce the above copyright 
 *        notice, this list of conditions and the following disclaimer in the 
 *        documentation and/or other materials provided with the distribution. 
 *     
 *     3. The name of the author may not be used to endorse or promote products 
 *        derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY SYS4U INC ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SYS4U INC BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.enbiz.common.base.util;

import java.util.HashMap;

public class DataMap extends HashMap<String, Object> {
    protected static final String EMPTY_STRING = "";
    private static final long serialVersionUID = 1L;

    /**
     * 지정된 key에 저장된 객체를 문자열로 변환하여 반환한다. 만약 key에 대해 저장된 값이 없거나 null이 저장된 경우
     * 공백문자열("")을 반환한다.
     * <p>
     * 
     * @param key
     *            얻을 값을 지정하기 위한 key
     * @return 지정된 key에 저장된 객체를 문자열로 변환한 값 또는 지정된 key에 해당하는 HTTP 요청 파라미터 값. 만약 두
     *         값이 null인 경우 공백문자열("")을 반환한다.
     */
    public String getString(String key) {
        if (key == null) {
            return EMPTY_STRING;
        }

        Object value = get(key);

        if (value == null) {
            return EMPTY_STRING;
        }

        return value.toString();
    }

    /**
     * 지정된 key에 저장된 객체를 int값으로 변환하여 반환한다. 만약 key에 대해 저장된 값이 없거나 null이 저장된 경우 0을
     * 반환한다.
     * <p>
     *
     * @param key
     *            얻을 값을 지정하기 위한 key
     * @return 지정된 key에 저장된 객체를 int로 변환한 값. 만약 값이 null인 0을 반환한다.
     */
    public int getInt(String key) {
        if (key == null) {
            return 0;
        }

        Object value = get(key);

        if (value == null) {
            return 0;
        }

        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 지정된 key에 저장된 객체를 long값으로 변환하여 반환한다. 만약 key에 대해 저장된 값이 없거나 null이 저장된 경우 0을
     * 반환한다.
     * <p>
     *
     * @param key
     *            얻을 값을 지정하기 위한 key
     * @return 지정된 key에 저장된 객체를 long으로 변환한 값. 만약 값이 null인 0을 반환한다.
     */
    public long getLong(String key) {
        if (key == null) {
            return 0;
        }

        Object value = get(key);

        if (value == null) {
            return 0;
        }

        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else {
            return 0;
        }
    }

    /**
     * 지정된 key에 저장된 객체를 double값으로 변환하여 반환한다. 만약 key에 대해 저장된 값이 없거나 null이 저장된 경우
     * 0을 반환한다.
     * <p>
     *
     * @param key
     *            얻을 값을 지정하기 위한 key
     * @return 지정된 key에 저장된 객체를 double값으로 변환한 값. 만약 값이 null인 0을 반환한다.
     */
    public double getDouble(String key) {
        if (key == null) {
            return 0;
        }

        Object value = get(key);

        if (value == null) {
            return 0;
        }

        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            return 0;
        }
    }
}
