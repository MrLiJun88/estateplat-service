package cn.gtmap.estateplat.server.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.swetake.util.Qrcode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Random;

import static cn.gtmap.estateplat.server.utils.QRCodeGenerator.mkdirs;

/**
 * Created by sunchao on 2016-5-28.
 * 生成二维码
 */
public class GetQRcode {
    private static final Logger logger = LoggerFactory.getLogger(GetQRcode.class);
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private GetQRcode(){

    }
    public static void encoderQRCode(String content, String imgPath, HttpServletResponse response) {
        try {
            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('L');
            qrcodeHandler.setQrcodeEncodeMode('B');
            String s = URLDecoder.decode(content, "utf-8");
            byte[] contentBytes = s.getBytes("utf-8");
            int picSize = 0;
            if (contentBytes.length < 124) {
                qrcodeHandler.setQrcodeVersion(8);
                picSize = 148;
            }
            BufferedImage bufImg = new BufferedImage(picSize, picSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, picSize, picSize);
            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                logger.info("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
            }
            gs.dispose();
            bufImg.flush();
            // 生成二维码QRCode图片
            if (StringUtils.isNotBlank(imgPath)) {
                //生成下载图片
                File imgFile = new File(imgPath);
                ImageIO.write(bufImg, "jpg", imgFile);
            } else {
                //生成IO流
                ImageIO.write(bufImg, "jpg", response.getOutputStream());
            }
        } catch (Exception e) {
            logger.error("GetQRcode.encoderQRCode",e);
        }
    }

    public static void encoderQRCode2(String content, String imgPath, HttpServletResponse response) {
        try {
            BufferedImage image = QRCodeGenerator.createImage(content, imgPath,
                    true);
            ImageIO.write(image, "jpg",response.getOutputStream());

        } catch (Exception e) {
            logger.error("GetQRcode.encoderQRCode",e);
        }
    }
}
