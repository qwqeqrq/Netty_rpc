package com.client.proxy;


import com.client.dto.Student;
import com.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.FutureListener;
import org.springframework.cglib.proxy.Proxy;

import java.util.concurrent.*;

/**
 * 创建消费者
 * 代理对象
 */
public class NettyProxy {

    //创建固定数量的线程池
    private static ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static ClientHandler client;

    //连接成功标识
    public static boolean connected = false;


    /**
     * 创建一个代理对象
     */
    public Object createProxy(final Class<?> serviceClass,
                              final String providerName) {
        return
                Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                            if (client == null) {
                                initClient();
                            }
                            // 设置参数
                            // client.setPara(providerName + args[0]);
                            System.out.println((Student) args[0]);
                            client.setStudentParam((Student) args[0]);
                            return executor.submit(client).get();
                        });

    }

    /**
     * 初始化客户端
     */
    public static void initClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            client = new ClientHandler();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(client);
                        }
                    });
            ChannelFuture future = b.connect("localhost", 8088);
            try {
                future.get(1000, TimeUnit.MILLISECONDS);
                System.out.println("连接状态" + future.isSuccess());
                if (future.isSuccess()) connected = true;//设置为连接成功
            } catch (TimeoutException t) {
                System.out.println("TimeoutException连接状态" + future.isSuccess());
                if (!connected) {
                    System.out.println(future.isSuccess());
                    group.schedule(() -> {
                        try {
                            while (!connected) {
                                Thread.sleep(10000);
                                System.out.println("定时重新连接开始");
                                ChannelFuture future1 = b.connect("localhost", 8088);
                                future1.get(1000, TimeUnit.MILLISECONDS);
                                System.out.println("链接成功取消10秒一次链接");
                                if (future1.isSuccess()) connected = true;//设置为连接成功
                                System.out.println("定时重新连接结果" + connected);
                            }
                        } catch (InterruptedException i) {
                            i.printStackTrace();
                        } catch (TimeoutException ti) {
                            ti.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

