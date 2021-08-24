package com.nicht.simplerpc.netty;

import com.nicht.simplerpc.codec.RpcDecoder;
import com.nicht.simplerpc.codec.RpcEncoder;
import com.nicht.simplerpc.model.RpcRequest;
import com.nicht.simplerpc.model.RpcResponse;
import com.nicht.simplerpc.netty.handle.RpcServerHandle;
import com.nicht.simplerpc.serializer.KryoSerialize.KryoSerialize;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Nicht
 * @Description
 * @Time 2021/8/24
 * @Link
 */
public class RpcServerBuilder {
    //存放接口名与服务对象之间的映射关系
    private Map<String, Object> handlerMap = new HashMap<>();
    private Class<?> interfaceclazz;
    private Object classimplement;
    private String version;
    private int port ;
    public RpcServerBuilder(int port){
        this.port = port;
    }

    public Class<?> getInterfaceclazz() {
        return interfaceclazz;
    }

    public RpcServerBuilder setInterfaceclazz(Class<?> interfaceclazz) {
        this.interfaceclazz = interfaceclazz;
        return this;
    }

    public Object getClassimplement() {
        return classimplement;
    }

    public RpcServerBuilder setClassimplement(Object classimplement) {
        this.classimplement = classimplement;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public RpcServerBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RpcServerBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public void  start() throws Throwable{

        handlerMap.put(interfaceclazz.getName(), classimplement);
        EventLoopGroup bossGroup =  new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//通道类型
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcDecoder(RpcResponse.class, new KryoSerialize()));//执行链顺序很重要
                            pipeline.addLast(new RpcEncoder(RpcRequest.class, new KryoSerialize()));
                            pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                            pipeline.addLast(new LoggingHandler(LogLevel.TRACE));
                            pipeline.addLast(new RpcServerHandle(handlerMap));
                        }
                    })// 配置handle处理器
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //服务端配置bytebuf接收Buffer的类型    EventExecutor
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535));
            System.out.println("RpcServer 服务端 启动了");

            // 绑定端口，开始接收进来的连接 buff
            ChannelFuture f = b.bind(port).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("RpcServer 关闭了");
        }
    }




}
