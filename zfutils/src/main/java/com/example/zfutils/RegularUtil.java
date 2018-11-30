package com.example.zfutils;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/27
 * desc  : 正则匹配工具类
 * version : 1.0
 */

public final class RegularUtil {
    private RegularUtil() {
    }

    /**
     * 判断是否是邮箱
     *
     * @param str 指定的字符串
     * @return true:是,false:否
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * 是否只是数字
     *
     * @param str 指定的字符串
     * @return true:是,false:否
     */
    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * 手机号格式验证
     *
     * @param str 指定的手机号码字符串
     * @return true:是,false:否
     * 这里判断的规则是只要是11位的数字
     */
    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
//            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,5-9]))\\d{8}$");
        String expr = "^1[0-9]{10}$";
        if (str.matches(expr)) {
            isMobileNo = true;
        }
        return isMobileNo;
    }

    /**
     * 是否只是字母和数字
     *
     * @param str 指定的字符串
     * @return true:是,false:否
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 是否是身份证号
     *
     * @param str 指定字符串
     * @return true:是,false:否
     */
    public static boolean isIDCard(String str) {
        //15位身份证
        String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //18位身份证
        String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        Boolean isIDCard = false;
        if (str.matches(REGEX_ID_CARD15) ||
                str.matches(REGEX_ID_CARD18)) {
            isIDCard = true;
        }
        return isIDCard;
    }
}
