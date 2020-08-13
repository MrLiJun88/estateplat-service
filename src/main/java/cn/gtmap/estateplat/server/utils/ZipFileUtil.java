package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.core.ex.AppException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 压缩包及文件工具类
 */
public class ZipFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private ZipFileUtil(){

    }
    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param inputStream,saveFileDir
     * @return saveFileDir
     * @description 根据文件存放位置,以及文件的输入流解压缩
     */
    public static void uncompressZip(InputStream inputStream, String saveFileDir) {
        File dir = new File(saveFileDir);
        //如果路径下不存在该文件夹则创建文件夹
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取输入流,以及压缩包输入流
        InputStream is = inputStream;
        ZipArchiveInputStream zais = null;
        try {
            zais = new ZipArchiveInputStream(is, "GBK");
            ArchiveEntry archiveEntry = null;
            while ((archiveEntry = zais.getNextEntry()) != null) {
                //获取文件名
                String entryFileName = archiveEntry.getName();
                //构造解压出来的文件存放路径
                String entryFilePath = saveFileDir + entryFileName;
                OutputStream os = null;
                try {
                    //把解压出来的文件写到指定路径
                    File entryFile = new File(entryFilePath);
                    //检测压缩包下每一项文件,如果是文件夹则创建文件夹
                    validateFileOrDirectory(entryFilePath);
                    //如果指定路径下不存在同名文件则写文件至指定路径
                    if (!entryFile.exists()) {
                        os = new BufferedOutputStream(new FileOutputStream(entryFile));
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = zais.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                        }
                    }
                } catch (IOException e) {
                    throw new AppException("无法将压缩包下文件写入指定文件夹!");
                } finally {
                    //关闭流
                    if (os != null) {
                        os.flush();
                        os.close();
                        os = null;
                    }
                }
            }
        } catch (Exception e) {
            throw new AppException("解压压缩包出错!");
        } finally {
            //关闭流
            if(zais != null){
                closeInputStream(zais);
            }
            if(is != null){
                closeInputStream(is);
            }
        }
    }

    public static void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
            inputStream = null;
        } catch (IOException e) {
            logger.error("ZipFileUtil.closeInputStream",e);
        }
    }

    public static String appendFileSeparator(String filePath) {
        if (!filePath.endsWith("\\") && !filePath.endsWith("/")) {
            filePath += File.separator;
        }
        return filePath;
    }

    private static void validateFileOrDirectory(String entryFilePath) {
        String separator = null;
        if (entryFilePath.lastIndexOf('/') == -1) {
            separator = "\\";
        } else {
            separator = "/";
        }
        File file = new File(entryFilePath.substring(0, entryFilePath.lastIndexOf(separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
