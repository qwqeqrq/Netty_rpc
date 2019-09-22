package com.netty.dto;

import java.io.Serializable;

public class Student implements Serializable {

    private String userName;

    private String id;

    private int code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userName='" + userName + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                '}';
    }
}
