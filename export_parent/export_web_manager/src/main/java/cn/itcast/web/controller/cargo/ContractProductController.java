package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ContractService;
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

import java.io.IOException;
import java.util.List;

/**
 * 货物
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController{

    /**
     * 远程获取合同的业务对象
     */
    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;


    /**
     * 分页展示货物
     *   方法请求地址：http://localhost:8080/cargo/contractProduct/list.do
     *   方法的参数：contractId=1&pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String list(String contractId,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){

        //1.查询货物的生产厂家
        //1.1 创建Example对象
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物"); //ctype='货物'

        //1.2 查询厂家列表
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //1.3 存入request
        request.setAttribute("factoryList",factoryList);

        //2.分页查询指定购销合同下所有货物
        //2.1 创建ContractProductExample对象
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdEqualTo(contractId);

        //2.2 查询货物列表
        PageInfo<ContractProduct> pageInfo = contractProductService.findByPage(contractProductExample,pageNum,pageSize);

        //2.3 存入request
        request.setAttribute("pageInfo",pageInfo);

        //补充contractId，为了分页的时候重新把contractId传递回来
        request.setAttribute("contractId",contractId);

        //3.返回页面
        return "cargo/product/product-list";
    }


    /**
     * 保存货物信息（添加、更新）
     *   方法请求地址：http://localhost:8080/cargo/contractProduct/edit.do
     *   方法的参数：ContractProduct对象
     *   方法的返回值/跳转的页面：重定向到部门列表（redirect:/cargo/contractProduct/list.do?contractId=1）
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Contract对象中
        contractProduct.setCompanyId(companyId);
        contractProduct.setCompanyName(compayName);

        //1.判断是否存在ID
        if(StringUtil.isEmpty(contractProduct.getId())){
            //调用七牛工具类，保存货物图片
            String imgUrl = fileUploadUtil.upload(productPhoto);
            //设置图片路径
            contractProduct.setProductImage("http://"+imgUrl);

            //1.1 不存在，添加
            contractProductService.save(contractProduct);
        }else{
            //1.2 存在，更新
            contractProductService.update(contractProduct);
        }

        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }


    /**
     * 进入修改货物页面
     *    方法请求地址：http://localhost:8080/cargo/contractProduct/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/product/product-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.查询货物
        ContractProduct contractProduct = contractProductService.findById(id);
        //2.存入request
        request.setAttribute("contractProduct",contractProduct);

        //3.查询货物的生产厂家
        //3.1 创建Example对象
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物"); //ctype='货物'

        //3.2 查询厂家列表
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //3.3 存入request
        request.setAttribute("factoryList",factoryList);
        return "cargo/product/product-update";
    }

    /**
     * 删除货物
     *   方法请求地址：http://localhost:8080/cargo/contractProduct/delete.do
     *   方法的参数：id=1&contractId=2
     *   方法的返回值/跳转的页面：重定向到部门列表（redirect:/cargo/contractProduct/list.do?contractId=1）
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId){
        //1.调用业务
        contractProductService.delete(id);
        //2.返回列表
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 进入批量导入页面
     *    方法请求地址：http://localhost:8080/cargo/contractProduct/toImport.do
     *   方法的参数：contractId=2
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/product/product-import.jsp
     *
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId){
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    /**
     * 上传货物
     *    方法请求地址：http://localhost:8080/cargo/contractProduct/import.do
     *   方法的参数：
     *       1）普通参数：contractId=2
     *       2）文件参数：file   (用户选择的Excel文件)
     *   方法的返回值/跳转的页面：redirect:/cargo/contractProduct/list.do?contractId=1
     */
    @RequestMapping("/import")
    public String productImport(String contractId,MultipartFile file) throws Exception {

        //1.创建工作簿
        //file.getInputStream(): 用户上传的文件输入流
        Workbook wb = new XSSFWorkbook(file.getInputStream());

        //2.获取第一个工作单: getSheetAt(0)
        Sheet sheet = wb.getSheetAt(0);

        //3.获取内容行（从第二行开始的）
        //3.1 获取工作单有内容的行数: getPhysicalNumberOfRows
        int totalRows = sheet.getPhysicalNumberOfRows();

        //4.遍历每一行
        for(int i=1;i<totalRows;i++){
            //获取该行对象
            Row row = sheet.getRow(i);

            //4.1 创建ContractProduct对象
            ContractProduct contractProduct = new ContractProduct();

            //4.2 把每一行的每列的内容分别封装到ContractProduct对象
            //生产厂家
            if(row.getCell(1)!=null){
                contractProduct.setFactoryName(row.getCell(1).getStringCellValue());
            }

            // 	货号
            if(row.getCell(2)!=null){
                contractProduct.setProductNo(row.getCell(2).getStringCellValue());
            }

            // 数量
            if(row.getCell(3)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setCnumber((int)row.getCell(3).getNumericCellValue());
            }

            // 包装单位(PCS/SETS)
            if(row.getCell(4)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setPackingUnit(row.getCell(4).getStringCellValue());
            }

            // 装率
            if(row.getCell(5)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setLoadingRate(row.getCell(5).getNumericCellValue()+"");
            }

            // 箱数
            if(row.getCell(6)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setBoxNum((int)row.getCell(6).getNumericCellValue());
            }

            // 单价
            if(row.getCell(7)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setPrice(row.getCell(7).getNumericCellValue());
            }
            // 	货物描述
            if(row.getCell(8)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setProductDesc(row.getCell(8).getStringCellValue());
            }

            //要求
            if(row.getCell(9)!=null){
                //getNumericCellValue:获取数字类型的列值
                contractProduct.setProductRequest(row.getCell(9).getStringCellValue());
            }

            //补充货物的其他信息
            //1.工厂ID
            Factory factory = factoryService.findByFactoryName(contractProduct.getFactoryName());
            if(factory!=null) {
                contractProduct.setFactoryId(factory.getId());
            }
            //2.企业信息
            contractProduct.setCompanyId(getLoginCompanyId());
            contractProduct.setCompanyName(getLoginCompanyName());
            //3.计算货物总价
            contractProduct.setAmount(contractProduct.getCnumber()*contractProduct.getPrice());
            //4.设置合同ID
            contractProduct.setContractId(contractId);


            //4.3 调用业务层，保存ContractProduct对象
            contractProductService.save(contractProduct);
        }

        //5.返回货物列表页面
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
}
