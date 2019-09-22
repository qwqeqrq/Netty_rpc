package com.client;


import com.client.dto.Student;
import org.springframework.web.bind.annotation.RequestBody;

public interface SayHello {
    String sayHello(String s);

    Student testStudent(@RequestBody Student student);
}
