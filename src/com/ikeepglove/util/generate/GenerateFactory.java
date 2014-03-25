package com.ikeepglove.util.generate;
/**
 * GenerateFactory.java
 * 1.12,Jun 3, 2010
 * Copyright 2010 Ben. All rights reserved
 */


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * Generate Map,List,Array,Object with implements Generable 
 * at random.
 * The Fields in the Class want be generated must be supply setter method;  
 * 
 * @author  Ben
 * @version 1.12,Jun 3, 2010
 * @see  com.ben.core.BaseType
 * @see  com.ben.generate.Generable
 * @since   JDK 5.0
 *
 */
@SuppressWarnings("unchecked")
public class GenerateFactory {
	/**
	 * generate array
	 * @param cls the array component class Type 
	 * @param length  generate array length
	 * @return generate array 
	 */
	public static Object generateArray(Class<?> cls, int length) {
		try {
			length = length < 1 ?RandomUtil.Integer.next(10)+1 : length;
			Object array = Array.newInstance(cls, length);
			for (int i = 0; i < length; i++) {
				Array.set(array, i, generate(cls));
			}
			return array;

		} catch (Throwable e) {
			return null;
		}
	}
	/**
	 * generate Bean 
	 * @param <T> generate  bean class Type
	 * @param cls generate  bean class Type
	 * @return generate array;
	 */
	public static <T> T generate(Class<T> cls) {
		BaseType type = BaseType.toBaseType(cls);
		if (type != null) {
			return (T) type.generate();
		}
		
		if(!Generable.class.isAssignableFrom(cls)){
			throw new UnsupportedOperationException(cls.getName()+" not implements interface "+Generable.class.getName());
		}
		
		try {
			Object result = cls.newInstance();
			Map<String, Field> setters = ClassUtil.fieldsWithoutStatic(cls);
			Method setter = null;
			Field field=null;
			for (String key : setters.keySet()) {
				field=setters.get(key);
				setter = ClassUtil._setter(cls, field);
				if(setter!=null){
					Object o=generate(field);
					setter.invoke(result, o);
				}
				
			}
			return (T)result;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * generate object by Field
	 * @param field 
	 * @return field type object 
	 */
	private static Object generate(Field field) {
		try{
			Class<?> fieldClass=field.getType();
			if(fieldClass.isArray()){
				return generateArray(fieldClass.getComponentType(),0);
			}
			if(Date.class.isAssignableFrom(fieldClass)){
				return fieldClass.newInstance();
			}
			if(Set.class.isAssignableFrom(fieldClass)){
				fieldClass=(Class<?>)(((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
				return generateSet(fieldClass, 0);
			}
			if(List.class.isAssignableFrom(fieldClass)){
				fieldClass=(Class<?>)(((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
				return generateList(fieldClass, 0);
			}
			if(Map.class.isAssignableFrom(fieldClass)){
				Type[] types=((ParameterizedType)field.getGenericType()).getActualTypeArguments();
				return generateMap((Class<?>)(types[0]),(Class<?>)(types[1]),0);
			}
			else{
				return generate(fieldClass);
			}
				
		}catch(Throwable e){
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * generate List
	 * @param <T> parameter type
	 * @param cls List component type
	 * @param length generate List size 
	 * @return generate List
	 */
	public static <T> List<T> generateList(Class<T> cls, int length) {
		length = length < 1 ? RandomUtil.Integer.next(10) + 1 : length;
		List<T> list = new ArrayList<T>(length);
		for (int i = 0; i < length; i++) {
			list.add(generate(cls));
		}
		return list;
	}
	/**
	 * generate Set
	 * @param <T> parameter type
	 * @param cls Set component type
	 * @param length generate Set size 
	 * @return generate List
	 */
	public static <T> Set<T> generateSet(Class<T> cls, int length) {
		length = length < 1 ? RandomUtil.Integer.next(10) + 1 : length;
		Set<T> set=new HashSet<T>();
		for (int i = 0; i < length; i++) {
			set.add(generate(cls));
		}
		return set;
	}
	/**
	 * generate Map
	 * @param <K> key Type
	 * @param <V> value Type 
	 * @param keyType key Class Type
	 * @param valueType value Class Type
	 * @param length generate  Map size 
	 * @return generate Map
	 */
	public static <K, V> Map<K, V> generateMap(Class<K> keyType,
			Class<V> valueType, int length) {
		length = length < 1 ? RandomUtil.Integer.next(10) + 1 : length;
		Map<K,V> map=new HashMap<K, V>();
		for (int i = 0; i < length; i++) {
			map.put(generate(keyType),generate(valueType));
		}
		return map;
	}


}
