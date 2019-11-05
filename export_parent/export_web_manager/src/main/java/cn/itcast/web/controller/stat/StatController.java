package cn.itcast.web.controller.stat;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 */
@Controller
@RequestMapping("/stat")
public class StatController extends BaseController{

    @Reference
    private StatService statService;

    /**
     * 跳转到统计页面
     *    请求路径：http://localhost:8080/stat/toCharts.do
     *    参数:chartsType=sell
     *    返回：
     *        1）chartsType=sell
     *            /WEB-INF/pages/stat/stat-sell.jsp
     *        2）chartsType=factory
     *            /WEB-INF/pages/stat/stat-factory.jsp
     *        3）chartsType=online
     *            /WEB-INF/pages/stat/stat-online.jsp
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+chartsType;
    }

    /**
     * 获取生产厂家销售统计
     */
    @RequestMapping("/getFactoryData")
    @ResponseBody // 转换Json字符串
    public List<Map<String,Object>> getFactoryData(){
        return statService.getFactoryData(getLoginCompanyId());
    }

    /**
     * 获取产品销量
     */
    @RequestMapping("/getSellData")
    @ResponseBody // 转换Json字符串
    public List<Map<String,Object>> getSellData(){
        return statService.getSellData(getLoginCompanyId());
    }


    /**
     * 每小时统计访问人数
     */
    @RequestMapping("/getOnlineData")
    @ResponseBody // 转换Json字符串
    public List<Map<String,Object>> getOnlineData(){
        return statService.getOnlineData(getLoginCompanyId());
    }
}
