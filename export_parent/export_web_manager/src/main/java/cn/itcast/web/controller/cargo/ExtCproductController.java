package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 附件
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{

    /**
     * 远程获取合同的业务对象
     */
    @Reference(retries = 0)
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;


    /**
     * 分页展示附件
     *   方法请求地址：http://localhost:8080/cargo/extCproduct/list.do
     *   方法的参数：contractId=1&contractProductId=2
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String list(String contractId,
                       String contractProductId,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){

        //1.查询附件的生产厂家
        //1.1 创建Example对象
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件"); //ctype='附件'

        //1.2 查询厂家列表
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //1.3 存入request
        request.setAttribute("factoryList",factoryList);

        //2.分页查询指定货物下的所有附件
        //2.1 创建ContractProductExample对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);

        //2.2 查询附件列表
        PageInfo<ExtCproduct> pageInfo = extCproductService.findByPage(extCproductExample,pageNum,pageSize);

        //2.3 存入request
        request.setAttribute("pageInfo",pageInfo);

        //补充contractId，为了在添加附件时可以使用以下两个ID值
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        //3.返回页面
        return "cargo/extc/extc-list";
    }

    /**
     * 保存附件信息（添加、更新）
     *   方法请求地址：http://localhost:8080/cargo/extCproduct/edit.do
     *   方法的参数：ExtCproduct
     *   方法的返回值/跳转的页面：重定向到附件列表（redirect:/cargo/extCproduct/list.do?contractId=1&contractProductId=1）
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) throws Exception {
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Contract对象中
        extCproduct.setCompanyId(companyId);
        extCproduct.setCompanyName(compayName);

        //1.判断是否存在ID
        if(StringUtil.isEmpty(extCproduct.getId())){
            //调用七牛工具类，保存货物图片
            String imgUrl = fileUploadUtil.upload(productPhoto);
            //设置图片路径
            extCproduct.setProductImage("http://"+imgUrl);

            //1.1 不存在，添加
            extCproductService.save(extCproduct);
        }else{
            //1.2 存在，更新
            extCproductService.update(extCproduct);
        }

        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"" +
                "&contractProductId="+extCproduct.getContractProductId();
    }

    /**
     * 进入附件修改页面
     *   方法请求地址：http://localhost:8080/cargo/extCproduct/toUpdate.do
     *   方法的参数：id=1&contractId=2&contractProductId=3
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/extc/extc-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId){
        //1.查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        //2.存入request
        request.setAttribute("extCproduct",extCproduct);

        //3.查询货物的生产厂家
        //3.1 创建Example对象
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件"); //ctype='附件'

        //3.2 查询厂家列表
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //3.3 存入request
        request.setAttribute("factoryList",factoryList);

        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        return "cargo/extc/extc-update";
    }


    /**
     * 删除附件
     *   方法请求地址：http://localhost:8080/cargo/extCproduct/delete.do
     *   方法的参数：id=1&contractId=2&contractProductId=3
     *   方法的返回值/跳转的页面：重定向到附件列表（redirect:/cargo/extCproduct/list.do?contractId=1&contractProductId=1）
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        //1.调用业务
        extCproductService.delete(id);
        //2.返回列表
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
