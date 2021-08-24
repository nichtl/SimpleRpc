package com.nicht.simplerpc.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/23
 * @Link
 */
public class RpcResponse implements Serializable {
    public static final Long serialVersionUID = 5606111910428846773L;

    private Class<?> clazz;

    private byte[] exption;

    private String requestId;

    private Throwable errorMsg;

    private Object appResponse;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public byte[] getExption() {
        return exption;
    }

    public void setExption(byte[] exption) {
        this.exption = exption;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Throwable errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getAppResponse() {
        return appResponse;
    }

    public void setAppResponse(Object appResponse) {
        this.appResponse = appResponse;
    }

    @Override
    public boolean equals(Object obj)  {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RpcResponse other = (RpcResponse) obj;

        if(clazz==null)
        {
            if(other.getClazz()!=null)
                return false;
        }
        else
        {
            if(other.getClazz()==null)
                return false;
            else if(!clazz.equals(other.getClazz()))
                return false;
        }
        if(exption==null)
        {
            if (other.getExption()!= null)
                return false;
        }
        else
        {
            if(other.getExption()== null)
                return false;
            if(exption.length!=other.getExption().length)
                return false;
            else
                for (int i = 0; i < exption.length; i++) {
                    if(exption[i]!=other.getExption()[i])
                        return false;
                }
        }
        if(appResponse==null)
        {
            if(other.getAppResponse()!=null)
                return false;
        }
        else
        {
            if(other.getAppResponse()==null)
                return false;
            else if(!appResponse.equals(other.getAppResponse()))
                return false;
        }
        return true;
    }
}
