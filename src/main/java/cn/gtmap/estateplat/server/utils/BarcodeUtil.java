package cn.gtmap.estateplat.server.utils;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/5/4
 * @description 条形码工具类
 */
public class BarcodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private BarcodeUtil(){

    }
    /**
     * 128条形码
     * @param strBarCode 条形码内容：0-100位
     * @param dimension  条形码精度
     * @param barheight  条形码高度
     * @param wideRatio  条形码宽度
     */
    public static void generateBarCode128(HttpServletResponse response,String strBarCode,String dimension,String barheight,String wideRatio) {
        try {
            BufferedImage bi = null;
            JBarcode productBarcode = new JBarcode(Code128Encoder.getInstance(),
                    WidthCodedPainter.getInstance(),
                    EAN13TextPainter.getInstance());

            // 尺寸，面积，大小 密集程度
            productBarcode.setXDimension(Double.parseDouble(dimension));
            // 高度 10.0 = 1cm 默认1.5cm
            productBarcode.setBarHeight(Double.parseDouble(barheight));
            // 宽度
            productBarcode.setWideRatio(Double.parseDouble(wideRatio));
//                  是否显示字体
            productBarcode.setShowText(true);
//                 显示字体样式
            productBarcode.setTextPainter(BaseLineTextPainter.getInstance());

            //生成条形码
            bi = productBarcode.createBarcode(strBarCode);
            ImageIO.write(bi, "jpg", response.getOutputStream());
        } catch (Exception e) {
            logger.error("BarcodeUtil.generateBarCode128",e);
        }
    }

}
