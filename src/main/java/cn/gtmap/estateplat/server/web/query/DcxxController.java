package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcHst;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjZdZdt;
import cn.gtmap.estateplat.server.core.service.BdcHstService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ImageUtil;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zuozhengwei
 * Date: 15-3-29
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/dcxx")
public class DcxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcHstService bdcHstService;
    @Autowired
    private BdcXmService bdcXmService;


    /*sc调查信息综合版**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid, String wiid) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(wiid)) {
            bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(wiid);
        } else if (StringUtils.isNotBlank(proid)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if (bdcBdcdy != null) {
                bdcBdcdyList = new ArrayList<BdcBdcdy>();
                bdcBdcdyList.add(bdcBdcdy);
            }
        }
        if (CollectionUtils.isEmpty(bdcBdcdyList))
            throw new NullPointerException();

        List<BdcBdcdy> tempBdcdyList = new ArrayList<BdcBdcdy>();
        //合并流程两个项目关联同一个不动产单元，需要去除重复值
        for (int i = 0; i < bdcBdcdyList.size(); i++) {
            if (!tempBdcdyList.contains(bdcBdcdyList.get(i))) {
                tempBdcdyList.add(bdcBdcdyList.get(i));
            }
        }
        bdcBdcdyList.clear();
        bdcBdcdyList.addAll(tempBdcdyList);

        String bdcdyh = "";
        String fwopen = "";
        String zdopen = "";
        String lqopen = "";
        String cbopen = "";
        String bdclx = bdcdyService.getBdclxByPorid(proid);
        if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
            bdcdyh = bdcBdcdyList.get(0).getBdcdyh();
            model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdyList.get(0).getBdcdyh());
            model.addAttribute(ParamsConstants.BDCDYID_LOWERCASE, bdcBdcdyList.get(0).getBdcdyid());
        }

        if (StringUtils.isNotBlank(bdclx)) {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                fwopen = "1";
                zdopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                zdopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
                zdopen = "1";
                lqopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_QT)) {
                zdopen = "1";
                cbopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_HY) || StringUtils.equals(bdclx, Constants.BDCLX_HYWJM)) {
                return "query/dcxxHy";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TDZJGZW) && StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() == 28 && StringUtils.equals(bdcdyh.substring(19, 20), "F")) {
                fwopen = "1";
                zdopen = "1";
            } else {
                return "query/dcxxTd";
            }
            model.addAttribute(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        }
        if (StringUtils.isBlank(bdclx) && CollectionUtils.isEmpty(bdcBdcdyList)) {
            return "main/error";
        }
        model.addAttribute("cbopen", cbopen);
        model.addAttribute("fwopen", fwopen);
        model.addAttribute("zdopen", zdopen);
        model.addAttribute("lqopen", lqopen);

        if (bdcBdcdyList != null && bdcBdcdyList.size() > 1) {
            return "query/dcxxList";
        }
        return "query/dcxx";
    }

    @RequestMapping(value = "queryfw", method = RequestMethod.GET)
    public String queryfw(Model model, String proid, String wiid, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        model.addAttribute(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return "query/dcxxFw";
    }

    @RequestMapping(value = "querytd", method = RequestMethod.GET)
    public String querytd(Model model, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return "query/dcxxTd";
    }

    @RequestMapping(value = "queryLq", method = RequestMethod.GET)
    public String queryLq(Model model, String proid) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        return "query/dcxxLq";
    }

    @RequestMapping(value = "queryCb", method = RequestMethod.GET)
    public String queryCb(Model model, String proid) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        return "query/dcxxCb";
    }

    @RequestMapping(value = "/selectHst", method = RequestMethod.GET)
    @ResponseBody
    public String selectHst(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) {
        OutputStream outStream = null;
        try {
            response.setContentType("application/x-wmf;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=HST.jpg");
            outStream = response.getOutputStream();
            if (StringUtils.isNotBlank(bdcdyh)) {
                List<BdcHst> bdcHstList = bdcHstService.selectBdcHst(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcHstList)) {
                    BdcHst bdcHst = bdcHstList.get(0);
                    if (bdcHst != null && bdcHst.getHst() != null) {
                        response.getOutputStream().write(bdcHst.getHst(), 0, bdcHst.getHst().length);
                    }
                }
            } else if (StringUtils.isNotBlank(proid)) {
                //zdd 临时性修改   需要确认页面对是否有bdcXm.bdcdyid 或者 bdcdyh 这样可以直接获取bdcdy
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                String bdcdyid = "";
                if (bdcXm != null)
                    bdcdyid = bdcXm.getBdcdyid();
                if (StringUtils.isNotBlank(bdcdyid)) {
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        List<BdcHst> bdcHstList = bdcHstService.selectBdcHst(bdcBdcdy.getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcHstList)) {
                            BdcHst bdcHst = bdcHstList.get(0);
                            if (bdcHst != null && bdcHst.getHst() != null) {
                                response.getOutputStream().write(bdcHst.getHst(), 0, bdcHst.getHst().length);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("DcxxController.selectHst", e);
        } finally {
            if (outStream != null) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    logger.error("DcxxController.selectHst", e);
                }
            }
        }
        return null;
    }


    @RequestMapping(value = "/selectZdt", method = RequestMethod.GET)
    @ResponseBody
    public String selectZdt(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) {
        DjsjZdZdt djsjZdZdt = null;
        OutputStream outStream = null;
        try {
            response.setContentType("application/x-wmf;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=ZDT.jpg");
            outStream = response.getOutputStream();
            if (StringUtils.isNotBlank(bdcdyh)) {
                List<DjsjZdZdt> djsjZdZdtList = bdcHstService.selectBdcZdt(StringUtils.substring(bdcdyh, 0, 19));
                if (CollectionUtils.isNotEmpty(djsjZdZdtList)) {
                    djsjZdZdt = djsjZdZdtList.get(0);
                    if (djsjZdZdt != null && djsjZdZdt.getZdt() != null) {
                        response.getOutputStream().write(djsjZdZdt.getZdt(), 0, djsjZdZdt.getZdt().length);
                    }
                }
            } else if (StringUtils.isNotBlank(proid)) {
                //zdd 临时性修改   需要确认页面对是否有bdcXm.bdcdyid 或者 bdcdyh 这样可以直接获取bdcdy
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                String bdcdyid = "";
                if (bdcXm != null) {
                    bdcdyid = bdcXm.getBdcdyid();
                }
                if (StringUtils.isNotBlank(bdcdyid)) {
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        List<DjsjZdZdt> djsjZdZdtList = bdcHstService.selectBdcZdt(StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19));
                        if (CollectionUtils.isNotEmpty(djsjZdZdtList)) {
                            djsjZdZdt = djsjZdZdtList.get(0);
                            if (djsjZdZdt != null && djsjZdZdt.getZdt() != null) {
                                response.getOutputStream().write(djsjZdZdt.getZdt(), 0, djsjZdZdt.getZdt().length);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("DcxxController.selectZdt", e);
        } finally {
            try {
                outStream.flush();
                outStream.close();
                outStream = null;
            } catch (IOException e) {
                logger.error("DcxxController.selectZdt", e);
            }
        }
        return null;
    }

    @RequestMapping(value = "getTp", method = RequestMethod.GET)
    public String getTp(Model model, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "xsfs", required = false) String xsfs) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        model.addAttribute("xsfs", xsfs);
        return "query/bdcXsTp";
    }

    @RequestMapping(value = "jzbsb", method = RequestMethod.GET)
    public String queryJzbsb(Model model, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return "query/jzbsb";
    }

    @ResponseBody
    @RequestMapping("/getJzbsbPagesJson")
    public Object getJzbsbPagesJson(Pageable pageable, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
            } else if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
        }
        return repository.selectPaging("getJzbsbListByPage", map, pageable);
    }

    @RequestMapping(value = "/viewHst", method = RequestMethod.GET)
    public String viewHst(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) {
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return "query/viewHst";
    }

    /**
     * 获取户室图
     *
     * @param bdcdyh   不动产单元号
     * @param response
     * @return
     */
    @RequestMapping(value = "/viewHstImg", method = RequestMethod.GET)
    @ResponseBody
    public String viewHst(@RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            response.setContentType("image/png;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=test.png");
            outputStream = response.getOutputStream();
            if (StringUtils.isNotBlank(bdcdyh)) {
                String bdcdylx = bdcdyService.getBdcdylxByBdcdyh(bdcdyh);
                if (StringUtils.isNotBlank(bdcdylx) && bdcdylx.equals("TDFW")) {
                    List<BdcHst> bdcHstList = bdcHstService.selectBdcHst(bdcdyh);
                    if (CollectionUtils.isNotEmpty(bdcHstList)) {
                        BdcHst bdcHst = bdcHstList.get(0);
                        if (bdcHst != null && bdcHst.getHst() != null) {
                            ByteArrayInputStream img = new ByteArrayInputStream(bdcHst.getHst());    //将b作为输入流；
                            BufferedImage image = ImageIO.read(img);
                            inputStream = ImageUtil.rotateImg(image, 90, null);//旋转90度显示
                            byte[] imgByte = ImageUtil.input2byte(inputStream);
                            outputStream.write(imgByte, 0, imgByte.length);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("DcxxController.viewHst", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    logger.error("DcxxController.viewHst", e);
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("DcxxController.viewHst", e);
                }
            }
        }
        return null;
    }

    /**
     * 根据不动产类型获取宗地图（TDFW类型的旋转90度显示，TD直接显示）
     *
     * @param bdcdyh   不动产单元号
     * @param response
     * @return
     */
    @RequestMapping(value = "/viewZdtImg", method = RequestMethod.GET)
    @ResponseBody
    public String viewZdt(@RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            response.setContentType("application/jpg;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=test.jpg");
            outputStream = response.getOutputStream();
            if (StringUtils.isNotBlank(bdcdyh)) {
                String bdcdylx = bdcdyService.getBdcdylxByBdcdyh(bdcdyh);
                if (StringUtils.isNotBlank(bdcdylx)) {
                    if (bdcdylx.equals("TDFW")) {
                        List<DjsjZdZdt> djsjZdZdtList = bdcHstService.selectBdcZdt(StringUtils.substring(bdcdyh, 0, 19));
                        if (CollectionUtils.isNotEmpty(djsjZdZdtList)) {
                            DjsjZdZdt bdcHst = djsjZdZdtList.get(0);
                            if (bdcHst != null && bdcHst.getZdt() != null) {
                                ByteArrayInputStream img = new ByteArrayInputStream(bdcHst.getZdt());    //将b作为输入流；
                                BufferedImage image = ImageIO.read(img);
                                inputStream = ImageUtil.rotateImg(image, 90, null);//旋转90度显示
                                byte[] imgByte = ImageUtil.input2byte(inputStream);
                                outputStream.write(imgByte, 0, imgByte.length);
                            }
                        }
                    } else if (bdcdylx.equals("TD")) {
                        List<DjsjZdZdt> djsjZdZdtList = bdcHstService.selectBdcZdt(StringUtils.substring(bdcdyh, 0, 19));
                        if (CollectionUtils.isNotEmpty(djsjZdZdtList)) {
                            DjsjZdZdt bdcHst = djsjZdZdtList.get(0);
                            if (bdcHst != null && bdcHst.getZdt() != null) {
                                ByteArrayInputStream img = new ByteArrayInputStream(bdcHst.getZdt());    //将b作为输入流；
                                BufferedImage image = ImageIO.read(img);
                                inputStream = ImageUtil.rotateImg(image, 0, null);
                                byte[] imgByte = ImageUtil.input2byte(inputStream);
                                outputStream.write(imgByte, 0, imgByte.length);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("DcxxController.viewZdt", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    logger.error("DcxxController.viewZdt", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("DcxxController.viewZdt", e);
                }
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/getDcxxListJson")
    public Object getDcxxListJson(Pageable pageable, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "dcxc", required = false) String dcxc) {
        HashMap map = new HashMap();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("hhSearch", StringUtils.deleteWhitespace(dcxc));
        }
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdclx())) {
                //参数修改为wiid @gtsy
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                    String[] bdcdyhs = new String[bdcBdcdyList.size()];
                    int i = 0;
                    for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                        bdcdyhs[i] = bdcBdcdy.getBdcdyh();
                        i++;
                    }
                    map.put("bdcdyhs", bdcdyhs);
                }
                if (StringUtils.equals(Constants.SQLX_ZJJZWDY_FW_DM, bdcXm.getSqlx())) {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, "YCTDFW");
                } else {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, bdcXm.getBdclx());
                }
            }
        }
        Page<Object> djBdcdyList = repository.selectPaging("getDjBdcdyListByPage", map, pageable);
        if (djBdcdyList != null && CollectionUtils.isNotEmpty(djBdcdyList.getRows())) {
            List<Object> rows = djBdcdyList.getRows();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (int i = 0; i < rows.size(); i++) {
                        ((Map) rows.get(i)).put("PROID", bdcXmList.get(i).getProid());
                    }
                }
            }
        }
        return djBdcdyList;
    }

    @RequestMapping(value = "/getDcxx", method = RequestMethod.GET)
    public String getDcxx(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                model.addAttribute("wiid", bdcXm.getWiid());
            }
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            model.addAttribute(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        }
        String fwopen = "";
        String zdopen = "";
        String lqopen = "";
        String cbopen = "";
        String bdclx = bdcdyService.getBdclxByPorid(proid);
        if (StringUtils.isNotBlank(bdclx)) {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                fwopen = "1";
                zdopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                zdopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
                zdopen = "1";
                lqopen = "1";
                cbopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_QT)) {
                zdopen = "1";
                cbopen = "1";
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TDZJGZW) && StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() == 28 && StringUtils.equals(bdcdyh.substring(19, 20), "F")) {
                fwopen = "1";
                zdopen = "1";
            } else {
                return "query/dcxxTd";
            }
        }
        model.addAttribute(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        model.addAttribute("cbopen", cbopen);
        model.addAttribute("fwopen", fwopen);
        model.addAttribute("zdopen", zdopen);
        model.addAttribute("lqopen", lqopen);
        return "query/dcxx";
    }


}
