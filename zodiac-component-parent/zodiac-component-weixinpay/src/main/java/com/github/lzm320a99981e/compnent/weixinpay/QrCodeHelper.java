package com.github.lzm320a99981e.compnent.weixinpay;

import com.github.lzm320a99981e.zodiac.tools.Codec;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Builder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 */
public class QrCodeHelper {
    @Builder
    public static class BitMatrixBuilder {
        private String content;
        private int width = 300;
        private int height = 300;
        private BarcodeFormat format = BarcodeFormat.QR_CODE;
        private static Map<EncodeHintType, Object> hints = new HashMap<>();

        static {
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");    //字符集，包含中文的话就要utf-8
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);    //纠错等级，等级越高图片越不清晰
            hints.put(EncodeHintType.MARGIN, 0);    //边距
        }

        public BitMatrix toBitMatrix() {
            try {
                // 二维码图片
                final BitMatrix matrix = new MultiFormatWriter().encode(content, format, width, height, hints);

                // 去除空白 left,top,width,height
                final int[] enclosingRectangle = matrix.getEnclosingRectangle();
                final int left = enclosingRectangle[0];
                final int top = enclosingRectangle[1];
                final int width = enclosingRectangle[2];
                final int height = enclosingRectangle[3];

                final BitMatrix newMatrix = new BitMatrix(width, height);
                newMatrix.clear();
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        if (matrix.get(i + left, j + top)) {
                            newMatrix.set(i, j);
                        }
                    }
                }

                return newMatrix;
            } catch (Exception e) {
                ExceptionHelper.rethrowRuntimeException(e);
            }
            return null;
        }

        public String toBase64String() {
            try {
                final ByteArrayOutputStream data = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(toBitMatrix(), "png", data);
                return Codec.createUseUtf8().encodeBase64String(data.toByteArray());
            } catch (Exception e) {
                ExceptionHelper.rethrowRuntimeException(e);
            }
            return null;
        }
    }
}
