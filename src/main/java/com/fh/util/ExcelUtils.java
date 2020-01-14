package com.fh.util;

import com.fh.common.annotaction.ExcelFild;
import com.fh.common.annotaction.ExcleHeard;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExcelUtils {
        /*关于excel了解几个对象(sheet页，row行，cell列)*/
    public static  void execelUtil(List list, HttpServletResponse response) {
        /*处理标题*/
        Object o = list.get(0);/*获取要下载的对象*/
        Class aClass = o.getClass()/*通过java反射（基于对象）获得类对象*/;
        /*通过注解获取sheet页的值*/
        ExcleHeard annotation = (ExcleHeard) aClass.getAnnotation(ExcleHeard.class);
        /*获取sheet值*/
        String title = annotation.title();
        /*创建workbook操作对象*/
        XSSFWorkbook workbook = new XSSFWorkbook();
        /*创建Sheet页*/    /*将获取到的title值赋给sheet页*/
        XSSFSheet sheet = workbook.createSheet(title);
        /*创建第一行*/
        XSSFRow row = sheet.createRow(0);
        /*处理列头，比如（学生姓名，年龄，家庭住址等标识）*/
        /*利用java反射获取类的所有属性*/
        Field[] declaredFields = aClass.getDeclaredFields();/*getDeclaredField(属性名)获取指定属性*/
        /*定义个计数器，以便值和标题对应*/
        int celName = 0;
        /*循环所有属性*/
        for (int i = 0; i < declaredFields.length; i++) {
            /*获取具体的一个属性*/
            Field field = declaredFields[i];
            /*判断该属性是否是需要导出的数据（利用java反射判断属性上是否有注解）*/
            ExcelFild annotation1 = field.getAnnotation(ExcelFild.class);
            /*如果有就是要导出的数据*/
            if (annotation1 != null) {
                /*获取注解上面的name值*/
                String name = annotation1.name();
                /*创建cell*/
                XSSFCell cell = row.createCell(celName);
                /*将name值赋到cell上*/
                cell.setCellValue(name);
                /*计数器计数*/
                celName++;
            }
        }
        /*处理数据,循环数据*/
        for (int i = 0; i < list.size(); i++) {
            /*获取具体的一个数据*/
            Object o1 = list.get(i);
            /*创建行，因为第一行是列头所以要i+1*/
            XSSFRow row1 = sheet.createRow(i + 1);
            /*定义计数器，效果和上面那个一样*/
            int cellcount = 0;
            /*循环所有属性*/
            for (int j = 0; j < declaredFields.length; j++) {
                /*获取具体的一个属性*/
                Field field = declaredFields[j];
                /*获取属性上面的注解*/
                boolean annotation1 = field.isAnnotationPresent(ExcelFild.class);
                /*判断属性上面是否有我们所需要的注解（如果有就是需要导出的属性，没有则反之）*/
                if (annotation1 == true) {
                    /*创建cell单元格*/
                    XSSFCell cell = row1.createCell(cellcount);
                    try {
                        /*利用java反射获取属性值*/
                        /*暴力访问私有属性*/
                        field.setAccessible(true);
                        Object o2 = field.get(o1);
                        /*判断属性值是否为空（数据库数据的字段值）*/
                        if (o2 != null) {
                            /*如果数据值不为空，利用java反射获取数据值的类型*/
                            Class type = field.getType();
                            /*处理日期格式的数据类型*/
                            if (type == Date.class) {
                                /*设置日期类型的输出格式*/
                                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                                /*将数据强转成date类型*/
                                Date date = (Date) o2;
                                /*将date类型转为指定输出格式的字符串类型*/
                                String format = sim.format(date);
                                /*赋值*/
                                cell.setCellValue(format);
                            } else {
                                /*如果数据类型不是日期类型，就不做处理，一律按字符串形式输出*/
                                cell.setCellValue(o2.toString());
                            }
                        } else {
                            /*如果数据值为空，就赋一个空字符串*/
                            cell.setCellValue("");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    cellcount++;
                }

            }
        }
        /*下载*/
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + UUID.randomUUID().toString() + ".xlsx\"");

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
