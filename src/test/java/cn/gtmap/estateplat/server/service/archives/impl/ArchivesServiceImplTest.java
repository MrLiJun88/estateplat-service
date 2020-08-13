package cn.gtmap.estateplat.server.service.archives.impl;

import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.general.ModuleContext;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.report.ReportHelper;
import com.fr.report.module.EngineModule;
import com.fr.stable.WriteActor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/22
 * @description
 */
public class ArchivesServiceImplTest {
    @Test
    public void TestPushArchivesSpbJpgFile(){
        String reportEnvpath = "D:\\xinghuajian\\Tomcat\\tomcat_sz_yzk\\tomcat-7.0.52\\webapps\\report\\WEB-INF";
        String reportSpbCptPath = "\\print\\bdc_spb_archive.cpt";
        String pathName = "D:\\test\\";
        String proid = "28V948368FWVL606";
        if(StringUtils.isNotBlank(reportEnvpath) && StringUtils.isNotBlank(reportSpbCptPath) && StringUtils.isNotBlank(pathName))  {
            try {
                FRContext.setCurrentEnv(new LocalEnv(reportEnvpath));
                ModuleContext.startModule(EngineModule.class.getName());
                TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), reportSpbCptPath);
                //设置模版参数
                Map<String, String> parameterMap = new HashMap<String, String>();
                parameterMap.put("proid", proid);
                parameterMap.put("userid", "0");
                parameterMap.put("tdOrTdfwMj", "20");
                parameterMap.put("tdOrTdfwYt", "成套住宅");
                parameterMap.put("tdOrTdfwQlxz", "房地产权");
                ReportHelper.clearFormulaResult(workbook);
                pathName += proid + ".jpg";
                FileOutputStream outputStream = new FileOutputStream(new File(pathName));
                ImageExporter ImageExport = new ImageExporter();
                ImageExport.export(outputStream, workbook.execute(parameterMap,new WriteActor()));
                outputStream.close();
                ModuleContext.stopModules();
            } catch (Exception e) {
                System.out.println("生成审批表jpg图片文件失败！");
                System.out.println(e.getMessage());
            }
        }
    }

}
