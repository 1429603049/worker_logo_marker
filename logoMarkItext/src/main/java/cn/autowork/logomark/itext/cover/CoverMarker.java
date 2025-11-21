package cn.autowork.logomark.itext.cover;

import cn.autowork.logomark.itext.util.FileUtil;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.FileNotFoundException;

public class CoverMarker {

    private static LogoMark markByDefType(LogoMark logoMark, Rectangle mediaBox) {
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
        try (PdfDocument pdf = new PdfDocument(new PdfReader(file), new PdfWriter(genFileName))) {
            int numberOfPages = pdf.getNumberOfPages();

            //逐页覆盖
            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = pdf.getPage(i);

                // 获取页面大小
                Rectangle mediaBox = page.getMediaBox();
//                Rectangle pageSize = page.getPageSize();
                LogoMark defType = markByDefType(logoMark, mediaBox);

                Rectangle rect = new Rectangle(
                        defType.getRectX(),
                        defType.getRectY(),
                        defType.getRectWidth(),
                        defType.getRectHeight()
                );

                PdfCanvas pdfCanvas = new PdfCanvas(page);
                pdfCanvas.saveState(); // 保存图形状态
                pdfCanvas.setFillColor(ColorConstants.WHITE); // 填充颜色
                pdfCanvas.rectangle(defType.getRectX(), defType.getRectY(), defType.getRectWidth(), defType.getRectHeight());
                pdfCanvas.fill();
                pdfCanvas.restoreState(); // 恢复图形状态
            }
            System.out.println("itext新PDF : " + genFileName);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
