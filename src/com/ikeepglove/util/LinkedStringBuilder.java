/*
 *@#MyStringBuilder.java V1.0.0 2012-11-28	
 * 
 */
package com.ikeepglove.util;

import java.util.LinkedList;

/**
 *通过链表实现的Stringbuilder，以提高效率
 *@author ben
 *@version V1.0.0 2012-11-28
 *@see LinkedList
 *@since JDK7.0 
 */
public class LinkedStringBuilder {
    private final LinkedList<String> linkedList;
    private int charCount=0;
    
    /**
     * 
     * 构建新的链表stringbuilder
     */
    public LinkedStringBuilder() {
        linkedList=new LinkedList<String>();
    }
    
    
    public LinkedStringBuilder append(String str) {
        return this.appendLast(str);
    }
   
    public LinkedStringBuilder appendFisrt(String str) {
        str=str == null ? "null":str;
        charCount+=str.length();
        linkedList.addFirst(str);
        return this;
    }
    public LinkedStringBuilder appendLast(String str) {
        str=str == null ? "null":str;
        charCount+=str.length();
        linkedList.addLast(str);
        return this;
    }
    /**
     * 
     * 添加字符串对象到链表最后，此方式等同于appendLast
     * @param obj 添加的对象，调用toString方法转换为字符串，空则为null
     * @return 当前stringbuilder
     */
    public LinkedStringBuilder append(Object obj){
        return this.appendLast(obj==null?null:obj.toString());
    }
  
    
    /**
     * 
     * 添加字符串对象到链表开头
     * @param obj 添加的对象，调用toString方法转换为字符串，空则为null
     * @return 当前stringbuilder
     */
    public LinkedStringBuilder appendFirst(Object obj){
       
        return this.appendFisrt(obj==null?null:obj.toString());
    }
    /**
     * 
     * 添加字符串对象到链表最后
     * @param obj 添加的对象，调用toString方法转换为字符串，空则为null
     * @return 当前stringbuilder
     */
    public LinkedStringBuilder appendLast(Object obj){
        return this.appendLast(obj==null?null:obj.toString());
    }
    /**
     * 
     * 删除头部的对象,若无则返回null
     * @return 已删除的头部对象
     */
    public String removeFirst(){
        String target=linkedList.poll();
        if(target!=null){
            charCount-=target.length();    
        }
        return target;
    }
    /**
     * 
     * 删除尾部的对象，若无，则返回null
     * @return 已删除的尾部对象
     */
    public String removeLast(){
        String target=linkedList.removeLast();
        if(target!=null){
            charCount-=target.length();
        }
        return target;
    }
    /**
     * 
     * 返回链表里字符串对象个数
     * @return 返回链表里字符串对象个数 
     */
    public int stringItemCount(){
        return this.linkedList.size();
    }
    /**
     * 
     * 返回当前已组装字符串的长度
     * @return 返回当前已组装字符串的长度 
     */
    public int length(){
        
        return this.charCount;
    }
    /**
     * 
     * 改变头部对象为指定对象,若头部对象无，则为新增
     * @param obj 指定对象
     * @return 原头部对象
     */
    public  String changeFirst(Object obj){
        String first=removeFirst();
        this.appendFirst(obj);
        return first;
    }
    /**
     * 
     * 改变尾部对象为指定对象,若尾部对象无，则为新增
     * @param obj 指定对象
     * @return 原头部对象
     */
    public  String changeLast(Object obj){
        String first=removeLast();
        this.appendLast(obj);
        return first;
    }
    /**
     * 
     * 获取头部字符串
     * @return 头部字符串，无则返回null
     */
    public String firstString(){
        return this.linkedList.getFirst();
    }

    /**
     * 
     * 获取尾部字符串
     * @return 尾部字符串，无则返回null
     */
    public String lastString(){
        return this.linkedList.getLast();
    }
    /**
     * 
     * 返回字符串
     * @see java.lang.Object#toString()
     */
    public String toString() {
        int l=0;
        char[] cs=new char[charCount];
        for (String item : linkedList) {
            item.getChars(0, item.length(),cs,l);
            l+=item.length();
        }
        return new String(cs);
    }
   public static void main(String[] args) throws Throwable{
       for(int i=0;i<500;i++){
           System.out.println((char)i+"="+i);
       }
}
}
