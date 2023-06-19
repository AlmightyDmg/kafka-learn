package com.dmg.kafkalearn.provider;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class MyProvider implements ProducerListener {
    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    /**
     * 简单发送消息
     * @author zhum
     * @date 2023/6/16 15:08
     * @param
     * @return void
     */
    @Test
    public void sendMessage(String content){
        // 第一个参数为topic，第二个为消息体
        kafkaTemplate.send("ifun",content);
    }



    /**
     * 带回调的生产者 1
     * @author zhum
     * @date 2023/6/16 15:09
     * @param
     * @return void
     */
    @Test
    void sendCallBackMessageOne() {
        kafkaTemplate.send("ifun", "hello callback one").addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            log.info("send success：topic:{} partition:{} offset:{}", topic, partition, offset);
        }, failure -> {
            log.info("send fail：message:{} ", failure.getMessage());
        });
    }


    /**
     * 回调方式2
     * @author zhum
     * @date 2023/6/16 15:13
     * @param
     * @return void
     */
    @Test
    void sendCallBackMessageTwo(){
        kafkaTemplate.send("ifun", "hello callback two").addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("send fail：message:{} ", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                String topic = result.getRecordMetadata().topic();
                int partition = result.getRecordMetadata().partition();
                long offset = result.getRecordMetadata().offset();
                log.info("send success：topic:{} partition:{} offset:{}", topic, partition, offset);
            }
        });
    }


    /**
     * 全局回调方式
     * @author zhum
     * @date 2023/6/16 15:14
     * @param producerRecord
     * @param recordMetadata
     * @return void
     */
    @Override
    public void onSuccess(ProducerRecord producerRecord,
                          RecordMetadata recordMetadata) {
        String topic = recordMetadata.topic();
        int partition = recordMetadata.partition();
        long offset = recordMetadata.offset();
        log.info("send success：topic:{} partition:{} offset:{}",topic,partition,offset);
    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.info("send fail : {}", exception.getMessage());
    }


    /**
     * 带事务的提交
     * @author zhum
     * @date 2023/6/16 15:24
     * @param
     * @return void
     */
    @Test
    @Transactional
    void sendWithException(){
        kafkaTemplate.send("ifun","不带事务提交！");
        kafkaTemplate.executeInTransaction(oper->{
            oper.send("ifun","带事务的提交");
            throw new RuntimeException("fail 1");
        });
        throw new RuntimeException("fail 2");
    }

    /**
     * 序列化发送消息
     * @author zhum
     * @date 2023/6/16 15:42
     * @param
     * @return void
     */
    @Test
    void sendMessageSer(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(21);
        userInfo.setId(1L);
        userInfo.setName("Jack");
        kafkaTemplate.send("ifun",userInfo);
    }

}
