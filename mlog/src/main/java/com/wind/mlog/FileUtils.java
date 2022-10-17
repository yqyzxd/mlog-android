package com.wind.mlog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: FileUtils
 * Author: wind
 * Date: 2022/10/17 14:55
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 * <author> <time> <version> <desc>
 */
public class FileUtils {

    public static File getOrCreate(String fileDir, String fileName) {
        File file = new File(fileDir, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean delete(File file){
        try {
           return file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void writeToFile(File file, String txt,boolean newLine) {

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
                fos = new FileOutputStream(file);
            } else {
                fos = new FileOutputStream(file, true);
            }
            osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(txt); //写入内容
            if (newLine){
                osw.write("\r\n");  //换行
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {   //关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
