package cn.itcast.web.task;

import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractTaskVo;
import cn.itcast.web.utils.MailUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
  * 每天早上10点，对交期还有2天的购销合同，发送提醒邮件
 */
public class ContractTask {

    //注入远程的dubbo服务
    //@Reference
    //通过本地方式 来 注入 远程服务
    @Autowired
    @Qualifier("contractService")  // @Qualifier: 指定bean的id进行注入
    private ContractService contractService;

    //注入RabbitTemplate对象
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void execute(){

        //System.out.println("触发了任务...");

        //1.调用业务，查询还有2天到期的购销合同
        //System.out.println("contractService=="+contractService);
        List<ContractTaskVo> list = contractService.findByPeriod(2);

        //2.遍历，给每个合同创建人发送提醒邮件
        if(list!=null &&list.size()>0){

            for(ContractTaskVo contractTaskVo:list){
               /* //交货期限
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
                }*/


                //异步发送消息到RabbitMQ
                rabbitTemplate.convertAndSend("contract.delivery",contractTaskVo);
                System.out.println("发送成功");

            }

        }

    }

}
