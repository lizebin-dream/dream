package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractTaskVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 购销合同业务实现
 */
@Service // 必须导入dubbo的注解
public class ContractServiceImpl implements ContractService{

    //注入dao对象
    @Autowired
    private ContractDao contractDao;

    /**
     * 分页查询
     * @param contractExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<Contract>(contractDao.selectByExample(contractExample));
    }

    /**
     * 查询所有
     * @param contractExample
     * @return
     */
    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param contract
     */
    @Override
    public void save(Contract contract) {
        //1.设置ID
        contract.setId(UUID.randomUUID().toString());
        //2.设置创建时间和更新时间
        contract.setCreateTime(new Date());//创建时间
        contract.setUpdateTime(new Date());//更新时间
        //3.初始化货物数量和附件数量为0（为了后面方便进行计算修改）
        contract.setProNum(0);
        contract.setExtNum(0);
        //4.初始化合同总金额为0（为了后面方便进行计算修改）
        contract.setTotalAmount(0D);
        //5.设置合同的状态：  0：草稿 （取消）（可以修改）  1： 已提交 （准备报运，不能再修改）   2：已报运（报运成功）
        contract.setState(0);

        contractDao.insertSelective(contract);
    }

    /**
     * 修改
     * @param contract
     */
    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> findPageByDeptId(String deptId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Contract>(contractDao.selectByDeptId(deptId));
    }

    @Override
    public List<ContractTaskVo> findByPeriod(Integer date) {
        return contractDao.findByPeriod(date);
    }
}
