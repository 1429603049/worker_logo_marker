package cn.autowok.logomarker.cover;

import cn.autowok.logomarker.util.FileUtil;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CoverMarker {

    private static LogoMark markByDefType(LogoMark logoMark, PDRectangle mediaBox) {
        LogoMark defMark = new LogoMark();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();

        if (logoMark.getZeroType().equals("1")) {
            if (pageWidth < logoMark.getRectX() + logoMark.getRectWidth()) {
                System.out.println("参数设置，超过页面宽度。可能覆盖效果不理想。");
            }
            if (pageHeight < logoMark.getRectY() + logoMark.getRectHeight()) {
                System.out.println("参数设置，超过页面高度。可能覆盖效果不理想。");
            }
            defMark.setRectX(logoMark.getRectX());
            defMark.setRectY(logoMark.getRectY());
        }
        if (logoMark.getZeroType().equals("2")) {
            if (logoMark.getRectX() < logoMark.getRectWidth()) {
                System.out.println("参数设置，超过页面宽度。可能覆盖效果不理想。");
            }
            if (pageHeight < logoMark.getRectY() + logoMark.getRectHeight()) {
                System.out.println("参数设置，超过页面高度。可能覆盖效果不理想。");
            }
            defMark.setRectX(pageWidth - logoMark.getRectX());
            defMark.setRectY(logoMark.getRectY());
        }
        if (logoMark.getZeroType().equals("3")) {
            if (logoMark.getRectX() < logoMark.getRectWidth()) {
                System.out.println("参数设置，超过页面宽度。可能覆盖效果不理想。");
            }
            if (logoMark.getRectY() < logoMark.getRectHeight()) {
                System.out.println("参数设置，超过页面高度。可能覆盖效果不理想。");
            }
            defMark.setRectX(pageWidth - logoMark.getRectX());
            defMark.setRectY(pageHeight - logoMark.getRectY());
        }
        if (logoMark.getZeroType().equals("4")) {
            if (pageWidth < logoMark.getRectX() + logoMark.getRectWidth()) {
                System.out.println("参数设置，超过页面宽度。可能覆盖效果不理想。");
            }
            if (logoMark.getRectY() < logoMark.getRectHeight()) {
                System.out.println("参数设置，超过页面高度。可能覆盖效果不理想。");
            }
            defMark.setRectX(logoMark.getRectX());
            defMark.setRectY(pageHeight - logoMark.getRectY());
        }

        defMark.setRectWidth(logoMark.getRectWidth());
        defMark.setRectHeight(logoMark.getRectHeight());
        return defMark;

    }

    public static void cover(String filePath, LogoMark logoMark, String fileDir) {
        //原文件
        File file = new File(filePath);
        //新文件
        String genFileName = FileUtil.genFileName(filePath, fileDir);
//        try (PDDocument document = Loader.loadPDF(file, MemoryUsageSetting.setupMainMemoryOnly())){


//        File file = new File("xxx.pdf");

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(file))) {
            //逐页覆盖
            for (PDPage page : document.getPages()) {
                // 获取页面大小
                PDRectangle mediaBox = page.getMediaBox();
                LogoMark defType = markByDefType(logoMark, mediaBox);

////                // 假设水印在右下角 150x100 区域
//                float rectX = pageWidth - 160; // 离右边 10px
//                float rectY = 20;               // 离下边 20px
//                float rectWidth = 150;
//                float rectHeight = 100;
                // 创建内容流，在原内容上叠加（AppendMode.APPEND）
                try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                    // TODO: 2025/7/13 可能不一定是白色的背景。
                    cs.setNonStrokingColor(Color.WHITE); // 设置填充颜色为白色
//                    cs.setNonStrokingColor(Color.RED); // 设置填充颜色为白色
//                    cs.addRect(rectX, rectY, rectWidth, rectHeight); // 添加矩形
                    cs.addRect(defType.getRectX(), defType.getRectY(), defType.getRectWidth(), defType.getRectHeight()); // 添加矩形
                    cs.fill(); // 填充矩形
                }
            }
            document.save(genFileName);
            System.out.println("新PDF : " + genFileName);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
