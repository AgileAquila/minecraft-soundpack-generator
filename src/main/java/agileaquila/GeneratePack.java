package agileaquila;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneratePack {
    /**
     * 获取所有输入的文件夹内的文件夹路径，包括文件夹嵌套
     * 
     * @param folder      输入的文件夹
     * @param folderPaths 一个列表，用来存放输入的文件夹内所有的文件夹路径，包括文件夹嵌套
     */
    public static void getAllFolders(File folder, List<String> folderPaths) {
        String path = folder.getPath();
        folderPaths.add(path);
        File[] dirElements = folder.listFiles();
        for (File element : dirElements) {
            if (element.isDirectory()) {
                getAllFolders(element, folderPaths);
            }
        }
    }

    /**
     * 返回输入的文件夹内所有的 .ogg 文件名，只有 .ogg 文件才会被返回
     * 
     * @param folder 被遍历的文件夹
     * @return 该文件夹的根目录内所有的 .ogg 文件名
     */
    public static List<String> getOggFileNamesInFolder(File folder) {
        File[] dirElements = folder.listFiles();
        List<String> oggFileNames = new ArrayList<>();

        for (File element : dirElements) {
            String elementPath = element.getPath();
            int lastDotIndex = elementPath.lastIndexOf(".");

            if (!element.isDirectory() && lastDotIndex == -1) {
                System.out.println("警告：无法写入 sounds.json，\"" + elementPath + "\" 并不是一个 .ogg 文件，将跳过此文件");
            } else if (!element.isDirectory() && lastDotIndex != -1) {
                String fileExtension = elementPath.substring(lastDotIndex).toLowerCase();
                if (fileExtension.equals(".ogg")) {
                    oggFileNames.add(element.getName()); // 将 .ogg 文件名提取到 oggFileNames 列表中
                } else if (!fileExtension.equals(".json") && !fileExtension.equals(".ogg")) {
                    System.out.println("警告：无法写入 sounds.json，\"" + elementPath + "\" 并不是一个 .ogg 文件，将跳过此文件");
                }
            }
        }
        return oggFileNames; // 返回输入的文件夹下所有的 .ogg 文件名
    }

    /**
     * 生成指定命名空间下整合了所有音频文件的 .json 字符串
     * 
     * @param classFolder 输入包含所有音频文件的命名空间
     * @return 返回生成的 .json 字符串
     */
    public static String GenerateClassJsonString(File classFolder) {
        // 获取命名空间下的所有文件夹路径
        List<String> folderPaths = new ArrayList<>();
        getAllFolders(classFolder, folderPaths);

        // 对命名空间下的这些所有文件夹进行遍历，对每一文件夹的 .ogg 文件路径生成对应的 .json，并存放在 allJsonStrings 列表中
        List<String> allJsonStrings = new ArrayList<>(); // 存放所有单个 .json 字符串的列表
        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            List<String> oggFileNamesInFolder = getOggFileNamesInFolder(folder);
            for (String oggFileName : oggFileNamesInFolder) {
                String oggFileRelativePath = folderPath + "\\" + oggFileName;

                // 调用 Json 类，生成单个 .ogg 文件的 .json
                SoundJson singleJson = new SoundJson();
                singleJson.relativePath(oggFileRelativePath);
                String singleJsonString = singleJson.generateJsonString();
                if (singleJsonString.length() > 0) {
                    allJsonStrings.add(singleJsonString);
                }
            }
        }

        // 将 allJsonStrings 列表转化为 .json 字符串
        String jsonString = "";
        for (int i = 0; i < allJsonStrings.size(); i++) {
            if (i == allJsonStrings.size() - 1) {
                jsonString = jsonString + allJsonStrings.get(i);
            } else {
                jsonString = jsonString + allJsonStrings.get(i) + ",";
            }
        }
        jsonString = "{" + jsonString + "}";
        return jsonString;
    }

    /**
     * 生成所有 sounds.json
     * 
     * @param mainContent 获取 assets 文件夹
     */
    public static void generateJson(File mainContent) throws IOException {
        File[] mainContentChildren = mainContent.listFiles();
        Boolean isEmpty = true;
        for (File children : mainContentChildren) {
            if (children.isDirectory()) {
                String jsonContent = GenerateClassJsonString(children);
                String classFolderPath = children.getPath();
                if (jsonContent.equals("{}")) {
                    System.out.println("警告：未在 \"" + classFolderPath + "\\sounds\" 找到任何 .ogg 文件，将跳过此命名空间");
                    continue;
                }
                isEmpty = false;
                File json = new File(classFolderPath + "\\sounds.json");
                FileWriter fr = new FileWriter(json);
                fr.write(jsonContent);
                fr.close();
            }
        }
        if (isEmpty) {
            System.out.println("警告：未找到任何 .ogg 文件");
        } else {
            System.out.println("已生成 sounds.json\n");
        }
    }

    public static void startProgram(File mainContent) throws IOException {
        Scanner scanner = new Scanner(System.in);
        // 生成 pack.mcmeta
        try {
            Mcmeta mcmeta = new Mcmeta();
            mcmeta.generateMcmeta(scanner);
            System.out.println("已生成 pack.mcmeta\n");
        } catch (Exception e) {
            System.out.println("错误：pack.mcmeta 生成失败");
            System.out.println(e);
            System.exit(1);
        }
        // 生成 sounds.json
        try {
            generateJson(mainContent);
        } catch (Exception e) {
            System.out.println("错误：sounds.json 生成失败");
            System.out.println(e);
            System.exit(1);
        }
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        // 获取用来存放所有 .ogg 文件的 assets 文件夹
        File mainContent = new File("assets");
        if (mainContent.exists() && mainContent.isDirectory()) {
            startProgram(mainContent);
        } else {
            try {
                mainContent.mkdir();
                System.out.println("assets 文件夹创建成功，请将整理好的所有文件放入该文件夹");
            } catch (Exception e) {
                System.out.println("错误：发生了未知的错误，创建 assets 文件夹失败，请尝试手动创建该文件夹");
                System.out.println(e);
            }
        }
    }
}
