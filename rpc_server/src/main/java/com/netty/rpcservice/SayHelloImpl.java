package com.netty.rpcservice;

import com.netty.dto.Student;
import org.springframework.web.bind.annotation.RequestBody;

public class SayHelloImpl implements SayHello {

    public String sayHello(String s) {
        System.out.println(s);
        return s;
    }

    @Override
    public Student testStudent(@RequestBody Student student)
    {
        student.setId("远程方法调用成功");
        return student;
    }
}
