package com.nicht.simplerpc.serializer;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/23
 * @Link
 */
public abstract class AbstractSerialize {
    public  abstract  <T> byte[] serialize(T obj);
    public  abstract  <T>  T deserialize(byte[] bytes);
    public  abstract  <T>  T deserialize(byte[] bytes,Class<T> clazz);
}
