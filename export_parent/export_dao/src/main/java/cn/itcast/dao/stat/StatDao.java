package cn.itcast.dao.stat;

import java.util.List;
import java.util.Map;

/**
 * 统计模块的Dao
 */
public interface StatDao {

    //统计生产商家销售金额
    public List<Map<String,Object>> getFactoryData(String companyId);

    //产品销售量要求按前5名统计
    public List<Map<String,Object>> getSellData(String companyId);

    //每小时统计访问人数
    public List<Map<String,Object>> getOnlineData(String companyId);
}
