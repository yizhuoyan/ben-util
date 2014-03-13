package com.ikeepglove.util.generate;
/**
 * RandomUtil.java
 * 1.01,Jun 4, 2010
 * Copyright 2010 Ben. All rights reserved
 */



import java.math.BigDecimal;
import java.util.Random;

/**
 * 
 *  
 * 
 * @author  Ben
 * @version 1.01,Jun 4, 2010
 * @since   JDK 5.0
 *
 */
public interface RandomUtil {
	public static final Random rootRandom=new Random();
	//boolean
	public final static class Boolean {
		public static boolean next(){
			return rootRandom.nextBoolean();
		}
	
		static	public boolean next(float rate){
			if(rate>0&&rate<1f){
				return Math.random()<rate;	
			}
			return rate>0f;
		}

		static	public boolean next(double rate){
			return next((float)rate);
		}
	}
	//byte
	public final  static class Byte{
		static	private final  byte[] bytes=new byte[1];
		static public java.lang.Byte next() {
				RandomUtil.rootRandom.nextBytes(bytes);
			return bytes[0];
		}
		static	public java.lang.Byte next(java.lang.Byte t) {
			return next((byte)0, t);
		}
		static public java.lang.Byte next(java.lang.Byte a, java.lang.Byte b) {
			return Integer.next(a.intValue(),b.intValue()).byteValue();
		}
	};
	//short
	public final static class Short{
		static public java.lang.Short next() {
			return next(java.lang.Short.MAX_VALUE,java.lang.Short.MIN_VALUE);
		}
		static public	java.lang.Short next(java.lang.Short a, java.lang.Short b) {
			return Integer.next(a.intValue(),b.intValue()).shortValue();
		}

		static public	 java.lang.Short next(java.lang.Short t) {
			return next(java.lang.Short.MAX_VALUE,(short)0);
		}
	};
	
	//char
	public final static class Character{
		final	static	public java.lang.Character next() {
			return RandomUtil.Boolean.next(50.0f/100)?nextChinese():
				RandomUtil.Boolean.next(30.0f/50)?nextLetter():
				RandomUtil.Boolean.next(15.0f/20)?nextNumber():nextSymbol();
		}
		final	static	public java.lang.Character nextChinese() {
			return next((int)'\u4e00',((int)'\u9fa5')+1);
		}
		final	static	public java.lang.Character nextLetter() {
			return RandomUtil.Boolean.next()?next(65,91):next(97,123);	
		}
		final	static	public java.lang.Character nextNumber() {
			return next(48,58);
		}
		final	static	public java.lang.Character nextSymbol() {
			//(33-47=15 58-64=7 91-96=6 123-126=4 164 167 168 183 215 247)=38
			if(Boolean.next(15f/38f)){
				return next(33,48); 
			}else if(Boolean.next(7f/23f)){
				return next(58,65);
			}else if(Boolean.next(6f/16f)){
				return next(91,97);
			}else if(Boolean.next(4f/10f)){
				return next(123,127);
			}else{
				if(Boolean.next(1f/6f))	return (char)164;	
				if(Boolean.next(1f/6f))	return (char)167;
				if(Boolean.next(1f/6f))	return (char)168;	
				if(Boolean.next(1f/6f))	return (char)183;	
				if(Boolean.next(1f/6f))	return (char)215;	
				return (char)247;
			}
		}
		final static	private java.lang.Character next(int a,int b) {
			return (char)RandomUtil.Integer.next(a,b).intValue();
		}
	}
	//int
	public final static class Integer{
		static	public java.lang.Integer next() {
			return RandomUtil.rootRandom.nextInt();
		}
		static	public java.lang.Integer next(java.lang.Integer t) {
			if(t==0)return 0;
			return t>0?RandomUtil.rootRandom.nextInt(t):-RandomUtil.rootRandom.nextInt(-t);
		}
		static	public java.lang.Integer next(java.lang.Integer a,java.lang.Integer b) {
			if(a==b)return a;
			int min=a,max=b;
			if(a>b){min=b;max=a;}
			long scope=(long)max-(long)min;
			if(scope>java.lang.Integer.MAX_VALUE){
				double rate=(double)(-min)/(double)scope;
				return RandomUtil.Boolean.next(rate)?next(min):next(max);
			}
			return next((int)scope)+min;	
		}
	};
	//float
	public static class Float{
		static public	java.lang.Float next() {
			return rootRandom.nextFloat();
		}

		static public	java.lang.Float next(java.lang.Float a, java.lang.Float b) {
			if(a==b)return a;
			float min=Math.min(a, b);
			float max=Math.max(a, b);
			double scope=(double)max-(double)min;
			if(scope>java.lang.Float.MAX_VALUE){
				return RandomUtil.Boolean.next((-min)/scope)?next(min):next(max);
			}
			return next((float)scope)+min;	
			
		}
		static	public	java.lang.Float next(java.lang.Float t) {
			
			return ((float)Math.random())*t;
		}
		
		
	};
	//double
	public static class Double{
		
		static	public java.lang.Double next() {
			return rootRandom.nextDouble();
		}
		static	public	java.lang.Double next(java.lang.Double a, java.lang.Double b) {
			if(a==b)return a;
			BigDecimal min=BigDecimal.valueOf(Math.min(a, b));
			BigDecimal max=BigDecimal.valueOf(Math.max(a, b));
			BigDecimal scope=max.subtract(min);
			if(scope.compareTo(BigDecimal.valueOf(java.lang.Double.MAX_VALUE))>0){
				double rate=min.negate().divide(scope).doubleValue();
				return RandomUtil.Boolean.next(rate)?next(min.doubleValue()):next(max.doubleValue());
			}
			return next(scope.doubleValue())+min.doubleValue();	
		}
		static	public	java.lang.Double next(java.lang.Double t) {
			return Math.random()*t;
		}
	};
	//string
	final public static class String{
		static char[] cs=new char[Integer.next(10)];
		static	public java.lang.String next() {
			for (int i =cs.length;i-->0;) {
				cs[i]=Character.next();
			}
			return new java.lang.String(cs); 
		}
		static	public java.lang.String nextChinese() {
			for (int i =cs.length;i>0; i--) {
				cs[i]=Character.nextChinese();
			}
			return new java.lang.String(cs); 
		}
		static	public java.lang.String nextLetter() {
			for (int i =cs.length;i>0; i--) {
				cs[i]=Character.nextLetter();
			}
			return new java.lang.String(cs); 
		}
		static	public java.lang.String nextNumber() {
			for (int i =cs.length;i>0; i--) {
				cs[i]=Character.nextNumber();
			}
			return new java.lang.String(cs); 
		}
		static	public java.lang.String nextSymbol() {
			for (int i =cs.length;i>0; i--) {
				cs[i]=Character.nextSymbol();
			}
			return new java.lang.String(cs); 
		}
	}
}
