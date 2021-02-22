package com.hcframe.activiti.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/***
 * @author lhc
 * @date 2020.1.2
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 读取文件内容
     * @param filePath 文件地址
     * @return 文件内容
     */
    public static String readFile(String filePath) {
        String record = "";
        // 创建文件记录读取数据最后一条的时间戳和id
        InputStream is = null;
        try {
             is= new FileInputStream(filePath);
            StringBuilder stringBuilder = new StringBuilder();
            int temp;
            //当temp等于-1时，表示已经到了文件结尾，停止读取
            while ((temp = is.read()) != -1) {
                stringBuilder.append((char) temp);
            }
            record = stringBuilder.toString();
        } catch (IOException e) {
            logger.error("File read Error!!!",e);
        }finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                logger.error("Cannot close File read Stream!!!",e);
            }
        }
        return record;
    }

    /**
     * 替换文件内容
     * @param string ---需要替换的内容
     * @return 是否成功
     */
    public static boolean replaceContentToFile(String string,String filePath) {
        File file = new File(filePath);
        Writer out = null;
        boolean flag = true;
        try {
            out = new FileWriter(file);
            out.write(string);
        } catch (IOException e) {
            flag = false;
            logger.error("Write string to file Error!!",e);
        }finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                logger.error("close replaceContentToFile Stream Error",e);
            }
        }
        return flag;
    }


}
