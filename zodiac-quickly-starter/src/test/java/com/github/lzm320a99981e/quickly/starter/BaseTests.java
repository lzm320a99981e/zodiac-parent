package com.github.lzm320a99981e.quickly.starter;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseTests {
    @Test
    public void fileList() throws IOException {
        final Path path = Paths.get("/Users/zhangguangyong/IdeaProjects/zodiac-parent/default-upload-folder");
        final Map<String, File> keyWithFileMap = Files.walk(path).filter(item -> item.toFile().isFile()).map(Path::toFile).collect(Collectors.toMap(File::getName, File::getAbsoluteFile));
        System.out.println(JSON.toJSONString(keyWithFileMap, true));
    }

    @Test
    public void testDeploy() throws IOException {
        String responseDir = "/Users/zhangguangyong/.m2/repository";
        final Path path = Paths.get(responseDir, "org/apache/commons");
        final List<Path> list = Files.walk(path).filter(item -> item.toFile().isFile() && item.toFile().getName().endsWith(".jar")).collect(Collectors.toList());
        final StringBuilder cmd = new StringBuilder();
        for (Path jar : list) {
            final String absolutePath = jar.toFile().getAbsolutePath();
            final String[] pathItems = absolutePath.substring(responseDir.length() + 1).split("/");
            String groupId = Joiner.on(".").join(Arrays.asList(pathItems).subList(0, pathItems.length - 3));
            String artifactId = pathItems[pathItems.length - 3];
            String version = pathItems[pathItems.length - 2];
            String repositoryId = version.contains("SNAPSHOT") ? "snapshots" : "releases";
            String mvnDeployCmd = String.format(
                    "call mvn deploy:deploy-file -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=jar -Dfile=%s -DrepositoryId=%s",
                    groupId, artifactId, version, absolutePath, repositoryId
            );
            cmd.append(mvnDeployCmd).append(System.lineSeparator());
        }
        System.out.println(cmd.toString());
        // mvn deploy:deploy-file -DgroupId=app.xxx -DartifactId=xxx -Dversion=1.0 -Dpackaging=jar -Dfile=D:\java\picture_server\target\xxx-1.0-SNAPSHOT.jar -Durl=http://localhost:8081/nexus/content/repositories/releases/ -DrepositoryId=releases
    }
}
