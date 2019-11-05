package cn.itcast.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类型转换器
 *  泛型一：源类型
 *  泛型二：目标类型
 */
public class StringToDateConverter implements Converter<String,Date>{
    @Override
    public Date convert(String s) {
        Date date = null;

        try {
            date =  new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
