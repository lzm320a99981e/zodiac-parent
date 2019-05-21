package com.github.lzm320a99981e.quickly.starter;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseTests {
    @Test
    public void fileList() throws IOException {
        final Path path = Paths.get("/Users/zhangguangyong/IdeaProjects/zodiac-parent/default-upload-folder");
        final Map<String, File> keyWithFileMap = Files.walk(path).filter(item -> item.toFile().isFile()).map(Path::toFile).collect(Collectors.toMap(File::getName, File::getAbsoluteFile));
        System.out.println(JSON.toJSONString(keyWithFileMap, true));
    }
}
