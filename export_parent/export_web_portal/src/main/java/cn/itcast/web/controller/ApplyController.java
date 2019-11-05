package cn.itcast.web.controller;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 企业申请控制器
 */
@Controller
public class ApplyController {

    @Reference  // 注意：远程调用，不用要使用@Autowired
    private CompanyService companyService;

    /**
     * 企业申请入驻的方法
     *    访问地址：http://localhost:8083/apply.do
     *    参数：Company对象
     *    返回：1 ：成功
     *         2：失败
     */
    @RequestMapping("/apply")
    @ResponseBody
    public String apply(Company company){

        try {
            company.setState(0);//未审核
            companyService.save(company);

            //成功
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "2";
        }
    }

}
