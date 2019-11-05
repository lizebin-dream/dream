package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *系统日志service接口
 */
public interface SysLogService {

    /**
     * 分页查询系统日志
     */
    public PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize);
    

    /**
     * 添加
     * @param sysLog
     */
    void save(SysLog sysLog);
}
