## pdf 水印去除。
方案，用白色方块覆盖。
## 由于需要依赖jdk环境，现升级18，准备做打包裁剪jre工作。
mini分支，做jre裁剪。用jlink或jpackage。
mini-launch4j。尝试压缩。
## 基于pdfBox方案。
1. git pdfBox源码。
2. 给pdfBox源码，加入module-info，进行模块化打包。
   + 注意：根据实际访问情况，补充requires、export。
   + 核心模块：
     + parent
     + io
     + fontbox
     + pdfbox
3. pdfBox install。自定义版本号。
4. 项目引入java模块化的pdfBox，进行开发。
5. 开发环境正常运行后，配置jlink插件，package。
6. target生成runtime.zip。
7. 启动命令：java -m {java模块}/{启动类}
   + 示例：.\runtime\bin\java -m cn.autowok.logomark.pdfbox/cn.autowok.logomark.pdfbox.Main



## 后续再研究吧。
1. 单独做一个空模块，配置jlink插件，进行jre裁剪。(好像不太行，找不到模块。)

