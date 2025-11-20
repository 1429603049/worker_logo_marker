package tomod;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarModularizer {

    public static void main(String[] args) throws IOException {
//        if (args.length < 3) {
//            System.out.println("Usage: java JarModularizer <原始jar> <输出jar> <模块名>");
//            return;
//        }

//        Path origJar = Paths.get(args[0]);
//        Path outputJar = Paths.get(args[1]);
//        String moduleName = args[2];
//        java -cp . JarModularizer

        String origJarStr = "D:\\soft\\mavenRep\\commons-logging\\commons-logging\\1.2\\commons-logging-1.2.jar";
        String outputJarStr = "D:\\soft\\mavenRep\\commons-logging\\commons-logging\\1.2\\commons-logging-modular.jar";
        String moduleName = "commons.logging";

//        D:\libs\commons-logging-modular.jar commons.logging


        Path tempDir = Files.createTempDirectory("jar_modularize");
        tempDir.toFile().deleteOnExit();

        // 1. 解压 JAR
        Path origJar = Paths.get(origJarStr);

        try (FileSystem fs = FileSystems.newFileSystem(origJar, (ClassLoader) null)) {
            for (Path root : fs.getRootDirectories()) {
                Files.walk(root).forEach(source -> {
                    try {
                        // 计算目标路径
                        Path dest = tempDir.resolve(root.relativize(source).toString().replace("/", File.separator));
                        if (Files.isDirectory(source)) {
                            Files.createDirectories(dest);
                        } else {
                            Files.createDirectories(dest.getParent()); // 确保父目录存在
                            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        }

        // 2. 创建或修改 MANIFEST.MF
        Path manifestDir = tempDir.resolve("META-INF");
        if (!Files.exists(manifestDir)) {
            Files.createDirectories(manifestDir);
        }
        Path manifestFile = manifestDir.resolve("MANIFEST.MF");

        Manifest manifest = new Manifest();
        if (Files.exists(manifestFile)) {
            try (InputStream is = Files.newInputStream(manifestFile)) {
                manifest.read(is);
            }
        }
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Automatic-Module-Name", moduleName);

        Path outputJar = Paths.get(outputJarStr);

        // 3. 重新打包 JAR
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(outputJar), manifest)) {
            Files.walk(tempDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.equals(outputJar))
                    .forEach(path -> {
                        String entryName = tempDir.relativize(path).toString().replace("\\", "/");
                        if ("META-INF/MANIFEST.MF".equalsIgnoreCase(entryName)) {
                            return; // 跳过 MANIFEST
                        }
                        try (InputStream is = Files.newInputStream(path)) {
                            jos.putNextEntry(new JarEntry(entryName));
                            byte[] buffer = new byte[8192];
                            int read;
                            while ((read = is.read(buffer)) != -1) {
                                jos.write(buffer, 0, read);
                            }
                            jos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }


        System.out.println("生成自动模块化 JAR: " + outputJar);
    }
}
