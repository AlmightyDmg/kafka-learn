## windows 使用docker安装kafka
1. 拉取zookeeper
```
docker pull wurstmeister/zookeeper
```
2. 拉取kafka
```
docker pull wurstmeister/kafka
```

3. 启动zookeeper
```
docker run -d --name zookeeper -p 2181:2181 wurstmeister/zookeeper
```

4. 启动kafka
```
docker run -d --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=172.16.28.132:2181/kafka -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://172.16.28.132:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 wurstmeister/kafka
```

5. 进入kafka
```
docker exec -it kafka /bin/bash
```

6. 再运行kafka生产者来发布消息，当前窗口称为【窗口A】
```
/opt/kafka_2.13-2.7.0/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
```

7. 再重新开启一个窗口【窗口B】，运行kafka消费者，接受消息
```
/opt/kafka_2.13-2.7.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```

8. 在生产者窗口A中，我们可以注意到生产者前面出现“>”符号，在后面位置输入内容然后按下回车，就可以到消费者窗口B中看到内容。这意味着简单的kafka结合zookeeper消息生产与消费就完成了

9. 参考：https://blog.csdn.net/lmchhh/article/details/120147229

## 遇到问题
1. kafka版本问题
   https://spring.io/projects/spring-kafka
