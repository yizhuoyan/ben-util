/*
 * @#JsonString.java V1.0.0 2012-12-26
 */
package com.ikeepglove.util.json;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * <请添加类功能说明和使用说明>
 * 
 * @author ben
 * @version V1.0.0 2012-12-26
 * @see
 * @since JDK5.0
 */
@SuppressWarnings("unchecked")
public class JsonString {
	public static interface CallBack<T> {
		public Object parse(T t);
	}

	/**
	 * 转换array对象为JsonString对象
	 * 
	 * @autor ben
	 * @param array
	 *            目前数组对象
	 * @param missItems
	 *            过滤，如果数组元素与过滤元素equals相等，则不进行转换，Class为数组元素类型过滤
	 * @return
	 */
	public static final JsonString parseArray(Object array, Object... missItems) {
		int length = Array.getLength(array);
		if (length == 0) {
			return new JsonString("[]");
		}
		LinkedStringBuilder sb = new LinkedStringBuilder();
		if (missItems.length == 0) {
			for (int i = 0; i < length; sb.append(
					parseObject(Array.get(array, i++))).append(","))
				;
		} else {
			for (int i = 0; i < length; i++) {
				if (!isMissInCollection(Array.get(array, i), missItems)) {
					sb.append(",").append(parseObject(Array.get(array, i)));
				}
			}
		}
		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	public static final JsonString parseArray(Object array, CallBack callback) {
		int length = Array.getLength(array);
		if (length == 0) {
			return new JsonString("[]");
		}
		LinkedStringBuilder sb = new LinkedStringBuilder();
		for (int i = 0; i < length; sb.append(
				parseObject(callback.parse(Array.get(array, i++)))).append(","))
			;
		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	/**
	 * 转换array对象为JsonString对象,用于明确定义的数组对象
	 * 
	 * @autor ben
	 * @param array
	 *            目前数组对象
	 * @param missItems
	 *            过滤，如果数组元素与过滤元素equals相等，则不进行转换，Class为数组元素类型过滤
	 */
	public static final <T> JsonString parseArray(T[] array,
			Object... missItems) {
		if (array == null)
			return new JsonString("null");
		if (array.length == 0)
			return new JsonString("[]");
		LinkedStringBuilder sb = new LinkedStringBuilder();
		if (missItems.length == 0) {
			for (Object object : array) {
				sb.append(',').append(parseObject(object));
			}
		} else {
			for (Object object : array) {
				if (!isMissInCollection(object, missItems)) {
					sb.append(',').append(parseObject(object));
				}
			}
		}
		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	public static final <T> JsonString parseArray(T[] array,
			CallBack<T> callback) {
		if (array == null)
			return new JsonString("null");
		if (array.length == 0)
			return new JsonString("[]");
		LinkedStringBuilder sb = new LinkedStringBuilder();
		for (T object : array) {
			sb.append(',').append(parseObject(callback.parse(object)));
		}

		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	/**
	 * 转换集合内对象为JsonString对象
	 * 
	 * @autor ben
	 * @param collection
	 *            目标对象
	 * @param missItems
	 *            过滤，如果集合元素与过滤元素equals相等，则不进行转换，Class为元素类型过滤
	 * @return
	 */
	public static final <T> JsonString parseCollection(
			Collection<T> collection, Object... missItems) {
		LinkedStringBuilder sb = new LinkedStringBuilder();
		Iterator it = (collection).iterator();
		if (missItems.length == 0) {
			while (it.hasNext()) {
				sb.append(',').append(parseObject(it.next()));
			}
		} else {
			Object item = null;
			while (it.hasNext()) {
				item = it.next();
				if (!isMissInCollection(item, missItems)) {
					sb.append(',').append(parseObject(item));
				}
			}
		}
		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	public static final <T> JsonString parseCollection(
			Collection<T> collection, CallBack<T> callBack) {
		LinkedStringBuilder sb = new LinkedStringBuilder();
		Iterator<T> it = (collection).iterator();
		while (it.hasNext()) {
			sb.append(',').append(parseObject(callBack.parse(it.next())));
		}
		if (sb.length() == 0) {
			sb.append("[]");
		} else {
			sb.append(']').changeFirst('[');
		}
		return new JsonString(sb.toString());
	}

	/**
	 * 将Jsonable对象转换为JSON字符串。<br/>
	 * 可进行属性过滤，即不转换指定属性，<br/>
	 * 如 JsonSwapper.object2JsonString(targetObject,"id",Modifier.STATIC,Map.
	 * class);<br/>
	 * 即过滤指定对象targetObject属性名称为id的，或者为static的，或者属性类型为Map的。<br/>
	 * 对于时间类型，默认转换为指定时间到1900-1-1 00:00:00间的毫秒数。<br/>
	 * 
	 * @autor ben
	 * @param obj
	 *            目标对象
	 * @param missFields
	 *            属性过滤，字符串为属性名称过滤，int为对象Modifiers过滤，Class为属性类型过滤
	 * @return
	 */
	public static final JsonString parseJsonable(Jsonable obj,
			Object... missFields) {
		Class type = obj.getClass();
		try {
			Method jsonMethod = type.getMethod("toJsonString");
			
			return (JsonString) jsonMethod.invoke(obj);
		} catch (Exception notmethod) {
			return parseObject0(obj, missFields);
		}
	}
	
	private static final JsonString parseObject0(Object obj, Object... missFields) {
		LinkedStringBuilder sb = new LinkedStringBuilder();
		try {
			Field[] fs = null;
			sb.append('{');
			LinkedList<Class> classDeque = getBeanAllSuperClasses(obj.getClass());
			Class<?> c = null;
			while ((c = classDeque.removeLast()) != null) {
				fs = c.getDeclaredFields();
				if (fs.length == 0)
					continue;
				
				AccessibleObject.setAccessible(fs, true);
				for (int i = 0; i < fs.length; i++) {
					if (missFields.length != 0 && isMissField(fs[i], missFields)) {
						continue;
					}
					sb.append('"').append(fs[i].getName()).append("\":");
					
					sb.append(parseObject(fs[i].get(obj))).append(',');
				}
				
			}
			if (sb.length() != 1) {
				sb.changeLast('}');
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return new JsonString(sb.toString());

	}
	/**
	 * 转换map对象为JsonString对象
	 * 
	 * @autor ben
	 * @param map
	 * @param missEntrys
	 *            entry过滤，如果Map元素Key与过滤元素equals相等，则不进行转换，Class为Map Value类型过滤
	 * @return
	 */
	public static final JsonString parseMap(Map map, Object... missEntrys) {
		if (map.size() == 0)
			return new JsonString("{}");
		LinkedStringBuilder sb = new LinkedStringBuilder();
		Map.Entry entry = null;
		if (missEntrys.length == 0) {
			for (Iterator<Map.Entry> it = map.entrySet().iterator(); it
					.hasNext();) {
				entry = it.next();
				sb.append(',').append('"').append(entry.getKey()).append("\":")
						.append(parseObject(entry.getValue()));
			}
		} else {
			for (Iterator<Map.Entry> it = map.entrySet().iterator(); it
					.hasNext();) {
				entry = it.next();
				if (!isMissInMap(entry, missEntrys)) {
					sb.append(',').append('"').append(entry.getKey())
							.append("\":")
							.append(parseObject(entry.getValue())).append(',');
				}
			}
		}
		if (sb.length() == 0) {
			return new JsonString("{}");
		} else {
			sb.append("}").changeFirst("{");
		}
		return new JsonString(sb.toString());

	}

	/**
	 * 统一方法，用于不确定对象类型转换，当不支持时抛出异常
	 * 
	 * @autor ben
	 * @param obj
	 *            目标对象
	 * @return
	 */
	public static final JsonString parseObject(Object obj) {

		if (obj == null)
			return new JsonString("null");
		if (obj instanceof CharSequence)
			return parseString((CharSequence) obj);
		if (obj instanceof JsonString)
			return (JsonString) obj;
		if(obj instanceof Character)
			return new JsonString(Character.toString((Character)obj));
		if (obj instanceof Number || obj instanceof Boolean)
			return new JsonString(obj.toString());
		if (obj instanceof Date)
			return new JsonString(Long.toString(((Date) obj).getTime()));
		if (obj instanceof Calendar)
			return new JsonString(Long.toString(((Calendar) obj)
					.getTimeInMillis()));
		if (obj instanceof Collection)
			return parseCollection((Collection) obj);
		if (obj instanceof Map)
			return parseMap((Map) obj);
		if (obj instanceof Jsonable)
			return parseJsonable((Jsonable) obj);
		if (obj.getClass().isArray())
			return parseArray(obj);
		return parseObject0(obj);
		
	}

	/**
	 * 转换java字符串对象为JsonString对象
	 * 
	 * @autor ben
	 */
	public static final JsonString parseString(CharSequence string) {
		int len = string.length();
		LinkedStringBuilder sb = new LinkedStringBuilder();
		char c = '\0';
		sb.append('"');
		for (int i = 0; i < len; i++) {
			c = string.charAt(i);
			sb.append(escapeChar(c));
		}
		return new JsonString(sb.append('"').toString());
	}

	/**
	 * 
	 * 转移字符处理
	 * 
	 * @autor ben
	 * @param c
	 * @return
	 */
	private static String escapeChar(char c) {
		switch (c) {
		case '"':
			return "\\\"";
			// case '/': return "\\/";
		case '\\':
			return "\\\\";
		case '\b':
			return "\\b";
		case '\t':
			return "\\t";
		case '\n':
			return "\\n";
		case '\f':
			return "\\f";
		case '\r':
			return "\\r";
		default:
			if (c < ' ') {
				String t = "000" + Integer.toHexString(c);
				return "\\u" + (t.substring(t.length() - 4));
			}
			return c + "";
		}
	}

	/**
	 * 获取类型所有父类,不包括Object类
	 * 
	 * @param cls
	 *            目标类型
	 * @return List<Class>
	 */
	static private LinkedList<Class> getBeanAllSuperClasses(Class cls) {
		LinkedList<Class> stack = new LinkedList<Class>();
		while (cls != null) {
			stack.add(cls);
			cls = cls.getSuperclass();
		}
		stack.removeFirst();
		return stack;
	}

	/**
	 * 
	 * 过滤空白字符，如果全是空白则抛出异常
	 * 
	 * @autor ben
	 * @param string
	 * @param pos
	 */
	private static final void ignoreBlank(String string, int pos[]) {
		int len = string.length();

		while ((pos[0] < len) && (string.charAt(pos[0]) <= ' ')) {
			pos[0]++;
		}
		if (pos[0] >= string.length()) {
			throw new IllegalArgumentException("json string format error:"
					+ string);
		}
	}
	
	/**
	 * 判断当前处理的Field是否在过滤字段中。 目前支持三种过滤类型 <br/>
	 * 1 字段名称过滤 <br/>
	 * 2 字段类型过滤 <br/>
	 * 3 字段修饰语过滤
	 * 
	 * @param field
	 *            监测字段
	 * @param missFields
	 *            过滤字段数组
	 * @return 是否过滤排除
	 */
	private static boolean isMissField(Field field, Object... missFields) {
		for (Object missField : missFields) {
			if (missField instanceof String) {
				if (missField.equals(field.getName())) {
					return true;
				}
			} else if (missField instanceof Class) {
				if (missField.equals(field.getType())) {
					return true;
				}
			} else if (missField instanceof Integer) {
				if (((Integer) missField & field.getModifiers()) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 元素过滤
	 * 
	 * @autor ben
	 * @param item
	 * @param missItems
	 * @return
	 */
	private static boolean isMissInCollection(Object item, Object... missItems) {
		for (Object missItem : missItems) {
			if (item instanceof Class) {
				return missItem.getClass().isAssignableFrom(item.getClass());
			} else {
				return missItem.equals(item);
			}
		}
		return false;
	}

	/**
	 * 元素过滤
	 * 
	 * @autor ben
	 * @param entry
	 * @param missEntrys
	 * @return
	 */
	private static boolean isMissInMap(Map.Entry entry, Object... missEntrys) {
		for (Object missEntry : missEntrys) {
			if (missEntry instanceof Class) {
				return entry.getValue().getClass()
						.isAssignableFrom(missEntry.getClass());
			} else {
				return missEntry.equals(entry.getKey());
			}
		}
		return false;
	}

	/**
	 * 
	 * 核心方法，用于不确定json类型转换
	 * 
	 * @autor ben
	 * @param json
	 * @param pos
	 * @return
	 */
	private static final Object parse(String json, int[] pos) {
		char c = '\0';
		int len = json.length();
		Object result = null;
		if (pos[0] < len) {
			switch ((c = json.charAt(pos[0]++))) {
			case '{':
				result = toMap(json, pos);
				break;
			case '[':
				result = toList(json, pos);
				break;
			case '"':
			case '\'':
				result = toString(json, c, pos);
				break;
			case 'n':// null
				if (json.charAt(pos[0]++) == 'u'
						&& json.charAt(pos[0]++) == 'l'
						&& json.charAt(pos[0]++) == 'l') {
					result = null;
				} else {
					throw new IllegalArgumentException(
							"json string format error:" + json);
				}
				break;
			case 't':// true
				if (json.charAt(pos[0]++) == 'r'
						&& json.charAt(pos[0]++) == 'u'
						&& json.charAt(pos[0]++) == 'e') {
					result = true;
				} else {
					throw new IllegalArgumentException(
							"json string format error:" + json);
				}
				break;
			case 'f':// false
				if (json.charAt(pos[0]++) == 'a'
						&& json.charAt(pos[0]++) == 'l'
						&& json.charAt(pos[0]++) == 's'
						&& json.charAt(pos[0]++) == 'e') {
					result = false;
				} else {
					throw new IllegalArgumentException(
							"json string format error:" + json);
				}
				break;
			default:
				pos[0]--;
				result = toNumber(json, pos);

			}
		}

		return result;
	}

	/**
	 * 
	 * 转 换[x,x,x]结构为List对象的核心方法
	 * 
	 * @autor ben
	 * @param json
	 * @param pos
	 * @return
	 */
	private static final List toList(String json, int[] pos) {
		List<Object> list = new LinkedList<Object>();
		int len = json.length();
		Object value = null;
		char c = '\0';
		while ((pos[0] < len)) {
			ignoreBlank(json, pos);
			c = json.charAt(pos[0]);
			if (c == ']' && list.size() == 0) {// handle []
				pos[0]++;
				return list;
			}
			value = parse(json, pos);
			list.add(value);
			ignoreBlank(json, pos);
			c = json.charAt(pos[0]);
			if (c == ']') {
				pos[0]++;
				return list;
			} else if (c == ',') {
				pos[0]++;
			} else {
				throw new IllegalArgumentException("json string format error:"
						+ json);
			}
		}
		throw new IllegalArgumentException("json string format error:" + json);
	}

	/**
	 * 
	 * 转换{x:y}结构为Map对象的核心方法
	 * 
	 * @autor ben
	 * @param json
	 * @param pos
	 * @return
	 */
	private static final Map<String, Object> toMap(String json, int[] pos) {
		char c = '\0';
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int len = json.length();
		String key = null;
		Object value = null;
		while ((pos[0] < len)) {
			ignoreBlank(json, pos);
			c = json.charAt(pos[0]);
			if (c == '}' && map.size() == 0) {// handle {}
				pos[0]++;
				return map;
			} else if (c == '"' || c == '\'') {
				pos[0]++;
				key = toString(json, c, pos);
				ignoreBlank(json, pos);
				if (json.charAt(pos[0]) != ':') {
					throw new IllegalArgumentException(
							"json string format error:" + json);
				}
				pos[0]++;
			} else {
				key = toString(json, ':', pos);
				key = key.trim();
			}
			ignoreBlank(json, pos);
			value = parse(json, pos);
			ignoreBlank(json, pos);
			map.put(key, value);
			c = json.charAt(pos[0]);
			if (c == '}') {
				pos[0]++;
				return map;
			} else if (c == ',') {
				pos[0]++;
			} else {
				throw new IllegalArgumentException("json string format error:"
						+ json);
			}
		}
		throw new IllegalArgumentException("json string format error:" + json);

	}

	/**
	 * 
	 * 转换json字符串为double类型
	 * 
	 * @autor ben
	 * @param json
	 * @param pos
	 * @return
	 */
	private static double toNumber(String json, int[] pos) {
		int len = json.length();
		char c = '\0';
		double result = 0d;
		int begin = pos[0];
		while ((begin < len)) {
			c = json.charAt(begin);
			if ((c >= '0' && c <= '9') || c == '-' || c == '.' || c == '+'
					|| c == 'e' || c == 'E') {
				begin++;
			} else {
				try {
					result = Double.parseDouble(json.substring(pos[0], begin));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"json string format error:" + json);
				}
				pos[0] = begin;
				return result;
			}

		}
		throw new IllegalArgumentException("json string format error:" + json);

	}

	/**
	 * 
	 * 转换json字符串为java字符串对象
	 * 
	 * @autor ben
	 * @param json
	 * @param mark
	 * @param pos
	 * @return
	 */
	private static String toString(String json, char mark, int[] pos) {
		int len = json.length();
		char c = '\0';
		String result = null;
		int begin = pos[0];
		StringBuilder sb = new StringBuilder();
		while ((begin < len)) {
			c = json.charAt(begin++);
			sb.append(escapeChar(c));
			if (c == '\\') {
				begin++;
				sb.append(escapeChar(json.charAt(begin)));
			} else if (c == mark) {
				result = json.substring(pos[0], begin - 1);
				pos[0] = begin;
				return result;
			}
			sb.append(c);
		}

		throw new IllegalArgumentException("json string format error:" + json);

	}

	/**
	 * 获取对象getter方法
	 * 
	 * @autor ben
	 * @param cls
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	static Method getter(Class cls, String fieldName) throws Exception {
		Method getter = null;
		String methodName = null;

		if (fieldName.length() > 1) {
			methodName = Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1);
		} else {
			methodName = fieldName.toUpperCase();
		}
		try {
			getter = cls.getMethod("get" + methodName);
		} catch (NoSuchMethodException e) {
			getter = cls.getMethod("is" + methodName);
		}
		return getter;
	}

	/** 已转换好的json字符串 */
	private String source;

	/**
	 * 接收json字符串，不做任何处理
	 * 
	 * @autor ben
	 * @param string
	 */
	public JsonString(String jsonString) {
		if (jsonString == null
				|| (jsonString = jsonString.trim()).length() == 0) {
			throw new IllegalArgumentException("json string is null or empty");
		}
		this.source = jsonString;
	}

	/**
	 * 转换json字符串为List结构
	 * 
	 * @autor ben
	 * @param <T>
	 * @return
	 */
	public <T> List<T> toList() throws IllegalArgumentException {
		int pos[] = new int[] { 0 };
		ignoreBlank(source, pos);
		if (source.charAt(pos[0]++) != '[') {
			throw new IllegalArgumentException(
					"json string is not begin with '['!");
		}

		List<T> list = toList(source, pos);
		if (pos[0] < this.source.length()) {
			throw new IllegalArgumentException("json string format error:"
					+ source);
		}
		return list;
	}

	/**
	 * 转换json字符串为Map结构
	 * 
	 * @autor ben
	 * @return map 对象
	 * @throws IllegalArgumentException
	 */
	public Map<String, Object> toMap() throws IllegalArgumentException {
		int pos[] = new int[] { 0 };
		ignoreBlank(source, pos);
		if (source.charAt(pos[0]++) != '{') {
			throw new IllegalArgumentException(
					"json string is not begin with '{'!");
		}

		Map<String, Object> map = toMap(source, pos);
		if (pos[0] < this.source.length()) {
			throw new IllegalArgumentException("json string format error:"
					+ source);
		}
		return map;
	}

	/**
	 * 
	 * 直接返回当前的json字符串对象
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.source;
	}

}
