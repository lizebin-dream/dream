package cn.itcast.stat;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 *
 */
public class StatProvider {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext cxt =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        cxt.start();
        System.in.read();

    }

}
