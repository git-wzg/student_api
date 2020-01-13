package com.fh.util;


import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ExceUtil {


    public static void exceleUtil(List list, List<String> arr, List<String> str, HttpServletResponse response, XSSFWorkbook book) {

        XSSFSheet sheet = book.createSheet("shuzhan");
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i <arr.size(); i++) {
            row.createCell(i).setCellValue(arr.get(i));
        }
        for (int i = 0; i <list.size() ; i++) {
            Object obj = list.get(i);
            XSSFRow row1 = sheet.createRow(i + 1);
            for (int j = 0; j <str.size() ; j++) {
                String fine = str.get(j);
                try {
                    Class aClass = obj.getClass();
                    Field fine1 = aClass.getDeclaredField(fine);
                    fine1.setAccessible(true);
                    Object name = fine1.get(obj);



                    if(name!=null){
                        row1.createCell(j).setCellValue(name+"");
                    }



                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\""+ UUID.randomUUID().toString()+".xlsx\"");

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            book.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    }

