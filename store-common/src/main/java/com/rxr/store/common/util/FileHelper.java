package com.rxr.store.common.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ZL
 * @date 2018-09-25 16:24
 **/
public class FileHelper {

    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if(null != children){
                // 递归删除目录中的子目录下
                for (String aChildren : children) {
                    boolean success = deleteDir(new File(dir, aChildren));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 将文件写入指定目录中
     *
     * @param in 文件输入流
     * @param filePath 路径
     */
    public static void writeFile(InputStream in, String filePath) throws IOException {
        // 创建文件实例
        File tempFile = new File(filePath);
        // 判断父级目录是否存在，不存在则创建
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        // 判断文件是否存在，否则创建文件
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        // 将接收的文件保存到指定文件中
        FileUtils.copyInputStreamToFile(in, tempFile);
    }

    /**
     * 拷贝文件
     * @param source 源文件路径
     * @param dest 目标文件路径
     * @throws IOException exception
     */
    public static void copyFile(String source, String dest) throws IOException {
        File sourceFile = new File(source);
        File destFile = new File(dest);
        if(sourceFile.exists()){
            if(!destFile.exists()){
                destFile.createNewFile();
            }
            copyFile(sourceFile, destFile);
        }
    }

    /**
     * 拷贝文件
     * @param source 源文件
     * @param dest 目标文件
     * @throws IOException exception
     */
    public static void copyFile(File source, File dest)
            throws IOException {
        FileUtils.copyFile(source, dest);
    }

}
