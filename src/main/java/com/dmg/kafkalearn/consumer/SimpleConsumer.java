package com.dmg.kafkalearn.consumer;

import java.util.List;

import com.dmg.kafkalearn.provider.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @ClassName SimpleConsumer
 * @Description 简单消费者
 * @author zhum
 * @date 2023/6/16 15:06
 */
@Component
@Slf4j
public class SimpleConsumer {
    // 消费监听
    @KafkaListener(topics = {"ifun"})
    public void onMessage(ConsumerRecord<String, String> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
    }



//    /**
//     * id：唯一标识。如果没有配置，取application.yml中的 consumer.groupId
//     * idIsGroup ：默认true，true的话代表该consumer分组group！
//     * groupId：消费者分组。如果不填，取id （idIsGroup=true）作为分组。否则取application.yml中的 consumer.groupId
//     * topic 与 topicPartitions 不能共用。
//     * topic：类似于subscripe订阅模式。
//     * topicPartitions类似于assign手动分配模式。
//     *
//     * 解释：这里定义了消费者id为ifun-001，消费者组id为ifun-01，同时监听两个topic，
//     * ifun1和ifun2，其中监听ifun1的0号分区，ifun2的0号和1号分区，其中1号分区开始的offset为8，
//     * 也就是说如果next-offset大于8就会消费，小于8不会消费。
//     * @author zhum
//     * @date 2023/6/16 15:25
//     * @param record
//     * @return void
//     */
//    @KafkaListener(id = "ifun-001",groupId = "ifun-01", topicPartitions={
//            @TopicPartition(topic = "ifun1",partitions = {"0"}),
//            @TopicPartition(topic = "ifun2",
//                    partitions = {"0"},
//                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
//    }
//    )
//    public void onTopicsMessage(ConsumerRecord<String, String> record){
//        log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//    }
//
//
//
//    /**
//     * 批量消费
//     * @author zhum
//     * @date 2023/6/16 15:28
//     * @param records
//     * @return void
//     */
//    @KafkaListener(topics = {"ifun"})
//    public void onBatchMessage(List<ConsumerRecord<String, String>> records){
//        log.info("批量消费");
//        for (ConsumerRecord<String, String> record : records) {
//            log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//        }
//    }
//
//
//    /**
//     * 手动ack
//     * 如果没有ack，那么会出现如下情况：
//     *
//     * 1. 如果在消费kafka的数据过程中，一直没有提交offset，那么在此程序运行的过程中它不会重复消费。但是如果重启之后，就会重复消费之前没有提交offset的数据。
//     * 2. 如果在消费的过程中有几条或者一批数据数据没有提交offset，后面其他的消息消费后正常提交offset，那么服务端会更新为消费后最新的offset，不会重新消费，就算重启程序也不会重新消费。
//     * 3. 消费者如果没有提交offset，程序不会阻塞或者重复消费，除非在消费到这个你不想提交offset的消息时你尝试重新初始化一个客户端消费者，即可再次消费这个未提交offset的数据。
//     * 因为客户端也记录了当前消费者的offset信息，所以程序会在每次消费了数据之后，自己记录offset，而手动提交到服务端的offset与这个并没有关系，所以程序会继续往下消费。
//     * 在你重新初始化客户端消费者之后，会从服务端得到最新的offset信息记录到本地。所以说如果当前的消费的消息没有提交offset，此时在你重新初始化消费者之后，
//     * 可得到这条未提交消息的offset,从此位置开始消费。
//     * @author zhum
//     * @date 2023/6/16 15:31
//     * @param records
//     * @param ack
//     * @return void
//     */
//    @KafkaListener(topics = {"ifun"})
//    public void onBatchMessage(List<ConsumerRecord<String, String>> records, Acknowledgment ack){
//
//        try{
//            log.info("批量消费");
//            for (ConsumerRecord<String, String> record : records) {
//                log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//            }
//        }finally {
//            ack.acknowledge();
//        }
//    }
//
//
//
//    @Bean
//    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
//        return (message, exception, consumer) -> {
//            System.out.println("消费异常："+message.getPayload());
//            return null;
//        };
//    }
//
//    /**
//     * 消费异常捕获
//     * @author zhum
//     * @date 2023/6/16 15:33
//     * @param records
//     * @param ack
//     * @return void
//     */
//    @KafkaListener(topics = {"ifun"}, errorHandler = "consumerAwareErrorHandler")
//    public void onBatchMessageErr(List<ConsumerRecord<String, String>> records, Acknowledgment ack){
//        try{
//            log.info("批量消费");
//            for (ConsumerRecord<String, String> record : records) {
//                log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//                throw new RuntimeException("消费异常");
//            }
//        }finally {
//            ack.acknowledge();
//        }
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        ConsumerFactory consumerFactory = new ConsumerFactory() {
//            @Override
//            public Consumer createConsumer(String s, String s1, String s2) {
//                return null;
//            }
//
//            @Override
//            public boolean isAutoCommit() {
//                return false;
//            }
//        };
//        factory.setConsumerFactory(consumerFactory);
//        // 被过滤的消息将被丢弃
//        factory.setAckDiscarded(true);
//        // 消息过滤策略
//        factory.setRecordFilterStrategy(consumerRecord -> {
//            if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
//                return false;
//            }
//            //返回true消息则被过滤
//            return true;
//        });
//        return factory;
//    }
//
//    /**
//     * 配置消息过滤器
//     * 消息过滤器可以在消息抵达consumer之前被拦截，在实际应用中，我们可以根据自己的业务逻辑，筛选出需要的信息再交由KafkaListener处理，不需要的消息则过滤掉。
//     *
//     * 需要为监听器工厂配置一个RecordFilterStrategy，返回true的时候消息将被抛弃，返回false会正常抵达监听器。
//     *
//     * 然后在监听器上设置containerFactory属性为配置的过滤器工厂类
//     * @author zhum
//     * @date 2023/6/16 15:36
//     * @param record
//     * @return void
//     */
//    @KafkaListener(topics = {"ifun"},containerFactory = "filterContainerFactory")
//    public void onMessageFilter(ConsumerRecord<String, String> record){
//        log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//    }
//
//
//    /**
//     * 转发消息
//     * @author zhum
//     * @date 2023/6/16 15:38
//     * @param record
//     * @return java.lang.String
//     */
//    @KafkaListener(topics = {"ifun"})
//    @SendTo("ifun1")
//    public String onMessageSend(ConsumerRecord<String, String> record){
//        log.info("topic {} 收到需要转发的消息:{}",record.topic(), record.value());
//        return record.value()+" 【forward message】";
//    }
//
//    @KafkaListener(topics = {"ifun1"})
//    public void onIFun1Message(ConsumerRecord<String, String> record){
//        log.info("topic:{}，partition:{}，消息:{}",record.topic(),record.partition(),record.value());
//    }
//
//
//    /**
//     * 序列化消费消息
//     * 注意：发送的类要和消费的类的全类名一致才行，不能是类名一样，字段一样，但是包名不一样，这样会抛异常。
//     * @author zhum
//     * @date 2023/6/16 15:43
//     * @param userInfo
//     * @return void
//     */
//    @KafkaListener(topics = {"ifun"})
//    public void onMessage(UserInfo userInfo){
//        log.info("消息:{}",userInfo);
//    }
}
