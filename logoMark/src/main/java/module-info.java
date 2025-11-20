module logoMark {
    requires java.base;
    requires java.desktop;
    requires org.apache.commons.logging;
    requires org.apache.pdfbox;      // 自动模块名
    requires org.apache.fontbox;     // 自动模块名
    requires org.apache.pdfbox.io;   // 自动模块名
    exports cn.autowok.logomarker;
}
