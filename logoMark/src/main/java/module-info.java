module cn.autowok.logomark.pdfbox {
    requires java.base;
    requires java.desktop;
    requires org.apache.commons.logging;
    requires org.apache.pdfbox;
    requires org.apache.fontbox;

    requires org.bouncycastle.provider;
    requires org.bouncycastle.util;
    requires org.bouncycastle.pkix;

    exports cn.autowok.logomark.pdfbox;
}
