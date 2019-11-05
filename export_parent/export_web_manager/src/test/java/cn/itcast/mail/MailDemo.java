package cn.itcast.mail;

import cn.itcast.web.utils.MailUtil;

/**
 * 演示JavaMail发送邮件
 */
public class MailDemo {

    public static void main(String[] args) throws Exception {
        MailUtil.sendMsg("1014671449@qq.com","测邮件","这是一封测试邮件");

    }

}
