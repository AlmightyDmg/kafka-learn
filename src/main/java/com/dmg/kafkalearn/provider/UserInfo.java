package com.dmg.kafkalearn.provider;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserInfo implements Serializable {
    private Long id;
    private String name;
    private Integer age;
}
