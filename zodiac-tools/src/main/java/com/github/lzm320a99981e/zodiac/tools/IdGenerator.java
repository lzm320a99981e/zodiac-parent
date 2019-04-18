package com.github.lzm320a99981e.zodiac.tools;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * UUID 工具类 https://github.com/souyunku/SnowFlake
 */
public abstract class IdGenerator {

    private static String[] chars = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    /**
     * 32位UUID
     *
     * @return
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 8位UUID
     *
     * @return
     */
    public static String uuid8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = uuid32();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成安全系数高的随机密码
     *
     * @return
     */
    public static String randomPassword() {
        StringBuilder password = new StringBuilder();
        // 1个短uuid
        password.append(IdGenerator.uuid8());
        // 生成 3-5 个特殊字符
        int min = 3;
        int max = min + 2;
        String specialChars = "!@#$%^&*";
        int specialCharsCount = RandomUtils.nextInt(min, max);
        for (int i = 0; i < specialCharsCount; i++) {
            int position = RandomUtils.nextInt(0, password.length());
            password.insert(position, RandomStringUtils.random(1, specialChars));
        }

        // 生成 3-5 个随机数字
        int randomNumberCount = RandomUtils.nextInt(1, 5);
        for (int i = 0; i < randomNumberCount; i++) {
            int position = RandomUtils.nextInt(0, password.length());
            password.insert(position, RandomStringUtils.randomAlphanumeric(1));
        }
        return password.toString();
    }

}