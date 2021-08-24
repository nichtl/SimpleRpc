package com.nicht.simplerpc.codec;

import com.nicht.simplerpc.Util.Tool;
import com.nicht.simplerpc.model.RpcRequest;
import com.nicht.simplerpc.model.RpcResponse;
import com.nicht.simplerpc.serializer.AbstractSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/24
 * @Link
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private static Object  responseName=null;
    private static byte[]  responseValue=null;
    private static Object  requestName=null;
    private static byte[]  requestValue=null;
    private Class<?> genericClass;
    private AbstractSerialize serialize;
    public RpcDecoder(Class<?> genericClass, AbstractSerialize serialize) {
        this.genericClass = genericClass;
        this.serialize = serialize;
    }



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int HEAD_LENGTH=4;
        if (in.readableBytes() < HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        if(genericClass.equals(RpcResponse.class)){
            int requestIdLength=in.readInt();//获取到requestId的长度

            byte[] requestIdBytes=new byte[requestIdLength];
            in.readBytes(requestIdBytes);

            int bodyLength=dataLength-4-requestIdLength;

            byte[] body = new byte[bodyLength];
            in.readBytes(body);
            String requestId=new String(requestIdBytes);
            RpcResponse obj=(RpcResponse) Tool.deserialize(body, genericClass);
            obj.setRequestId(requestId);//设置requestId
            out.add(obj);
        }
        else if(genericClass.equals(RpcRequest.class)) {
            int requestIdLength = in.readInt();//获取到requestId的长度

            byte[] requestIdBytes = new byte[requestIdLength];
            in.readBytes(requestIdBytes);

            int bodyLength = dataLength - 4 - requestIdLength;

            byte[] body = new byte[bodyLength];
            in.readBytes(body);
            String requestId = new String(requestIdBytes);
            RpcRequest obj=(RpcRequest) Tool.deserialize(body, genericClass);
            obj.setRequestId(requestId);//设置requestId
            out.add(obj);

        }
        else
        {
            byte[] body = new byte[dataLength];
            in.readBytes(body);
            Object obj=Tool.deserialize(body, genericClass);
            out.add(obj);
        }






    }
}
