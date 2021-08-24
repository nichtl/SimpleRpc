package com.nicht.simplerpc.codec;

import com.nicht.simplerpc.model.RpcRequest;
import com.nicht.simplerpc.model.RpcResponse;
import com.nicht.simplerpc.serializer.AbstractSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/24
 * @Link
 */
public class RpcEncoder  extends MessageToByteEncoder {
    private static Object  responseName=null;
    private static byte[]  responseValue=null;
    private static Object  requestName=null;
    private static byte[]  requestValue=null;
    private Class<?> genericClass;
    private AbstractSerialize serialize;

    public RpcEncoder(Class<?> genericClass,AbstractSerialize serialize) {
        this.genericClass = genericClass;
        this.serialize = serialize;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        if(genericClass.equals(RpcResponse.class)){ //
            RpcResponse response=(RpcResponse)msg;
            String requestId=response.getRequestId();
            response.setRequestId("");
            byte[] requestIdByte=requestId.getBytes();
            byte[] body=null;
            body=serialize.serialize(msg);
            int totalLen=requestIdByte.length+4+body.length;
            out.writeInt(totalLen);
            out.writeInt(requestIdByte.length);
            out.writeBytes(requestIdByte);
            out.writeBytes(body);
        }
        else if(genericClass.equals(RpcRequest.class)){
            RpcRequest request=(RpcRequest)msg;
            String requestId=request.getRequestId();
            request.setRequestId("");
            byte[] requestIdByte=requestId.getBytes();

            byte[] body=null;
            int totalLen=requestIdByte.length+4+body.length;
            out.writeInt(totalLen);
            out.writeInt(requestIdByte.length);
            out.writeBytes(requestIdByte);
            out.writeBytes(body);
        }
        else
        {
            byte[] body=serialize.serialize(msg);
            out.writeInt(body.length);
            out.writeBytes(body);
        }

    }
}
