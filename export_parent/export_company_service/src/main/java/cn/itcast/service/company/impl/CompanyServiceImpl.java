package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 企业service实现
 */
@Service  // 注意：必须换成dubbo的@Service注解
public class CompanyServiceImpl implements CompanyService{
    //注入dao对象
    @Autowired
    private CompanyDao companyDao;


    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    /**
     * 添加企业
     * @param company 企业对象
     */
    @Override
    public void save(Company company) {
        //1.自行生成主键值，进行赋值
        company.setId(UUID.randomUUID().toString());
        //2.保存数据
        companyDao.save(company);
    }

    /**
     * 更新企业
     * @param company 企业对象
     */
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    /**
     * 根据id查询企业
     * @param id
     * @return
     */
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    /**
     * 删除企业
     * @param id
     */
    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    /**
     * 分页查询企业
     *   pageNum: 当前页码
     *   pagesize： 页面大小
     */
    @Override
    public PageInfo<Company> findByPage(int pageNum, int pagesize) {
        //1.设置当前页码，页面大小
        PageHelper.startPage(pageNum,pagesize);

        //2.查询所有数据
        List<Company> list = companyDao.findAll();

        //3.封装PageInfo分页结果
        PageInfo<Company> pageInfo = new PageInfo<>(list);

        //4.返回数据
        return pageInfo;
    }
}
