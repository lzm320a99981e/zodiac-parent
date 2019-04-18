package com.github.lzm320a99981e.zodiac.tools;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CompressorTests {
    @Test
    public void test() throws Exception {
        // 压缩：添加条目，写入数据
        final File[] files = Paths.get("/Users/zhangguangyong/IdeaProjects/zodiac/zodiac-tools/src/test/java/com/github/lzm320a99981e/zodiac/tools").toFile().listFiles();
        final File zip = new File("/Users/zhangguangyong/IdeaProjects/zodiac/output.zip");
        final ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zip));
        for (File file : files) {
            final ZipEntry entry = new ZipEntry("中华人民共和国/中国施蒂利克解放路上肯定积分" + file.getName());
            zipOutput.putNextEntry(entry);
            zipOutput.write(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        }
        zipOutput.close();

        // 解压缩
        final File output = new File("/Users/zhangguangyong/IdeaProjects/zodiac/output");
        final ZipFile zipFile = new ZipFile(zip);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = entries.nextElement();
            final InputStream input = zipFile.getInputStream(zipEntry);
            final Path path = Paths.get(output.getAbsolutePath(), zipEntry.getName());
            final File target = path.toFile();
            if (target.isDirectory()) {

            }
            Files.copy(input, path);
        }
    }

    @Test
    public void test2() throws IOException {
        // 压缩
        final File file = new File("/Users/zhangguangyong/IdeaProjects/zodiac/Documents");
        final byte[] compression = Compressor.create().addCompressEntry(file).compression();
        final Path compressed = Paths.get(file.getAbsolutePath(), "Documents.zip");
        Files.write(compressed, compression);

        // 解压
        Compressor.create().addCompressedFile(compressed.toFile()).decompression(file);
    }
}
