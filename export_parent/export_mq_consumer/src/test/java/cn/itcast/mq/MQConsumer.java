package cn.itcast.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * MQ消费方启动
 */
public class MQConsumer {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext cxt
                  = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-rabbitmq-consumer.xml");

        System.in.read();

    }

}
