package cn.autowork.logomark.itext.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 路劲工具类
 *
 * @author gang.feng
 * @since 2025-07-13 09:31
 */
public class FileUtil {

    //判断路径是否存在
    public static boolean fileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    //获取文件列表
    public static List<String> getFilePathList(String folderRoot) {
        List<String> filePathList = new ArrayList<>();
        // 获取目录
        File folder = new File(folderRoot);
        // 过滤出以 .pdf 结尾的文件
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".pdf") || file.getName().endsWith(".PDF")) {
                filePathList.add(file.getAbsolutePath());
            }
        }
        return filePathList;
    }

//    //生成文件副本，当前目录。
//    public static String genFileName(String filePath) {
//        String[] fileItems = filePath.split("\\\\|/");
//        String fileDir = "";
//        for (int i = 0; i < fileItems.length - 1; i++) {
//            fileDir += fileItems[i] + "\\";
//        }
//        String fileName = fileItems[fileItems.length - 1];
//        int lastDotIndex = fileName.lastIndexOf('.');
//        String copyFileName = "";
//        for (int i = 0; ; i++) {
//            copyFileName = fileName.substring(0, lastDotIndex) + "-" + i + "." + fileName.substring(lastDotIndex + 1);
//            if (!fileExist(fileDir + copyFileName)) {
//                break;
//            }
//        }
//        return fileDir + copyFileName;
//    }

    public static void createDirectories(String folderName){
        File file = new File(folderName);
        if (!file.exists()) {
            boolean ok = file.mkdirs();
            System.out.println(ok ? "创建成功" : "创建失败");
        }
    }

    //生成文件副本，fileDir 为空，则当前目录。
    public static String genFileName(String filePath, String fileDir) {
        String[] fileItems = filePath.split("\\\\|/");
//        String fileDir = "";
        if (fileDir == null) {
            for (int i = 0; i < fileItems.length - 1; i++) {
                fileDir += fileItems[i] + "/";
            }
        }
        if (!fileDir.equals("/")) {
            fileDir += "/";
        }

        String fileName = fileItems[fileItems.length - 1];
        int lastDotIndex = fileName.lastIndexOf('.');
        String copyFileName = fileName;
        for (int i = 0; ; i++) {
            if (!fileExist(fileDir + copyFileName)) {
                break;
            }
            copyFileName = fileName.substring(0, lastDotIndex) + "-" + i + "." + fileName.substring(lastDotIndex + 1);

        }
        return fileDir + copyFileName;
    }


}
