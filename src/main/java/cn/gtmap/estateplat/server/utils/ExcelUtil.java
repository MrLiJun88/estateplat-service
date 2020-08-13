package cn.gtmap.estateplat.server.utils;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/28
 * @description
 */
public class ExcelUtil {
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(ExcelUtil.class);
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 生成excel文件并下载到客户端
     */
    public static void downloadFileToClient(String outputPath, String fileName, HSSFWorkbook wb, HttpServletResponse response){
        FileOutputStream fos = null;
        InputStream fis = null;
        OutputStream out = null;
        try {
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String savePath = outputPath+fileName;
            fos = new FileOutputStream(savePath);
            wb.write(fos);
            File file = new File(savePath);
            fileName = file.getName();
            fis = new BufferedInputStream(new FileInputStream(savePath));
            byte buffer[] = new byte[fis.available()];
            fis.read(buffer);
            response.reset();
            response.setContentType("application/vnd.ms-excel");//设置生成的文件类型
            response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes(ParamsConstants.DEFAULT_CHARSET_UTF8), "ISO8859-1"));
            response.setHeader("Content-type","application-download");
            out = new BufferedOutputStream(response.getOutputStream());
            out.write(buffer);
            File deleteFile = new File(savePath);
            deleteFile.delete();
        } catch (Exception e) {
            log.error("/bdcGdxxServiceImpl/exportExcel");
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    log.error("/bdcGdxxServiceImpl/exportExcel");
                }
            }
            if(fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    log.error("/bdcGdxxServiceImpl/exportExcel");
                }
            }
            if(out != null) {
                try {
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    log.error("/bdcGdxxServiceImpl/exportExcel");
                }
            }
        }
    }
}
