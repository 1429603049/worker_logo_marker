package cn.autowok.logomark.pdfbox.cover;

/**
 * 实体
 *
 * @author gang.feng
 * @since 2025-07-13 09:50
 */

public class LogoMark {

    //离右边
    Float rectX = 0f;
    //离下边
    Float rectY = 0f;

    //覆盖宽度
    Float rectWidth = 0f;
    //覆盖高度
    Float rectHeight = 0f;

    String zeroType = "1";


    public Float getRectX() {
        return rectX;
    }

    public void setRectX(Float rectX) {
        this.rectX = rectX;
    }

    public Float getRectY() {
        return rectY;
    }

    public void setRectY(Float rectY) {
        this.rectY = rectY;
    }

    public Float getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(Float rectWidth) {
        this.rectWidth = rectWidth;
    }

    public Float getRectHeight() {
        return rectHeight;
    }

    public void setRectHeight(Float rectHeight) {
        this.rectHeight = rectHeight;
    }

    public String getZeroType() {
        return zeroType;
    }

    public void setZeroType(String zeroType) {
        this.zeroType = zeroType;
    }
}
