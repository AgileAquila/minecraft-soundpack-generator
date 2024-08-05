# Minecraft 音效包生成器

这是一个基于 JAVA ，可以为您的 OGG 文件自动生成 sounds.json ，并且生成 pack.mcmeta 的小工具

## 使用教程

1. 双击启动 Minecraft Soundpack Generator.bat，会在根目录生成一个 assets/ 目录
2. 将 pack.png 文件（如果有的话）放进根目录，并将您整理好的 OGG 文件放进 assets/ 文件夹，所有的 OGG 文件应放在 assets/命名空间/sounds/ 内，例如：

    ```c
    assets/
    |----- minecraft/
    |      |-------- sounds/
    |                |----- a.ogg
    |                |----- b.ogg
    |----- custom/
    |      |-------- sounds/
    |                |----- c.ogg
    |                |----- test/
    |                       |--- d.ogg
    |                       |--- e.ogg
    |----- ...
    ```

    命名空间名称可以任意，不是必须为 minecraft/，并且可以有多个命名空间，每个命名空间中必须存在唯一一个 sounds/ 目录

3. 再次双击启动 Minecraft Soundpack Generator.bat，根据提示输入游戏版本和资源包描述，下图中的小字部分就是资源包描述

   ![image](https://github.com/user-attachments/assets/87c19573-49e7-4c71-a4f4-baaf71d397b6)

4. 若命令行提示 “已生成 sounds.json”，将根目录中的 assets/ 目录、pack.mcmeta 和 pack.png 压缩为 ZIP 文件即可

## 温馨提示

1. 若 assets/ 目录下，sounds/ 目录和音频文件的位置不正确，且有些文件并非 OGG 格式，会出现警告，生成 sounds.json 时将跳过这些文件
2. 目前，一个声音事件只能对应一个音频，不支持生成如下的 sounds.json：

    ```c
    {
        ...
        "mob.hajimi.meow": {
            "sounds": [
                "custom:mob/hajimi/meow1",
                "custom:mob/hajimi/meow2",
                ...
            ]
        },
        ...
    }
    ```

    或许后面会加，反正也不麻烦（小声）

3. 不支持为声音事件指定 category 一项
4. OGG 文件不要使用除 “-” 和 “\_” 之外的特殊符号，强烈建议所有文件和文件夹统一遵守连字符或下划线命名法，例如 water-boil.ogg 或 water_boil.ogg，即所有字母均使用小写，单词和单词之间用 “-” 或 “\_” 连接
