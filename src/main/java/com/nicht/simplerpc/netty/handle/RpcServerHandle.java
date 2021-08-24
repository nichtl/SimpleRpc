package com.nicht.simplerpc.netty.handle;

import com.nicht.simplerpc.model.RpcRequest;
import com.nicht.simplerpc.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/24
 * @Link
 */
public class RpcServerHandle extends ChannelInboundHandlerAdapter {
    private   static  RpcRequest  rpcRequest  =null;
    private   static RpcResponse  rpcResponse =null;
    //服务端接口-实现类的映射表
    private final Map<String, Object> handlerMap;

    public RpcServerHandle(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        rpcRequest = (RpcRequest) msg;
        try {
            rpcResponse = (RpcResponse) handle(rpcRequest);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ctx.channel().writeAndFlush(rpcResponse);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
    private static RpcRequest methodCacheName=null;
    private static Object  methodCacheValue=null;
    private Object handle(RpcRequest request) throws Throwable
    {
        String className = request.getClassName();

        Object classimpl = handlerMap.get(className);//通过类名找到实现的类

        Class<?> clazz = classimpl.getClass();

        String methodName = request.getMethodName();

        Class<?>[] parameterTypes = request.getParameterTypes();

        Object[] parameters = request.getParameters();

//		 Method method = ReflectionCache.getMethod(clazz.getName(),methodName, parameterTypes);
//		 method.setAccessible(true);
            try
            {
                    FastClass serviceFastClass = FastClass.create(clazz);
                    FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
                    return serviceFastMethod.invoke(classimpl, parameters);
                //return method.invoke(classimpl, parameters);
            }
            catch (Throwable e)
            {
                throw e.getCause();
            }

    }
}
