package cn.itcast.service.stat;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface StatService {

    public List<Map<String,Object>> getFactoryData(String companyId);

    public List<Map<String,Object>> getSellData(String companyId);

    public List<Map<String,Object>> getOnlineData(String companyId);
}
