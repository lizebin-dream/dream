package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;

import java.util.List;

/**
 * 企业dao接口
 */
public interface CompanyDao {

    /**
     * 查询所有
     */
    public List<Company> findAll();

    /**
     * 添加企业
     * @param company
     */
    void save(Company company);

    /**
     * 更新企业
     * @param company
     */
    void update(Company company);

    /**
     * 根据id查询企业
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 删除企业
     * @param id
     */
    void delete(String id);

}
