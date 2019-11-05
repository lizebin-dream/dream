package cn.itcast.util;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 使用JavaMail发送邮件的工具类
 */
public class MailUtil {

    //实现邮件发送的方法
    public static void sendMsg(String to ,String subject ,String content) throws Exception{

        //(一) 建立和邮件发送服务器的连接
        //1.1 设置邮件服务器的参数
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.sina.com");  //设置主机地址   smtp.qq.com    smtp.sina.com

        props.setProperty("mail.smtp.auth", "true");//认证

        //2.产生一个用于邮件发送的Session对象
        Session session = Session.getInstance(props);


        //(二)编写一封邮件内容
        //3.产生一个邮件的消息对象
        MimeMessage message = new MimeMessage(session);

        //4.设置消息的发送者
        Address fromAddr = new InternetAddress("itcast004@sina.com");
        message.setFrom(fromAddr);

        //5.设置消息的接收者
        Address toAddr = new InternetAddress(to);
        //TO 直接发送  CC抄送    BCC密送
        message.setRecipient(MimeMessage.RecipientType.TO, toAddr);

        //6.设置主题
        message.setSubject(subject);
        //7.设置正文
        message.setText(content);


        //（三）发送邮件
        //8.准备发送，得到火箭
        Transport transport = session.getTransport("smtp");
        //9.设置火箭的发射目标
        transport.connect("smtp.sina.com", "itcast004@sina.com", "loveyou");
        //10.发送
        transport.sendMessage(message, message.getAllRecipients());

        //11.关闭
        transport.close();
    }
}
