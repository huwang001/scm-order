package com.lyf.scm.common.util;

import java.math.BigDecimal;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/11
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);
}
