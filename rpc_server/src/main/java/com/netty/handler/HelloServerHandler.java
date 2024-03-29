package com.netty.handler;

import com.alibaba.fastjson.JSON;
import com.netty.dto.Student;
import com.netty.rpcservice.SayHelloImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义一个 处理程序 handler 处理来的消息 类型
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    //首先继承ChannelInboundHandlerAdapter 重写方法肯定是必须的了 不然继承他干啥

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //通道读取 read
        // 如何符合约定，则调用本地方法，返回数据(这里显示判断了是否符合约定（并没有使用复杂的协议，只是一个字符串判断），然后创建一个具体实现类，并调用方法写回客户端。)
      /*  if (msg.toString().startsWith("rpcniubi")) {
            String result = new SayHelloImpl().sayHello(msg.toString());
            ctx.writeAndFlush(result);
        }*/
       // String studentJson = msg.toString().split("t")[1];
        Student student12 = JSON.parseObject(msg.toString(), Student.class);
        System.out.println("服务器收到的入参："+student12.toString());
        //if (msg.toString().startsWith("rpcniubi")) {
            Student student = new SayHelloImpl().testStudent(student12);
            ctx.writeAndFlush(JSON.toJSONString(student));
            System.out.println("服务端结果计算完成写入信道");
       // }
    }
}
