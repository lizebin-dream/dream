package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 模块service实现
 */
@Service
public class ModuleServiceImpl implements ModuleService{

    //注入dao对象
    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.查询全部数据
        List<Module> list = moduleDao.findAll();

        //3.封装PageInfo
        PageInfo<Module> pageInfo = new PageInfo<>(list);

        //4.返回
        return pageInfo;
    }

    /**
     * 查询指定企业的所有模块
     * @return
     */
    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    /**
     * 添加
     * @param module
     */
    @Override
    public void save(Module module) {
        //1.生成ID
        module.setId(UUID.randomUUID().toString());
        //2.保存数据
        moduleDao.save(module);
    }

    /**
     * 更新
     * @param module
     */
    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    /**
     * 根据id查询模块
     * @param id
     * @return
     */
    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    /**
     * 删除模块
     * @param id
     * @return
     */
    @Override
    public void delete(String id) {
        //这里补充模块有没有其他关联数据，有的话，提示用户

        moduleDao.delete(id);
    }

    @Override
    public List<Module> findRoleModuleByRoleId(String roleid) {
        return moduleDao.findRoleModuleByRoleId(roleid);
    }
}
