package cn.itcast.poi;

import cn.itcast.domain.cargo.ContractProductVo;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Excel工作单事件处理器
 *    作用：当读到哪些行哪些列的时候怎样的处理？  -- 创建成ContractProductVo对象！！！
 *
 */
public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler{

    /**
     * 需求：
     * 1）每一行创建一个 vo对象
     * 2）当此行解析结束之后，打印vo对象
     */
    private ContractProductVo vo;

    /**
     * 读到行的开始
     * @param i 行号
     */
    @Override
    public void startRow(int i) {
        //从第3行开始才需要实例化vo对象
        if (i>=2) {
            vo = new ContractProductVo();
        }
    }

    /**
     * 读到行的结束
     * @param i
     */
    @Override
    public void endRow(int i) {
        System.out.println(vo);
        //保存vo对象到数据库表
    }

    /**
     * 当读到 当前行的每列的内容
     * @param xssfComment
     */
    @Override
    public void cell(String cellName, String cellValue, XSSFComment xssfComment) {
        //获取列的字母符合
        String name = cellName.substring(0,1);
        if(vo != null) {
            switch (name) {
                case "B" :{
                    vo.setCustomName(cellValue);
                    break;
                }
                case "C" :{
                    vo.setContractNo(cellValue);
                    break;
                }
                case "D" :{
                    vo.setProductNo(cellValue);
                    break;
                }
                case "E" :{
                    vo.setCnumber(Integer.parseInt(cellValue));
                    break;
                }
                case "F" :{
                    vo.setFactoryName(cellValue);
                    break;
                }
                case "G" :{
                    try {
                        vo.setDeliveryPeriod(new SimpleDateFormat("yyyy-MM-dd").parse(cellValue) );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "H" :{
                    try {
                        vo.setShipTime(new SimpleDateFormat("yyyy-MM-dd").parse(cellValue) );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "I" :{
                    vo.setTradeTerms(cellValue);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }
}
