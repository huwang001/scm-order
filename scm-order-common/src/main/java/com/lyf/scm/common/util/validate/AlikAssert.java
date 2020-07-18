package com.lyf.scm.common.util.validate;

import com.rome.arch.core.exception.RomeException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 类AlikAssert.java的实现描述：断言工具类
 */
@SuppressWarnings("rawtypes")
public class AlikAssert {

    /**
     * Assert a boolean expression, throwing <code>RomeException</code> if the test result is <code>false</code>.
     * 
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
     * </pre>
     *
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if expression is <code>false</code>
     */
    public static void isTrue(boolean expression, String code ,String message) {
        if (!expression) {
            throw  new RomeException(code, message);
        }
    }

    public static void isFalse(boolean expression, String code ,String message) {
        if (expression) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * Assert that an object is <code>null</code> .
     * 
     * <pre class="code">
     * Assert.isNull(value, &quot;The value must be null&quot;);
     * </pre>
     * 
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the object is not <code>null</code>
     */
    public static void isNull(Object object, String code ,String message) {
        if (object != null) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * 断言对象不为null，为null则抛出异常
     * 
     * @param object
     * @param message
     */
    public static void isNotNull(Object object, String code ,String message) {
        if (object == null) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * 断言字符串不为空，为空则抛出异常
     * 
     * @param str
     * @param message
     */
    public static void isNotEmpty(String str, String code ,String message) {
        if (StringUtils.isEmpty(str)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * 断言字符串不为空，为空则抛出异常
     * 
     * @param str
     * @param message
     */
    public static void isNotBlank(String str, String code ,String message) {
        if (StringUtils.isBlank(str)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * 断言集合不为空，为空则抛出异常
     * 
     * @param message
     */
    public static void isNotEmpty(Collection<?> coll, String code ,String message) {
        if (CollectionUtils.isEmpty(coll)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * Assert that an object is not <code>null</code> .
     * 
     * <pre class="code">
     * Assert.notNull(clazz, &quot;The class must not be null&quot;);
     * </pre>
     * 
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the object is <code>null</code>
     */
    public static void notNull(Object object, String code ,String message) {
        if (object == null) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * Assert that an array has elements; that is, it must not be <code>null</code> and must have at least one element.
     * 
     * <pre class="code">
     * Assert.notEmpty(array, &quot;The array must have elements&quot;);
     * </pre>
     * 
     * @param array the array to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the object array is <code>null</code> or has no elements
     */
    public static void notEmpty(Object[] array, String code ,String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw  new RomeException(code, message);
        }
    }

    public static void empty(Object[] array, String code ,String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * Assert that an array has no null elements. Note: Does not complain if the array is empty!
     * 
     * <pre class="code">
     * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
     * </pre>
     * 
     * @param array the array to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the object array contains a <code>null</code> element
     */
    public static void noNullElements(Object[] array, String code ,String message) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    throw  new RomeException(code, message);
                }
            }
        }
    }

    /**
     * Assert that a collection has elements; that is, it must not be <code>null</code> and must have at least one
     * element.
     * 
     * <pre class="code">
     * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
     * </pre>
     * 
     * @param collection the collection to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the collection is <code>null</code> or has no elements
     */
    public static void notEmpty(Collection collection, String code ,String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw  new RomeException(code, message);
        }
    }

    public static void empty(Collection collection, String code ,String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * Assert that a Map has entries; that is, it must not be <code>null</code> and must have at least one entry.
     * 
     * <pre class="code">
     * Assert.notEmpty(map, &quot;Map must have entries&quot;);
     * </pre>
     * 
     * @param map the map to check
     * @param message the exception message to use if the assertion fails
     * @throws RomeException if the map is <code>null</code> or has no entries
     */
    public static void notEmpty(Map map, String code ,String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the String. That functionality is available in
     * isBlank().
     * </p>
     * 
     * @param str the String to check, may be null
     * @throws RomeException if the str is empty
     */
    public static void notEmpty(String str, String code ,String message) {
        if (StringUtils.isEmpty(str)) {
            throw  new RomeException(code, message);
        }
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     * 
     * @param str the String to check, may be null
     * @throws RomeException if the str is blank
     */
    public static void notBlank(String str, String code ,String message) {
        if (StringUtils.isBlank(str)) {
            throw  new RomeException(code, message);
        }
    }

    public static void notEquals(String str1, String str2, String code ,String message) {
        if (StringUtils.equals(str1, str2)) {
            throw  new RomeException(code, message);
        }
    }


}
