package cn.itcast.listener;

import cn.itcast.util.MailUtil;
import cn.itcast.vo.ContractTaskVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 合同到期消息监听器
 */
@Component
public class ContractEmailListener implements MessageListener{

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        //1.获取消息内容
        byte[] body = message.getBody();

        //2.转换为JavaBean对象
        try {
            ContractTaskVo contractTaskVo = mapper.readValue(body, ContractTaskVo.class);

            //3.发送邮件
            Date deliveryPeriod = contractTaskVo.getDeliveryPeriod();
            String delvertyDate = new SimpleDateFormat("yyyy-MM-dd").format(deliveryPeriod);

            //收件人
            String receiver = contractTaskVo.getEmail();
            //标题
            String title = "交货期限到期提醒邮件";
            //内容
            String content = "尊敬的"+contractTaskVo.getUserName()+"：你有一个合同（合同号："+contractTaskVo.getContractNo()+"在"+delvertyDate+"到期，请请及时处理。如果已经处理请忽略。）";

            //发送邮件
            try {
                MailUtil.sendMsg("1014671449@qq.com",title,content);
                System.out.println("发送成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
