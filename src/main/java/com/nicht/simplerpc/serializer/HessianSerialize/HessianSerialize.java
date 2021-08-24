package com.nicht.simplerpc.serializer.HessianSerialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.nicht.simplerpc.serializer.AbstractSerialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/23
 * @Link
 */
public class HessianSerialize  extends AbstractSerialize {
    @Override
    public <T> byte[] serialize(T obj) {
        if (obj  == null){
            throw new NullPointerException();
        }
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(obj);
            return  bos.toByteArray();
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new  RuntimeException();
        }
    }

    @Override
    public <T> T deserialize(byte[] data) {
        if (data == null){
            throw  new  NullPointerException();
        }
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(bis);
            return (T)hi.readObject();
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new  RuntimeException();
        }

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
