package cn.itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 企业服务发布
 */
public class CompanyProvider {

    public static void main(String[] args) throws IOException {
        //1.初始化环境   一共加载了三个文件：dubbo, tx, dao
        ClassPathXmlApplicationContext cxt =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        //2.启动
        cxt.start();

        //3.阻塞
        System.in.read();
    }

}
