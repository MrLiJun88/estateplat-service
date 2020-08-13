package cn.gtmap.estateplat.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
 * @Version 1.0
 * @description 1.0，2019/5/10
 */
public class Base64Util {
    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);

    /**
     * @param bytes
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description 数组 加密 为base64
     */
    public static String encodeByteToBase64Str(byte[] bytes) {

        String base64Str = "";
        if (null != bytes) {
            base64Str = Base64.encodeBase64String(bytes);
        }
        return base64Str;
    }



    /**
     * @param base64Str
     * @return
     * @description base64字符串解密为数组
     */
    public static byte[] decodeBase64StrToByte(String base64Str) {
        byte[] bytes = null;
        if (StringUtils.isNotBlank(base64Str)) {
            bytes = Base64.decodeBase64(base64Str);
        }
        return bytes;
    }

    public static byte[] encodeImageToByte(BufferedImage image) throws IOException {

        byte[] imageInByte = null;
        if (null != image) {
            //新建流。
            //ByteArrayOutputStream os = new ByteArrayOutputStream();
            //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            //ImageIO.write(image, "png", os);
            //data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
            //从流中获取数据数组。
            //data = os.toByteArray();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        }
        return imageInByte;
    }


    private Base64Util() {
    }


    private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
            -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
            38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
            -1, -1};


    /**
     * @param
     * @return
     * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
     * @description 将输入流改成 base64
     */
    public static String ImageToBase64ByIn(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        if(in !=null){
            byte[] data = null;
            // 读取图片字节数组
            try {
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(data != null){
                return encode(data);// 返回Base64编码过的字节数组字符串
            }
        }
        return "";
    }

    /**
     * 将字节数组编码为字符串
     *
     * @param data
     */
    public static String encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;

        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4)
                        | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4)
                    | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
                    | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }
}
