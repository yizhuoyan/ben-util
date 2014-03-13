/**
 * BaseType.java
 * 1.12,Jun 4, 2010
 * Copyright 2010 Ben. All rights reserved
 */
package com.ikeepglove.util.generate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * Be used by GenerateFactory
 *  
 * 
 * @author  Ben
 * @version 1.12,Jun 4, 2010
 * @see  com.ben.util.RandomUtil
 * @since   JDK 5.0
 *
 */
@SuppressWarnings("unchecked")
public  enum BaseType{
	_boolean(Boolean.TYPE){	public Object generate() {return RandomUtil.Boolean.next();}},
	_byte(Byte.TYPE){	public Object generate() {return RandomUtil.Byte.next();}},
	_short(Short.TYPE){	public Object generate() {return RandomUtil.Short.next();}},
	_char(Character.TYPE){	public Object generate() {return RandomUtil.Character.next();}},
	_int(Integer.TYPE){	public Object generate() {return RandomUtil.Integer.next();}},
	_long(Long.TYPE){	public Object generate() {return RandomUtil.Integer.next().longValue();}},
	_float(Float.TYPE){	public Object generate() {return RandomUtil.Float.next();}},
	_double(Double.TYPE){	public Object generate() {return RandomUtil.Double.next();}},
	
	_Byte(Byte.class){	public Object generate() {return _byte.generate();}},
	_Boolean(Boolean.class){	public Object generate() {return _boolean.generate();}},
	_Short(Short.class){	public Object generate() {return _short.generate();}},
	_Character(Character.class){	public Object generate() {return _char.generate();}},
	_Integer(Integer.class){	public Object generate() {return _int.generate();}},
	_Long (Long.class){	public Object generate() {return _long.generate();}},
	_Float(Float.class){	public Object generate() {return _float.generate();}},
	_Double (Double.class){	public Object generate() {return _double.generate();}},
	
	
	_String (String.class){	public Object generate() {return RandomUtil.String.next();}},
	_Date(Date.class){	public Object generate() {return new Date();}},
	_BigInteger(BigInteger.class){	public Object generate() {return BigInteger.valueOf((Integer)_int.generate());}},
	_BigDecimal(BigDecimal.class){	public Object generate() {return BigDecimal.valueOf((Float)_float.generate());}},
	_Object(Object.class){	public Object generate() {return BaseType.values()[RandomUtil.Integer.next(20)].generate();}};
	Class<?> type;
	BaseType(Class<?> type){
		this.type=type;
	}
	public Class<?> getType(){
		return this.type;
	}
	public abstract <T>T generate();
	public static BaseType toBaseType(Class<?> c) {
		try {
			return BaseType.valueOf("_"+c.getSimpleName());
		} catch (Throwable e) {
			return null;
		}
	}

}
