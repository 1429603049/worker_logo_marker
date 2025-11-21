package cn.autowok.logomarker;

import cn.autowok.logomarker.config.ConfLoader;
import cn.autowok.logomarker.cover.LogoMark;
import cn.autowok.logomarker.cover.PdfWhiteBoxMultiPage;
import cn.autowok.logomarker.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author gang.feng
 * @since ${TIME}
 */
public class Main {

    public static void main(String[] args) {
        //加载配置。
//        Map<String, Object> configMap = ConfLoader.loadConfigByYaml(null);
        Map<String, Object> configMap = ConfLoader.loadConfigByYaml();


        //获取指定目录下的pdf文件。
        List<String> filePathList = FileUtil.getFilePathList(configMap.get("pdfReadPath").toString());
        if (filePathList.isEmpty()) {
            System.out.println("没有发现PDF");
        }

        //创建输出目录
        String pdfWritePath = configMap.get("pdfWritePath").toString();
        FileUtil.createDirectories(pdfWritePath);


        //循环输出pdf文件。
        for (String tempFilePath : filePathList) {


//            String inputPath = "input.pdf";
//            String outputPath = "output.pdf";

            try {
                byte[] data = Files.readAllBytes(Paths.get(tempFilePath));
                byte[] newData = PdfWhiteBoxMultiPage.addWhiteBoxToAllPages(data);


                //新文件
                String genFileName = FileUtil.genFileName(tempFilePath, pdfWritePath);
                Files.write(Paths.get(genFileName), newData);
                System.out.println("生成新 PDF 完成：" + genFileName);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (FileUtil.fileExist(tempFilePath)) {
//                CoverMarker.cover(tempFilePath, toLogoMark(configMap), pdfWritePath);
//            } else {
//                System.out.println("路径错误");
//            }

        }

//        //循环输出pdf文件。
//        for (String tempFilePath : filePathList) {
//            if (FileUtil.fileExist(tempFilePath)) {
//                CoverMarker.cover(tempFilePath, toLogoMark(configMap), pdfWritePath);
//            } else {
//                System.out.println("路径错误");
//            }
//        }

    }

    //解析配置到 logoMark
    private static LogoMark toLogoMark(Map<String, Object> configMap) {
        LogoMark logoMark = new LogoMark();
        logoMark.setZeroType(configMap.get("markType").toString());
        logoMark.setRectX(Float.parseFloat(configMap.get("markX").toString()));
        logoMark.setRectY(Float.parseFloat(configMap.get("markY").toString()));
        logoMark.setRectWidth(Float.parseFloat(configMap.get("markWidth").toString()));
        logoMark.setRectHeight(Float.parseFloat(configMap.get("markHeight").toString()));
        return logoMark;
    }

//    private static String getFilePath(String[] args) {
//        if (args.length == 6) {
//            return args[5];
//        } else {
//            return null;
//        }
//    }


    // 读取当前目录下的pdf。

    // 去掉水印。


    // 生成新文件名。

    // 生成当前目录下的pdf。

}
