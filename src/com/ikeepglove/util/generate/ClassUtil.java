package com.ikeepglove.util.generate;
/**
 * ClassUtil.java
 * 1.01,Jun 4, 2010
 * Copyright 2010 Ben. All rights reserved
 */


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * 
 *  
 * 
 * @author  Ben
 * @version 1.01,Jun 4, 2010
 * @since   JDK 5.0
 *
 */
@SuppressWarnings("unchecked")
public class ClassUtil {
	/**
	 * get cls all super Class 
	 * @param cls
	 *            class type of parameter T
	 * @return a List of class type T and its super class types without Object
	 *         class,if cls is null, return list with size 0
	 */
	static public List<Class> _superClasses(Class cls) {
		LinkedList<Class> classes = new LinkedList<Class>();
		while (cls != null) {
			classes.add(cls);
			cls = cls.getSuperclass();
		}
		classes.removeFirst();//delete object class
		return classes;
	}
	/**
	 * get Field from cls(include subClass) by fieldName 
	 * @param cls class Type
	 * @param fieldName field name
	 * @return Field object
	 */
	static public Field _field(Class cls, String fieldName) {
		List<Class> superClasses = _superClasses(cls);
		Field field = null;
		for (Class next : superClasses) {
			try {
				field = next.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				continue;//try parent
			}
		}
		return field;
	}
	/**
	 * get Field Map without static modifier
	 * @param cls Class Type
	 * @return map with key of Field name and value of Field
	 */
	static public Map<String, Field> fieldsWithoutStatic(Class cls) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		List<Class> superClasses = _superClasses(cls);
		Field[] fs=null;
		for (Class next : superClasses) {
			fs = next.getDeclaredFields();
			AccessibleObject.setAccessible(fs, true);
			for (Field f : fs)
				if(f.getModifiers()!=Modifier.STATIC)
					map.put(f.getName(), f);
		}
		return map;
	}
	/**
	 * get Field Map 
	 * @param cls Class Type
	 * @return map with key of Field name and value of Field
	 */
	static public Map<String, Field> fields(Class cls) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		List<Class> superClasses = _superClasses(cls);
		Field[] fs=null;
		for (Class next : superClasses) {
			fs = next.getDeclaredFields();
			AccessibleObject.setAccessible(fs, true);
			for (Field f : fs)
					map.put(f.getName(), f);
		}
		return map;
	}
	static public Method _setter(Class cls, String fieldName) {
		try {
			Field field = _field(cls, fieldName);
			Method setter = cls.getMethod("set"
					+ upperFirstChar(fieldName), field.getType());
			setter.setAccessible(true);
			return setter;
		}catch(NoSuchMethodException e){
			return null;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	static public Method _setter(Class cls, Field field) {
		try {
			Method setter = cls.getMethod("set"
					+ upperFirstChar(field.getName()), field
					.getType());
			setter.setAccessible(true);
			return setter;
		}catch(NoSuchMethodException e){
			return null;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	static public Method _getter(Class cls, Field field) {
		return _getter(cls, field.getName());
	}

	static public Method _getter(Class cls, String fieldName) {
		Method getter = null;
		try {
			getter = cls
					.getMethod("get" + upperFirstChar(fieldName));

		} catch (NoSuchMethodException e) {
			try {
				getter = cls.getMethod("is"
						+ upperFirstChar(fieldName));
				if(!Boolean.class.isAssignableFrom(getter.getReturnType())){
					return null; 
				}
				getter.setAccessible(true);
				return getter;
			}catch(NoSuchMethodException e1){
				return null;
			} catch (Throwable e2) {
				throw new RuntimeException(e);
			}
			
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return getter;
	}
	public static String upperFirstChar(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
}
