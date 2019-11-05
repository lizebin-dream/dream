package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * 出口报运的控制器
 */
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController{

    /**
     * 远程获取合同的业务对象
     */
    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;


    /**
     * 分页展示待报关合同
     *   方法请求地址：http://localhost:8080/cargo/export/contractList.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/export/export-contractList.jsp
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        //1.（租用）登录企业的ID
        String companyId = getLoginCompanyId();

        //2.创建Example，把条件封装到Example中
        ContractExample contractExample = new ContractExample();

        //添加按照创建时间倒序
        contractExample.setOrderByClause("create_time desc");

        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId); // 根据企业ID查询

        //3.查询状态为1的合同
        criteria.andStateEqualTo(1);

        //4.调用业务，获取PageInfo
        PageInfo<Contract> pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);

        //4.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "cargo/export/export-contractList";
    }


    /**
     * 分页展示报运单
     *   方法请求地址：http://localhost:8080/cargo/export/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/export/export-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "5") int pageSize){
        //1.（租用）登录企业的ID
        String companyId = getLoginCompanyId();

        //2.创建Example，把条件封装到Example中
        ExportExample exportExample = new ExportExample();

        //添加按照创建时间倒序
        exportExample.setOrderByClause("create_time desc");

        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId); // 根据企业ID查询


        //3.调用业务，获取PageInfo
        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);

        //4.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //5.返回页面
        return "cargo/export/export-list";
    }


    /**
     * 进入报运单添加页面
     *   方法请求地址：http://localhost:8080/cargo/export/toExport.do
     *   方法的参数：id=1&id=2....  多个购销合同ID
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/export/export-toExport.jsp
     */
    @RequestMapping("/toExport")
    public String toExport(String id){   //两种方式接收多个同名参数： 方式一：数组  方式二：字符串，默认逗号分隔每个ID
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    /**
     * 保存报运单信息（添加、更新）
     *   方法请求地址：http://localhost:8080/cargo/contract/edit.do
     *   方法的参数：Dept对象
     *   方法的返回值/跳转的页面：重定向到部门列表（redirect:/cargo/export/list.do）
     */
    @RequestMapping("/edit")
    public String edit(Export export){
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Export对象中
        export.setCompanyId(companyId);
        export.setCompanyName(compayName);

        //给合同设置创建人，创建人的部门
        export.setCreateBy(getLoginUser().getId());
        export.setCreateDept(getLoginUser().getDeptId());

        //1.判断是否存在ID
        if(StringUtil.isEmpty(export.getId())){
            //1.1 不存在，添加
            exportService.save(export);
        }else{
            //1.2 存在，更新
            exportService.update(export);
        }

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 进入修改报运单页面
     *    方法请求地址：http://localhost:8080/cargo/export/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/export/export-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.查询报运单
        Export export = exportService.findById(id);
        //2.存入request
        request.setAttribute("export",export);

        //3.查询报运单下面的所有报运商品
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);

        //4.存入request
        request.setAttribute("eps",list);

        return "cargo/export/export-update";
    }


    /**
     * 合同删除
     *   方法请求地址：http://localhost:8080/cargo/contract/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/contract/list.do
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //1.调用业务，删除部门
        contractService.delete(id);
        //3.返回result的Map对象
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 查询报运单
     *   方法请求地址：http://localhost:8080/cargo/export/toView.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/export/export-view.jsp
     */
    @RequestMapping("/toView")
    public String toView(String id){
        //1.查询合同详情
        Export export = exportService.findById(id);
        //2.存入request
        request.setAttribute("export",export);
        return "cargo/export/export-view";
    }

    /**
     * 提交报运单
     *   方法请求地址：http://localhost:8080/cargo/export/submit.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/export/list.do
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //需求：把合同的state从0改为1
        Export export = new Export();
        export.setId(id);
        //修改状态
        export.setState(1);
        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 取消报运单
     *    方法请求地址：http://localhost:8080/cargo/export/cancel.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/export/list.do
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //需求：把合同的state从0改为1
        Export export = new Export();
        export.setId(id);
        //修改状态
        export.setState(0);
        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 电子报运
     *    方法请求地址：http://localhost:8080/cargo/export/exportE.do
     *   方法的参数：id=1 报运单ID
     *   方法的返回值/跳转的页面："redirect:/cargo/export/list.do";
     */
    @RequestMapping("/exportE")
    public String exportE(String id){

        //1）把上报数据提交到海关平台
        //1.1 创建ExportVo 对象
        ExportVo exportVo = new ExportVo();

        //1.2 封装ExportVo 数据（来源：查询报运单信息，查询报运商品数据）
        // =====  查询报运单信息 ======
        //1.2.1 查询当前报运单
        Export export = exportService.findById(id);
        //1.2.2 拷贝数据到ExportVo
        BeanUtils.copyProperties(export,exportVo);
        //1.2.3 设置ExportVo的ID
        exportVo.setId(UUID.randomUUID().toString());
        //1.2.4 设置报运单ID
        exportVo.setExportId(export.getId());

        // =====  查询报运商品信息 ======
        ExportProductExample epExample = new ExportProductExample();
        epExample.createCriteria().andExportIdEqualTo(export.getId());
        List<ExportProduct> exportProducts = exportProductService.findAll(epExample);

        //1.2.5 把查询的报运商品封装奥ExportVo的products集合中
        if(exportProducts!=null && exportProducts.size()>0){
            for(ExportProduct exportProduct:exportProducts){
                //1）创建ExportProductVo对象
                ExportProductVo exportProductVo = new ExportProductVo();
                //2）拷贝数据到ExportProductVo对象
                BeanUtils.copyProperties(exportProduct,exportProductVo);
                //3）设置记录的ID
                exportProductVo.setId(UUID.randomUUID().toString());
                //4)设置报运商品ID
                exportProductVo.setExportProductId(exportProduct.getId());
                //5)设置ExportVo的ID
                exportProductVo.setExportId(exportVo.getId());

                exportVo.getProducts().add(exportProductVo);
            }
        }

        //1.3 使用WebClient方法远程调用海关平台（exportE），传递ExportVo 对象
        WebClient.create("http://127.0.0.1:8082/ws/export/user")
                .post(exportVo);

        //2）远程调用海关平台获取报运结果，更新表数据
        //2.1 远程调用海关平台获取报运结果：ExportResult对象
        ExportResult exportResult = WebClient.create("http://127.0.0.1:8082/ws/export/user/"+exportVo.getId())
                .get(ExportResult.class);

        //2.2 如果报运状态为2,（成功），更新报运单表的状态改为2，更新报运商品表的tax税额
        exportService.updateExport(exportResult);

        return "redirect:/cargo/export/list.do";
    }


}
