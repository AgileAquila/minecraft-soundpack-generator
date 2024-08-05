package agileaquila;

import java.util.Arrays;
import java.util.List;

public class SoundJson {
    private String path;

    /**
     * 获取 .ogg 相对路径
     * 
     * @param path 传入相对路径参数，作为生成 .json 的原料
     */
    public void relativePath(String path) {
        this.path = path;
    }

    /**
     * 生成单个 .ogg 文件的 .json 字符串，并返回该值
     * 
     * @return 返回单个 .ogg 文件的 .json 字符串
     */
    public String generateJsonString() {
        // 获取路径中 assets\ 之后的部分，并去除 .ogg 后缀，作为 .json 文件内的内容
        int lastIndexOfDot = path.lastIndexOf(".");
        String relativePath = path.substring("assets\\".length(), lastIndexOfDot);

        // 分割相对路径为 String 列表，用来生成 .json 里的内容
        String[] pathSplit = relativePath.split("\\\\");
        List<String> pathSplitList = Arrays.asList(pathSplit);
        // 生成 .json
        if (!pathSplitList.contains("sounds")) {
            System.out.println("警告：无法写入 sounds.json，\"" + path + "\" 不属于任何一个 sounds 文件夹，将跳过此文件");
            return "";
        } else if (!pathSplitList.get(1).equals("sounds")) {
            System.out.println("警告：无法写入 sounds.json，\"" + path + "\" 所属的 sounds 文件夹并不在正确的位置，将跳过此文件");
            return "";
        } else {
            String jsonOggPath = "", jsonOggKey = "";
            for (int i = 0; i < pathSplitList.size(); i++) {
                // 生成 .json 中的文件路径
                if (i == pathSplitList.size() - 1) {
                    jsonOggPath = jsonOggPath + pathSplitList.get(i);
                } else if (i == 0) {
                    jsonOggPath = jsonOggPath + pathSplitList.get(i) + ":";
                } else if (i > 1) {
                    jsonOggPath = jsonOggPath + pathSplitList.get(i) + "/";
                }

                // 生成 .json 中的键
                if (i == pathSplitList.size() - 1) {
                    jsonOggKey = jsonOggKey + pathSplitList.get(i);
                } else if (i > 1 && i < pathSplitList.size() - 1) {
                    jsonOggKey = jsonOggKey + pathSplitList.get(i) + ".";
                }
            }
            // 在此处生成 .json
            String singleJson = "\"" + jsonOggKey + "\":{\"sounds\":[\"" + jsonOggPath + "\"]}";
            return singleJson;
        }
    }
}
