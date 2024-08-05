package agileaquila;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mcmeta {
    /**
     * 将 pack-format.json 的每一个键拆成字符串列表
     * 
     * @return 返回拆分后的 json 字符串列表
     */
    public List<String> getPackFormatJson() throws IOException {
        // 将 pack-format.json 转成字符串
        InputStream jsonInputStream = getClass().getClassLoader().getResourceAsStream("pack-format.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(jsonInputStream, StandardCharsets.UTF_8));
        String line, jsonContent = "";
        while ((line = br.readLine()) != null) {
            String processedLine = line.replaceAll("\\s+", "");
            jsonContent = jsonContent + processedLine;
        }
        br.close();

        // 拆分 json 字符串为列表
        jsonContent = jsonContent.substring(1, jsonContent.length() - 1);
        List<String> separatedJson = Arrays.asList(jsonContent.split("},"));
        for (int i = 0; i < separatedJson.size(); i++) {
            separatedJson.set(i, separatedJson.get(i) + "}");
        }
        return separatedJson;
    }

    /**
     * 根据用户输入的版本号返回对应的 formatValue 值
     * 
     * @param inputVersion 传入的用户输入的版本号
     * @return 返回对应的 formatValue
     */
    public int getPackFormat(String inputVersion) throws IOException {
        List<String> separatedJson = getPackFormatJson();
        ObjectMapper mapper = new ObjectMapper();
        int formatValue = 0;
        for (String singleJson : separatedJson) {
            List<String> splitSingleJson = Arrays.asList(singleJson.split(":\\{"));
            splitSingleJson.set(0, "{\"formatValue\":" + splitSingleJson.get(0) + ",");
            String standardizedJson = splitSingleJson.get(0) + splitSingleJson.get(1);
            PackFormat value = mapper.readValue(standardizedJson, PackFormat.class);
            List<String> versionList = value.getVersions();
            if (versionList.contains(inputVersion)) {
                formatValue = value.getFormatValue();
                break;
            }
        }
        return formatValue;
    }

    /**
     * 生成 pack.mcmeta
     * 
     * @param Scanner 与用户交互的接口
     * @return 返回最终的 pack.mcmeta 文件
     */
    public void generateMcmeta(Scanner scanner) throws IOException {
        // 从用户输入的游戏版本获取对应的 packFormat 编号
        int packFormat = 0;
        while (packFormat == 0) {
            System.out.println("请输入游戏版本，如 1.18.2：");
            String inputVersion = scanner.nextLine();
            // 检查用户输入的版本是否为空
            if (inputVersion.equals("")) {
                System.out.println("警告：此项不能为空，请重新输入\n");
                continue;
            }

            // 检查用户输入的版本格式是否合法
            if (!inputVersion.replace(".", "").matches("\\d+")
                    || inputVersion.charAt(0) == '.'
                    || inputVersion.charAt(inputVersion.length() - 1) == '.') {
                System.out.println("警告：非法的游戏版本格式，请重新输入\n");
                continue;
            }

            // 检查用户输入的版本号数量是否正确
            List<String> splitVersion = Arrays.asList(inputVersion.split("\\."));
            if (splitVersion.size() != 2 && splitVersion.size() != 3) {
                System.out.println("警告：非法的游戏版本格式，请重新输入\n");
                continue;
            }

            // 检查用户输入的版本号是否存在
            int formatValue = getPackFormat(inputVersion);
            if (formatValue == 0) {
                System.out.println("警告：仅支持正式版版本号，或该版本不存在，请重新输入\n");
                continue;
            }
            packFormat = formatValue; // 最后进行传参
        }

        // 从用户输入的资源包描述获取对应的 description
        System.out.println("请输入资源包描述：");
        String description = scanner.nextLine();

        // 生成 pack.mcmeta 字符串内容
        String mcmetaString = "{\"pack\":{\"pack_format\":" + packFormat + ",\"description\":\"" + description + "\"}}";
        // 生成 pack.mcmeta 文件
        File mcmetaFile = new File("pack.mcmeta");
        FileWriter fw = new FileWriter(mcmetaFile);
        fw.write(mcmetaString);
        fw.close();
    }
}
