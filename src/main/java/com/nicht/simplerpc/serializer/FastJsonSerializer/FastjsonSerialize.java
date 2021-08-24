package com.nicht.simplerpc.serializer.FastJsonSerializer;

import com.alibaba.fastjson.JSON;
import com.nicht.simplerpc.serializer.AbstractSerialize;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/23
 * @Link
 */
public class FastjsonSerialize extends AbstractSerialize {
    @Override
    public <T> byte[] serialize(T obj) {
        if (obj  == null){
            throw new NullPointerException();
        }

        String json = JSON.toJSONString(obj);
        byte[] data = json.getBytes();
        return data;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        T obj = (T)JSON.parse(new String(bytes));
        return obj;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T obj = JSON.parseObject(new String(bytes),clazz );
        return obj;
    }
}
