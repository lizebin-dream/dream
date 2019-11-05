package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProductVo;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.DownloadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 导出出货表数据控制器
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController{

    @Reference
    private ContractProductService contractProductService;


    /**
     * 进入出货表导出页面
     *   方法请求地址：http://localhost:8080/cargo/contract/print.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/print/contract-print.jsp
     */
    @RequestMapping("/print")
    public String print(){
        return "cargo/print/contract-print";
    }

    /**
     *  出货表导出： 方式一：传统方式
     *     方法请求地址：http://localhost:8080/cargo/contract/printExcel.do
     *    方法的参数：inputDate   船期
     *    方法的返回值/跳转的页面：无
     *
     *//*
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws IOException {

        //1.创建工作簿
        Workbook wb = new XSSFWorkbook();

        //2.创建工作单（出货表）
        Sheet sheet = wb.createSheet("出货表");

        //合并单元格: addMergedRegion
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        //设置每列的列宽
        *//**
         * 参数一：列索引
         * 参数二：列数值
         *//*
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,11*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,11*256);
        sheet.setColumnWidth(5,11*256);
        sheet.setColumnWidth(6,11*256);
        sheet.setColumnWidth(7,11*256);
        sheet.setColumnWidth(8,11*256);

        //3.创建标题行（第一行）
        Row row = sheet.createRow(0);
        //设置行高
        row.setHeightInPoints(36);
        //3.1 设置第二列
        Cell cell = row.createCell(1);
        //3.2 设置列的标题
        //inputData  2019-01 或 2018-10
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        //给列设置样式
        cell.setCellStyle(this.bigTitle(wb));
        //设置列的内容
        cell.setCellValue(title);


        //4.创建表头行（第二行）
        //客户	订单号	货号	数量	工厂	工厂交期	船期	贸易条款
        String[] headers = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        //4.1 创建第二行
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        int index = 1;
        for(String header:headers){
            cell = row.createCell(index);
            //设置列的样式
            cell.setCellStyle(this.title(wb));
            //设置列值
            cell.setCellValue(headers[index-1]);
            index++;
        }

        //5.创建内容行（第三行以后）
        //5.1 查询需要导出的数据
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(),inputDate);

        //5.2 把数据写成行
        if(list!=null && list.size()>0){

            index = 2;
            for(ContractProductVo contractProductVo:list){
                //5.3 创建内容行
                row = sheet.createRow(index++);
                //设置行高
                row.setHeightInPoints(24);

                //客户	订单号	货号	数量	工厂	工厂交期	船期	贸易条款
                //5.4 创建列
                cell = row.createCell(1);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getCustomName()!=null) {
                    cell.setCellValue(contractProductVo.getCustomName());
                }else{
                    cell.setCellValue("");
                }
                //5.4 创建列
                cell = row.createCell(2);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getContractNo()!=null) {
                    cell.setCellValue(contractProductVo.getContractNo());
                }else{
                    cell.setCellValue("");
                }
                //5.4 创建列
                cell = row.createCell(3);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getProductNo()!=null) {
                    cell.setCellValue(contractProductVo.getProductNo());
                }else{
                    cell.setCellValue("");
                }


                //5.4 创建列
                cell = row.createCell(4);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getCnumber()!=null) {
                    cell.setCellValue(contractProductVo.getCnumber());
                }else{
                    cell.setCellValue(0);
                }

                //5.4 创建列
                cell = row.createCell(5);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getFactoryName()!=null) {
                    cell.setCellValue(contractProductVo.getFactoryName());
                }else{
                    cell.setCellValue("");
                }

                cell = row.createCell(6);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getDeliveryPeriod()!=null) {
                    cell.setCellValue(contractProductVo.getDeliveryPeriod());
                }else{
                    cell.setCellValue("");
                }

                cell = row.createCell(7);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getShipTime()!=null) {
                    cell.setCellValue(contractProductVo.getShipTime());
                }else{
                    cell.setCellValue("");
                }

                cell = row.createCell(8);
                cell.setCellStyle(this.text(wb));
                if(contractProductVo.getTradeTerms()!=null) {
                    cell.setCellValue(contractProductVo.getTradeTerms());
                }else{
                    cell.setCellValue("");
                }
            }

        }

        //6.把工作簿下载到本地（输出到response） （等于文件下载）
        DownloadUtil downloadUtil = new DownloadUtil();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //6.1 把工作簿的数据写入到字节流
        wb.write(bos);
        //6.2 下载文件
        downloadUtil.download(bos,response,"出货表.xlsx");
    }*/


    /**
     *  出货表导出： 方式二：模板方式
     *     方法请求地址：http://localhost:8080/cargo/contract/printExcel.do
     *    方法的参数：inputDate   船期
     *    方法的返回值/跳转的页面：无
     *
     *//*
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws IOException {

        //1.创建工作簿
        //1.1 读取项目下的模块文件
        InputStream in = session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");
        Workbook wb = new XSSFWorkbook(in);

        //2.获取工作单（出货表）
        Sheet sheet = wb.getSheetAt(0);


        //3.获取标题行（第一行）
        Row row = sheet.getRow(0);
        //3.1 修改标题的内容
        Cell cell = row.getCell(1);
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        cell.setCellValue(title);


        //4.创建内容行（第三行以后）
        //4.1 查询需要导出的数据
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(),inputDate);

        //4.2 获取到现有内容行的所有列的样式
        CellStyle[] cellStyles = new CellStyle[8];
        row = sheet.getRow(2);
        for(int i=1;i<=8;i++){
            cellStyles[i-1] =  row.getCell(i).getCellStyle();
        }

        //4.3 把数据写成行
        if(list!=null && list.size()>0){

            int index = 2;
            for(ContractProductVo contractProductVo:list){

                for(int i=1;i<=50000;i++) {
                    //5.3 创建内容行
                    row = sheet.createRow(index++);


                    //客户	订单号	货号	数量	工厂	工厂交期	船期	贸易条款
                    //5.4 创建列
                    cell = row.createCell(1);
                    cell.setCellStyle(cellStyles[0]);
                    if (contractProductVo.getCustomName() != null) {
                        cell.setCellValue(contractProductVo.getCustomName());
                    } else {
                        cell.setCellValue("");
                    }
                    //5.4 创建列
                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyles[1]);
                    if (contractProductVo.getContractNo() != null) {
                        cell.setCellValue(contractProductVo.getContractNo());
                    } else {
                        cell.setCellValue("");
                    }
                    //5.4 创建列
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyles[2]);
                    if (contractProductVo.getProductNo() != null) {
                        cell.setCellValue(contractProductVo.getProductNo());
                    } else {
                        cell.setCellValue("");
                    }


                    //5.4 创建列
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyles[3]);
                    if (contractProductVo.getCnumber() != null) {
                        cell.setCellValue(contractProductVo.getCnumber());
                    } else {
                        cell.setCellValue(0);
                    }

                    //5.4 创建列
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyles[4]);
                    if (contractProductVo.getFactoryName() != null) {
                        cell.setCellValue(contractProductVo.getFactoryName());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyles[5]);
                    if (contractProductVo.getDeliveryPeriod() != null) {
                        cell.setCellValue(contractProductVo.getDeliveryPeriod());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyles[6]);
                    if (contractProductVo.getShipTime() != null) {
                        cell.setCellValue(contractProductVo.getShipTime());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyles[7]);
                    if (contractProductVo.getTradeTerms() != null) {
                        cell.setCellValue(contractProductVo.getTradeTerms());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

        }

        //6.把工作簿下载到本地（输出到response） （等于文件下载）
        DownloadUtil downloadUtil = new DownloadUtil();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //6.1 把工作簿的数据写入到字节流
        wb.write(bos);
        //6.2 下载文件
        downloadUtil.download(bos,response,"出货表.xlsx");
    }*/

    /**
     *  出货表导出： 方式三：百万数据导入（只能支持传统方式导出，不支持模板方法）
     *     方法请求地址：http://localhost:8080/cargo/contract/printExcel.do
     *    方法的参数：inputDate   船期
     *    方法的返回值/跳转的页面：无
     *
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws IOException {

        //1.创建工作簿
        Workbook wb = new SXSSFWorkbook();

        //2.创建工作单（出货表）
        Sheet sheet = wb.createSheet("出货表");

        //合并单元格: addMergedRegion
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        //设置每列的列宽
        /**
         * 参数一：列索引
                * 参数二：列数值
                */
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,11*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,11*256);
        sheet.setColumnWidth(5,11*256);
        sheet.setColumnWidth(6,11*256);
        sheet.setColumnWidth(7,11*256);
        sheet.setColumnWidth(8,11*256);

        //3.创建标题行（第一行）
        Row row = sheet.createRow(0);
        //设置行高
        row.setHeightInPoints(36);
        //3.1 设置第二列
        Cell cell = row.createCell(1);
        //3.2 设置列的标题
        //inputData  2019-01 或 2018-10
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        //给列设置样式
        cell.setCellStyle(this.bigTitle(wb));
        //设置列的内容
        cell.setCellValue(title);


        //4.创建表头行（第二行）
        //客户	订单号	货号	数量	工厂	工厂交期	船期	贸易条款
        String[] headers = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        //4.1 创建第二行
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        int index = 1;
        for(String header:headers){
            cell = row.createCell(index);
            //设置列的样式
            cell.setCellStyle(this.title(wb));
            //设置列值
            cell.setCellValue(headers[index-1]);
            index++;
        }

        //5.创建内容行（第三行以后）
        //5.1 查询需要导出的数据
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(),inputDate);

        //5.2 把数据写成行
        if(list!=null && list.size()>0){

            index = 2;
            for(ContractProductVo contractProductVo:list){

                for(int i=1;i<=50000;i++) {
                    //5.3 创建内容行
                    row = sheet.createRow(index++);
                    //设置行高
                    row.setHeightInPoints(24);

                    //客户	订单号	货号	数量	工厂	工厂交期	船期	贸易条款
                    //5.4 创建列
                    cell = row.createCell(1);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getCustomName() != null) {
                        cell.setCellValue(contractProductVo.getCustomName());
                    } else {
                        cell.setCellValue("");
                    }
                    //5.4 创建列
                    cell = row.createCell(2);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getContractNo() != null) {
                        cell.setCellValue(contractProductVo.getContractNo());
                    } else {
                        cell.setCellValue("");
                    }
                    //5.4 创建列
                    cell = row.createCell(3);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getProductNo() != null) {
                        cell.setCellValue(contractProductVo.getProductNo());
                    } else {
                        cell.setCellValue("");
                    }


                    //5.4 创建列
                    cell = row.createCell(4);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getCnumber() != null) {
                        cell.setCellValue(contractProductVo.getCnumber());
                    } else {
                        cell.setCellValue(0);
                    }

                    //5.4 创建列
                    cell = row.createCell(5);
                   //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getFactoryName() != null) {
                        cell.setCellValue(contractProductVo.getFactoryName());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(6);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getDeliveryPeriod() != null) {
                        cell.setCellValue(contractProductVo.getDeliveryPeriod());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(7);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getShipTime() != null) {
                        cell.setCellValue(contractProductVo.getShipTime());
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(8);
                    //cell.setCellStyle(this.text(wb));
                    if (contractProductVo.getTradeTerms() != null) {
                        cell.setCellValue(contractProductVo.getTradeTerms());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

        }

        //6.把工作簿下载到本地（输出到response） （等于文件下载）
        DownloadUtil downloadUtil = new DownloadUtil();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //6.1 把工作簿的数据写入到字节流
        wb.write(bos);
        //6.2 下载文件
        downloadUtil.download(bos,response,"出货表.xlsx");
    }

    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }

}
