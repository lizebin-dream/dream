package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 货物业务实现
 */
@Service // 必须导入dubbo的注解
public class ContractProductServiceImpl implements ContractProductService{

    //注入dao对象
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao; //附件的dao

    /**
     * 分页查询
     * @param contractProductExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<ContractProduct>(contractProductDao.selectByExample(contractProductExample));
    }

    /**
            * 查询所有
     * @param contractProductExample
     * @return
             */
    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    /**
     * 添加货物
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        //0.设置ID
        contractProduct.setId(UUID.randomUUID().toString());

        //1.计算当前货物的总价（数量cnumber*单价price）
        Double amount = 0D;
        if(contractProduct.getCnumber()!=null && contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        //1.1 修改货物总价
        contractProduct.setAmount(amount);

        //2.往货物表插入一条记录
        contractProductDao.insertSelective(contractProduct);

        //3.计算购销合同的总价（新合同总价=合同原总价+货物总价）
        //3.1 根据合同ID查询合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount( contract.getTotalAmount()+ amount ); //新合同总价=合同原总价+货物总价

        //4.计算购销合同的货物数量（货物数量=原数量+1）
        contract.setProNum(contract.getProNum()+1);

        //5.更新购销合同表
       contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改货物
     * @param contractProduct
     */
    @Override
    public void update(ContractProduct contractProduct) {

        //1.计算当前货物的总价（数量cnumber*单价price）
        Double amount = 0D;
        if(contractProduct.getCnumber()!=null && contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        //1.1 修改货物总价
        contractProduct.setAmount(amount);

        //1.2 查询原货物总价
        ContractProduct dbContractProduct = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAmout = dbContractProduct.getAmount();

        //2.修改货物表的一条记录
        contractProductDao.updateByPrimaryKeySelective(contractProduct);

        //3.计算购销合同的总价（新合同总价=合同原总价-原货物总价+新货物总价）
        //3.1 根据合同ID查询合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-oldAmout+amount);

        //4.更新购销合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {

        //1.先查询该货物下的所有附件，计算所有附件的总价，计算附件总数
        //1.1 创建ExtcProductExample对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);// 根据货物ID查询附件
        //1.2 查询指定货物下的附件
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //1.3 计算所有附件的总价，计算附件总数
        //所有附件的总价
        Double allExtAmout = 0D;
        Integer totalExt = 0;
        if(extCproducts!=null && extCproducts.size()>0) {
            for (ExtCproduct extCproduct : extCproducts) {
                //每个附件的总价
                allExtAmout += extCproduct.getAmount();

                //2.删除该货物下的每个附件数据
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
            //计算附件总数
            totalExt = extCproducts.size();
        }

        //1.4 先根据货物ID查询货物对象
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);

        //3.删除货物表的一条记录
        contractProductDao.deleteByPrimaryKey(id);

        //4.计算购销合同的总价（新合同总价=合同原总价-货物总价-所有附件的总价）
        //4.1 根据合同ID查询合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //4.2 计算购销新合同的总价（新合同总价=合同原总价-货物总价-所有附件的总价）
        contract.setTotalAmount(contract.getTotalAmount()-contractProduct.getAmount()-allExtAmout);

        //5.计算购销合同的货物数量（货物数量=原数量-1）
        contract.setProNum(contract.getProNum()-1);

       //6.计算购销合同下附件数量（附件数量=原数量-附件总数）
        contract.setExtNum(contract.getExtNum()-totalExt);

       //7.更新购销合同表
       contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public List<ContractProductVo> findByShipTime(String companyId, String shipTime) {
        return contractProductDao.findByShipTime(companyId,shipTime);
    }


}
