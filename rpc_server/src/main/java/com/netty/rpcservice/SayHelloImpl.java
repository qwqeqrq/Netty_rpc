package com.netty.rpcservice;

public class SayHelloImpl implements SayHello {

    public String sayHello(String s) {
        System.out.println(s);
        return s;
    }
}
