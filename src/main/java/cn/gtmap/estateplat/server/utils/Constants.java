package cn.gtmap.estateplat.server.utils;


/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 15-3-18
 * Time: 下午7:06
 * Des:常量配置
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String BDCQZS_BH_FONT = "不动产权";
    public static final String BDCQZS_BH_DM = "zs";
    public static final String BDCQZM_BH_FONT = "不动产证明";
    public static final String BDCQZM_BH_DM = "zms";
    public static final String BDCDZZZZMS_BH_DM = "dzzzzms";
    public static final String BDCQSCDJZ_BH_FONT = "首次登记证";
    public static final String BDCQSCDJZ_BH_DM = "scdjz";
    public static final String BDCQSCDJXX_BH_DM = "scdjxx";
    public static final String BDCZSBH_SYQK_YSY = "1";
    public static final String BDCZSBH_SYQK_WSY = "0";
    public static final String BDCZSBH_SYQK_ZF = "2";
    public static final String BDCZSBH_SYQK_LS = "3";
    public static final String BDCZSBH_SYQK_YS = "4";//遗失
    public static final String BDCZSBH_SYQK_XH = "5";//销毁
    public static final String BDCQ_BH_LEFT_BRACKET = "(";
    public static final String BDCQ_BH_RIGHT_BRACKET = ")";
    public static final String BDCQZS_BH_XL = "sq_zscqz_lsh";//不动产权证序列名字
    public static final String BDCQZM_BH_XL = "sq_zsczm_lsh";//不动产权证明序列名字
    //zdd  选择地籍库不动产单元 对应的不动产类型  视图（djsj_bdcdy）
    public static final String BDCLX_TD = "TD";
    public static final String BDCLX_TDANDFW = "TDANDFW";
    public static final String BDCLX_TD_CHA = "土地";
    public static final String BDCLX_LQ = "TDSL";
    public static final String BDCLX_TDFW = "TDFW";
    public static final String BDCLX_TDFW_CHA = "土地房屋";
    public static final String BDCLX_TDGZW = "TDGZW";
    public static final String BDCLX_TDZJGZW = "TDZJGZW";//在建建筑构筑物
    public static final String BDCLX_TDSL = "TDSL";
    public static final String BDCLX_TDSL_CHA = "林权";
    public static final String BDCLX_QT = "TDQT";
    public static final String BDCLX_TDQT = "TDQT";
    public static final String BDCLX_HY = "HY";
    public static final String BDCLX_HY_CHA = "海域";
    public static final String BDCLX_HYWJM = "HYWJM";
    public static final String BDCLX_HYFW = "HYFW";
    public static final String BDCLX_HYGZW = "HYGZW";
    public static final String BDCLX_HYSL = "HYSL";
    public static final String BDCLX_HYQT = "HYQT";
    public static final String SIGN = "%";
    //    文件中心
    public static final String WORK_FLOW_STUFF = "WORK_FLOW_STUFF";
    public static final String TOKEN = "whosyourdaddy";
    /*Table_Name**/
    public final static String NAME_LQ = "BDC_LQ";
    public final static String NAME_TDCBNYDSYQ = "BDC_TDCBNYDSYQ";
    public final static String NAME_FDCQ = "BDC_FDCQ";
    public final static String NAME_FDCQDZ = "BDC_FDCQ_DZ";
    public final static String NAME_JSYDZJDSYQ = "BDC_JSYDZJDSYQ";
    public final static String NAME_TDSYQ = "BDC_TDSYQ";
    public final static String NAME_HYSYQ = "BDC_HYSYQ";
    //在建建筑物抵押申请类型
    public static final String[] QLLX_TDFW = {"4", "6", "8"};

    //工作流事件处理类
    public final static String EVENT_TYPE_WORKFLOW_BEGIN = "WorkFlow_Begin";           //工作流创建以后执行本事件
    public final static String EVENT_TYPE_WORKFLOW_BEFORETURN = "WorkFlow_BeforeTurn";  //转发之前执行本事件
    public final static String EVENT_TYPE_WORKFLOW_TURN = "WorkFlow_Turn";               //转发之后执行本事件
    public final static String EVENT_TYPE_WORKFLOW_BACK = "WorkFlow_Back";      //回退之后执行本事件
    public final static String EVENT_TYPE_WORKFLOW_STOP = "WorkFlow_Stop";      //中止之后执行本事件
    public final static String EVENT_TYPE_WORKFLOW_DEL = "WorkFlow_Del";           //工作流删除后执行本事件
    public final static String EVENT_TYPE_WORKFLOW_END = "WorkFlow_End";               //工作流办结束后执行本事件
    //    zx 权利人类型
    public static final String QLRLX_QLR = "qlr";
    public static final String QLRLX_YWR = "ywr";
    public static final String QLRLX_JKR = "jkr";
    public static final String QLRLX_DYQR = "dyqr";
    public static final String QLRXZ_GR = "1"; //个人
    public static final String QLRXZ_QY = "2";  //企业
    public static final String QLRXZ_SQDW = "3"; //事业单位
    public static final String QLRXZ_GJJG = "4";  //国家机关
    public static final String QLRZJHLX_ZZJG = "6"; //证件类型，组织机构
    public static final String QLRZJHLX_YYZZ = "7"; //证件类型，营业执照
    //审核人类型
    public static final String SHRLX_CSR_MC = "csr";
    public static final String SHRLX_FSR_MC = "fsr";
    public static final String SHRLX_HDR_MC = "hdr";

    public static final String XMZT_LS = "0";
    //zdd 项目处于缮证状态
    public static final String XMZT_SZ = "2";
    //    zx审批表单位
    public static final String DW_PFM = "1";
    public static final String DW_M = "2";
    public static final String DW_GQ = "3";
    //sc 共有情况默认值
    public static final String GYQK_DDGY = "0";
    public static final String GYQK_QTGY = "3";
    //    zx登记类型代码
    public static final String DJLX_CSDJ_DM = "100";
    public static final String DJLX_ZYDJ_DM = "200";
    public static final String DJLX_BGDJ_DM = "300";
    public static final String DJLX_ZXDJ_DM = "400";
    public static final String DJLX_GZDJ_DM = "500";
    public static final String DJLX_YYDJ_DM = "600";
    public static final String DJLX_YGDJ_DM = "700";
    public static final String DJLX_CFDJ_DM = "800";
    public static final String DJLX_QTDJ_DM = "900";
    public static final String DJLX_DYDJ_DM = "1000";
    public static final String DJLX_HBDJ_DM = "999";

    //sc预告sqlx代码
    public static final String SQLX_YG_YGSPF = "700";//预购商品房预告登记
    public static final String YGDJZL_YGSPF = "1";//预购商品房预告登记
    public static final String YGDJZL_QTBDCMMYG = "2";//其他不动产买卖预告
    public static final String YGDJZL_YGSPFDY = "3";//预购商品房抵押权预告登记
    public static final String YGDJZL_QTYGSPFDY = "4";//其它不动产抵押权预告登记
    public static final String SQLX_YG_YGSPFDY = "702";//预购商品房抵押权预告登记
    public static final String SQLX_YG_BDCDY = "707";//不动产抵押预告登记
    public static final String SQLX_YG_DY = "706";//预告和预告抵押合并流程
    public static final String SQLX_YG_DY_XS = "9981803";//预告和预告抵押合并流程(线上)
    public static final String SQLX_YG_DYPL = "715";//预告抵押批量
    public static final String SQLX_YZX = "135";//预告商品房预转现合并流程
    public static final String SQLX_CLF = "218";//商品房房转移、抵押组合
    public static final String SQLX_CLF_ZDYD = "9980424"; //存量房转移、抵押组合
    public static final String SQLX_ZY_DYBG = "219";//转移、抵押变更组合

    public static final String SQLX_YG_FWZYYG = "701";//房屋（构筑物）转移预告登记
    public static final String SQLX_YG_GYJSYT = "704";//国有建设用地使用权转移预告登记
    public static final String SQLX_YG_JSYTJT = "704";//集体建设用地使用权转移预告登记
    public static final String SQLX_YG_ZX = "799";//预告注销登记
    public static final String SQLX_YGDY_ZX = "7102704";//预告抵押注销登记
    public static final String SQLX_MMYG = "7002705";//买卖预告，存量房的预告

    //土地使用权人，全体业主
    public static final String TDSYQR_QTYZ = "全体业主";

    //司法裁定申请类型
    public static final String SQLX_SFCD = "8009910";
    //批量司法裁定
    public static final String SQLX_SFCD_PL = "9930005";


    //司法裁定转移申请类型
    public static final String SQLX_ZY_SFCD = "2003301";

    //地役权注销登记
    public static final String SQLX_DY_ZX = "409";
    //地役权变更登记
    public static final String SQLX_DY_BG = "335";
    //地役权转移登记
    public static final String SQLX_DY_ZY = "234";
    //土地证面积核减
    public static final String SQLX_BG_TDMJHJ = "343";
    //不动产权面积核减
    public static final String SQLX_BG_BDCMJHJ = "342";
    //异议登记
    public static final String SQLX_YYDJ_DM = "601";

    //房屋租赁
    public static final String SQLX_FWZL = "907";

    //房屋部分转移
    public static final String SQLX_FWBFZY_DM = "226";

    //项目状态已办结
    public static final String XMZT_BJ = "1";
    //项目状态不予登记
    public static final String XMZT_BYDJ = "4";
    //项目状态不予受理
    public static final String XMZT_BYSL = "6";

    //ZX登记类型代码
    public static final String SQLX_GZ_DM = "501";
    public static final String SQLX_YSBZ_DM = "9940001";
    public static final String SQLX_YSBZ_OLDDM = "902";
    public static final String SQLX_YSBZ_ZM_DM = "9940002";
    public static final String SQLX_YZQHZT_ZX_DM = "9920016";

    public static final String SQLX_HZ_DM = "901";
    public static final String SQLX_PLHZ_DM = "9950003";
    public static final String SQLX_PLYSBZ_DM = "9940004";
    public static final String SQLX_SPFGYSCDJ_DM = "130";
    public static final String SQLX_SPFGYSCDJ_MC = "商品房及业主共有部分首次登记";

    //ZX地役权
    public static final String SQLX_ZXYY_DM = "6009901";

    public static final String SQLX_JSYY_DM = "603";


    public static final String SQLX_DY_DM = "122";
    public static final String SQLX_FWFGHBBG_DM = "9960401";
    public static final String SQLX_FWFGHBZY_DM = "9960402";
    public static final String SQLX_TDFGHBBG_DM = "9960301";
    public static final String SQLX_TDFGHBZY_DM = "9960302";
    public static final String[] SQLX_FGHB_DM = {"9960401", "9960402", "9960301", "9960302"};
    public static final String SQLX_CQDJHZ_DM = "903";
    public static final String SQLX_HZDY_DM = "904";//换证抵押组合登记sqlx代码
    public static final String SQLX_DYHZ_DM = "9980405";//带抵押换证组合登记sqlx代码
    public static final String SQLX_DYBG_DM = "9980409";//带抵押变更组合登记申请类型代码
    public static final String SQLX_YSBZDY_DM = "9980408";//遗失补证抵押合办
    public static final String SQLX_BGZY_DM = "9980445";//变更登记和转移登记合并申请类型代码
    public static final String SQLX_ZYQTZY_DM = "9980446";//变更登记和转移登记合并申请类型代码

    //房屋（构筑物）首次，抵押权首次登记
    public static final String SQLX_SC_DY = "9980411";
    //jyl  换证东台特殊，单一权利转移不动产换发证
    public static final String SQLX_DT_DYQLZY_DM = "949";
    //jyl  换证东台特殊，单一权利变更不动产换发证
    public static final String SQLX_DT_DYQLBG_DM = "950";

    //转移登记忽略义务人
    public static final String[] YSQLX_ZYDJ_DM = {"233", "225", "226", "227", "228", "229", "230", "231", "232", "1003", "1004", "1010", "1016"
            , "223", "211", "212", "213", "214", "215", "217", "218", "219", "224", "234", "2001001", "2003501", "2001101", "2001201", "2003601", "9980424"};

    //在建建筑物抵押申请类型
    public static final String[] SQLX_ZJJZW_DM = {"1015", "1016", "1017", "1018"};
    public static final String SQLX_ZJJZWDY_DM = "1015";
    public static final String SQLX_ZJJZW_ZY_DM = "1016";
    public static final String SQLX_ZJJZW_BG_DM = "1017";
    public static final String SQLX_ZJJZW_ZX_DM = "1018";
    //以房屋为主的在建工程
    public static final String[] SQLX_ZJJZW_FW_DM = {"9999905", "9999906", "9999907", "9999908", "9999910"};
    public static final String SQLX_ZJJZWDY_FW_DM = "9999905";
    public static final String SQLX_ZJJZW_ZY_FW_DM = "9999906";
    public static final String SQLX_ZJJZW_BG_FW_DM = "9999907";
    public static final String SQLX_ZJJZW_ZX_FW_DM = "9999908";


    public static final String[] SQLX_FWDYSCDJ_DM = {"1019", "1009", "9990413"};
    public static final String SQLX_SPFMMZYDJ_DM = "211";
    public static final String SQLX_FWDY_DM = "1019";//房屋（构筑物）抵押权首次登记
    public static final String SQLX_FWDY_XS_DM = "9990413";//房屋（构筑物）抵押权首次登记(线上)
    public static final String SQLX_TDDY_DM = "1001";//国有建设用地使用权抵押权首次登记
    public static final String SQLX_FWMM_DM = "212";
    public static final String SQLX_FWDYBG_DM = "1012";//房屋（构筑物）抵押权变更登记
    public static final String SQLX_HYDYBG_DM = "9991503";//海域使用权变更登记
    public static final String SQLX_LQDYBG_DM = "9991203";//林权抵押权变更登记
    public static final String SQLX_JTJSDYBG_DM = "9990703";//集体建设用地使用权抵押权变更登记
    public static final String SQLX_GYJSDYBG_DM = "1005";//国有建设用地使用权抵押权变更登记
    public static final String SQLX_NYDDYBG_DM = "";//农用地使用权抵押权变更登记

    public static final String SQLX_YG_YGSPFZY = "721";//预售商品房预告转移登记
    public static final String SQLX_YG_YGSPFBG = "722";//预售商品房预告变更登记
    public static final String SQLX_YG_YGDYZY = "723";//预告抵押权转移登记
    //public static final String SQLX_YG_YGDYBG = "724";//预告抵押权变更登记流程
    public static final String SQLX_YG_YGSPFZX = "725";//预售商品房预告注销登记
    public static final String SQLX_YG_YGDYZX = "726";//预告抵押权注销登记
    public static final String SQLX_DY_GDDY = "1050";//带入的抵押变更流程
    public static final String SQLX_CF_GDCF = "810";//带入的查封变更流程
    public static final String SQLX_YG_GFYG = "710";//带入的预告变更流程

    //匹配带入的抵押、查封、预告变更流程不验证必填项
    public static final String[] SQLX_DRBG = new String[]{"810", "1050", "710"};

    //证明的变更或转移为Yfczh赋值
    public static final String[] SQLX_YFCZH = new String[]{"1003", "1005", "1010", "1012", "1016", "1017", "9990702", "9990703", "1042", "1041", "9991503",
            "9991502", "9990902", "9990903", "9991203", "9991202", "9980405", "904", "335", "234"};

    //预告权利人验证的对照
    public static final String[] SQLX_YGQLR_DZ = {"211-700", "211-701", "212-701", "215-704", "217-704", "215-701", "1019-700", "1019-701"};

    //需要继承权利人预告的转移流程
    public static final String[] SQLX_ZYDJ_DM = new String[]{"211", "215", "217", "212", "1019", "9990413"};

    //需要继承权利人预告登记
    public static final String[] SQLX_YGDJ_DM = new String[]{"708", "710", "723", "7501", "722", "7003", "7001", "712", "745"};

    //衡水新增流程的预告登记
    public static final String[] SQLX_HSYGDJ_DM = new String[]{"7002", "7001", "7003", "710", "721", "722", "708", "711", "712", "745", "724", "723"};

    //预告转移登记
    public static final String[] SQLX_YGZY_DM = new String[]{"700", "704", "705", "701", "702"};

    //zx针对抵押单独是登记流程，变更登记名称
    public static final String[] DJLX_DY_BGDJ_SQLXDM = new String[]{"1005", "1006", "1013", "1012", "1031"};
    //    转移登记名称
    public static final String[] DJLX_DY_ZYDJ_SQLXDM = new String[]{"1003", "1004", "1010", "1011", "1022", "9991202"};

    //    注销登记名称
    public static final String[] DJLX_DY_ZXDJ_SQLXDM = new String[]{"803", "807", "1007", "1014", "1023", "1027", "1025", "1021", "4009901", "4009902", "4009903", "4009904", "9979999", "9930004", "9979912", "8009910", "799", "1018", "402", "403", "404", "409", "415", "7102704","9930005"};

    //抵押权注销登记
    public static final String[] DJLX_DYAQ_ZXDJ_SQLXDM = new String[]{"1007", "1014", "4009903", "4009904", "9979999", "9990704"};

    //抵押权注销登记
    public static final String[] DJLX_DYAQ_ZXDJ_NEEDDJZX_SQLXDM = new String[]{"1007", "1014", "4009904", "9979999", "9990704"};

    //解封登记申请类型集合
    public static final String[] DJLX_CFDJ_JF_SQLXDM = new String[]{"803", "807", "8009902", "8009904"};

    //最高抵押
    public static final String[] DJLX_DY_ZGDY_SQLXDM = new String[]{"1002", "1004", "1006", "1009", "1011", "1013"};

    //zdd 批量抵押登记产生一本证书业务对应的申请类型代码
    public static final String DJLX_PLDY_YBZS_SQLXDM = "1028";
    //jyl 批量抵押登记产生多本证书业务对应的申请类型代码
    public static final String DJLX_PLDY_DBZS_SQLXDM = "1038";
    //zq 批量抵押多抵多
    public static final String DJLX_PLDY_DDD_SQLXDM = "9979902";
    //lst 批量抵押变更登记产生一本证书业务对应的申请类型代码
    public static final String DJLX_PLDYBG_YBZS_SQLXDM = "1029";
    //    纯土地不匹配不动产单元，不传入原权利
    //809:纯土地解封 808:纯土地查封登记 407:纯土地使用权注销登记 420土地抵押权注销登记 421房屋抵押权注销登记 8009901房屋查封，8009902房屋解封,8009903土地查封，8009904土地解封
    public static final String[] DJLX_CTD_NOQL_SQLXDM = new String[]{"1029", "809", "808", "407", "420", "421", "8009901", "8009902", "8009903", "8009904", "9930004"};
    public static final String SQLX_TDCF_DM_OLD = "808"; //土地查封登记
    public static final String SQLX_TDCF_DM_NEW = "8009903"; //土地查封登记  (有老的申请类型808，这个暂不用)
    public static final String SQLX_TDJF_DM = "8009904"; //土地解封登记
    public static final String SQLX_FWCF_DM = "8009901";//房屋查封  8009901
    public static final String SQLX_FWJF_DM = "8009902";//房屋解封  8009902
    public static final String SQLX_TDSYQ_ZX_DM = "407";//纯土地使用权注销登记
    public static final String SQLX_TDDY_ZX_DM = "420";//土地抵押权注销登记
    public static final String SQLX_FWDY_ZX_DM = "421";//房屋抵押权注销登记
    public static final String SQLX_BDC_CXSQ = "9920003";//不动产查询申请
    public static final String SQLX_GDDYZX_DM = "4009903";//抵押（原他项证）注销登记
    public static final String SQLX_PLGDDYZX_DM = "4009904";//批量抵押（原他项证）注销登记
    //原产权证注销登记（不匹配不动产单元）
    public static final String[] DYFS_ZXDJ_NOBDC = new String[]{"4009901", "4009902", "4009903", "4009904"};

    //不匹配不动产单元需要生成权利的sqlx集合
    public static final String[] SQLX_NOBDCDY_GDQL = new String[]{"8009901", "8009903", "808"};

    //一般抵押
    public static final String DYFS_YBDY = "1";
    //最高额抵押
    public static final String DYFS_ZGEDY = "2";

    //    房屋合并方向
    public static final String FW_HBFX_LEFT = "1";
    public static final String FW_HBFX_RIGHT = "2";
    public static final String FW_HBFX_UP = "3";
    public static final String FW_HBFX_DOWN = "4";
    //解封状态
    public static final Integer QFZT_JF = 1;

    //预告登记种类
    //买卖
    public static final String YGDJZL_MM = "1,2";
    //抵押
    public static final String YGDJZL_DY = "3,4";

    public static final String DJFW_EMPTY_DYH = " ";
    //zdd 权利类型状态
    //临时状态  办理中  查封
    public static final Integer QLLX_QSZT_LS = 0;
    //现势状态  正常状态
    public static final Integer QLLX_QSZT_XS = 1;
    //历史状态  注销转移状态
    public static final Integer QLLX_QSZT_HR = 2;
    //查封是否失效
    public static final String SXZT_ISSX = "1";
    //终止状态  异常状态
    public static final Integer QLLX_QSZT_ZZ = 3;
    //登记数据房屋多幢
    public static final String DJSJ_FWDZ_DM = "1";
    //登记数据房屋独幢
    public static final String DJSJ_FW_DM = "2";
    //登记数据房屋戶室
    public static final String DJSJ_FWHS_DM = "4";
    //zdd 权利类型
    public static final String QLLX_JTTD_SUQ = "1";
    public static final String QLLX_GYTD_SUQ = "2";
    public static final String QLLX_GYTD_JSYDSYQ = "3";
    public static final String QLLX_GYTD_FWSUQ = "4";
    public static final String QLLX_ZJD_SYQ = "5";
    public static final String QLLX_ZJD_FWSUQ = "6";
    public static final String QLLX_JTTD_JSYDSYQ = "7";
    public static final String QLLX_JTTD_JSYDFWSUQ = "8";
    public static final String QLLX_JTTD_CBJYQ = "9";
    public static final String QLLX_LQ_TDCBJYQ = "10";
    public static final String QLLX_JTTD_NYDQTSYQ = "11";
    public static final String QLLX_LQ_SYQ = "12";
    public static final String QLLX_HY_SYQ = "13";
    public static final String QLLX_HY_GZWSYQ = "14";
    public static final String QLLX_HY_WJMSYQ = "15";
    public static final String QLLX_HY_WJM_GZWSYQ = "16";
    public static final String QLLX_DYQ = "17";
    public static final String QLLX_DYAQ = "18";
    public static final String QLLX_YGDJ = "19";
    public static final String QLLX_YYDJ = "20";
    public static final String QLLX_CFDJ = "21";
    public static final String QLLX_QT = "qt";
    //权利其他状况中不添加持证人的权利类型
    public static final String[] QLLX_WCZR = new String[]{"17", "18", "19", "20", "21"};
    //出证明的权利类型
    public static final String[] QLLX_ZMS = new String[]{"17", "18", "19", "20"};
    //zdd 不动产单元来源
    public static final Integer BDCDYLY_DJ = 0; //查询地籍库
    public static final Integer BDCDYLY_BDC = 1;//查询不动产业务库
    public static final Integer BDCDYLY_ALL = 2;//两者都有
    public static final Integer BDCDYLY_CF = 3;//查封权利
    public static final Integer BDCDYLY_CF_ALL = 4;//查封权利

    //sc过度权力类型
    public static final String GDQL_TDZ = "土地证";
    public static final String GDQL_TDSYQ = "土地所有权";
    public static final String GDQL_GDXM_CPT = "gd_xm";
    public static final String GDQL_TDSYQ_CPT = "gd_tdsyq";
    public static final String GDQL_TDSYNQ = "土地使用权";
    public static final String GDQL_TDSYNQ_CPT = "gd_tdsynq";
    public static final String GDQL_FWSYQ = "房屋所有权";
    public static final String GDQL_FWSYQ_CPT = "gd_fwsyq";
    public static final String GDQL_CF = "查封";
    public static final String GDQL_CF_CPT = "gd_cf";
    public static final String GDQL_DY = "抵押";
    public static final String GDQL_TXZ = "他项权证";
    public static final String GDQL_TXZM = "他项权证明";
    public static final String GDQL_DY_CPT = "gd_dy";
    public static final String GDQL_YG = "预告";
    public static final String GDQL_YGZM = "预告证明";
    public static final String GDQL_YG_CPT = "gd_yg";
    public static final String GDQL_YY = "异议";
    public static final String GDQL_YY_CPT = "gd_yy";
    public static final String GDTD_SYQLX_CR = "102";
    public static final String GD_QLR = "gd_qlr";
    public static final String GD_SYQ = "所有权";

    //sc  不动产房屋类型
    public static final String BDCFWLX_DZ = "多幢房屋";
    public static final String BDCFWLX_DZ_DM = "1";
    //zx  不动产房屋权利类型代码
    public static final String BDC_FDCQ_DZ = "bdc_fdcq_dz";
    public static final String BDC_FDCQ = "bdc_fdcq";

    //    zx 帆软报表不动产单元登记信息封面
    public static final String FR_BDCDYXX_FM = "bdcdydjxx";

    //    zx字符串分割符
    public static final String SPLIT_STR = "$";
    //ZX过度已匹配
    public static final String GD_PPZT_WCPP = "2";
    //ZX过度部分匹配
    public static final String GD_PPZT_BFPP = "1";
    //ZX过度没有匹配成功
    public static final String GD_PPZT_WPP = "0";
    //ZLL过度没有匹配不动产单元创建项目
    public static final String GD_PPZT_YSL = "6";

    //    系统版本海门
    public static final String SYS_VERSION_HM = "hm";
    //   系统版本涟水
    public static final String SYS_VERSION_LS = "ls";
    //    系统版本林口
    public static final String SYS_VERSION_LK = "lk";
    //    系统版本林口
    public static final String SYS_VERSION_NM = "nm";
    //    系统版本通州
    public static final String SYS_VERSION_TZ = "tz";
    //    系统版本南通开发区
    public static final String SYS_VERSION_NT = "nt";


    //    过渡
    public static final String SJLY_GD = "gd";
    public static final String SJLY_GD_XMLY = "2";
    //    //过渡房屋以成果数据
    public static final String SJLY_GDFW_WAY = "cg";
    public static final String SJLY_GDFW_XMLY = "3";
    public static final String XMLY_XM = "3";
    //zdd 项目表项目来源 1 代表项目来源自不动产系统自身
    public static final String XMLY_BDC = "1";
    //zdd 项目表项目来源 2 代表项目来源自房产审批系统（包括过渡）
    public static final String XMLY_TDSP = "2";
    //zdd 项目表项目来源 3 代表项目来源自土地审批系统（包括过渡）
    public static final String XMLY_FWSP = "3";
    //zwq 混合项目来源 苏州有不动产单元+不动产权证+房产证+土地证这种批量查封和匹配，这个只是作为判断，不作为最终存入数据库值
    public static final String XMLY_HHLC = "4";
    //zhouwanqing 过程匹配
    public static final String PPLX_GC = "gc";
    ////zhouwanqing 成果匹配
    public static final String PPLX_CG = "cg";

    //过度表单
    public static final String GD_FW = "gd_fw";
    public static final String GD_TD = "gd_td";
    public static final String GD_LQ = "gd_lq";
    public static final String GD_CQ = "gd_cq";

    public static final String ZDZHTZM_JA = "JA";
    public static final String ZDZHTZM_JC = "JC";
    public static final String ZDZHTZM_JB = "JB";
    public static final String ZDZHTZM_JF = "JF";
    public static final String ZDZHTZM_JD = "JD";
    public static final String ZDZHTZM_GF = "GF";
    public static final String ZDZHTZM_GD = "GD";
    public static final String ZDZHTZM_GB = "GB";
    public static final String ZDZHTZM_GX = "GX";
    public static final String ZDZHTZM_GE = "GE";
    public static final String ZDZHTZM_H = "H";
    public static final String ZDZHTZM_G = "G";
    public static final String DZWTZM_W = "W";
    public static final String DZWTZM_F = "F";
    public static final String DZWTZM_L = "L";
    public static final String DZWTZM_Q = "Q";

    //   验证单元已经发过权利或者正在发权利的排除权利类型
    public static final String[] CHECK_EXCLUDE_QLLX = new String[]{"19"};
    public static final String[] YG_YGDJZL_DY = new String[]{"3", "4"};

    public static final String[] YG_ZHUANXIAN = new String[]{"1", "3"};

    public static final String[] YG_ZHUANXIAN_YG = new String[]{"1"};

    public static final String[] YG_ZHUANXIAN_YGDY = new String[]{"3"};


    //登记事由，房屋所有权
    public static final String DJSY_FWSYQ = "9";
    //登记事由，国有建设用地使用权
    public static final String DJSY_GYJSYDSHQ = "2";
    //登记事由，抵押权
    public static final String DJSY_DYAQ = "13";
    //登记事由，其他权利
    public static final String DJSY_QT = "15";

    //申请类型，预查封登记
    public static final String YCFDJ = "804";


    //查封类型（查封）
    public static final String CFLX_ZD_CF = "1";
    //查封类型（预查封）
    public static final String CFLX_ZD_YCF = "2";
    //续封
    public static final String CFLX_XF = "3";
    //轮候查封
    public static final String CFLX_LHCF = "4";
    //查封类型（轮候预查封）
    public static final String CFLX_ZD_LHYCF = "5";

    public static final String CFLX_GD_XF = "续封";
    public static final String CFLX_GD_YCF = "预查封";
    public static final String CFLX_GD_CF = "查封";
    public static final String CFLX_GD_LHCF = "轮候查封";
    public static final String CFLX_GD_LHYCF = "轮候预查封";

    //查封申请类型代码集合
    public static final String[] CF_SQLX = {"801", "806", "810"};

    //查封申请类型代码集合
    public static final String[] CF_SQLX_GY = {"801", "802", "804", "805"};
    public static final String GY_CF_CFWJ = " 裁定书、执行通知书";
    //查封类型代码集合
    public static final String[] CF_CFLX = {"1", "2", "", "3", "5"};
    public static final String SQLX_CF = "801";
    //解封申请类型
    public static final String SQLX_JF = "803";
    //解封申请类型
    public static final String SQLX_JF_NOQL = "809";
    //不匹配不动产单元解封
    public static final String SQLX_JF_NOBDCDY = "8009902";
    //司法处置解封
    public static final String SQLX_JF_SFCZ = "8009910";
    //司法处置注销申请类型
    public static final String SQLX_ZX_SFCD = "9930004";

    //抵押首次登记
    public static final String[] SQLX_DY_SCDJ = {"1001", "1002", "1009", "1015", "1019", "1020", "1024", "1026", "1028", "9999905", "9999910", "1020", "9991001", "9993501", "9991101", "9991201", "9993601", "9971201", "9990413"};
    //抵押转移登记
    public static final String[] SQLX_DY_ZYDJ = {"1003", "1004", "1010", "1011", "1016", "9999906", "9991202", "9991002", "9993502", "9991202", "9993602", "9991102"};
    //抵押变更登记
    public static final String[] SQLX_DY_BGDJ = {"1005", "1006", "1012", "1013", "1017", "1031", "9999907", "9991203", "9991003", "9993503", "9991103", "9991203", "9993603"};
    //抵押注销登记
    public static final String[] SQLX_DY_ZXDJ = {"1007", "1014", "1018", "9999908", "1021", "9991004", "9993504", "9991104", "9991204", "9993604"};
    //批量查封
    public static final String SQLX_PLCF = "806";
    //批量抵押注销
    public static final String SQLX_PLDYZX = "9979999";
    //批量解封
    public static final String SQLX_PLJF = "807";
    //批量换证
    public static final String SQLX_PLHZ = "910";
    //建设用地使用权抵押权注销登记
    public static final String SQLX_JSYDSYQDYAQ_ZX = "1007";
    //建设用地使用权注销登记
    public static final String SQLX_JSYDSYQ_ZX = "403";
    //建设用地使用权及建筑物、构筑物所有权注销登记
    public static final String SQLX_JSYDGZWSYQ_ZX = "415";
    //需验证上一首权利类型的登记
    public static final String[] SQLX_LAST_ONE_DJ = {"501"};

    //商品房楼盘批量发证
    public static final String SQLX_PLFZ_DM = "130";

    //国有建设用地使用权首次登记
    public static final String SQLX_GYJSYDSYQSCDJ_DM = "137";

    //房屋分割登记
    public static final String[] SQLX_FWFG_DJ = {"233", "330"};

    //不改变历史证书状态信息的申请类型
    public static final String[] UNCHANGE_QSZT_SQLX = {"801", "802", "805", "806", "1001", "1019", "1024", "1026", "1020", "1002", "1009", "1015", "707", "704", "705", "701", "702", "602", "603", "601", "122", "1028", "1040", "9971201", "9993401", "9993402", "9993403", "9991001", "9993501", "9991101", "9991201", "9993601", "342", "343", "9920043", "9990413"};

    //不改变预告权属状态的申请类型
    public static final String[] UNCHANGE_YGQSZT_SQLX = {"130", "133", "9920043"};

    //面积核减
    public static final String[] MJHJ_SQLX = {"342", "343"};
    //权利限制类型
    public static final String QLLX_XZ = "'17','18','19','20','21'";

    public static final String SQL_ROWNUM_100 = "100";
    //车库、储藏室抵押附记
    public static final String CK_DY_FJ = "随同抵押。";
    //车库
    public static final String CK = "车库";

    //车库代码
    public static final String[] GHYT_CK_DM = {"8", "216"};
    //储藏室
    public static final String CCS = "储藏室";

    //修改匹配状态的登记类型
    public static final String[] EDIT_PPZT_DJLX = {"100", "200", "300", "500", "900"};

    //抵押首次申请类型
    public static final String[] SPYZ_DYSC_SQLX = {"1001", "1015", "1019", "1020", "9980421", "9990701", "9990901", "9999905", "9979902", "9990413"};

    //审批验证只验证当前proid的申请流程
    public static final String[] SPYZ_DQPROID_SQLX = {"130", "1015", "9999905", "902", "1028", "9979999", "9999909", "9971201", "133"};
    //抵押
    public static final String GD_QLZT_DY = "DY";
    //预告
    public static final String GD_QLZT_YG = "YG";
    //异议
    public static final String GD_QLZT_YY = "YY";
    //所有权
    public static final String GD_QLZT_SYQ = "SYQ";
    //查封
    public static final String GD_QLZT_CF = "CF";
    //地役
    public static final String GD_QLZT_DYI = "DI";
    //注销
    public static final String GD_QLZT_ZX = "ZX";
    //多个权利
    public static final String GD_QLZT_DGQLZT = "DGQLZT";

    //流程节点
    public static final String WORKFLOW_SJ = "收件";
    public static final String WORKFLOW_SLCS = "受理（初审）";
    public static final String WORKFLOW_SL = "受理";
    public static final String WORKFLOW_CS = "初审";
    public static final String WORKFLOW_FS = "复审";
    public static final String WORKFLOW_HD = "核定（登簿）";
    public static final String WORKFLOW_SZ = "缮证";
    public static final String WORKFLOW_DB = "登簿";//内蒙缮证节点叫“登簿”
    public static final String WORKFLOW_SF = "收费";
    public static final String WORKFLOW_FZ = "发证";

    public static final String WORKFLOW_BQBZ = "补齐补正";
    //zdd 分别持证
    public static final String BDCXM_FBCZ = "是";

    //登记子项字典表代码
    public static final String DJZX_XF = "1204";
    public static final String DJZX_LH = "1203";
    public static final String DJZX_CF = "1202";
    public static final String DJZX_YCF = "1201";
    public static final String DJZX_JF = "1205";
    public static final String DJZX_LHYCF = "1206";
    //过渡项目的匹配状态
    public static final String GD_XM_DPPWFZ = "0"; //待匹配未发证
    public static final String GD_XM_BFPPWFZ = "1"; //已部分匹配未发证
    public static final String GD_XM_YPPWFZ = "2"; //已匹配未发证
    public static final String GD_XM_YPPZZFZ = "3"; //已匹配正在发证
    public static final String GD_XM_YPPYFZ = "4"; //已匹配已发证

    //宗地类别判定方式
    public static final String ZDLB_PDFS_BDCDY = "BDCDY";//以不动产单元判定宗地类别
    public static final String ZDLB_PDFS_QLR = "QLR";//以权利人判定宗地类别
    //宗地类别
    public static final String ZDLB_DYZ = "0";//独用宗
    public static final String ZDLB_GYZ = "1";//共有宗
    //默认宗地特征码
    public static final String ZDTZM_DEFAULT = "DEFAULT";//默认宗地特征码

    //提示
    public static final String PLMSG = "所选房产证房屋存在有未匹配的，请先匹配再批量处理！";
    //共有方式代码及名称
    public static final String GYFS_DDGY_MC = "单独所有";
    public static final String GYFS_GTGY_MC = "共同共有";
    public static final String GYFS_AFGY_MC = "按份共有";
    public static final String GYFS_QTGY_MC = "其他共有";
    public static final String GYFS_DDGY_DM = "0";
    public static final String GYFS_GTGY_DM = "1";
    public static final String GYFS_AFGY_DM = "2";
    public static final String GYFS_QTGY_DM = "3";
    // 国有建设用地使用权（合并不动产单元）变更登记
    public static final String SQLX_GYJSYDHB_BGDJ = "321";

    public static final String SQLX_BG_DM = "332";//房屋构/建筑物变更登记
    public static final String SQLX_TDBG_DM = "317";//集体土地所有权变更登记
    public static final String SQLX_NYDBG_DM = "334";//农用地使用权（非林地）变更登记
    public static final String SQLX_ZJDBG_DM = "318";//宅基地使用权变更登记
    public static final String SQLX_GYJSBG_DM = "320";//国有建设用地使用权变更登记
    public static final String SQLX_JTJSBG_DM = "324";//集体建设用地使用权变更登记
    public static final String SQLX_LQBG_DM = "3001001";//林权变更登记
    public static final String SQLX_HYBG_DM = "3001601";//海域使用权及构建筑物所有权变更登记
    public static final String SQLX_JDMJYTBG_DM = "3000303";//面积、用途变更登记（净地）

    //过渡数据锁定状态 (0:解锁；1：锁定)
    public static final int SDZT_SD = 1;
    public static final int SDZT_JS = 0;

    //过渡数据分割转移等需要读取权籍数据的流程
    public static final String[] SQLX_FGZY_DM = new String[]{"233"};

    //系统验证类型字典项
    //只验证项目创建选择不动产单元
    public static final String XT_CHECKTYPE_BDCDY = "1";
    //只验证转发
    public static final String XT_CHECKTYPE_ZF = "2";
    //只验证全部
    public static final String XT_CHECKTYPE_ALL = "0";
    //南通版本产权归属个人
    public static final String CQGS_GR = "个人";
    //南通版本产权归属单位
    public static final String CQGS_DW = "单位";

    //整幢不动产单元发证，不动产单元的备注信息
    public static final String LJZFZ_BDCDY_BZ = "整幢";
    //商品房及业主共有部分首次登记分割不动产单元备注信息
    public static final String FG_BDCDY_BEGIN = "由";
    public static final String FG_BDCDY_END = "分割而来";

    //zll 芜湖抵押权变更（批量）流程申请类型代码
    public static final String SQLX_DYBGPL_DM = "1031";

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description 查封权利人配置
     */
    public static final String CFQLR_COURT = "court";

    //yy 海安县 产权证明单批量换证
    public static final String SQLX_CQZMD_HZ = "905";

    //房屋抵押权注销
    public static final String SQLX_DYZX = "1014";
    //房屋抵押权变更
    public static final String SQLX_DYBG = "1012";
    //不动产土地逐户解押登记
    public static final String SQLX_TD_ZHJY = "9990305";
    //不动产土地逐户注销登记
    public static final String SQLX_TD_ZHZX = "4000406";
    //商品房分摊土地核减登记(逐户解押+逐户注销)
    public static final String SQLX_SPFFTTD_ZHJY_ZHZX = "9980407";
    //yy 组合贷款抵押权首次登记
    public static final String SQLX_ZHDK_DYSC = "1040";
    //yy 组合贷款抵押权转移登记
    public static final String SQLX_ZHDK_DYZY = "1041";
    //yy 组合贷款抵押权变更登记
    public static final String SQLX_ZHDK_DYBG = "1042";
    //yy 组合贷款抵押权注销登记
    public static final String SQLX_ZHDK_DYZX = "1043";
    //yy 组合贷款抵押 生成多本多本证
    public static final String[] SQLX_ZHDK = new String[]{"1040", "1041", "1042"};
    //yy 组合贷款抵押 注销原证书
    public static final String[] SQLX_ZHDK_ZX = new String[]{"1043", "1041", "1042"};
    //jyl 任意组合流程
    public static final String SQLX_HDDJ_RYZH = "9990001";

    //liujie 发证类型（1是发证书，2是发证明，3是不发证）
    public static final String FZLX_FZS = "1";
    public static final String FZLX_FZM = "2";
    public static final String FZLX_BFZ = "3";
    //商品房首次开发商自持
    public static final String SQLX_SPFSCKFSZC_DM = "131";
    //商品房性质变更登记
    public static final String SQLX_SPFXZBG_DM = "132";
    // 公建配套首次登记
    public static final String SQLX_GJPTSCDJ_DM = "133";

    // 国有水产养殖权首次登记
    public static final String SQLX_SYTT_DM = "1001401";

    // 国有水产养殖权变更登记
    public static final String SQLX_SYTTBG_DM = "3001401";

    public static final String PL_CHOSE_ONE = "1";

    public static final String ZJGCDY_FW = "1";
    public static final String ZJGCDY_TD = "2";
    public static final String NOT_ZJGCDY = "3";

    //限制状态（0：正常，1：锁定，8：遗失）
    public static final String XZZT_ZC = "0";
    public static final String XZZT_SD = "1";
    public static final String XZZT_YS = "8";

    //不动产单元冻结登记
    public static final String SQLX_BDCDYDJ_DM = "9920001";
    //不动产单元解冻登记
    public static final String SQLX_BDCDYJD_DM = "9920005";
    //不动产单元变更登记
    public static final String SQLX_BDCDYBGDJ_DM = "9920003";
    //权籍调查登记
    public static final String SQLX_QJDCDJ_DM = "9920004";

    //预购商品房预告变更登记
    public static final String SQLX_YG_BGDJ = "7000003";
    //在建工程抵押权转现抵押
    public static final String SQLX_ZJJZWDY_ZX_DM = "9999909";
    //预告抵押登记变更
    public static final String SQLX_YG_YGDYBG = "7100003";
    //在建建筑物抵押状态注销
    public static final String ZJJZWXX_DYZT_ZX = "1";
    //在建建筑物抵押状态现势
    public static final String ZJJZWXX_DYZT_XS = "0";

    //合并流程申请类型判断
    public static final String[] SQLX_hblc_zlc = new String[]{"135", "218", "219", "706", "904", "9980405", "9980409", "9980411", "9980001", "9980424", "9981803"};

    public static final String[] SQLX_QLLX_QJ = new String[]{"123", "1001401", "1001001", "1003501", "1001101", "1001201", "1003601", "3001001", "3003501", "3001101", "3001201", "3003601", "2001001", "2003501", "2001101", "2001201", "2003601", "4001001", "4003501", "4001101", "4001201", "4003601", "901"};
    //警示状态（0：正常，1：锁定）
    public static final String JSZT_JS = "1";
    public static final String JSZT_QX = "0";

    //验证方式
    public static final String CHECKMODEL_ALERT = "alert";
    //验证方式
    public static final String CHECKMODEL_COMFIRM = "comfirm";

    //编号类型名称
    public static final String BHLX_BYSL_MC = "不予受理";
    public static final String BHLX_BYDJ_MC = "不予登记";
    public static final String BHLX_BQBZ_MC = "补齐补正";
    public static final String BHLX_BDCSCXXB_MC = "不动产首次信息表";
    public static final String BHLX_BDCGG_MC = "不动产公告";
    //    申请人类别
    public static final String SQRLB_QLR = "1";
    public static final String SQRLB_YWR = "2";

    //土地分割、合并换证登记
    public static final String SQLX_TDFGHBHZ_DM = "9980301";
    public static final String SQLX_GYJSYD_GZW_DM = "111";

    public static final String SQLX_JSYDSYQ_LHDJ = "9980302";//建设用地使用权量化登记

    public static final String SQLX_SPFSCDY_DM = "9980421";//商品房首次登记加抵押组合登记(在建抵押转现)

    //权籍逻辑幢工程进度
    public static final String GCJD_NJ = "1";//拟建
    public static final String GCJD_ZJ = "2";//在建
    public static final String GCJD_JG = "3";//竣工
    public static final String GCJD_SD = "4";//首登

    //按幢设宗后宗地是否为剩余宗地
    public static final String ISSYZD_NO = "0";  //非剩余宗地
    public static final String ISSYZD_YES = "1"; //剩余宗地

    //苏州新建商品房首次选择不发证时权利人名称为全体业主
    public static final String SPFSC_BFZ_QLRMC = "全体业主";

    //以房屋为主的在建工程(多抵多)
    public static final String SQLX_ZJJZWDY_DDD_FW_DM = "9999910";
    //在建功能抵押转现房抵押(多抵多)
    public static final String SQLX_ZJJZWDYZXFDY_DM = "9999913";

    //持证人（0：不持证，1：持证）
    public static final String CZR = "1";

    //合并流程申请类型代码
    public static final String[] HB_SQLXDM = {"706", "135", "219", "218", "9980409", "1040", "904", "9980424"};

    public static final String SQLX_SPFZYDY_MC = "商品房买卖转移登记和抵押登记";


    //=================Begin林权==========================//
    //林权验证不动产单元号是否正确（所有林权有关流程都进行验证）
    public static final String[] SQLXDM_LQ = new String[]{"1001001", "1003501", "1001101", "1001201", "1003601", "3001001", "3003501", "3001101", "3001201", "3003601", "2001001", "2003501", "2001101", "2001201", "2003601", "4001001", "4003501", "4001101", "4001201",
            "4003601", "5001201", "1020", "9991203", "9991202", "1021", "9991001", "9991003", "9991002", "9991004", "9993501", "9993503", "9993502", "9993504", "9991101", "9991103", "9991102", "9991104", "9991201", "9991204", "9993601", "9993603", "9993602", "9993604"};

    //林权首次登记
    public static final String[] SQLX_LQ_SCDJ = {"1001001", "1003501", "1001101", "1001201", "1003601"};
    //林权转移登记
    public static final String[] SQLX_LQ_ZYDJ = {"2001001", "2003501", "2001101", "2001201", "2003601"};
    //林权变更登记
    public static final String[] SQLX_LQ_BGDJ = {"3001001", "3003501", "3001101", "3001201", "3003601"};
    //林权注销登记
    public static final String[] SQLX_LQ_ZXDJ = {"4001001", "4003501", "4001101", "4001201", "4003601"};
    //林权更正登记
    public static final String SQLX_LQ_GZDJ_DM = "5001201";
    //批量林权抵押多对一流程
    public static final String SQLX_LQ_PLDY_DM = "9971201";
    //林权抵押首次登记
    public static final String[] SQLX_LQ_DY_SCDJ = {"1020", "9991001", "9993501", "9991101", "9991201", "9993601", "9971201"};
    //林权抵押转移登记
    public static final String[] SQLX_LQ_DY_ZYDJ = {"9991002", "9993502", "9991202", "9993602", "9991102"};
    //林权抵押变更登记
    public static final String[] SQLX_LQ_DY_BGDJ = {"9991003", "9993503", "9991103", "9991203", "9993603"};
    //林权抵押注销登记
    public static final String[] SQLX_LQ_DY_ZXDJ = {"1021", "9991004", "9993504", "9991104", "9991204", "9993604"};
    //=================End林权==========================//

    //获取上一手项目的原产权证号申请类型
    public static String[] YXMBDQZH_SQLX = {"807", "803", "1003", "1005", "1010", "1012", "9940002", "8009910", "1014", "1007", "1018","9930005"};
    //苏州单位代码,用于版本控制
    public static String DWDM_SZ = "320500";

    public static final String TSXX_ZYYZ = "请收回证书!";
    //解封登记
    public static final String[] SQLX_ZXCFDJ_DM = {"803", "807", "8009902", "8009910","9930005"};
    //查封登记
    public static final String[] SQLX_CFDJ_DM = {"801", "806", "8009901", "8009903"};

    //依法转移和抵押登记
    public static final String SQLX_YFZYDYDJ_DM = "9980430";

    //不匹配不动产单元登记
    public static final String[] SQLX_UNPPBDCDYDJ_DM = {"8009901", "8009902", "4009903", "4009901", "4009902", "8009903", "8009904"};

    //查封是否失效
    public static final String SXZT_ISSX_WSX = "0";

    //网外创建
    public static final String[] REGISTER_KEY_SPFMMZY = {"sqh", "sqdjlxmc", "sqlx", "qllx", "djlx", "gyfs", "sffbcz", "bdcdyh", "zl"};
    public static final String[] REGISTER_KEY_DYDJ = {"sqh", "sqdjlxmc", "qllx", "djlx", "gyfs", "sffbcz", "bdcdyh", "zl", "bdbzzqse", "zwlxqxksrq", "zwlxqxjsrq", "dyfs"};
    public static final String[] REGISTER_KEY_QLR = {"sqh", "bdcdyh", "qlrmc", "qlrsfzjzl", "qlrzjh", "qlrlx", "qlrlxdh"};
    public static final String[] REGISTER_KEY_QLRHB = {"sqh", "bdcdyh", "qlrmc", "qlrsfzjzl", "qlrzjh", "qlrlx", "qlrlxdh", "zdjlxmc"};
    public static final String[] REGISTER_KEY_YG = {"sqh", "sqdjlxmc", "qllx", "djlx", "gyfs", "sffbcz", "bdcdyh", "zl"};
    public static final String[] REGISTER_KEY_YDY = {"sqh", "sqdjlxmc", "qllx", "djlx", "gyfs", "sffbcz", "bdcdyh", "zl", "bdbzzqse", "zwlxqxksrq", "zwlxqxjsrq", "dyfs"};

    //一般抵押
    public static final String DYFS_YBDY_MC = "一般抵押";
    //最高额抵押
    public static final String DYFS_ZGEDY_MC = "最高额抵押";


    //预告登记种类名称
    //预购商品房预告登记
    public static final String YGDJZL_YGSPF_MC = "预购商品房预告登记";
    //预购商品房抵押权预告登记
    public static final String YGDJZL_YGSPFDYAQ_MC = "预购商品房抵押权预告登记";

    //批量操作的流程
    public static final String[] BATCH_OPERATION_SQLX_DM = {"9979902", "9999910", "806", "130", "133", "9979912"};

    //最高额抵押登记子项代码
    public static final String[] DJZX_ZGE = {"802", "1522"};

    //一般抵押担保数额描述
    public static final String SEMS_YBDY = "被担保主债权数额:";

    //最高额抵押担保数额描述
    public static final String SEMS_ZGEDY = "最高债权数额:";

    //一般抵押债务时间描述
    public static final String SJMS_YBDY = "债务履行期限:";

    //最高额抵押债务时间描述
    public static final String SJMS_ZGEDY = "债权确定期间:";

    //新增互联网加抵押的证书编号备注信息
    public static final String HLW_ZSBH_BZ = "互联网+使用";

    //发短信的模式：中国联通
    public static final String SMS_MODE_CUCC = "cucc";
    //发短信的模式：中国移动
    public static final String SMS_MODE_CMCC = "cmcc";

    //审批信息宗地宗海面积读取gd_tdsyq面积字段
    public static final String SPXX_ZDZHMJ_TDZMJ = "tdzmj";
    public static final String SPXX_ZDZHMJ_SYQMJ = "syqmj";

    //选择不动产单元时证书来源
    public static final String SELECT_ZSLY_BDCANDGD = "bdcAndGd";
    public static final String SELECT_ZSLY_BDC = "bdc";
    public static final String SELECT_ZSLY_GD = "gd";

    //抵押登记的登记子项一般抵押权和最高额抵押权
    public static final String DJZX_YBDYQ = "801";
    public static final String DJZX_ZGEDY = "802";

    //存量房合同交易状态:0-在办,1-已办
    public static final String CLFHTJYZT_ZB = "0";
    public static final String CLFHTJYZT_YB = "1";


    //附属设施是否计入发证面积
    public static final String BDCFWFSSS_JRFZMJ_YES = "1";
    public static final String BDCFWFSSS_JRFZMJ_NO = "0";

    //多幢情况详见附页
    public static final String BDCFDCQDZ_FWFZXX_DZQKXJFY = "多幢情况详见附页";

    //纯土地不动产单元号后9位
    public static final String TD_BDCDYH_HJW = "W00000000";

    //不动产权证号
    public static final String BDCQZH_ZWMC = "不动产权证号";
    //不动产证明号
    public static final String BDCZMH_ZWMC = "已有的不动产权证明号";

    public static final String XTLY_YHSL = "1"; //互联网+银行受理
    public static final String XTLY_JYSL = "3"; //交易和集成平台
    public static final String XTLY_PLSL = "4"; //批量受理导入excel压缩包
    public static final String XTLY_JCPTSL = "5"; //集成平台受理


    public static final String BANK_BJBLDBZT = "5"; //报件办理登薄状态
    public static final String BANK_BJBLDBYJ = "同意办理"; //报件办理登薄状态

    //银行报件办理状态
    public static final String HLWJ_YH_BJBLZT_DSL = "1";//待受理
    public static final String HLWJ_YH_BJBLZT_DJJ = "2";//待接件
    public static final String HLWJ_YH_BJBLZT_BLZ = "3";//办理中
    public static final String HLWJ_YH_BJBLZT_YTH = "4";//已退回
    public static final String HLWJ_YH_BJBLZT_YDB = "5";//已登簿
    public static final String HLWJ_YH_BJBLZT_YSZ = "6";//已缮证
    public static final String HLWJ_YH_BJBLZT_YFQ = "9";//已废弃

    //选择不动产单元查询类型
    public static final String SELECTBDCDY_CXLX_CQ = "cq";//待受理
    public static final String SELECTBDCDY_CXLX_DY = "dy";//待接件
    public static final String SELECTBDCDY_CXLX_YG = "yg";//办理中
    // 锁定状态, 1 已锁定，0 未锁定
    public static final Integer ZS_SDZT_YSD = 1;
    public static final Integer ZS_SDZT_WSD = 0;
    // 过度权利 0.未注销，1.已注销
    public static final Integer GDQL_ISZX_WZX = 0;
    public static final Integer GDQL_ISZX_YZX = 1;

    // bdc_xygl中sjly字段
    public static final String SJLY_ZFGLZX = "2"; // 查询是否保障性住房者用，此时数据来源为住房管理中心。

    //收费信息收费状态(0：未收费1：已收费2：部分缴费)
    public static final String SFXX_SFZT_WJF = "0";//未缴费
    public static final String SFXX_SFZT_YJF = "1";//已缴费
    public static final String SFXX_SFZT_BFJF = "2";//部分缴费

    // bdc_tszt中tsdx字段
    public static final String TSDX_PJXT = "5";// 推送对象5：评价系统

    //合并中带抵押登记的申请类型
    public static final String[] SQLX_HBDY = {"904", "218", "9980424", "9980430", "706", "9981803"};

    //身份证信息来源
    public static final String SFZXXLY_JY = "1";//交易
    public static final String SFZXXLY_JCPT = "2";//集成平台
    public static final String SFZXXLY_MZ = "3";//民政
    public static final String SFZXXLY_HLW = "4";//互联网
    public static final String SFZXXLY_DK = "5";//读卡
    public static final String SFZXXLY_LR = "6";//录入

    /**
     * 裁定状态 是
     */
    public static final String ISCD_POSITIVE = "1";

    /**
     * 裁定状态 否
     */
    public static final String ISCD_NEGETIVE = "0";


    //是否统一收费
    public static final String SFTYSF_S = "是";//是
    public static final String SFTYSF_F = "否";//否
    public static final String SFTYSF_S_DM = "1";//是
    public static final String SFTYSF_F_DM = "0";//否

    //批量缴费统一收费银行提示申请类型
    public static final String[] TYSF_SHOW_SQLX = {"1001", "1003", "1005", "1010", "1012", "1019", "218", "219", "706", "9979902", "9980405", "9980409", "9980424", "9980430", "9999905", "9999906", "9999907", "9999910", "9981803", "9990413"};

    //不需要搜到首次登记证的流程
    public static final String[] SQLX_SCDJZM_NO = {"218", "131", "132", "806", "801", "2003301", "601"};

    //抵押首次，批量多抵多流程选择产权证
    public static final String[] SQLX_DYSC_NO = {"1001", "1019", "9979902", "9999905", "9990413"};

    //昆山除转移登记外抵押首次流程也需要搜首次登记证
    public static final String[] SQLX_DYSC_YES_KS = {"211", "1019", "135", "2000415", "9990413"};

    //交易状态常量
    public static final String JYZT_WQBAZ_MC = "网签备案中";
    public static final String JYZT_YBA_MC = "已备案";
    public static final String JYZT_ZLBAZ_MC = "租赁备案中";
    public static final String JYZT_YZL_MC = "已租赁";
    public static final String JYZT_YCF_MC = "已查封";
    public static final String JYZT_WJYZT_MC = "无交易状态";
    public static final String JYZT_DJ_MC = "冻结";

    //资金托管状态,未资金托管（0）
    public static final String FUND_SUPERVISION_STATUS_TG = "0";
    //资金托管状态,资金托管到账（1）
    public static final String FUND_SUPERVISION_STATUS_DZ = "1";
    //资金托管状态,资金托管未到账（2）
    public static final String FUND_SUPERVISION_STATUS_WDZ = "2";

    //必填项验证房地产权多幢table_id
    public static final String BTXYZ_TABLEID_FDCQDZ = "28";
    public static final String BTXYZ_TABLEID_FDCQDZ_ZXX = "29";
    //必填项验证房地产权table_id
    public static final String BTXYZ_TABLEID_FDCQ = "8";
    //必填项验证初审，复审，核定签名table_id
    public static final String[] BTXYZ_TABLEID_SHQM = {"21", "22", "23"};

    //过渡权利证书类型（房屋所有权、土地所有权、土地使用权、房屋登记证明、他项证明、查封文号、预告证明、预告抵押证明、其他证明）
    public static final String GDQL_CFWH_ZSLX = "查封文号";
    public static final String GDQL_FWSYQ_ZSLX = "房屋所有权";
    public static final String GDQL_YGZM_ZSLX = "预告证明";
    public static final String GDQL_YGDYZM_ZSLX = "预告抵押证明";
    public static final String GDQL_TXZM_ZSLX = "他项证明";
    public static final String GDQL_TDSYQ_ZSLX = "土地所有权";
    public static final String GDQL_TDSYONGQ_ZSLX = "土地使用权";

    //原权利信息列表需要展示的流程sqlx
    public static final String[] SQLX_YQLXXLB = {"9960401", "8009901", "8009903"};

    //登簿失败提示语
    public static final String REGISTER_FAIL_TIPS = "登簿失败！";

    //产权权利类型
    public static final String[] SQLX_CQ = {"1", "11", "2", "3", "4", "5", "6", "7", "8", "9"};
    //产权权利类型
    public static final String[] SQLX_DYAQ = {"18"};


    //注销登记流程申请类型代码
    public static final String[] SQLX_ZXDJ = {"1007", "1014", "1018", "4009901", "4009902", "4009903", "402", "403", "404", "409", "415", "6009901", "799", "8009902", "8009904", "8009910", "7102704","9930005"};
    //不生成证书流程申请类型代码
    public static final String[] SQLX_N0_SCZS = {"806", "801", "8009903", "8009901"};
    //合并登记转移加抵押的流程
    public static final String[] SQLX_HBZYDY = {"218", "9980424", "9980430"};
    //分割合并等签名需要签多条的流程
    public static final String[] SQLX_SIGNMUL = {"9960401", "9960402", "9980301", "9960301", "9960302"};

    //登记子项名称 解封
    public static final String DJZXMC_JF = "解封";

    //抵押转移、抵押更正、抵押变更
    public static final String[] SQLX_DY_ZYGZBG = {"1003", "1005", "1010", "1012", "9999906", "9999907", "501"};


    //是否是附属设施
    public static final String ISFWFSSS_YES = "1";
    public static final String ISFWFSSS_NO = "0";

    //固定模版
    public static final String[] ZJDMJ_TD = {"宗地面积：@zdzhmj㎡", "宗地批准面积：@zdpzmj㎡"};
    public static final String[] ZJDMJ_TDFW = {"房屋建筑面积：@scmj㎡", "房屋批准建筑面积：@fwpzjzmj㎡"};
    public static final String[] YZDFMJ_TDFW = {"宗地面积：@zdzhmj㎡", "房屋面积：@fwmj㎡"};

    //初始化权籍子户室附属设施房屋首次登记申请类型
    public static final String[] SQLX_FWSCDJ_CSHQJZHS = {"130", "111", "120", "110"};

    public static final String[] SQLX_ZJDMJ = {"110", "111", "225", "229", "901", "9940001"};

    //是否持证人（1：是；0：否）
    public static final String SFCZR_YES = "1";
    public static final String SFCZR_NO = "0";

    //权利限制类型
    public static final String QLLX_CQ = "4,6,8";

    //裁定状态
    public static final String CDZT_CD = "1";
    public static final String CDZT_ZC = "0";

    //10:已受理|20:已收档|0:业务撤销或退回
    public static final String JYTSZT_SL = "10";
    public static final String JYTSZT_SD = "20";
    public static final String JYTSZT_CX = "0";

    //解除限制
    public static final String SQLX_JCXZ = "9920044";
    public static final String YW_QT = "qt";
    public static final String YES_CHS = "是";//是
    public static final String NO_CHS = "否";//否
    //现势状态  正常状态
    public static final String QLLX_QSZT_XS_CHS = "现势";
    //历史状态  注销转移状态
    public static final String QLLX_QSZT_HR_CHS = "历史";

    //江阴虚拟宗地籍号
    public static final String XNZDJH = "320281030005GB00187";

    //备案房屋查询 返回状态码 成功
    public static final String BA_FWCX_SUCCESS_CODE = "0";
    //备案房屋查询 房屋状态 已售
    public static final String BA_FWCX_FWZT_YS = "2";
    //备案房屋查询 房屋状态 内部限制
    public static final String BA_FWCX_FWZT_NBXZ = "5";

    public static final String[] INIT_YW_NOBC = {"qt", "bcsj"};


    public static final String[] SYSLY_YGS = {"yg", "ygzx", "ydy", "ydyzx"};
    public static final String SYSLY_YG = "yg";
    public static final String SYSLY_YG_ZX = "ygzx";
    public static final String SYSLY_YDY = "ydy";
    public static final String SYSLY_YDY_ZX = "ydyzx";
    public static final String SYSLY_DY = "dy";
    //商品房常量名小写
    public static final String GETSPFXX_LOWERCAS = "getspfxx";
    //存量房常量名小写
    public static final String GETCLFXX_LOWERCAS = "getclfxx";

    //流程类型——单一
    public static final String LCLX_DY = "dy";
    //流程类型——组合
    public static final String LCLX_ZH = "ZH";
    //流程类型——批量
    public static final String LCLX_PL = "PL";

    public static final String DEFULT_STR = "defult";

    //撤销匹配
    public static final String BDC_PP_CZLX_CX = "0";
    //匹配
    public static final String BDC_PP_CZLX_PP = "1";

    //不初始化ybdcqzh的sqlx
    public static final String[] SQLX_INITYBDCQZH_NO = {"1012", "218", "706", "9980424", "219", "9980409", "9980405", "135", "9980430", "9981803"};

    //开发进度  分期开发
    public static final String BDC_KFJD_FQKF = "2";

    //匹配状态
    public static final String PPZT_WPP = "未匹配";
    public static final String PPZT_YPP = "已匹配";
    public static final String PPZT_WPPTDZ = "未匹配土地证";

    //土地首次登记信息表补登记
    public static final String SQLX_TDSCDJXXBBDJ_DM = "3000304";
    //读取房屋的分摊土地面积的sqlx
    public static final String[] SQLX_DQFTTDMJ_DM = {"3000304", "130"};

    //证书裁定撤销
    public static final String SQLX_ZSCDCX = "9920020";

    //只取预测面积的预告sqlx
    public static final String[] SQLX_YG_YCMJ_DM = new String[]{"700", "706", "707"};

    //商品房首次（出一本证）
    public static final String SQLX_SPFGYSCDJ_CYBZ_DM = "1000407";

    //出一本证sqlx代码
    public static final String[] DJLX_YBZS_SQLXDM = {"1028","1000407"};


}
