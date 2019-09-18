package com.netty.nettyserver;

import com.netty.handler.HelloServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 启动netty代码
 */
public class NettyServer {
    public static void startNetty(String hostName, int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();//创建服务器引导程序
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();//创建NIO时间循环 非阻塞io
        serverBootstrap.group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)//配置NioServerSocketChannel 的通道;
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //重写这个实例化的
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());//代码中添加了 String类型的编解码 handler
                        p.addLast(new HelloServerHandler());//此处是自定义的 handler
                    }
                });
        //关键步骤来了  绑定  同步
        try {
            serverBootstrap.bind(hostName,port).sync();
            System.out.println("===========netty启动成功！！！！！！=============");
            System.out.println("===========netty启动成功！！！！！！=============");
        } catch (InterruptedException ie) {
            ie.printStackTrace();//打印中断异常
        }
    }
}
