package cn.autowork.logomark.itext.config;

/**
 * 配置实体
 *
 * @author gang.feng
 * @since 2025-11-20 16:58
 */

public class Config {

    private String pdfReadPath;
    private String pdfWritePath;

    //模式-锚点位置。
    private String markType;

    //位置
    private Float markX;
    private Float markY;

    //大小
    private Float markWidth;
    private Float markHeight;


    public String getPdfReadPath() {
        return pdfReadPath;
    }

    public void setPdfReadPath(String pdfReadPath) {
        this.pdfReadPath = pdfReadPath;
    }

    public String getPdfWritePath() {
        return pdfWritePath;
    }

    public void setPdfWritePath(String pdfWritePath) {
        this.pdfWritePath = pdfWritePath;
    }


    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }

    public Float getMarkX() {
        return markX;
    }

    public void setMarkX(Float markX) {
        this.markX = markX;
    }

    public Float getMarkY() {
        return markY;
    }

    public void setMarkY(Float markY) {
        this.markY = markY;
    }

    public Float getMarkWidth() {
        return markWidth;
    }

    public void setMarkWidth(Float markWidth) {
        this.markWidth = markWidth;
    }

    public Float getMarkHeight() {
        return markHeight;
    }

    public void setMarkHeight(Float markHeight) {
        this.markHeight = markHeight;
    }
}
