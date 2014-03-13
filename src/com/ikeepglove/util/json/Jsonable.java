/*
 *@#Jsonable.java V1.0.0 2012-10-29  
 * 
 */
package com.ikeepglove.util.json;

/**
 * 对象可转换为json字符串的标记接口，
 * 当使用JsonString的parseObject方法时，对象必须实现此接口。
 * 若对象实现此接口，也可同时实现方法toJSONString，parseObject在转换对象为json字符串时会优先执行此方法。
 * 否则采用反射获取对象所有字段构建json字符串
 *@author ben
 *@version V1.0.0 2012-10-29
 *@see  JsonString
 *@since JDK5.0
 */
public interface Jsonable {
	
}
