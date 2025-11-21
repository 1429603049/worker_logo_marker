## pdf 水印去除。
方案，用白色方块覆盖。
## 由于需要依赖jdk环境，现升级18，准备做打包裁剪jre工作。
mini分支，做jre裁剪。用jlink或jpackage。
mini-launch4j。尝试压缩。
## 最终尝试方案。
1. git pdfBox源码。
2. 给pdfBox源码，加入module-info，自行模块化打包。根据实际访问情况，补充requires、export。
   + parent
   + io
   + fontbox
   + pdfbox
3. 正常运行后，package。

## 后续再研究吧。
4. 单独做一个空模块，配置jlink插件，进行jre裁剪。(好像不太行，找不到模块。)

