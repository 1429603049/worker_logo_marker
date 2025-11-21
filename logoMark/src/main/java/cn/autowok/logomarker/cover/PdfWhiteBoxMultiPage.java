package cn.autowok.logomarker.cover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfWhiteBoxMultiPage {

    public static void main(String[] args) throws IOException {
        String inputPath = "input.pdf";
        String outputPath = "output.pdf";

        byte[] data = Files.readAllBytes(Paths.get(inputPath));
        byte[] newData = addWhiteBoxToAllPages(data);

        Files.write(Paths.get(outputPath), newData);
        System.out.println("生成新 PDF 完成：" + outputPath);
    }

    public static byte[] addWhiteBoxToAllPages(byte[] pdfBytes) {
        String content = new String(pdfBytes);

        // 正则匹配每页 MediaBox
        Pattern mediaBoxPattern = Pattern.compile("/MediaBox\\s*\\[([0-9\\s]+)\\]");
        Matcher mediaBoxMatcher = mediaBoxPattern.matcher(content);

        // 正则匹配每页 /Contents
        Pattern contentsPattern = Pattern.compile("/Contents\\s*(\\d+)\\s+0\\s+R");
        Matcher contentsMatcher = contentsPattern.matcher(content);

        StringBuilder sb = new StringBuilder(content);

        while (mediaBoxMatcher.find() && contentsMatcher.find()) {
            // 解析 MediaBox
            String[] coords = mediaBoxMatcher.group(1).trim().split("\\s+");
            int llx = Integer.parseInt(coords[0]);
            int lly = Integer.parseInt(coords[1]);
            int urx = Integer.parseInt(coords[2]);
            int ury = Integer.parseInt(coords[3]);

            int boxWidth = urx - llx;
            int boxHeight = ury - lly;

            // 计算右下角坐标
            int rectX = boxWidth - 100;
            int rectY = lly;


            // 找到对应 /Contents 对象位置
            int objNum = Integer.parseInt(contentsMatcher.group(1));
            String objPatternStr = objNum + " 0 obj";
            int objPos = sb.indexOf(objPatternStr);
            if (objPos == -1) continue;

            int streamPos = sb.indexOf("stream", objPos);
            if (streamPos == -1) continue;
            int insertPos = streamPos + 6; // 跳过 "stream"

            // 绘制白色方块 PDF 指令
            String drawCmd = createBox(rectX, rectY, 1, 1);
            // 插入绘制指令
            sb.insert(insertPos, drawCmd);

            // 更新 /Length
            Pattern lengthPattern = Pattern.compile("/Length\\s+(\\d+)");
            Matcher lengthMatcher = lengthPattern.matcher(sb.substring(objPos, streamPos));
            if (lengthMatcher.find()) {
                int oldLen = Integer.parseInt(lengthMatcher.group(1));
                int newLen = oldLen + drawCmd.getBytes().length;
                int lenStart = objPos + lengthMatcher.start(1);
                int lenEnd = objPos + lengthMatcher.end(1);
                sb.replace(lenStart, lenEnd, String.valueOf(newLen));
            }
        }

        return sb.toString().getBytes();
    }


    public static String createBox(int rectX, int rectY, int boxWidth, int boxHeight) {
        // 绘制白色方块 PDF 指令
        String drawCmd = String.format("\nq\n0 0 1 rg\n%d %d %d %d re\nf\nQ\n", rectX, rectY, boxWidth, boxHeight);

        return drawCmd;
    }
}
