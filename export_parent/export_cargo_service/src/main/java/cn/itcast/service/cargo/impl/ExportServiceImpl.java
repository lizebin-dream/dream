package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductResult;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 报运单业务实现
 */
@Service // 必须导入dubbo的注解
public class ExportServiceImpl implements ExportService{

    //注入dao对象
    @Autowired
    private ExportDao exportDao; //报运单dao
    @Autowired
    private ContractDao contractDao; //购销合同dao
    @Autowired
    private ContractProductDao contractProductDao;  // 购销合同的货物dao
    @Autowired
    private ExtCproductDao extCproductDao;// 购销合同的附件dao
    @Autowired
    private ExportProductDao exportProductDao;// 报运商品dao
    @Autowired
    private ExtEproductDao extEproductDao;//报运附件dao

    /**
     * 分页查询
     * @param exportExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Export> findByPage(ExportExample exportExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<Export>(exportDao.selectByExample(exportExample));
    }


    /**
     * 查询所有
     * @param exportExample
     * @return
     */
    @Override
    public List<Export> findAll(ExportExample exportExample) {
        return exportDao.selectByExample(exportExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param export
     */
    @Override
    public void save(Export export) {
        //1.保存报运单表数据
        //1.1 设置ID
        export.setId(UUID.randomUUID().toString());

        //1.2 录入时间
        export.setInputDate(new Date());

        //1.3 设置创建时间
        export.setCreateTime(new Date());
        //1.4 设置需要报运的所有合同ID（已经通过表单封装好啦）

        //1.5 设置所有合同号（使用空格分开）
        //1.5.1 通过合同ID查询合同号
        String contractIds = export.getContractIds();
        String[] array = contractIds.split(",");
        //1.5.2 定义一个字符串用于存储合同号
        String contractNos = "";
        for(String contracntId:array){
            Contract contract = contractDao.selectByPrimaryKey(contracntId);
            //1.5.3 拼接合同号
            contractNos += contract.getContractNo()+" ";

            //2.把报运单里面的所有购销合同的状态改为2
            //2.1 修改合同的状态
            contract.setState(2);
            //2.2 更新合同数据
            contractDao.updateByPrimaryKeySelective(contract);

        }
        //1.5.4 设置合同号
        export.setCustomerContract(contractNos);

        //1.6 设置报运单状态为0（草稿）
        export.setState(0);

        //1.7 设置报运的商品数量
        //1.7.1 根据购销合同查询其下货物
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(Arrays.asList(array)); // contract_id in (1,2)
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        if(contractProducts!=null) {
            export.setProNum(contractProducts.size());
        }
        //1.8 设置报运的附件数量
        //1.8.1 根据购销合同查询其下附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(Arrays.asList(array)); // contract_id in (1,2)
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        if(extCproducts!=null) {
            export.setExtNum(extCproducts.size());
        }

        exportDao.insertSelective(export);


        //3.创建报运单下的报运商品（数据来源：需要报运的购销合同下的所有货物（3条） 拷贝到 报运商品表里（3条））

        // 合同的商品ID -> 报运商品ID

        //用于存储合同商品ID和报运商品ID的Map集合
        Map<String,String> map = new HashMap<>();
        if(contractProducts!=null && contractProducts.size()>0){
            for(ContractProduct contractProduct:contractProducts){
                //3.1 创建报运商品对象
                ExportProduct exportProduct = new ExportProduct();

                //3.2 从购销合同的商品对象 拷贝 报运商品 对象
                /**
                 * 参数一：源对象
                 * 参数二：目标对象
                 */
                BeanUtils.copyProperties(contractProduct,exportProduct);

                //设置报运商品ID(不要和购销合同商品的ID冲突)
                exportProduct.setId(UUID.randomUUID().toString());
                //设置报运单ID
                exportProduct.setExportId(export.getId());

                //3.3 添加到报运商品表
                exportProductDao.insertSelective(exportProduct);

                //存入合同商品ID和报运商品ID
                map.put(contractProduct.getId(),exportProduct.getId());
            }
        }

        // 通过合同的附件  -> 查询合同的商品ID -> 报运附件ID
        //4.创建报运单下的报运附件（数据来源：需要报运的购销合同下的所有附件（2条） 拷贝到 报运附件表里面（2条））
        if(extCproducts!=null && extCproducts.size()>0){
            for(ExtCproduct extCproduct:extCproducts){
                //4.1 创建报运附件对象
                ExtEproduct extEproduct = new ExtEproduct();

                //4.2 拷贝数据
                BeanUtils.copyProperties(extCproduct,extEproduct);

                //设置ID
                extEproduct.setId(UUID.randomUUID().toString());

                //设置报运单ID
                extEproduct.setExportId(export.getId());

                //设置报运商品ID
                extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));

                //4.3 保存报运附件表数据
                extEproductDao.insertSelective(extEproduct);
            }
        }


    }

    /**
     * 修改
     * @param export
     */
    @Override
    public void update(Export export) {
        //1.更新报运单数据
        exportDao.updateByPrimaryKeySelective(export);

        //2.更新报运商品数据
        //2.1 获取页面传递的商品数据
        List<ExportProduct> exportProducts = export.getExportProducts();
        if(exportProducts!=null && exportProducts.size()>0){
            for(ExportProduct exportProduct:exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }

    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }


    @Override
    public void updateExport(ExportResult exportResult) {
        //如果报运状态为2,（成功），更新报运单表的状态改为2，更新报运商品表的tax税额
        if(exportResult.getState()==2){
            //1.1 更新报运单表的状态改为2
            //1.1.1 查询报运单
            Export export = exportDao.selectByPrimaryKey(exportResult.getExportId());
            //1.1.2 修改状态为2
            export.setState(2);
            //1.1.3 更新数据表
            exportDao.updateByPrimaryKeySelective(export);


            //1.2 更新报运商品表的tax税额
            //1.2.1 获取需要更新税额的商品
            if(exportResult.getProducts()!=null && exportResult.getProducts().size()>0){

                //1.2.2 遍历所有商品
                for(ExportProductResult exportProductResult:exportResult.getProducts()){
                    //1.2.3 查询需要更新的报运商品
                    ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(exportProductResult.getExportProductId());
                    //1.2.4 更新税额
                    exportProduct.setTax(exportProductResult.getTax());
                    //1.2.5 更新报运商品表数据
                    exportProductDao.updateByPrimaryKeySelective(exportProduct);
                }

            }
        }

    }
}
