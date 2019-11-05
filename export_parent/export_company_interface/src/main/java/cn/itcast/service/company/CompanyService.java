package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 企业service接口
 */
public interface CompanyService {

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

    /**
     * 分页查询企业
     *   pageNum: 当前页码
     *   pagesize： 页面大小
     */
    public PageInfo<Company> findByPage(int pageNum, int pagesize);
}
