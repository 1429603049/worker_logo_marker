package cn.autowok.logomark.pdfbox.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * 加载配置
 *
 * @author gang.feng
 * @since 2025-11-20 16:57
 */
public class ConfLoader {

    public static Map<String, Object> loadConfigByYaml() {
        String configPath = System.getProperty("user.dir");
        return loadConfigByYaml(configPath);
    }

    public static Map<String, Object> loadConfigByYaml(String path) {
        if (path == null) {
            System.out.println("---configPath 不能为空---");
            return new HashMap<>();
        }
        path += "/config.yaml";

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
            System.out.println("---------------------------------");
            System.out.println("配置文件，加载失败。");
            System.out.println("jar所在目录：conf/config.yaml。");
            System.out.println("---------------------------------");
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
