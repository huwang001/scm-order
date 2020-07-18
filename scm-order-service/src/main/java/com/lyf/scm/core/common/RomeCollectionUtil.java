package com.lyf.scm.core.common;

import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类RomeCollectionUtil的实现描述：罗马Collection工具
 *
 * @author sunyj 2019/4/21 11:09
 */
public class RomeCollectionUtil {

    private RomeCollectionUtil() {
    }

    private static final Probe PROBE = new Probe();
    @SuppressWarnings("rawtypes")
    public static final Collection NULL_COLLECTION = new NullCollection();

    @SuppressWarnings("unchecked")
    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }

    static class NullCollection extends AbstractList<Object> implements RandomAccess, Serializable  {

        private static final long serialVersionUID = 5206887786441397812L;

        @Override
        public Object get(int index) {
            return null;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean contains(Object obj) {
            return null == obj;
        }

        private Object readResolve() {
            return null;
        }
    }

    /**
     * 去重
     *
     * @param list
     * @return
     */
    public static <V> List<V> distinct(List<V> list) {
        //为空或者长度为1的不需要去重
        if (null != list && list.size() > 1) {
            Set<V> set = new HashSet<V>(list);
            list = new ArrayList<V>(set);
        }
        return list;
    }

    /**
     * @param <K>
     * @param <V>
     * @param keys
     * @param source
     * @return
     * @author liuhongbo
     * <pre>
     * 批量获取map中的值
     * </pre>
     */
    public static <K, V> List<V> getAllFormMap(List<K> keys, Map<K, V> source) {
        List<V> values = Collections.emptyList();
        if (keys != null && !keys.isEmpty() && source != null && !source.isEmpty()) {
            values = new ArrayList<V>();
            for (K k : keys) {
                values.add(source.get(k));
            }
        }
        return values;
    }

    /**
     * @param <E>
     * @param list
     * @param valueProp
     * @return
     * @author liuhongbo
     * <pre>
     * 从List中获取valueProp组成一个新的list
     * </pre>
     */
    public static <E, K> List<K> getValueList(List<E> list, String valueProp) {
        List<K> valueList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(list)) {
            list.removeAll(nullCollection());
            valueList = new ArrayList<K>(list.size());
            for (E e : list) {
                @SuppressWarnings("unchecked")
                K value = (K) PROBE.getObject(e, valueProp);
                valueList.add(value);
            }
        }
        return valueList;
    }

    /**
     * <pre>
     * 从Object中获取某个属性的value
     * </pre>
     */
    public static Object getPropValue(Object o, String propName) {
        try {
            propName =  propName.toLowerCase();
            if(propName.indexOf(".")!=-1){
                propName = propName.substring(propName.indexOf(".")+1);
            }
            while(propName.indexOf("_")!=-1) {
                propName = propName.substring(0, propName.indexOf("_"))
                        + Character.toUpperCase(propName.charAt(propName.indexOf("_") + 1))
                        + propName.substring(propName.indexOf("_") + 2);
            }

            String getMethod = "get" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
            Method propMethod = o.getClass().getMethod(getMethod, (Class[]) null);
            return propMethod.invoke(o, (Object[]) null);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @author liuhongbo
     * <pre>
     * 用list生成一个map,keyProp 为指定的key,valueProp 为指定是value
     * </pre>
     *
     * @param <K>
     * @param <V>
     * @param <E>
     * @param list
     * @param keyProp
     * @param valueProp
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K,V,E> Map<K,V> listforMap(List<E> list, String keyProp, String valueProp) {
        Map<K,V> map = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(list)) {
            list.removeAll(nullCollection());
            map = new HashMap<K, V>(list.size());
            for (E object : list) {
                K key = (K)PROBE.getObject(object, keyProp);
                Object value = null;
                if (StringUtils.isEmpty(valueProp)) {
                    value = object;
                } else {
                    value = PROBE.getObject(object, valueProp);
                }
                map.put(key, (V)value);
            }
        }
        return map;
    }

    /**
     * @author liuhongbo
     * <pre>
     * 用list生成一个map,keyProp 为指定的key,value为该对象
     * </pre>
     *
     * @param <K>
     * @param <E>
     * @param list
     * @param keyProp
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K,E> Map<K,E> listforMap(List<E> list, String keyProp) {
        Map<K,E> map = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(list)) {
            list.removeAll(nullCollection());
            map = new HashMap<K, E>(list.size());
            for (E object : list) {
                K key = (K)PROBE.getObject(object, keyProp);
                map.put(key, object);
            }
        }
        return map;
    }

    /**
     * @author liuhongbo
     * <pre>
     * list 生成一个Map<K,List<V>>
     * </pre>
     *
     * @param <K>
     * @param <V>
     * @param <E>
     * @param list
     * @param keyProp
     * @param valueProp
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K,V,E> Map<K,List<V>> listforListMap(List<E> list, String keyProp,
                                                        String valueProp) {
        Map<K, List<V>> map = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(list)) {
            list.removeAll(nullCollection());
            map = new HashMap<K, List<V>>(list.size());
            V value = null;
            for (E object : list) {
                K key = (K)PROBE.getObject(object, keyProp);
                if (StringUtils.isEmpty(valueProp)) {
                    value = (V)object;
                } else {
                    value = (V)PROBE.getObject(object, valueProp);
                }
                List<V> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<V>();
                }
                values.add(value);
                map.put(key, values);
            }
        }
        return map;
    }
    /**
     * 将map value转换为list
     * @param map
     * @return
     */
    public static <K,T> List<T> mapToValueList(Map<K,T> map){
        if(map==null||map.isEmpty()){
            return new ArrayList<T>();
        }
        List<T> resultList = new ArrayList<T>();
        Set<Entry<K, T>> entrySet = map.entrySet();
        Iterator<Entry<K, T>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            resultList.add(iterator.next().getValue());
        }
        return resultList;
    }

    /**
     * 将map value转换为list
     * @param map
     * @return
     */
    public static <K,T> List<T> listMapToValueList(Map<K,List<T>> map){
        if(map==null||map.isEmpty()){
            return new ArrayList<T>();
        }
        List<T> resultList = new ArrayList<T>();
        Set<Entry<K, List<T>>> entrySet = map.entrySet();
        Iterator<Entry<K, List<T>>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            resultList.addAll(iterator.next().getValue());
        }
        return resultList;
    }

    /**
     *
     * @param conVos
     * @param string
     */
    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,List<V>> listforListMap(List<V> list,
                                                      String keyProp) {
        Map<K, List<V>> map = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(list)) {
            list.removeAll(nullCollection());
            map = new HashMap<K, List<V>>(list.size());
            for (V object : list) {
                K key = ((K)PROBE.getObject(object, keyProp));
                List<V> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<V>();
                    map.put(key, values);
                }
                values.add(object);
            }
        }
        return map;
    }

    /**
     * 将一个List拆开为多个List
     * @param targe,源List
     * @param size 子LIst的大小
     * @return
     */
    public static <T> List<List<T>> createList(List<T> targe,int size) {
        List<List<T>> listArr = new ArrayList<List<T>>();
        //获取被拆分的数组个数
        int arrSize = targe.size()%size==0?targe.size()/size:targe.size()/size+1;
        for(int i=0;i<arrSize;i++) {
            List<T>  sub = new ArrayList<T>();
            //把指定索引数据放入到list中
            for(int j=i*size;j<=size*(i+1)-1;j++) {
                if(j<=targe.size()-1) {
                    sub.add(targe.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }

    /**
     * 将map key进行转换
     * @param sourcemap
     * @return
     */
    public static <K,V> Map<String, V> convertStringMap(Map<K, V> sourcemap){
        Map<String, V> distinctMap = new HashMap<String, V>();
        if(sourcemap == null){
            return distinctMap;
        }

        for (Entry<K, V> entry : sourcemap.entrySet()) {
            distinctMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return distinctMap;
    }

}

/**
 * -----------------------------------------------------------------------------
 * 辅助类
 * <pre>
 *
 * </pre>
 *
 * @author gang.xu
 * @version $Id: FengCollectionUtil.java, v 0.1 2016年5月18日 上午10:48:58 gang.xu Exp $
 */
class Probe {
    private static final ComplexBeanProbe BEAN_PROBE = new ComplexBeanProbe();
    private static final DomProbe DOM_PROBE = new DomProbe();

    public Object getObject(Object object, String name) {

        if (object instanceof Document) {
            return DOM_PROBE.getObject(object, name);
        } else if (object instanceof List) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof Object[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof char[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof boolean[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof byte[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof double[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof float[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof int[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof long[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else if (object instanceof short[]) {
            return BEAN_PROBE.getIndexedProperty(object, name);
        } else {
            return BEAN_PROBE.getObject(object, name);
        }
    }

}

class ComplexBeanProbe{
    private static final Object[] NO_ARGUMENTS = new Object[0];

    protected Object getIndexedProperty(Object object, String indexedName) {

        Object value = null;

        try {
            String name = indexedName.substring(0, indexedName.indexOf('['));
            int i = Integer.parseInt(indexedName.substring(indexedName.indexOf('[') + 1, indexedName.indexOf(']')));
            Object list = null;
            if("".equals(name)) {
                list = object;
            } else {
                list = getProperty(object, name);
            }

            if (list instanceof List) {
                value = ((List) list).get(i);
            } else if (list instanceof Object[]) {
                value = ((Object[]) list)[i];
            } else if (list instanceof char[]) {
                value = Character.valueOf(((char[]) list)[i]);
            } else if (list instanceof boolean[]) {
                value = new Boolean(((boolean[]) list)[i]);
            } else if (list instanceof byte[]) {
                value = Byte.valueOf(((byte[]) list)[i]);
            } else if (list instanceof double[]) {
                value = new Double(((double[]) list)[i]);
            } else if (list instanceof float[]) {
                value = new Float(((float[]) list)[i]);
            } else if (list instanceof int[]) {
                value = Integer.valueOf(((int[]) list)[i]);
            } else if (list instanceof long[]) {
                value = Long.valueOf(((long[]) list)[i]);
            } else if (list instanceof short[]) {
                value = Short.valueOf(((short[]) list)[i]);
            } else {
                throw new RuntimeException("The '" + name + "' property of the " + object.getClass().getName() + " class is not a List or Array.");
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting ordinal list from JavaBean. Cause " + e, e);
        }

        return value;
    }

    public Object getObject(Object object, String name) {
        if (name.indexOf('.') > -1) {
            StringTokenizer parser = new StringTokenizer(name, ".");
            Object value = object;
            while (parser.hasMoreTokens()) {
                value = getProperty(value, parser.nextToken());

                if (value == null) {
                    break;
                }

            }
            return value;
        } else {
            return getProperty(object, name);
        }
    }

    protected Object getProperty(Object object, String name) {
        try {
            Object value = null;
            if (name.indexOf('[') > -1) {
                value = getIndexedProperty(object, name);
            } else {
                if (object instanceof Map) {
                    int index = name.indexOf('.');
                    if (index > -1) {
                        String mapId = name.substring(0, index);
                        value = getProperty(((Map) object).get(mapId), name.substring(index + 1));
                    } else {
                        value = ((Map) object).get(name);
                    }

                } else {
                    int index = name.indexOf('.');
                    if (index > -1) {
                        String newName = name.substring(0, index);
                        value = getProperty(getObject(object, newName), name.substring(index + 1));
                    } else {
                        ClassInfo classCache = ClassInfo.getInstance(object.getClass());
                        Invoker method = classCache.getGetInvoker(name);
                        /*if (method == null) {
                            throw new NoSuchMethodException("No GET method for property " + name + " on instance of " + object.getClass().getName());
                        }*/
                        try {
                            value = method.invoke(object, NO_ARGUMENTS);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        /*try {
                        } catch (Throwable t) {
                            throw ClassInfo.unwrapThrowable(t);
                        }*/
                    }

                }
            }
            return value;
        } catch (RuntimeException e) {
            throw e;
        }/* catch (Throwable t) {
            if (object == null) {
                throw new RuntimeException("Could not get property '" + name + "' from null reference.  Cause: " + t.toString(), t);
            } else {
                throw new RuntimeException("Could not get property '" + name + "' from " + object.getClass().getName() + ".  Cause: " + t.toString(), t);
            }
        }*/
    }
}

class DomProbe{
    public Object getObject(Object object, String name) {
        Object value = null;
        Element element = findNestedNodeByName(resolveElement(object), name, false);
        if (element != null) {
            value = getElementValue(element);
        }
        return value;
    }

    private Element resolveElement(Object object) {
        Element element = null;
        if (object instanceof Document) {
            element = (Element) ((Document) object).getLastChild();
        } else if (object instanceof Element) {
            element = (Element) object;
        } else {
            throw new RuntimeException("An unknown object type was passed to DomProbe.  Must be a Document.");
        }
        return element;
    }

    private Element findNestedNodeByName(Element element, String name, boolean create) {
        Element child = element;

        StringTokenizer parser = new StringTokenizer(name, ".", false);
        while (parser.hasMoreTokens()) {
            String childName = parser.nextToken();
            if (childName.indexOf('[') > -1) {
                String propName = childName.substring(0, childName.indexOf('['));
                int i = Integer.parseInt(childName.substring(childName.indexOf('[') + 1, childName.indexOf(']')));
                child = findNodeByName(child, propName, i, create);
            } else {
                child = findNodeByName(child, childName, 0, create);
            }
            if (child == null) {
                break;
            }
        }

        return child;
    }

    private Element findNodeByName(Element element, String name, int index, boolean create) {
        Element prop = null;

        // Find named property element
        NodeList propNodes = element.getElementsByTagName(name);
        if (propNodes.getLength() > index) {
            prop = (Element) propNodes.item(index);
        } else {
            if (create) {
                for (int i = 0; i < index + 1; i++) {
                    prop = element.getOwnerDocument().createElement(name);
                    element.appendChild(prop);
                }
            }
        }
        return prop;
    }

    private Object getElementValue(Element element) {
        StringBuffer value = null;

        Element prop = element;

        if (prop != null) {
            // Find text child elements
            NodeList texts = prop.getChildNodes();
            if (texts.getLength() > 0) {
                value = new StringBuffer();
                for (int i = 0; i < texts.getLength(); i++) {
                    Node text = texts.item(i);
                    if (text instanceof CharacterData) {
                        value.append(((CharacterData) text).getData());
                    }
                }
            }
        }

        //convert to proper type
        //value = convert(value.toString());

        if (value == null) {
            return null;
        } else {
            return String.valueOf(value);
        }
    }
}

class ClassInfo {

    private static boolean cacheEnabled = true;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Set SIMPLE_TYPE_SET = new HashSet();
    private static final Map<Class, ClassInfo> CLASS_INFO_MAP = new ConcurrentHashMap<Class, ClassInfo>();

    private String className;
    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;
    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;
    private HashMap setMethods = new HashMap();
    private HashMap getMethods = new HashMap();
    private HashMap setTypes = new HashMap();
    private HashMap getTypes = new HashMap();
    private Constructor defaultConstructor;

    static {
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);

        SIMPLE_TYPE_SET.add(Collection.class);
        SIMPLE_TYPE_SET.add(Set.class);
        SIMPLE_TYPE_SET.add(Map.class);
        SIMPLE_TYPE_SET.add(List.class);
        SIMPLE_TYPE_SET.add(HashMap.class);
        SIMPLE_TYPE_SET.add(TreeMap.class);
        SIMPLE_TYPE_SET.add(ArrayList.class);
        SIMPLE_TYPE_SET.add(LinkedList.class);
        SIMPLE_TYPE_SET.add(HashSet.class);
        SIMPLE_TYPE_SET.add(TreeSet.class);
        SIMPLE_TYPE_SET.add(Vector.class);
        SIMPLE_TYPE_SET.add(Hashtable.class);
        SIMPLE_TYPE_SET.add(Enumeration.class);
    }

    private ClassInfo(Class clazz) {
        className = clazz.getName();
        addDefaultConstructor(clazz);
        addGetMethods(clazz);
        addSetMethods(clazz);
        addFields(clazz);
        readablePropertyNames = (String[]) getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = (String[]) setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
    }

    private void addDefaultConstructor(Class clazz) {
        Constructor[] consts = clazz.getDeclaredConstructors();
        for (int i = 0; i < consts.length; i++) {
            Constructor constructor = consts[i];
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception e) {
                        // Ignored. This is only a final precaution, nothing we can do.
                    }
                }
                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }
    }

    private void addGetMethods(Class cls) {
        Method[] methods = getClassMethods(cls);
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                if (method.getParameterTypes().length == 0) {
                    name = dropCase(name);
                    addGetMethod(name, method);
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (method.getParameterTypes().length == 0) {
                    name = dropCase(name);
                    addGetMethod(name, method);
                }
            }
        }
    }

    private void addGetMethod(String name, Method method) {
        getMethods.put(name, new MethodInvoker(method));
        getTypes.put(name, method.getReturnType());
    }

    private void addSetMethods(Class cls) {
        Map conflictingSetters = new HashMap();
        Method[] methods = getClassMethods(cls);
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (method.getParameterTypes().length == 1) {
                    name = dropCase(name);
                    ///------------
                    addSetterConflict(conflictingSetters, name, method);
                    // addSetMethod(name, method);
                    ///------------
                }
            }
        }
        resolveSetterConflicts(conflictingSetters);
    }

    private void addSetterConflict(Map conflictingSetters, String name, Method method) {
        List list = (List) conflictingSetters.get(name);
        if (list == null) {
            list = new ArrayList();
            conflictingSetters.put(name, list);
        }
        list.add(method);
    }

    private void resolveSetterConflicts(Map conflictingSetters) {
//      for (Iterator propNames = conflictingSetters.keySet().iterator(); propNames.hasNext();) {
        Set<Entry<String, List>> entryseSet = conflictingSetters.entrySet();
        for (Entry<String, List> entry:entryseSet) {
//        String propName = (String) propNames.next();
//        List setters = (List) conflictingSetters.get(propName);
            String propName = entry.getKey();
            List setters =  entry.getValue();
            Method firstMethod = (Method) setters.get(0);
            if (setters.size() == 1) {
                addSetMethod(propName, firstMethod);
            } else {
                Class expectedType = (Class) getTypes.get(propName);
                if (expectedType == null) {
                    throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                            + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                            "specification and can cause unpredicatble results.");
                } else {
                    Iterator methods = setters.iterator();
                    Method setter = null;
                    while (methods.hasNext()) {
                        Method method = (Method) methods.next();
                        if (method.getParameterTypes().length == 1
                                && expectedType.equals(method.getParameterTypes()[0])) {
                            setter = method;
                            break;
                        }
                    }
                    if (setter == null) {
                        throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                                "specification and can cause unpredicatble results.");
                    }
                    addSetMethod(propName, setter);
                }
            }
        }
    }

    private void addSetMethod(String name, Method method) {
        setMethods.put(name, new MethodInvoker(method));
        setTypes.put(name, method.getParameterTypes()[0]);
    }

    private void addFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Ignored. This is only a final precaution, nothing we can do.
                }
            }
            if (field.isAccessible()) {
                if (!setMethods.containsKey(field.getName())) {
                    addSetField(field);
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }

    private void addSetField(Field field) {
        setMethods.put(field.getName(), new SetFieldInvoker(field));
        setTypes.put(field.getName(), field.getType());
    }

    private void addGetField(Field field) {
        getMethods.put(field.getName(), new GetFieldInvoker(field));
        getTypes.put(field.getName(), field.getType());
    }

    /**
     * This method returns an array containing all methods
     * declared in this class and any superclass.
     * We use this method, instead of the simpler Class.getMethods(),
     * because we want to look for private methods as well.
     *
     * @param cls The class
     * @return An array containing all methods in this class
     */
    private Method[] getClassMethods(Class cls) {
        HashMap uniqueMethods = new HashMap();
        Class currentClass = cls;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            // we also need to look for interface methods -
            // because the class may be abstract
            Class[] interfaces = currentClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                addUniqueMethods(uniqueMethods, interfaces[i].getMethods());
            }

            currentClass = currentClass.getSuperclass();
        }

        Collection methods = uniqueMethods.values();

        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(HashMap uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                String signature = getSignature(currentMethod);
                // check to see if the method is already known
                // if it is known, then an extended class must have
                // overridden a method
                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            // Ignored. This is only a final precaution, nothing we can do.
                        }
                    }

                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    private String getSignature(Method method) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.getName());
        Class[] parameters = method.getParameterTypes();

        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }

        return sb.toString();
    }

    private static String dropCase(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.US) + name.substring(1);
        }

        return name;
    }

    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets the name of the class the instance provides information for
     *
     * @return The class name
     */
    public String getClassName() {
        return className;
    }

    public Object instantiateClass() {
        if (defaultConstructor != null) {
            try {
                return defaultConstructor.newInstance(null);
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating class. Cause: " + e, e);
            }
        } else {
            throw new RuntimeException("Error instantiating class.  There is no default constructor for class " + className);
        }
    }

    /**
     * Gets the setter for a property as a Method object
     *
     * @param propertyName - the property
     * @return The Method
     */
    //  public Method getSetter(String propertyName) {
//      Invoker method = (Invoker) setMethods.get(propertyName);
//      if (method == null) {
//        throw new RuntimeException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
//      }
//      if (!(method instanceof MethodInvoker)) {
//        throw new RuntimeException("Can't get setter method because '" + propertyName + "' is a field in class '" + className + "'");
//      }
//      return ((MethodInvoker) method).getMethod();
    //  }

    /**
     * Gets the getter for a property as a Method object
     *
     * @param propertyName - the property
     * @return The Method
     */
    //  public Method getGetter(String propertyName) {
//      Invoker method = (Invoker) getMethods.get(propertyName);
//      if (method == null) {
//        throw new RuntimeException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
//      }
//      if (!(method instanceof MethodInvoker)) {
//        throw new RuntimeException("Can't get getter method because '" + propertyName + "' is a field in class '" + className + "'");
//      }
//      return ((MethodInvoker) method).getMethod();
    //  }

    public Invoker getSetInvoker(String propertyName) {
        Invoker method = (Invoker) setMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
        }
        return method;
    }

    public Invoker getGetInvoker(String propertyName) {
        Invoker method = (Invoker) getMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
        }
        return method;
    }

    /**
     * Gets the type for a property setter
     *
     * @param propertyName - the name of the property
     * @return The Class of the propery setter
     */
    public Class getSetterType(String propertyName) {
        Class clazz = (Class) setTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
        }
        return clazz;
    }

    /**
     * Gets the type for a property getter
     *
     * @param propertyName - the name of the property
     * @return The Class of the propery getter
     */
    public Class getGetterType(String propertyName) {
        Class clazz = (Class) getTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
        }
        return clazz;
    }

    /**
     * Gets an array of the readable properties for an object
     *
     * @return The array
     */
    public String[] getReadablePropertyNames() {
        return readablePropertyNames;
    }

    /**
     * Gets an array of the writeable properties for an object
     *
     * @return The array
     */
    public String[] getWriteablePropertyNames() {
        return writeablePropertyNames;
    }

    /**
     * Check to see if a class has a writeable property by name
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a writeable property by the name
     */
    public boolean hasWritableProperty(String propertyName) {
        return setMethods.keySet().contains(propertyName);
    }

    /**
     * Check to see if a class has a readable property by name
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a readable property by the name
     */
    public boolean hasReadableProperty(String propertyName) {
        return getMethods.keySet().contains(propertyName);
    }

    /**
     * Tells us if the class passed in is a knwon common type
     *
     * @param clazz The class to check
     * @return True if the class is known
     */
    public static boolean isKnownType(Class clazz) {
        if (SIMPLE_TYPE_SET.contains(clazz)) {
            return true;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Map.class.isAssignableFrom(clazz)) {
            return true;
        } else if (List.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Set.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Iterator.class.isAssignableFrom(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets an instance of ClassInfo for the specified class.
     *
     * @param clazz The class for which to lookup the method cache.
     * @return The method cache for the class
     */
    public static ClassInfo getInstance(Class clazz) {
        if (cacheEnabled) {
            ClassInfo cached = (ClassInfo) CLASS_INFO_MAP.get(clazz);
            if (cached == null) {
                cached = new ClassInfo(clazz);
                CLASS_INFO_MAP.put(clazz, cached);
            }
            return cached;
        } else {
            return new ClassInfo(clazz);
        }
    }

    public static void setCacheEnabled(boolean cacheEnabled) {
        ClassInfo.cacheEnabled = cacheEnabled;
    }

    /**
     * Examines a Throwable object and gets it's root cause
     *
     * @param t - the exception to examine
     * @return The root cause
     */
    /*public static Throwable unwrapThrowable(Throwable t) {
      Throwable t2 = t;
      while (true) {
        if (t2 instanceof InvocationTargetException) {
          t2 = ((InvocationTargetException) t).getTargetException();
        } else if (t instanceof UndeclaredThrowableException) {
          t2 = ((UndeclaredThrowableException) t).getUndeclaredThrowable();
        } else {
          return t2;
        }
      }
    }*/
}
