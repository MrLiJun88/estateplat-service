<@com.html title="不动产登记业务管理系统" import="fr,ace,jqueryVersion">
<style xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    .modal-dialog {
        width: 1000px;
    }

    /*高级搜索样式添加 begin*/
    .AdvancedSearchForm {
        position: absolute;
        top: 10px;
        left: 48px;
        z-index: 9999;
        display: none;
    }

    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    .Advanced .modal-backdrop {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1;
        background-color: #000;
        opacity: 0.5;
        filter: alpha(opacity=50);
        display: none;
    }

    .Advanced .AdvancedLab {
        display: block;
        margin: 0;
        background: #f5f5f5;
        font-size: 12px;
        border-top: 1px solid #ddd;
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        padding: 0px 20px 10px 20px;
        position: absolute;
        top: -57px;
        left: 486px;
        z-index: 3;
        width: 90px;
        line-height: 25px;
    }

    .Advanced {
        position: relative;
        margin: 0px 0px 10px 0px;
    }

    .AdvancedSearchForm .form-base {
        padding: 20px 20px 20px 20px;
        border: 1px solid #ddd;
        background: #f5f5f5;
        width: 623px;
        position: absolute;
        top: -22px;
        left: -47px;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

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

    /*高级搜索样式添加 end*/
    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    /*高级搜索样式添加 end*/
    /*帆软flash打印样式问题*/
    .fr-label {
        padding: 0px !important;
    }

    .fr-texteditor {
        height: 20px !important;
    }
</style>

<script type="text/javascript">
    _$(function () {
        /*   文字水印  */
        _$(".watermarkText").watermark();

        //拖拽功能
        _$(".modal-header").mouseover(function () {
            _$(this).css("cursor", "move");//改变鼠标指针的形状
        })
        _$(".modal-header").mouseout(function () {
            _$(".show").css("cursor", "default");
        })
        _$(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    });


    function enableTooltips(table) {
        _$('.navtable .ui-pg-button').tooltip({container: 'body'});
        _$(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        _$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = _$(this);
            var _$class = _$.trim(icon.attr('class').replace('ui-icon', ''));

            if (_$class in replacement) icon.attr('class', 'ui-icon ' + replacement[_$class]);
        })
    }
    var lastSel;
    _$(function () {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        //resize to fit page size
        _$(window).on('resize.jqGrid', function () {
            _$(grid_selector).jqGrid('setGridWidth', _$(".page-content").width());
        });
        //resize on sidebar collapse/expand
        var parent_column = _$(grid_selector).closest('[class*="col-"]');
        _$(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                _$(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });

        _$(grid_selector).jqGrid({
            url: '${bdcdjUrl}/bdcqldj/getQldjPagesJson?djbid=' + '${djbid!}',
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'BDCDYID'},
            colNames: ["序号", '不动产单元号', '不动产类型', '所在本数', '备注', '打印目录'],
            colModel: [

                {name: 'XH', index: 'XH', width: '8%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '30%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '15%', sortable: false},
                {name: 'SZBS', index: 'SZBS', width: '8%', sortable: false},
                {name: 'BZ', index: 'BZ', width: '30%', sortable: false},
                {
                    name: 'myac',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="printCk(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>' +
                                '</div>'
                    }
                }
            ],
            viewrecords: true,

            rowNum: 10,
            rowList: [10, 20, 30],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: true,
            multiselect: true,
            loadComplete: function () {
                //翻页之后按钮隐藏
                _$("#qldjUl>li").each(function (index) {
                    var display = _$("#qldjUl > li:eq(" + index + ")").css('display');
                    if (display != "none") {
                        _$("#qldjUl > li:eq(" + index + ")").hide();
                    }
                });
                _$("#qlxxPrintBtn").hide();

                var table = this;
                /*去掉全选按钮*/
                _$("#jqgh_grid-table_cb>input").each(function (index) {
                    _$("#jqgh_grid-table_cb>input[id='cb_grid-table']").remove();
                });
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);

            },
            /*通过onSelectRow、beforeSelectRow执行checkbox单选*/
            onSelectRow: function (rowid, status, e) {
                if (rowid == lastSel) {
                    _$(this).jqGrid("resetSelection");
                    lastSel = undefined;
                    status = false;
                    _$("#qldjUl>li").each(function (index) {
                        var display = _$("#qldjUl > li:eq(" + index + ")").css('display');
                        if (display != "none") {
                            _$("#qldjUl > li:eq(" + index + ")").hide();
                        }
                    });
                    _$("#qlxxPrintBtn").hide();
                } else {
                    var grid = _$(grid_selector);
                    var cm = grid.jqGrid("getGridParam", "colModel");
                    var ids = grid.jqGrid('getGridParam', 'selarrrow');
                    var index;
                    for (var i = 0, h = cm.length; i < h; i++) {
                        if (cm[i].name == 'BDCDYH') {
                            index = i;
                        }
                    }
                    var data = _$("#" + rowid + ">td");
                    var bdcdyh = data[index].innerHTML;
                    var options = {
                        url: '${bdcdjUrl}/bdcDjb/getQlJson',
                        type: 'post',
                        dataType: 'json',
                        data: {bdcdyh: bdcdyh},
                        success: function (data) {
                            _$("#qldjUl>li").each(function (index) {
                                var display = _$("#qldjUl > li:eq(" + index + ")").css('display');
                                if (display != "none") {
                                    _$("#qldjUl > li:eq(" + index + ")").hide();
                                }
                            });
                            _$("#qlxxPrintBtn").hide();
                            if (data != null && data.length > 0) {
                                var isHidden = true;
                                for (var i = 0; i < data.length; i++) {
                                    var obj = _$("#" + data[i].qllx);
                                    obj.attr("onclick", "printQldj('" + data[i].tableName.toUpperCase() + "')");
                                    obj.show();
                                    if (data[i].tableName.indexOf("gd_") == 0) {
                                        obj.attr("onclick", "printQldj_djb('" + data[i].tableName.toUpperCase() + "','" + data[i].qlid + "')")
                                        _$("#" + data[i].tableName.toUpperCase() + " button").attr("id", data[i].qlid);
                                    }
                                    if (obj.length > 0) {
                                        isHidden = false;
                                    }
                                }
                                if (!isHidden) {
                                    _$("#qlxxPrintBtn").show();
                                }
                            }
                        },
                        error: function (data) {

                        }
                    };
                    _$.ajax(options);
                    lastSel = rowid;
                }
            },
            beforeSelectRow: function (rowid, e) {

                _$(this).jqGrid("resetSelection");
                return true;
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });

        _$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp._$1, (this.getFullYear() + "").substr(4 - RegExp._$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp._$1, (RegExp._$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    });
    //查询
    function serch() {
        var bdcdyh = _$("#search_xmmc").val();
        var Url = "${bdcdjUrl}/bdcqldj/getQldjPagesJson?djbid=${djbid!}";
        tableReload("grid-table", Url, {bdcdyh: bdcdyh});
    }

    //打印项目信息的函数
    //这个无法给登记簿显示页码，下面那个才可以
    <#--function printCk(id) {-->
    <#--if(id!=""){-->
    <#--var djbid=encodeURI(encodeURI('${djbid!}'));-->
    <#--var bdcdyh = encodeURI(encodeURI(id));-->
    <#--openWin("${reportUrl}/ReportServer?reportlet=bdcdj_djb%2Fbdcdydjxx.cpt&op=form&bdcdyh="+bdcdyh+"&djbid="+djbid,"printck");-->

    <#--}-->
    <#--}-->

    function printCk(id) {
        var requestQlPageUrl = '${bdcdjUrl}/bdcDjb/getQlPageJson?bdcdyh=' + id;
        _$.ajax({
            type: "get",
            url: requestQlPageUrl,
            dataType: "json",
            data: {},
            success: function (data) {
                openBdcdyDjxxWin(id, data);
            },
            error: function (data) {
                //alert(data);
            }
        });
    }

    //打开不动产权利及其他事项登记信息窗口
    function openBdcdyDjxxWin(id, pagedata) {
        if (id != "" && pagedata != null) {
            var djbid = encodeURI(encodeURI('${djbid!}'));
            var bdcdyh = encodeURI(encodeURI(id));
            var qt = pagedata.qt == null || pagedata.qt == undefined ? "" : pagedata.qt;
            var dya = pagedata.dya == null || pagedata.dya == undefined ? "" : pagedata.dya;
            var dy = pagedata.dy == null || pagedata.dy == undefined ? "" : pagedata.dy;
            var yg = pagedata.yg == null || pagedata.yg == undefined ? "" : pagedata.yg;
            var yy = pagedata.yy == null || pagedata.yy == undefined ? "" : pagedata.yy;
            var cf = pagedata.cf == null || pagedata.cf == undefined ? "" : pagedata.cf;
            openWin("${reportUrl}/ReportServer?reportlet=bdcdj_djb%2Fbdcdydjxx.cpt&op=form&bdcdyh=" + bdcdyh + "&djbid=" + djbid + "&qt=" + qt + "&dya=" + dya + "&dy=" + dy + "&yg=" + yg + "&yy=" + yy + "&cf=" + cf, "打印单元登记簿");

        }
    }


    function openWin(url, name) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    function preview() {
        var djbid = encodeURI(encodeURI('${djbid!}'));
        openWin("${reportUrl}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjml.cpt&op=form&djbid=" + djbid, "printDjdbml");
    }

    function tableReload(table, Url, data) {
        var jqgrid = _$("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function printQldj(obj) {
        var rowid = _$("#grid-table").jqGrid('getGridParam', 'selrow');
        var rowData = _$("#grid-table").jqGrid('getRowData', rowid);
        var bdcdyh = rowData.BDCDYH;
        var startpage = null;
        var page = "";
        var requestQlPageUrl = '${bdcdjUrl}/bdcDjb/getQlPageJson?bdcdyh=' + bdcdyh;
        _$.ajax({
            type: "post",
            url: requestQlPageUrl,
            dataType: "json",
            async: false,
            success: function (data) {
                page = getStartPage(data, obj);
            },
            error: function (data) {
                //alert(data);
            }
        });
        if (page != null) {
            if (page.indexOf(",") > -1) {
                startpage = page.split(",")[0];
            } else {
                startpage = page;
            }
        }
        var url = "${reportUrl}/ReportServer?reportlet=bdcdj_djb%2F" + obj + ".cpt&op=form&bdcdyh=" + bdcdyh + "&djbid=" + "${djbid!}" + "&startpage=" + startpage;
        openWin(url, '');
    }
    function printQldj_djb(obj, bdcid) {
        var rowid = _$("#grid-table").jqGrid('getGridParam', 'selrow');
        var rowData = _$("#grid-table").jqGrid('getRowData', rowid);
        var url = "${reportUrl}/ReportServer?reportlet=edit%2F" + obj + ".cpt&op=form&bdcid=" + bdcid;
        openWin(url, '');
    }
    function printQldjAll() {
        var rowid = _$("#grid-table").jqGrid('getGridParam', 'selrow');
        var rowData = _$("#grid-table").jqGrid('getRowData', rowid);
        var bdcdyh = rowData.BDCDYH;
        var rp = "";
        var p = [];
        _$("#qldjUl>li").each(function (index) {
            var display = _$("#qldjUl > li:eq(" + index + ")").css('display');
            if (display != "none") {
                var objId = _$("#qldjUl > li:eq(" + index + ")").attr("id");
                var reportlet = "";
                if (objId.indexOf("GD_") == 0) {
                    var qlid = _$("#" + objId + " button").attr("id");
                    reportlet = "{reportlet: '/edit/" + objId.toLowerCase() + ".cpt',bdcid:'" + qlid + "'}";
                } else {
                    reportlet = "{reportlet: '/bdcdj_djb/" + objId.toLowerCase() + ".cpt',bdcdyh='" + bdcdyh + "'}";
                }
                if (rp == "" || rp == null || !rp) {
                    rp = reportlet;
                } else {
                    rp += "," + reportlet;
                }
            }
        });
        // console.info(rp);
        if (rp != "" && rp != null && rp) {
            var reportlets = FR.cjkEncode("[" + rp + "]");
            var config = {
                url: "${reportUrl}/ReportServer",
                isPopUp: true,
                data: {
                    reportlets: reportlets
                }
            };
            FR.doURLFlashPrint(config);
        } else {
            FR.Msg.alert("警告", "无可打印的数据!");
        }
    }

    function getStartPage(pagedata, obj) {
        var page = null;
        if (obj && obj != "undefined" && pagedata != null) {
            if (obj == "BDC_CF") {
                page = pagedata.cf == null || pagedata.cf == undefined ? "" : pagedata.cf;
            } else if (obj == "BDC_DYAQ") {
                page = pagedata.dya == null || pagedata.dya == undefined ? "" : pagedata.dya;
            } else if (obj == "BDC_YG") {
                page = pagedata.yg == null || pagedata.yg == undefined ? "" : pagedata.yg;
            } else if (obj == "BDC_YY") {
                page = pagedata.yy == null || pagedata.yy == undefined ? "" : pagedata.yy;
            } else if (obj == "BDC_DYQ") {
                page = pagedata.dy == null || pagedata.dy == undefined ? "" : pagedata.dy;
            } else {
                page = pagedata.qt == null || pagedata.qt == undefined ? "" : pagedata.qt;
            }
        }
        return page;
    }

</script>
<div class="main-container">
    <input type="hidden" id="dibid" value="${djbid!}">

    <div class="space-10"></div>

    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入不动产单元号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                <#--<td>-->
                <#--<button type="button" class="btn btn-sm btn-primary" id="match" onclick="preview()">打印目录</button>-->
                <#--</td>-->
                </tr>
            </table>
        </div>
        <div class="tableHeader">
        <#--<ul>-->
        <#--<li>-->
        <#--<button type="button" id="addLimitFieldConfig">-->
        <#--<i class="ace-icon fa fa-download"></i>-->
        <#--<span>新建</span>-->
        <#--</button>-->
        <#--</li>-->
        <#--<li>-->
        <#--<button type="button" id="updateLimitFieldConfig">-->
        <#--<i class="ace-icon fa fa-pencil"></i>-->
        <#--<span>修改</span>-->
        <#--</button>-->
        <#--</li>-->
        <#--<li>-->
        <#--<button type="button" id="delLimitFieldConfig">-->
        <#--<i class="ace-icon glyphicon glyphicon-remove"></i>-->
        <#--<span>删除</span>-->
        <#--</button>-->
        <#--</li>-->
        <#--</ul>-->
            <ul id="qldjUl">
                <#list qllxList as qllx>
                    <li style="display: none;" class="" id="${qllx.dm!}"
                    <#--onclick="printQldj('${qllx.dm!}',${qllx.tableName!}')"-->>
                        <button type="button">
                            <span>${qllx.mc!}</span>
                        </button>
                    </li>

                </#list>
                <li style="display: none;" id="GD_FW">
                    <button type="button">
                        <span>房屋所有权信息</span>
                    </button>
                </li>
                <li style="display: none;" id="GD_DY">
                    <button type="button">
                        <span>抵押权利信息</span>
                    </button>
                </li>
                <li style="display: none;" id="GD_CF">
                    <button type="button">
                        <span>查封权利信息</span>
                    </button>
                </li>
            </ul>
            <ul>
                <li style="display: none" id="qlxxPrintBtn" onclick="printQldjAll()">
                    <button type="button">
                        <span>权利信息打印</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="grid-table"></table>

        <div id="grid-pager"></div>
        <iframe id="frPdfFrame" name="frPdfFrame" style="display: none"
                src="#"
                width=100%
                class="iframeStyle" frameborder="0"></iframe>
    </div>
</div>


<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
