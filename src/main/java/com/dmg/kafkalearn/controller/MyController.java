package com.dmg.kafkalearn.controller;

import javax.annotation.Resource;

import com.dmg.kafkalearn.provider.MyProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Resource
    private MyProvider myProvider;

    @GetMapping("/send/{content}")
    private void send(@PathVariable("content")String content) {
        System.out.println(content);
        myProvider.sendMessage(content);
    }
}
