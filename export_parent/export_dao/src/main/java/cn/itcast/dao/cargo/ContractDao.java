package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.vo.ContractTaskVo;

import java.util.List;

public interface ContractDao {

	//删除
    int deleteByPrimaryKey(String id);

	//保存
    int insertSelective(Contract record);

	//条件查询
    List<Contract> selectByExample(ContractExample example);

	//id查询
    Contract selectByPrimaryKey(String id);

	//更新
    int updateByPrimaryKeySelective(Contract record);

    //根据部门ID查询本部门及其子部门的购销合同
    List<Contract> selectByDeptId(String deptId);

    //查询还有几天到期的购销合同
    public List<ContractTaskVo> findByPeriod(Integer date);
}