package com.netty.rpcservice;


import com.netty.dto.Student;
import org.springframework.web.bind.annotation.RequestBody;

public interface SayHello {
    String sayHello(String s);

    Student testStudent(@RequestBody Student student);
}
