//package com.dmg.kafkalearn.provider;
//
//import java.util.Map;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.Partitioner;
//import org.apache.kafka.common.Cluster;
//import org.springframework.stereotype.Component;
//
///**
// * 配置自定义分区  配合配置文件
// * @author zhum
// * @date 2023/6/16 15:15
// * @param null
// * @return
// */
//@Component
//@Slf4j
//public class CustomPartitioner implements Partitioner {
//
//    @Override
//    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
//        // 自定义分区规则(这里假设全部发到0号分区)
//        log.info("自定义分区策略 topic:{} key:{} value:{}",topic,key,value.toString());
//        return 0;
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void configure(Map<String, ?> configs) {
//
//    }
//}
