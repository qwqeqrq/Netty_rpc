package com.client;


import com.alibaba.fastjson.JSON;
import com.client.dto.Student;
import com.client.handler.ClientHandler;
import com.client.proxy.NettyProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.proxy.UndeclaredThrowableException;

import java.util.Optional;

@SpringBootApplication
public class NettyClientApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientApplication.class, args);
        int n=0;
        //在这里进行测试
        for (; ; ) {
            NettyProxy nettyProxy = new NettyProxy();
            // 创建一个代理对象
            SayHello service = (SayHello) nettyProxy
                    .createProxy(SayHello.class, "rpcniubi");
            System.out.println(n++);
            Thread.sleep(2000);
            Student s = new Student();
            s.setCode(101);
            s.setUserName("李明");
            try {
                System.out.println("rpc客户端返回结果：" + Optional.ofNullable(service.testStudent(s)).orElse(new Student()).toString());
            } catch (UndeclaredThrowableException e) {
                e.printStackTrace();
            }
        }
       /* for (; ; ) {
            Thread.sleep(1000);
            System.out.println(service.sayHello("你去吃屎吧！垃圾"));
        }*/
    }
}
