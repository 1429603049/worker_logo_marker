package cn.autowok.logomarker.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加载配置
 *
 * @author gang.feng
 * @since 2025-11-20 16:57
 */
public class ConfLoader {


    public static Config loadConfig(String configPath) {
        if (configPath == null) {
            // 获取 jar 同级目录
            configPath = new File(ConfLoader.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getParent();
        }

        File configFile = new File(configPath, "conf/config.json");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            // 简单 JSON 解析（仅适合简单键值对）
            String json = sb.toString().trim();
            Config config = new Config();

            json = json.substring(1, json.length() - 1); // 去掉 {}
            String[] entries = json.split(",");

            for (String entry : entries) {
                if (entry.startsWith("#")) continue;
                String[] kv = entry.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].trim().replace("\"", "");
                    String value = kv[1].trim().replace("\"", "");
                    switch (key) {
                        case "pdfReadPath":
                            config.setPdfReadPath(value);
                            break;
                        case "pdfWritePath":
                            config.setPdfWritePath(value);
                            break;
                        case "markType":
                            config.setMarkType(value);
                            break;
                        case "markX":
                            config.setMarkX(Float.parseFloat(value));
                            break;
                        case "markY":
                            config.setMarkY(Float.parseFloat(value));
                            break;
                        case "markWidth":
                            config.setMarkWidth(Float.parseFloat(value));
                            break;
                        case "markHeight":
                            config.setMarkHeight(Float.parseFloat(value));
                            break;
                    }
                }
            }
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Config loadConfig() {
        String configPath = System.getProperty("user.dir");
//        D:\work\p_idea\worker_logo_marker\logoMark\src\main\resources\conf
        configPath += "\\logoMark\\src\\main\\resources";
        return loadConfig(configPath);
    }

    public static void main(String[] args) throws IOException {
        Config cfg = loadConfig();
        System.out.println("loadOver");
    }

    public static Map<String, Object> loadConfigByYaml() {
        String configPath = System.getProperty("user.dir");
//        D:\work\p_idea\worker_logo_marker\logoMark\src\main\resources\conf
        configPath += "\\logoMark\\src\\main\\resources";
        return loadConfigByYaml(configPath);
    }

    public static Map<String, Object> loadConfigByYaml(String path) {
        if (path == null) {
            // 获取 jar 同级目录
            path = new File(ConfLoader.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getParent();
        }
        path += "/conf/config.yaml";

        Map<String, Object> root = new LinkedHashMap<>();
        Deque<Map<String, Object>> stack = new ArrayDeque<>();
        Deque<Integer> indentStack = new ArrayDeque<>();

        stack.push(root);
        indentStack.push(0);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 去除注释与空行
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                // 计算缩进
                int indent = countIndent(line);
                String trimmed = line.trim();

                // key: value
                String[] parts = trimmed.split(":", 2);
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : null;

                // 换层级
                while (indent < indentStack.peek()) {
                    stack.pop();
                    indentStack.pop();
                }

                Map<String, Object> current = stack.peek();

                if (value == null || value.isEmpty()) {
                    // 嵌套对象
                    Map<String, Object> child = new LinkedHashMap<>();
                    current.put(key, child);

                    stack.push(child);
                    indentStack.push(indent);
                } else {
                    // 普通 key-value
                    current.put(key, castValue(value));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("配置文件，加载失败");
        }
        return root;
    }

    private static int countIndent(String s) {
        int n = 0;
        for (char c : s.toCharArray()) {
            if (c == ' ') n++;
            else break;
        }
        return n;
    }

    private static Object castValue(String v) {
        if ("true".equalsIgnoreCase(v)) return true;
        if ("false".equalsIgnoreCase(v)) return false;
        try {
            return Integer.parseInt(v);
        } catch (Exception ignored) {
        }
        return v;
    }

}
