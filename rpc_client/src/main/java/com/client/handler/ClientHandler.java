package com.client.handler;


import com.alibaba.fastjson.JSON;
import com.client.dto.Student;
import com.client.proxy.NettyProxy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;


/**
 * 创建客户端 处理程序
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context; //通道处理上下文
    private Student student;//同学结果
    private Student studentParam;//同学参数
    private String result;//处理结果
    private String para;//参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //重写通道方法
        context = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //result = msg.toString();
        student = JSON.parseObject(msg.toString(), Student.class);
        System.out.println("客户端收到返回值：正在唤醒线程 返回值内容" + student.toString());
        notify();
    }

    /**
     * 加锁  这个地方是多个线程执行的内容
     *
     * @return
     * @throws Exception
     */
    public synchronized Object call() throws Exception {
        if (context != null) {
            context.writeAndFlush(JSON.toJSONString(studentParam)); // todo 这个方法值得研究
            wait(5);  //线程释放锁
        }
        return student;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public void setStudentParam(Student studentParam) {
        this.studentParam = studentParam;
    }

    //todo 该类缓存了 ChannelHandlerContext，用于下次使用，有两个属性：返回结果和请求参数。
    //
    //todo 当成功连接后，缓存 ChannelHandlerContext，当调用 call 方法的时候，将请求参数发送到服务端，等待。当服务端收到并返回数据后，调用 channelRead 方法，将返回值赋值个 result，并唤醒等待在 call 方法上的线程。此时，代理对象返回数据。
    //————————————————

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyProxy.connected=false;//设置断开=标志
        System.out.println("netty已经断开链接,正在尝试重新链接");
        NettyProxy.initClient();
        //super.channelInactive(ctx);
    }
}
