package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.SysLogDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 系统日志service实现
 */
@Service
public class SysLogServiceImpl implements SysLogService{

    //注入dao对象
    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.查询全部数据
        List<SysLog> list = sysLogDao.findAll(companyId);

        //3.封装PageInfo
        PageInfo<SysLog> pageInfo = new PageInfo<>(list);

        //4.返回
        return pageInfo;
    }

   

    /**
     * 添加
     * @param sysLog
     */
    @Override
    public void save(SysLog sysLog) {
        //1.生成ID
        sysLog.setId(UUID.randomUUID().toString());
        //2.保存数据
        sysLogDao.save(sysLog);
    }

}
