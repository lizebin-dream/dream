package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 附件业务实现
 */
@Service // 必须导入dubbo的注解
public class ExtCproductServiceImpl implements ExtCproductService{

    //注入dao对象
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;

    /**
     * 分页查询
     * @param extCproductExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<ExtCproduct>(extCproductDao.selectByExample(extCproductExample));
    }

    /**
            * 查询所有
     * @param extCproductExample
     * @return
             */
    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 添加附件
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        //0.设置ID
        extCproduct.setId(UUID.randomUUID().toString());

        //1.计算当前附件的总价（数量cnumber*单价price）
        Double amount = 0D;
        if(extCproduct.getCnumber()!=null && extCproduct.getPrice()!=null){
            amount = extCproduct.getCnumber()*extCproduct.getPrice();
        }
        extCproduct.setAmount(amount);


        //2.往附件表插入一条记录
        extCproductDao.insertSelective(extCproduct);

        //3.计算购销合同的总价（新合同总价=合同原总价+附件总价）
        //3.1 查询合同数据
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+amount);

        //4.计算购销合同的附件数量（附件数量=原数量+1）
        contract.setExtNum(contract.getExtNum()+1);

        //5.更新购销合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改附件
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {

        //1.计算当前附件的总价（数量cnumber*单价price）
        Double amount = 0D;
        if(extCproduct.getCnumber()!=null && extCproduct.getPrice()!=null){
            amount = extCproduct.getCnumber()*extCproduct.getPrice();
        }
        extCproduct.setAmount(amount);

        //2.获取原附件的总价
        ExtCproduct dbExtCproduct = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldAmount = dbExtCproduct.getAmount();

        //3.修改附件表的一条记录
        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        //4.计算购销合同的总价（新合同总价=合同原总价-原附件总价+新附件总价）
        //4.1 查询合同数据
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-oldAmount+amount);

        //5.更新购销合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        //1.查询货物数据
        ExtCproduct dbExtCproduct = extCproductDao.selectByPrimaryKey(id);

        //2.删除附件表的一条记录
        extCproductDao.deleteByPrimaryKey(id);

       //3.计算购销合同的总价（新合同总价=合同原总价-附件的总价）

        //3.1 查询合同数据
        Contract contract = contractDao.selectByPrimaryKey(dbExtCproduct.getContractId());
        if(dbExtCproduct.getAmount()!=null) {
            contract.setTotalAmount(contract.getTotalAmount() - dbExtCproduct.getAmount());
        }
        //4.计算购销合同下附件数量（附件数量=原数量-1）
        contract.setExtNum(contract.getExtNum()-1);

       //4.更新购销合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }


}
