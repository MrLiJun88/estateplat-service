<@com.html title="外网导入上传" import="fr,ace,public">
<style>
    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }
    .SSinput {
        min-width: 350px !important;
    }
    .name {
        line-height: inherit;
        color: #ffffff;
        text-align: center;
    }

    .btn-name {
        margin-left: 49%;
        color: #0000ff;
    }
</style>
<script type="text/javascript" src="../static/thirdControl/jquery/plugins/jquery.form.min.js"></script>
<script type="text/javascript">
    //多选数据
    jQuerymulData = new Array();
    jQuerymulRowid = new Array();
    proidArray = new Array();
    wiidArray = new Array();
    var etlUrl = "${etlUrl!}";
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var wxgzhDwdm = "${wxgzhDwdm!}";
    var sqlxMc = "";
    var sldPrintNum = "${sldPrintNum!}";
    var height = $(window).height() - 300;
    var errorHeight = $(window).height() - 600;
    //引用下面这段js，防止调用$.browser时浏览器报错
    jQuery.browser={};
    (function(){
        jQuery.browser.msie=false;
        jQuery.browser.version=0;
        if(navigator.userAgent.match(/MSIE ([0-9]+)./)){
            jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;
        }
    })();
    $(function () {
        Array.prototype.remove = function (index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };
        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject) {
            if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
                $(window).resize(function () {
                    $.each($(".moveModel > .modal-dialog"), function () {
                        $(this).css("left", ($(window).width() - $(this).width()) / 2);
                        $(this).css("top", "40px");
                    })
                })
            }
        }
        $("#upload").click(function () {
            $.blockUI({message: "请稍等……"});
        });
        $("#uploadForm").ajaxForm({
            url: etlUrl + "/ont/unzip?userid=${userid!}",
            success: function (json) {
                var result = JSON.parse(json);
                setTimeout($.unblockUI, 10);
                if (null != result&&result.checkMsg != null&&result.checkMsg != ""&&result.checkMsg != undefined) {
                    if(result.checkType == "alert") {
                        alert(result.checkMsg);
                    }
                }else if(null != result&&result.wwslbh != null&&result.wwslbh != ""&&result.wwslbh != undefined) {
                    alert("创建成功！");
                    tableReload("#grid-table", etlUrl +"/ont/getPlslResultDataPageJson", {wwslbh: result.wwslbh});
                }
            },
            error: function (result) {
                setTimeout($.unblockUI, 10);
                alert("创建失败！");
            }
        });
        $("#plsl_search_btn").click(function () {
            var dcxc = $("#plsl_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                tipInfo("请输入收件编号/权利人/义务人/不动产单元号/房屋坐落/虚拟编号");
            }else{
                tableReload("#grid-table", etlUrl +"/ont/getPlslResultDataPageJson", {dcxc:dcxc});
            }
        });

        $(function () {
            /*   文字水印  */
            $(".watermarkText").watermark();
            var grid_selector = "#grid-table";
            var pager_selector = "#grid-pager";

            //resize to fit page size
            $(window).on('resize.jqGrid', function () {
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            });
            //resize on sidebar collapse/expand
            var parent_column = $(grid_selector).closest('[class*="col-"]');
            $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
                if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }
            });

            jQuery(grid_selector).jqGrid({
                url: "",
                datatype: "local",
                height: 'auto',
                jsonReader: {id: 'WIID'},
                colNames: ["收件编号", "权利人", "义务人", "不动产单元号", "房屋坐落", "虚拟编号", "WIID"],
                colModel: [
                    {name: 'BH', index: 'BH', width: '13%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
                    {name: 'YWR', index: 'YWR', width: '15%', sortable: false},
                    {name: 'BDCDYH', index: 'BDCDYH', width: '22%', sortable: false},
                    {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
                    {name: 'WWSLBH', index: 'WWSLBH', width: '13%', sortable: false},
                    {name: 'WIID', index: 'WIID', width: '0%', sortable: false, hidden: true}
                ],
                viewrecords: true,
                rowNum: 10,
                rowList: [10, 20, 30],
                pager: pager_selector,
                pagerpos: "left",
                height: height,
                multiselect:true,
                altRows: false,
                loadComplete: function () {
                    var table = this;
                    setTimeout(function () {
                        updatePagerIcons(table);
                        enableTooltips(table);
                        var replacement =
                                {
                                    'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
                                };
                        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                            var icon = $(this);
                            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
                            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                        })
                    }, 0);

                    var jqData = $(grid_selector).jqGrid("getRowData");
                    $.each(jqData, function (index, data) {
                        getQlrAndYwr(data.BH, $(grid_selector), data.WIID);
                    })
                },
                onSelectAll: function (aRowids, status) {
                    var jQuerymyGrid = jQuery(this);
                    //aRowids.forEach(function(e){
                    $.each(aRowids, function (i, e) {
                        var cm = jQuerymyGrid.jqGrid('getRowData', e);
                        if (cm.WIID == e) {
                            var index = $.inArray(e, jQuerymulRowid);
                            if (status && index < 0) {
                                jQuerymulData.push(cm);
                                jQuerymulRowid.push(e);
                            } else if (!status && index >= 0) {
                                jQuerymulData.remove(index);
                                jQuerymulRowid.remove(index);
                            }
                        }
                    })
                },
                onSelectRow: function (rowid, status) {
                    var jQuerymyGrid = jQuery(this);
                    var cm = jQuerymyGrid.jqGrid('getRowData', rowid);
                    if (cm.WIID == rowid) {
                        var index = $.inArray(rowid, jQuerymulRowid);
                        if (status && index < 0) {
                            jQuerymulData.push(cm);
                            jQuerymulRowid.push(rowid);
                        } else if (!status && index >= 0) {
                            jQuerymulData.remove(index);
                            jQuerymulRowid.remove(index);
                        }
                    }
                },
                editurl: "",
                caption: "",
                autowidth: true
            });
            //tableReload("#grid-table", etlUrl +"/ont/getPlslResultDataPageJson", {wwslbh:"201808070001"});
        });
    });

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 二次请求获取权利人和义务人
     */
    function getQlrAndYwr(bh, table, rowid) {
        $.ajax({
            type: "GET",
            url: etlUrl + "/ont/getPlslQlrAndYwr?wiid=" + rowid,
            success: function (result) {
                table.setCell(rowid, "QLR", result.qlr);
                table.setCell(rowid, "YWR", result.ywr);
            }
        });
    }

    //批量收件单打印（本地打印不支持列表拓展，采用帆软批量打印的方式）
    function multiSjdPrint(){
        var $jq = jQuery.noConflict();
        if (jQuerymulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var wiids = "";
        var wiidArr = new Array();
        for (var i = 0; i < jQuerymulData.length; i++) {
            wiidArr.push(jQuerymulData[i].WIID)
        }
        wiids = wiidArr.join(",");
        $.ajax({
            type:'POST',
            async:false,
            url:etlUrl+'/ont/checkMulXm?wiids=' + wiids,
            success:function(data){
                if(data.type=="false"){
                    alert(data.msg);
                }
                proidArray = data.proidList;
                wiidArray = data.wiidList;
                sqlxMc = data.sqlxMc;
                if(data.isHb == "false"){
                    printSld();
                }else{
                    printHbSld();
                }
            }
        });
    }

    function printSld(){
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        for (var i = 0; i < proidArray.length; i++) {
            p.push("{reportlet: '/print/bdc_sld.cpt', proid : '" + proidArray[i] + "',dwdm:'" + wxgzhDwdm + "'}");
        }
        //将参数值组成的数组转化为字符串
        var rp = p.join(",");
        //使用FineReport自带的方法cjkEncode进行转码
        var reportlets = FR.cjkEncode("[" + rp + "]");
        var config = {
            url: printurl,
            isPopUp: false,
            data: {
                reportlets: reportlets
            }
        };
        FR.doURLFlashPrint(config);
    }

    function printHbSld(){
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        for (var i = 0; i < proidArray.length; i++) {
            p.push("{reportlet: '/print/bdc_hb_sld.cpt', proid : '" + proidArray[i] + "',wiid:'" + wiidArray[i] + "',hbSqlxdm:'218',dwdm:'" + wxgzhDwdm + "',sjxxnum:'',djzxdm:'',mainProid:'" + proidArray[i] + "',sldPrintNum:'" + sldPrintNum + "'}");
        }
        //将参数值组成的数组转化为字符串
        var rp = p.join(",");
        //使用FineReport自带的方法cjkEncode进行转码
        var reportlets = FR.cjkEncode("[" + rp + "]");
        var config = {
            url: printurl,
            isPopUp: false,
            data: {
                reportlets: reportlets
            }
        };
        FR.doURLFlashPrint(config);
    }

    //收费单本地打印
    function multiSfdPrint(){
        var $jq = jQuery.noConflict();
        if (jQuerymulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var wiids = "";
        var wiidArr = new Array();
        for (var i = 0; i < jQuerymulData.length; i++) {
            wiidArr.push(jQuerymulData[i].WIID)
        }
        wiids = wiidArr.join(",");
        $.ajax({
            type:'POST',
            async:false,
            url:etlUrl+'/ont/checkMulXm?wiids=' + wiids,
            success:function(data){
                if(data.type=="false"){
                    alert(data.msg);
                }
                proidArray = data.proidList;
                wiidArray = data.wiidList;
                sqlxMc = data.sqlxMc;
                printSfd();
            }
        });
    }

    function printSfd(){
        var p = [];
        var sqlxmc = encodeURI(encodeURI(sqlxMc));
        var printurl = "${reportUrl}/ReportServer";
        for (var i = 0; i < proidArray.length; i++) {
            p.push("{reportlet: '/print/bdc_sfgzd_pl.cpt', proid : '" + proidArray[i] + "',sqlxMc:'" + sqlxmc + "',wiid:'" + wiidArray[i] + "'}");
        }
        //将参数值组成的数组转化为字符串
        var rp = p.join(",");
        //使用FineReport自带的方法cjkEncode进行转码
        var reportlets = FR.cjkEncode("[" + rp + "]");
        var config = {
            url: printurl,
            isPopUp: false,
            data: {
                reportlets: reportlets
            }
        };
        FR.doURLFlashPrint(config);
    }

    function multiPrint(type) {
        if (jQuerymulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var wiids = "";
        var wiidArr = new Array();
        for (var i = 0; i < jQuerymulData.length; i++) {
            wiidArr.push(jQuerymulData[i].WIID)
        }
        wiids = wiidArr.join(",");
        $.ajax({
            type:'POST',
            async:false,
            url:etlUrl+'/ont/checkMulXm?wiids=' + wiids,
            success:function(data){
                if(data.type=="false"){
                    alert(data.msg);
                }
                proidArray = data.proidList;
                wiidArray = data.wiidList;
                sqlxMc = data.sqlxMc;
                //调用本地打印
                if(data.isHb == "false"){
                    var printUrl = "${serverUrl}/bdcPrint/printMulBdc?wiids=" + wiids +"&hiddeMode=false"+"&printType="+type;
                }else{
                    var printUrl = "${serverUrl}/bdcPrint/printAllMulBdc?wiids=" + wiids +"&hiddeMode=true"+"&printType="+type;
                }
                window.location.href = "eprt:"+printUrl;
            }
        });
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
                {
                    'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
                    'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
                    'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
                    'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
                };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function tableReload(table, Url, data) {
        var jqgrid = $(table);
        jqgrid.setGridParam({url: Url, datatype: 'jsonp', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
</script>
<div class="main-container" id="dataPop">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch" style="width: 100%">
            <table cellpadding="0" cellspacing="0" border="0" style="width: 100%">
                <tr style="border: 0">
                    <td style="border: 0">
                        <form method="post" enctype="multipart/form-data" id="uploadForm" style="border: 0">
                            <input type="file" id="impFile" name="multipartFile" class="btn01 AdvancedButton"
                                   style="float: left"/>
                            <button type="submit" class="btn01 AdvancedButton" id="upload">读取</button>
                        </form>
                    </td>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="plsl_search"
                               data-watermark="请输入收件编号/权利人/义务人/不动产单元号/房屋坐落/虚拟编号" >
                    </td>
                    <td class="Search" style="border: 0">
                        <a href="#" id="plsl_search_btn">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                </tr>
            </table>
        </div>
        <div class="leftToolTop">
            <button type="button"  class="btn btn-primary save" id="sjdxxPrintButton" name="sjdxxPrintButton" onclick="multiSjdPrint()">批量打印收件单</button>
            <button type="button"  class="btn btn-primary save" id="sqsxxPrintButton" name="sqsxxPrintButton" onclick="multiPrint('sqs')">批量打印申请书</button>
            <button type="button"  class="btn btn-primary save" id="spbxxPrintButton" name="spbxxPrintButton" onclick="multiPrint('spb')">批量打印审批表</button>
            <button type="button"  class="btn btn-primary save" id="fzjlPrintButton" name="fzjlPrintButton" onclick="multiPrint('fzjl')">批量打印发证记录</button>
            <button type="button"  class="btn btn-primary save" id="sfdPrintButton" name="sfdPrintButton" onclick="multiSfdPrint()">批量打印收费单</button>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>

<input type="hidden" id="logid" name="logid"/>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
