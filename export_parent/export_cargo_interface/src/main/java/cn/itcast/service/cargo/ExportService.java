package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportExample;
import cn.itcast.vo.ExportResult;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface ExportService {

    Export findById(String id);

    List<Export> findAll(ExportExample example);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize);

    /**
     * 如果报运状态为2,（成功），更新报运单表的状态改为2，更新报运商品表的tax税额
     * @param exportResult
     */
    void updateExport(ExportResult exportResult);
}
