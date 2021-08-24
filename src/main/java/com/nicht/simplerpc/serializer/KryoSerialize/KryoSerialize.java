package com.nicht.simplerpc.serializer.KryoSerialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.nicht.simplerpc.serializer.AbstractSerialize;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/23
 * @Link
 */
public class KryoSerialize extends AbstractSerialize {
    private Kryo kryo;
    @Override
    public<T> byte[] serialize(T obj) {
        Output  output =new Output(1,4096);
        kryo.writeClassAndObject(output,obj);
        byte[] bb = output.toBytes();
        output.flush();
        return  bb;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        Input input = null;
        @SuppressWarnings("unchecked")
        T res = (T) kryo.readClassAndObject(input);
        input.close();
        return res;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
