package cn.gtmap.estateplat.server.web.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.gtis.config.AppConfig;


/**
 * 多sheet Excel 导出 view
 * @author  wuhongrui
 * 2016年11月1日
 * @version v1.0 
 * @description
 */
public class ViewExcel extends AbstractExcelView{

	public static final String PARAMETER_QLRMC = "QLRMC";
	public static final String PARAMETER_QLRLXDH = "QLRLXDH";
	public static final String PARAMETER_ROWNUM = "ROWNUM";

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(@SuppressWarnings("rawtypes")Map model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<List<Map<String,Object>>> rowsList = (List<List<Map<String,Object>>>) model.get("rowList");
		List sqlxList =  (List) model.get("sqlxList");
		
		String cxqssj = (String) model.get("cxqssj");
		String cxjssj = (String) model.get("cxjssj");
		String qsy="";
		String qsm="";
		String qsd="";
		String jsy="";
		String jsm="";
		String jsd="";
		if(StringUtils.isNotBlank(cxqssj)){
		 qsy = cxqssj.substring(0,4);
		 qsm = cxqssj.substring(5,7);
		 qsd = cxqssj.substring(8,10);
		}else{
			 qsy=" ";
			 qsm=" ";
			 qsd=" ";
		}
		
		if(StringUtils.isNotBlank(cxjssj)){
		 jsy = cxjssj.substring(0,4);
		 jsm = cxjssj.substring(5,7);
		 jsd = cxjssj.substring(8,10);
		}else{
			 jsy=" ";
			 jsm=" ";
			 jsd=" ";
		}
				
		if (CollectionUtils.isNotEmpty(rowsList)) {
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("yyyy-mm-dd hh:mm:ss"));
			// excel name
		     
			String excelName = AppConfig.getProperty("excelName.order");
			String excelWbjyy = AppConfig.getProperty("excelWbjyy.order");
			// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(excelName.getBytes("gb2312"), "iso8859-1" )+ ".xls");
			// sheet name
			for (int i = 0; i < rowsList.size(); i++) {
				if (rowsList.get(i).size() > 0) {
					String sheetmc = (String) sqlxList.get(i);
					HSSFSheet sheet = workbook.createSheet(sheetmc);
					sheet.createFreezePane(0,5);
					
					// 表头设置字体   
					HSSFFont headfont = workbook.createFont();
					headfont.setFontName("宋体");
					headfont.setFontHeightInPoints((short) 22);// 字体大小
					headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
					
					// 表头标题样式
					HSSFCellStyle headstyle = workbook.createCellStyle();
					headstyle.setFont(headfont);
					headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
					headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
					headstyle.setLocked(true);
					headstyle.setWrapText(true);

					//列表头字体
					HSSFFont columnHeadFont = workbook.createFont();
					columnHeadFont.setFontName("宋体");
					columnHeadFont.setFontHeightInPoints((short) 10);
					columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					
					// 列头的样式
					HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
					columnHeadStyle.setFont(columnHeadFont);
					columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
					columnHeadStyle
							.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
					columnHeadStyle.setLocked(true);
					columnHeadStyle.setWrapText(true);
					// 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
					columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);
					HSSFFont font = workbook.createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short) 10);
					// 普通单元格样式
					HSSFCellStyle style = workbook.createCellStyle();
					style.setFont(font);
					style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
					style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
					style.setWrapText(true);
					style.setLeftBorderColor(HSSFColor.BLACK.index);
					style.setBorderLeft((short) 1);
					style.setRightBorderColor(HSSFColor.BLACK.index);
					style.setBorderRight((short) 1);
					style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
					style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
					//style.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
					// 另一个样式
					HSSFCellStyle centerstyle = workbook.createCellStyle();
					centerstyle.setFont(font);
					centerstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左对齐
					centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
					centerstyle.setWrapText(true);
					centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．

					try {   
						// 创建第一行
						HSSFRow row0 = sheet.createRow(0);
						// 设置行高
						row0.setHeight((short) 600);
						// 创建第一列
						HSSFCell cell0 = row0.createCell(0);
						cell0.setCellValue(new HSSFRichTextString(
								"国土不动产登记窗口办件情况登记表"));
						cell0.setCellStyle(headstyle);
						//
						CellRangeAddress range = new CellRangeAddress(0, 0, 0,8);
						sheet.addMergedRegion(range);
						// 创建第二行
						HSSFRow row1 = sheet.createRow(1);
						HSSFCell cell1 = row1.createCell(0);
						cell1.setCellValue(new HSSFRichTextString(
								qsy+" "+"年"+" "+qsm+" "+"月"+" "+ qsd +"日 --"+" "+jsy+" "+"年"+" "+jsm+" "+"月"+" "+jsd+"日 "));
						cell1.setCellStyle(centerstyle);
						// 合并单元格
						range = new CellRangeAddress(1, 2, 0, 8);
						sheet.addMergedRegion(range);
						// 第三行
						HSSFRow row2 = sheet.createRow(3);
						row2.setHeight((short) 500);

					for (int j = 0; j < rowsList.get(i).size(); j++) {
						HSSFRow row = sheet.createRow(j+4);
						if (0 == j) {
							row.createCell(0).setCellStyle(columnHeadStyle);
							ArrayList<String> arrayList = new ArrayList<String>();
							arrayList.add("项目名称");
							arrayList.add("时限要求");
							arrayList.add("序号");
							arrayList.add("申请人姓名");
							arrayList.add("联系电话");
							arrayList.add("受理日期");
							arrayList.add("办结日期");
							arrayList.add("未办结原因");
							arrayList.add("备注");

							for (int k = 0; k < 9; k++) {
								row.createCell(k).setCellValue(
										arrayList.get(k));
								sheet.setColumnWidth(k, 300* 15);
							}
   				} else if (j > 0) {
   					row.createCell(0).setCellValue(
   							rowsList.get(i).get(j-1).get("SQLX") ==null || rowsList.get(i).get(j).get("SQLX") =="" ? "":rowsList.get(i).get(j - 1).get("SQLX").toString());
   					row.createCell(1).setCellValue(
   							rowsList.get(i).get(j-1).get("BLSX") == null || rowsList.get(i).get(j).get("BLSX") =="" ? "" :rowsList.get(i).get(j - 1).get("BLSX").toString());
   					row.createCell(2).setCellValue(
   							rowsList.get(i).get(j-1).get(PARAMETER_ROWNUM) == null || rowsList.get(i).get(j).get(PARAMETER_ROWNUM) =="" ? "" :rowsList.get(i).get(j - 1).get(PARAMETER_ROWNUM).toString());
   					row.createCell(3).setCellValue(
   							rowsList.get(i).get(j-1).get(PARAMETER_QLRMC) ==null || rowsList.get(i).get(j-1).get(PARAMETER_QLRMC) =="" ? "":rowsList.get(i).get(j - 1).get(PARAMETER_QLRMC).toString());
   					row.createCell(4).setCellValue(
   							rowsList.get(i).get(j-1).get(PARAMETER_QLRLXDH) ==null || rowsList.get(i).get(j-1).get(PARAMETER_QLRLXDH)== "" ? "":rowsList.get(i).get(j - 1).get(PARAMETER_QLRLXDH).toString());
   					row.createCell(5).setCellValue(
   							rowsList.get(i).get(j-1).get("CJSJ")==null || rowsList.get(i).get(j).get("CJSJ")=="" ? "":rowsList.get(i).get(j - 1).get("CJSJ").toString());
   					row.createCell(6).setCellValue(
   							rowsList.get(i).get(j-1).get("BJSJ")==null || rowsList.get(i).get(j).get("BJSJ")=="" ? "":rowsList.get(i).get(j - 1).get("BJSJ").toString());
   					row.createCell(7).setCellValue(
   							rowsList.get(i).get(j-1).get("BJSJ")==null || rowsList.get(i).get(j).get("BJSJ")=="" ? excelWbjyy :"");
   					row.createCell(8).setCellValue(
   							rowsList.get(i).get(j-1).get("BZ")==null || rowsList.get(i).get(j).get("BZ")=="" ? "":rowsList.get(i).get(j - 1).get("WBJYY").toString());
							}
						}
					} catch (Exception e) {
						logger.error("ViewExcel.buildExcelDocument",e);
					}

				}
			}
		}
	}
}
