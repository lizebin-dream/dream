package cn.itcast.cargo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 货运模块服务
 */
public class CargoProvider {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext cxt =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        cxt.start();
        System.in.read();

    }

}
