package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 角色service实现
 */
@Service
public class RoleServiceImpl implements RoleService{

    //注入dao对象
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.查询全部数据
        List<Role> list = roleDao.findAll(companyId);

        //3.封装PageInfo
        PageInfo<Role> pageInfo = new PageInfo<>(list);

        //4.返回
        return pageInfo;
    }

    /**
     * 查询指定企业的所有角色
     * @param companyId 企业ID
     * @return
     */
    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    /**
     * 添加
     * @param role
     */
    @Override
    public void save(Role role) {
        //1.生成ID
        role.setId(UUID.randomUUID().toString());
        //2.保存数据
        roleDao.save(role);
    }

    /**
     * 更新
     * @param role
     */
    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public void delete(String id) {
        //这里需要补充判断是否存在角色关系数据，如果有的话，需要提示用户


        roleDao.delete(id);
    }

    @Override
    public void updateRoleModule(String roleid, String moduleIds) {

        /**
         * -- 角色分配权限
         -- 1.先删除该角色之前分配过的模块 -- 10
         DELETE FROM pe_role_module WHERE role_id = '10'
         -- 2.给该角色插入新的模块
         INSERT INTO pe_role_module(role_id,module_id) VALUES('10','1')
         INSERT INTO pe_role_module(role_id,module_id) VALUES('10','2')
         INSERT INTO pe_role_module(role_id,module_id) VALUES('10','3')
         ......
         */
        //1.先删除该角色之前分配过的模块
        roleDao.deleteRoleModuleByRoleId(roleid);

        //2.给该角色插入新的模块
        if(moduleIds!=null && !moduleIds.equals("")){
            //2.1 切割出模块ID
            String[] array = moduleIds.split(",");

            //2.2 遍历数组
            for(String moduleId:array){
                //2.3 添加角色模块数据
                roleDao.saveRoleMoudle(roleid,moduleId);
            }
        }

    }
}
