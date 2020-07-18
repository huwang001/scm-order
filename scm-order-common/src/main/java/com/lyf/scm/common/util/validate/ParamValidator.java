package com.lyf.scm.common.util.validate;

import com.rome.arch.core.clientobject.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Pattern;

public enum ParamValidator {

    /**
     * INSTANCE
     */
    INSTANCE;

    private final Pattern AMOUNT_PATTERN = Pattern.compile("([1-9]\\d*|0)(\\.\\d{1,2})?");
    private final Pattern NUMBER_PATTERN = Pattern.compile("[1-9]+\\d*");
    private final Pattern PHONE_PATTERN = Pattern.compile("1\\d{10}");

    /**
     * 检查字符串是否为空
     */
    public boolean validStr(String str) {
        return StringUtils.isNotBlank(str);
    }

    public boolean validParam(Serializable paramDTO) {
        return !(null == paramDTO);
    }

    /**
     * 检查集合
     */
    public boolean validCollection(Collection collection) {
        if (null == collection) {
            return false;
        }
        return !collection.isEmpty();
    }

    /**
     * 检查是否是正确的金额格式(最多两位小数)
     */
    public boolean validAmount(String str) {
        if (!validStr(str)) {
            return false;
        }
        return AMOUNT_PATTERN.matcher(str).matches();
    }

    /**
     * 检查是否为正整数
     */
    public boolean validNumber(String str) {
        if (!validStr(str)) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 检查Long类型是否合规(正整型)
     */
    public boolean validPositiveLong(Long ln) {
        if (null == ln) {
            return false;
        }
        return (ln > 0);
    }

    /**
     * 检查Long类型是否合规(正整型)
     */
    public boolean validPositiveInt(Integer in) {
        if (null == in) {
            return false;
        }
        return (in > 0);
    }

    /**
     * 检查BigDecimal类型是否合规(正整型)
     */
    public boolean validPositiveBigDecimal(BigDecimal bd) {
        if (null == bd) {
            return false;
        }
        return (bd.compareTo(BigDecimal.ZERO) > 0);
    }

    /**
     * 检查是否为手机号码
     */
    public boolean validPhoneNo(String str) {
        if (!validStr(str)) {
            return false;
        }
        return PHONE_PATTERN.matcher(str).matches();
    }

    public boolean validResponse(Response response) {
        if (null == response) {
            return false;
        }
        return "0".equals(response.getCode());
    }
}
