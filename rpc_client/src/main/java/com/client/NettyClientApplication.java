package com.client;


import com.client.handler.ClientHandler;
import com.client.proxy.NettyProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientApplication.class, args);
        //在这里进行测试
        NettyProxy nettyProxy = new NettyProxy();
        // 创建一个代理对象
        SayHello service = (SayHello) nettyProxy
                .createProxy(SayHello.class,"rpcniubi");
        for (; ; ) {
            Thread.sleep(1000);
            System.out.println(service.sayHello("你去吃屎吧！垃圾"));
        }
    }
}
