package cool.zzy.sems.application.util;

import android.graphics.*;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/6 11:16
 * @since 1.0
 */
public class EAN13Utils {
    private static final String TAG = EAN13Utils.class.getSimpleName();
    // 图像宽度
    private static final int DEFAULT_IMAGE_WIDTH = 207;
    // 图像高度
    private static final int DEFAULT_IMAGE_HEIGHT = 98;
    // EAN13总块数
    private static final int EAN_13_CODE_LENGTH = 95;
    // EAN13字符总数
    private static final int EAN_13_CODE_CHAR_LENGTH = 13;
    // EAN13开始字符
    private static final byte[] EAN_13_CODE_START_CHAR = {1, 0, 1};
    // EAN13中间字符
    private static final byte[] EAN_13_CODE_CENTER_CHAR = {0, 1, 0, 1, 0};
    private static final byte[][] EAN_13_STRUCTURE_LEFT = {
            {1, 1, 1, 1, 1, 1},// 0, LLLLLL, 111111
            {1, 1, 0, 1, 0, 0},// 1, LLGLGG, 110100
            {1, 1, 0, 0, 1, 0},// 2, LLGGLG, 110010
            {1, 1, 0, 0, 0, 1},// 3, LLGGGL, 110001
            {1, 0, 1, 1, 0, 0},// 4, LGLLGG, 101100
            {1, 0, 0, 1, 1, 0},// 5, LGGLLG, 100110
            {1, 0, 0, 0, 1, 1},// 6, LGGGLL, 100011
            {1, 0, 1, 0, 1, 0},// 7, LGLGLG, 101010
            {1, 0, 1, 0, 0, 1},// 8, LGLGGL, 101001
            {1, 0, 0, 1, 0, 1},// 9, LGGLGL, 100101
    };

    private static final byte[][] L_CODE_PATTERN_LENGTH = {
            {0, 0, 0, 1, 1, 0, 1},// 0, 0001101
            {0, 0, 1, 1, 0, 0, 1},// 1, 0011001
            {0, 0, 1, 0, 0, 1, 1},// 2, 0010011
            {0, 1, 1, 1, 1, 0, 1},// 3, 0111101
            {0, 1, 0, 0, 0, 1, 1},// 4, 0100011
            {0, 1, 1, 0, 0, 0, 1},// 5, 0110001
            {0, 1, 0, 1, 1, 1, 1},// 6, 0101111
            {0, 1, 1, 1, 0, 1, 1},// 7, 0111011
            {0, 1, 1, 0, 1, 1, 1},// 8, 0110111
            {0, 0, 0, 1, 0, 1, 1},// 9, 0001011
    };

    private static final byte[][] G_CODE_PATTERN_LENGTH = {
            {0, 1, 0, 0, 1, 1, 1},// 0, 0100111
            {0, 1, 1, 0, 0, 1, 1},// 1, 0110011
            {0, 0, 1, 1, 0, 1, 1},// 2, 0011011
            {0, 1, 0, 0, 0, 0, 1},// 3, 0100001
            {0, 0, 1, 1, 1, 0, 1},// 4, 0011101
            {0, 1, 1, 1, 0, 0, 1},// 5, 0111001
            {0, 0, 0, 0, 1, 0, 1},// 6, 0000101
            {0, 0, 1, 0, 0, 0, 1},// 7, 0010001
            {0, 0, 0, 1, 0, 0, 1},// 8, 0001001
            {0, 0, 1, 0, 1, 1, 1},// 9, 0010111
    };

    private static final byte[][] R_CODE_PATTERN_LENGTH = {
            {1, 1, 1, 0, 0, 1, 0},// 0, 1110010
            {1, 1, 0, 0, 1, 1, 0},// 1, 1100110
            {1, 1, 0, 1, 1, 0, 0},// 2, 1101100
            {1, 0, 0, 0, 0, 1, 0},// 3, 1000010
            {1, 0, 1, 1, 1, 0, 0},// 4, 1011100
            {1, 0, 0, 1, 1, 1, 0},// 5, 1001110
            {1, 0, 1, 0, 0, 0, 0},// 6, 1010000
            {1, 0, 0, 0, 1, 0, 0},// 7, 1000100
            {1, 0, 0, 1, 0, 0, 0},// 8, 1001000
            {1, 1, 1, 0, 1, 0, 0},// 9, 1110100
    };


    private static final byte[] EAN_13_CHECK_DIGIT = {
            3, 1, 3,
            1, 3, 1, 3,
            1, 3, 1, 3
    };

    public static Bitmap drawEan13Code(String code) {
        return drawEan13Code(code, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    public static Bitmap drawEan13Code(String code, int width, int height) {
        if (code == null || code.length() != EAN_13_CODE_CHAR_LENGTH - 1) {
            throw new IllegalArgumentException("EAN 13 code format error.");
        }
        return createBitmap(code, width, height);
    }

    private static Bitmap createBitmap(String code, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // 画布颜色：白色
        canvas.drawColor(Color.WHITE);
        // 计算校验位
        code = generateCheckCode(code);
        // 生成二进制条形码
        byte[] barcodeCharArray = generateBarcode(code);
        // 画条形码
        drawCode(canvas, code, barcodeCharArray, width, height);
        return bitmap;
    }

    private static String generateCheckCode(String code) {
        int codeChar;
        int checkSum = 0;
        // 先把校验位算出来
        for (int i = 1; i < code.length(); i++) {
            codeChar = code.charAt(i) - '0';
            checkSum += codeChar * EAN_13_CHECK_DIGIT[i - 1];
        }
        checkSum += code.charAt(0) - '0';
        int currentCheckDigit = 10 - (checkSum % 10);
        if (currentCheckDigit == 10) {
            currentCheckDigit = 0;
        }
        return code + currentCheckDigit;
    }

    private static byte[] generateBarcode(String code) {
        int currentLength = 0;
        byte[] barcodeCharArray = new byte[EAN_13_CODE_LENGTH];
        // 开始字符
        System.arraycopy(EAN_13_CODE_START_CHAR, 0, barcodeCharArray, currentLength, EAN_13_CODE_START_CHAR.length);
        currentLength += EAN_13_CODE_START_CHAR.length;
        int codeChar = code.charAt(0) - '0';
        byte[] initCodeFormat = EAN_13_STRUCTURE_LEFT[codeChar];
        for (int i = 1; i < code.length(); i++) {
            codeChar = code.charAt(i) - '0';
            if (i < 7) {
                if (initCodeFormat[i - 1] == 1) {
                    System.arraycopy(L_CODE_PATTERN_LENGTH[codeChar], 0,
                            barcodeCharArray, currentLength, L_CODE_PATTERN_LENGTH[codeChar].length);
                    currentLength += L_CODE_PATTERN_LENGTH[codeChar].length;
                } else {
                    System.arraycopy(G_CODE_PATTERN_LENGTH[codeChar], 0,
                            barcodeCharArray, currentLength, G_CODE_PATTERN_LENGTH[codeChar].length);
                    currentLength += G_CODE_PATTERN_LENGTH[codeChar].length;
                }
                if (i == 6) {
                    // 中间分隔字符
                    System.arraycopy(EAN_13_CODE_CENTER_CHAR, 0,
                            barcodeCharArray, currentLength, EAN_13_CODE_CENTER_CHAR.length);
                    currentLength += EAN_13_CODE_CENTER_CHAR.length;
                }
            } else {
                System.arraycopy(R_CODE_PATTERN_LENGTH[codeChar], 0,
                        barcodeCharArray, currentLength, R_CODE_PATTERN_LENGTH[codeChar].length);
                currentLength += R_CODE_PATTERN_LENGTH[codeChar].length;
            }
        }
        // 结束字符和开始字符一样
        System.arraycopy(EAN_13_CODE_START_CHAR, 0,
                barcodeCharArray, currentLength, EAN_13_CODE_START_CHAR.length);
        return barcodeCharArray;
    }

    /**
     * EAN13总共13个码
     *
     * @param canvas           画布
     * @param code             条形码
     * @param barcodeCharArray 条形码二进制数据
     */
    private static void drawCode(Canvas canvas, String code, byte[] barcodeCharArray, int width, int height) {
        Paint paint = new Paint();
        // 设置颜色：黑色
        paint.setColor(Color.BLACK);
        Paint textPaint = new Paint();
        // 设置颜色：黑色
        textPaint.setColor(Color.BLACK);
        float textSize = height * 0.2F - (height * 0.015F);
        // 设置字体大小
        textPaint.setTextSize(textSize);
        // 设置字体风格：粗体
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置字体抗锯齿
        textPaint.setAntiAlias(true);
        float tempWidth = width * 0.92F;
        float prefixWidth = width - width * 0.92F;
        // 每个块的宽度
        float rectWidth = tempWidth / barcodeCharArray.length;
        // 每个块的高度
        float rectHeight = height - (height * 0.2F);
        int centerIndex = EAN_13_CODE_START_CHAR.length + L_CODE_PATTERN_LENGTH[0].length * 6;
        // 画块
        for (int i = 0; i < barcodeCharArray.length; i++) {
            if (barcodeCharArray[i] == 1) {
                float left = prefixWidth + i * rectWidth;
                float right = left + rectWidth;
                float top = 0F;
                float bottom = rectHeight;
                if (i < EAN_13_CODE_START_CHAR.length
                        || i > centerIndex && i < centerIndex + EAN_13_CODE_CENTER_CHAR.length
                        || i >= EAN_13_CODE_LENGTH - EAN_13_CODE_START_CHAR.length) {
                    bottom += height * 0.2F;
                }
                RectF rect = new RectF(left, top, right, bottom);
                canvas.drawRect(rect, paint);
            }
        }
        // 画字体
        for (int i = 0; i < code.length(); i++) {
            int x = 0;
            if (i > 0 && i < 7) {
                x = (int) (prefixWidth + EAN_13_CODE_START_CHAR.length * rectWidth + (i - 1) * rectWidth * 7);
            } else if (i >= 7) {
                x = (int) (prefixWidth + EAN_13_CODE_START_CHAR.length * rectWidth
                        + L_CODE_PATTERN_LENGTH[0].length * 6 * rectWidth
                        + EAN_13_CODE_CENTER_CHAR.length * rectWidth
                        + (i - 7) * rectWidth * 7);
            }
            canvas.drawText(String.valueOf(code.charAt(i)), x, rectHeight + textSize, textPaint);
        }
    }
}
