<@com.html title="审批数据匹配登记项目" import="ace,public">
<style>

    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
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

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
        margin: 0px 5px 0px 0px;
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

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    /*高级搜索样式添加 begin*/
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
    /*zsy 缮证按钮去掉边框
    .simpleSearch table tr td.btn_sz {
        border: none;
    }*/

    /*高级搜索样式添加 end*/
    /*  .ui-jqgrid .ui-jqgrid-bdiv {

          overflow-x: hidden;
      }*/
</style>
<script type="text/javascript">
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '300px';
    //全局的不动产类型
    $bdclx = 'TDFW';
    //定义公用的基础colModel
    fwColModel = [
        {
            name: 'XL', index: '', width: '8%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.BDCDYBH + '\',\'' + rowObject.YWH + '\',\'' + rowObject.XMBH + '\',\'' + rowObject.YWLY + '\')"/>'
        }
        },
        {name: 'YWH', index: 'YWH', width: '0%', sortable: false, hidden: true},
        {name: 'YWMC', index: 'YWMC', width: '30%', sortable: false},
        {name: 'SLH', index: 'SLH', width: '10%', sortable: false},
        {
            name: 'DJSJ',
            index: 'DJSJ',
            width: '15%',
            sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return "";
                }
                var value = cellvalue;
                var data = new Date(value).Format("yyyy-MM-dd");
                return data;
            }
        },
        {name: 'XZQMC', index: 'XZQMC', width: '15%', sortable: false},
        {name: 'YWLYMC', index: 'YWLYMC', width: '0%', sortable: false, hidden: true},
        {name: 'XMBH', index: 'XMBH', width: '12%', sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                if(rowObject.XMBH && rowObject.XMBH!=""){
                    return '<span class="label label-success">已经办理</span>';
                }else{
                    return '<span class="label label-warning">等待办理</span>';
                }
            }
        },

    ];
    dyhColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" onclick="dyhSel(\'' + rowObject.PROID + '\')"/>'
        }
        },
        {name: 'CJR', index: 'CJR', width: '20%', sortable: false},
        {name: 'BH', index: 'BH', width: '30%', sortable: false,},
        {name: 'SQLXMC', index: 'SQLXMC', width: '40%', sortable: false,}
    ];
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
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    dyhLoadComplete = function () {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        //如果10条设置宽度为auto,如果少于7条就设置固定高度
        if ($(table).jqGrid("getRowData").length == $rownum) {
            $(table).jqGrid("setGridHeight", "auto");
        } else {
            $(table).jqGrid("setGridHeight", $pageHight);
        }
        //去掉遮罩
        setTimeout($.unblockUI, 10);
    };
    $(function () {
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据"});
        var width = $(window).width() / 2;
        if (width < 795) {
            width = 795;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
        $(window).resize(function () {
            var width = $(window).width() / 2;
            if (width < 795) {
                width = 795;
            }
            var height = $(window).height() - 20;
            $("#ace-settings-box").css("width", width).css("height", height);
        })
        //默认初始化表格
        dyhTableInit();
        //左边房屋林权草权土地
        $("#fwTab,#lqTab,#cqTab,#tdTab,#hyTab").click(function () {
            var url;
            //清空查询内容
            $("#dyh_search_qlr").val("");
            $("#dyh_search_qlr").next().show();
            if (this.id == "dyhTab") {
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/todatasjpp/getBdcXmPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: '', bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
            } else {
                //清空刷新不动产单元表
                $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                $("#dyh-grid-table").trigger("reloadGrid");
                if (this.id == "fwTab") {
                    $bdclx = 'TDFW';
                    $("#fw").addClass("active");
                    var fwUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
                    fwTableInit();
                    if (isLoadGrid("fw"))
                        tableReload("fw-grid-table", fwUrl, {hhSearch: '', bdclx: $bdclx}, fwColModel, '');
                    $("#dyhTab").click();
                } else if (this.id == "lqTab") {
                    $bdclx = 'TDSL';
                    $("#lq").addClass("active");
                    $("#dyhTab").click();
                    var lqUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
                    lqTableInit();
                    if (isLoadGrid("lq"))
                        tableReload("lq-grid-table", lqUrl, {hhSearch: '', bdclx: $bdclx},fwColModel, '');
                } else if (this.id == "cqTab") {
                    $bdclx = 'TDQT';
                    $("#cq").addClass("active");
                    $("#dyhTab").click();
                    var cqUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
                    cqTableInit();
                    if (isLoadGrid("cq"))
                        tableReload("cq-grid-table", cqUrl, {hhSearch: '', bdclx: $bdclx},fwColModel, '');
                } else if (this.id == "tdTab") {
                    $bdclx = 'TD';
                    $("#td").addClass("active");
                    $("#dyhTab").click();
                    var tdUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
                    tdTableInit();
                    if (isLoadGrid("td"))
                        tableReload("td-grid-table", tdUrl, {hhSearch: '', bdclx: $bdclx}, fwColModel, '');
                }
                else if (this.id == "hyTab") {
                    $bdclx = 'HY';
                    $("#hy").addClass("active");
                    $("#dyhTab").click();
                    var hyUrl = "${bdcdjUrl}/bdcsjpp/getQzYwxxFwJsonByPage";
                    hyTableInit();
                    if (isLoadGrid("hy"))
                        tableReload("hy-grid-table", hyUrl, {hhSearch: '', bdclx: $bdclx}, fwColModel, '');
                }
            }
        })
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        if (gdTabOrderArray != null && gdTabOrderArray.length > 0) {
            if (gdTabOrderArray[0] == 'td')
                $("#tdTab").click();
            else if (gdTabOrderArray[0] == 'lq')
                $("#lqTab").click();
            else if (gdTabOrderArray[0] == 'cq')
                $("#cqTab").click();
            else if (gdTabOrderArray[0] == 'hy')
                $("#hyTab").click();
            else
                $("#fwTab").click();
        }
        /*   文字水印  */
        $(".watermarkText").watermark();

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
            tableReload("fw-grid-table", fwUrl, {hhSearch: hhSearch, bdclx: $bdclx}, fwColModel, '');
        })
        $("#td_search").click(function () {
            var hhSearch = $("#td_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
            tableReload("td-grid-table", tdUrl, {hhSearch:hhSearch, bdclx: $bdclx},fwColModel, '');
        })
        $("#lq_search").click(function () {
            var hhSearch = $("#lq_search_qlr").val();
            var lqUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
            tableReload("lq-grid-table", lqUrl, {hhSearch:hhSearch, bdclx: $bdclx}, fwColModel, '');
        })
        $("#cq_search").click(function () {
            var hhSearch = $("#cq_search_qlr").val();
            var cqUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
            tableReload("cq-grid-table", cqUrl, {hhSearch:hhSearch, bdclx: $bdclx}, fwColModel, '');
        })
        $("#hy_search").click(function () {
            var hhSearch = $("#hy_search_qlr").val();
            var hyUrl = "${bdcdjUrl}/todatasjpp/getQzYwxxFwJsonByPage";
            tableReload("hy-grid-table", hyUrl, {hhSearch:hhSearch, bdclx: $bdclx}, fwColModel, '');
        })
        $("#dyh_search").click(function () {
            var hhSearch = $("#dyh_search_qlr").val();
            var dyhUrl = "${bdcdjUrl}/todatasjpp/getBdcXmPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch}, dyhColModel, '');
        })
        //保存事件
        $("#save_fw").click(function () {
            var proid = $("#proid").val();
            var ppzt = $("#ppzt").val();
            if (ppzt != '1') {
                if (proid != "") {
                    //遮罩
                    $.blockUI({message: "请稍等……"});
                    $.ajax({
                        url: '${bdcdjUrl}/todatasjpp/creatCsdj?lx=' + $bdclx,
                        type: 'get',
                        dataType: 'json',
                        data: $("#form").serialize(),
                        success: function (data) {
                        <#--openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid);-->
                            tipInfo(data);
                            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                            $("#dyh-grid-table").trigger("reloadGrid");
                            setTimeout($.unblockUI, 10);
                        },
                        error: function (data) {
                            tipInfo("创建项目失败！");
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
                else {
                    tipInfo("请选择不动产项目");
                }
            }
            else {

                tipInfo("您所选信息已经匹配过！");
            }
        })
        $("#save_td").click(function () {
            var proid = $("#proid").val();
            var ppzt = $("#ppzt").val();
            if (ppzt != '1') {
                if (proid != "") {
                    //遮罩
                    $.blockUI({message: "请稍等……"});
                    $.ajax({
                        url: '${bdcdjUrl}/todatasjpp/creatCsdj?lx=' + $bdclx,
                        type: 'get',
                        dataType: 'json',
                        data: $("#form").serialize(),
                        success: function (data) {
                        <#--openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid);-->
                            tipInfo(data);
                            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                            $("#dyh-grid-table").trigger("reloadGrid");
                            setTimeout($.unblockUI, 10);
                        },
                        error: function (data) {
                            tipInfo("创建项目失败！");
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
                else {
                    tipInfo("请选择不动产项目");
                }
            }
            else {

                tipInfo("您所选信息已经匹配过！");
            }
        })
        $("#save_lq").click(function () {
            var proid = $("#proid").val();
            var ppzt = $("#ppzt").val();
            if (ppzt != '1') {
                if (proid != "") {
                    //遮罩
                    $.blockUI({message: "请稍等……"});
                    $.ajax({
                        url: '${bdcdjUrl}/todatasjpp/creatCsdj?lx=' + $bdclx,
                        type: 'get',
                        dataType: 'json',
                        data: $("#form").serialize(),
                        success: function (data) {
                        <#--openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid);-->
                            tipInfo(data);
                            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                            $("#dyh-grid-table").trigger("reloadGrid");
                            setTimeout($.unblockUI, 10);
                        },
                        error: function (data) {
                            tipInfo("创建项目失败！");
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
                else {
                    tipInfo("请选择不动产项目");
                }
            }
            else {

                tipInfo("您所选信息已经匹配过！");
            }
        })
        $("#save_cq").click(function () {
            var proid = $("#proid").val();
            var ppzt = $("#ppzt").val();
            if (ppzt != '1') {
                if (proid != "") {
                    //遮罩
                    $.blockUI({message: "请稍等……"});
                    $.ajax({
                        url: '${bdcdjUrl}/todatasjpp/creatCsdj?lx=' + $bdclx,
                        type: 'get',
                        dataType: 'json',
                        data: $("#form").serialize(),
                        success: function (data) {
                        <#--openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid);-->
                            tipInfo(data);
                            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                            $("#dyh-grid-table").trigger("reloadGrid");
                            setTimeout($.unblockUI, 10);
                        },
                        error: function (data) {
                            tipInfo("创建项目失败！");
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
                else {
                    tipInfo("请选择不动产项目");
                }
            }
            else {

                tipInfo("您所选信息已经匹配过！");
            }
        })
        $("#save_hy").click(function () {
            var proid = $("#proid").val();
            var ppzt = $("#ppzt").val();
            if (ppzt != '1') {
                if (proid != "") {
                    //遮罩
                    $.blockUI({message: "请稍等……"});
                    $.ajax({
                        url: '${bdcdjUrl}/todatasjpp/creatCsdj?lx=' + $bdclx,
                        type: 'get',
                        dataType: 'json',
                        data: $("#form").serialize(),
                        success: function (data) {
                        <#--openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid);-->
                            tipInfo(data);
                            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                            $("#dyh-grid-table").trigger("reloadGrid");
                            setTimeout($.unblockUI, 10);
                        },
                        error: function (data) {
                            tipInfo("创建项目失败！");
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
                else {
                    tipInfo("请选择不动产项目");
                }
            }
            else {

                tipInfo("您所选信息已经匹配过！");
            }
        })
        //定位
        $("#ace-settings-btn").click(function () {
            if ($("#ace-settings-box").hasClass("open")) {
                $("#iframe").attr("src", $("#iframeSrcUrl").val())
            }
        })

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth = $(".tab-content").width();
            $("#fw-grid-table,#lq-grid-table,#dyh-grid-table,#cq-grid-table,#td-grid-table,#fwTd-grid-table").jqGrid('setGridWidth', contentWidth);
        });

        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

    })
    function hideModal(proid) {
        if (proid && proid != undefined) {
            openWin('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid);
        }
    }
    function openWin(url) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    //提示信息
    function tipInfo(msg) {
        // bootbox.dialog({
        //     message: "<h3><b>" + msg + "</b></h3>",
        //     title: "",
        //     buttons: {
        //         main: {
        //             label: "关闭",
        //             className: "btn-primary"
        //         }
        //     }
        // });
        // return;
        $.Prompt(msg,1500);
    }
    //选择房产证
    function fwSel(bdcdyh,ywh,xmbh,ywly) {
        if (ywh && ywh != 'undefined') {
            $("#gdproid").val(ywh);
        }
        $("#proid").val("");
        $("#ppzt").val("");
        if (xmbh && xmbh != 'undefined') {
            $("#ppzt").val('1');
        }
        if (bdcdyh && bdcdyh != 'undefined') {
            $("#bdcdyh").val(bdcdyh);
            $.blockUI({message: "请稍等……"});
            $.ajax({
                url: '${bdcdjUrl}/todatasjpp/getBdcDyhPagesJson',
                type: 'get',
                dataType: 'json',
                data: {hhSearch: bdcdyh, bdclx: $bdclx},
                success: function (data) {
                    $("#djId").val(data);
                    setTimeout($.unblockUI, 10);
                },
                error: function (data) {
                    tipInfo("地籍数据中没有对应的不动产单元！");
                    setTimeout($.unblockUI, 10);
                }
            });
        }
        else{
            tipInfo("您所选择业务信息没有不动产单元号，请核对！");
        }
    }
    //选择土地
    function tdSel(bdcdyh,ywh,xmbh,ywly) {
        if (ywh && ywh != 'undefined') {
            $("#gdproid").val(ywh);
        }
        $("#proid").val("");
        $("#ppzt").val("");
        if (xmbh && xmbh != 'undefined') {
            $("#ppzt").val('1');
        }
        if (bdcdyh && bdcdyh != 'undefined') {
            $("#bdcdyh").val(bdcdyh);
            $.blockUI({message: "请稍等……"});
            $.ajax({
                url: '${bdcdjUrl}/todatasjpp/getBdcDyhPagesJson',
                type: 'get',
                dataType: 'json',
                data: {hhSearch: bdcdyh, bdclx: $bdclx},
                success: function (data) {
                    $("#djId").val(data);
                    setTimeout($.unblockUI, 10);
                },
                error: function (data) {
                    tipInfo("地籍数据中没有对应的不动产单元！");
                    setTimeout($.unblockUI, 10);
                }
            });
        }
        else{
            tipInfo("您所选择业务信息没有不动产单元号，请核对！");
        }
    }
    //选择不动产单元号的赋值操作
    function dyhSel(proid) {
        if (proid && proid != 'undefined') {
            $("#proid").val(proid);
        } else {
            $("#proid").val("");
        }
    }
    function tableReload(table, Url, data, colModel, loadComplete) {
        var index = 0;
        var jqgrid = $("#" + table);
        if (colModel == '' && loadComplete == '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        } else if (loadComplete == '' && colModel != '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel});
        } else if (loadComplete != '' && colModel != '') {
            jqgrid.setGridParam({
                url: Url,
                datatype: 'json',
                page: 1,
                postData: data,
                colModel: colModel,
                loadComplete: loadComplete
            });
        }
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function fwTableInit() {
        var grid_selector = "#fw-grid-table";
        var pager_selector = "#fw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'YWH'},
            colNames: ['序列', '业务号', '业务名称', '受理号', '登记时间', '行政区名称','业务来源', '办理状态'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    //获取匹配状态
    function getPpzt(ppzt, table, rowid) {
        if (ppzt == "")
            ppzt = '<span class="label label-warning">等待办理</span>';
        else
            ppzt = '<span class="label label-success">已经办理</span>';
        table.setCell(rowid, "XMBH", ppzt);
    }
    //土地证初始化
    function tdTableInit() {
        var grid_selector = "#td-grid-table";
        var pager_selector = "#td-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'YWH'},
            colNames: ['序列', '业务号', '业务名称', '受理号', '登记时间', '行政区名称','业务来源', '办理状态'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    //草权表初始化
    function cqTableInit() {
        var grid_selector = "#cq-grid-table";
        var pager_selector = "#cq-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'YWH'},
            colNames: ['序列', '业务号', '业务名称', '受理号', '登记时间', '行政区名称','业务来源', '办理状态'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //林权表初始化
    function lqTableInit() {
        var grid_selector = "#lq-grid-table";
        var pager_selector = "#lq-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'YWH'},
            colNames: ['序列', '业务号', '业务名称', '受理号', '登记时间', '行政区名称','业务来源', '办理状态'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //海域表初始化
    function hyTableInit() {
        var grid_selector = "#hy-grid-table";
        var pager_selector = "#hy-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'YWH'},
            colNames: ['序列', '业务号', '业务名称', '受理号', '登记时间', '行政区名称','业务来源', '办理状态'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    function dyhTableInit() {
        var grid_selector = "#dyh-grid-table";
        var pager_selector = "#dyh-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'PROID'},
            colNames: ['序列', '创建人', "受理编号", '申请类型'],
            colModel: dyhColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false, /*
            multiboxonly:true,
            multiselect:true,*/
            loadComplete: dyhLoadComplete,
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
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

    /**
     * 根据key获取是否加载数据
     * @param key
     * @returns {boolean}
     */
    function isLoadGrid(key) {
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        var gdTabLoadData = "${gdTabLoadData!}";
        var gdTabLoadDataArray = new Array();
        gdTabLoadDataArray = gdTabLoadData.split(",");
        if (gdTabOrderArray.length == gdTabLoadDataArray.length) {
            for (var i = 0; i < gdTabOrderArray.length; i++) {
                if (gdTabOrderArray[i] == key) {
                    var loadGrid = gdTabLoadDataArray[i];
                    if (loadGrid == 1) {
                        return true;
                        break;
                    } else {
                        return false;
                        break;
                    }
                }
            }
            return false;
        } else
            return false;
    }

    function refreshStore() {
        if ($bdclx == "TDFW") {
            $("#fw_search").click();
            $("#fwtd_search").click();
        } else if ($bdclx == "TD")
            $("#td_search").click();
        else if ($bdclx == "TDQT")
            $("#cq_search").click();
        else if ($bdclx == "TDSL")
            $("#lq_search").click();
        else if ($bdclx == "HY")
            $("#hy_search").click();
    }

</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <#list gdTabOrderList as gdTabOrder>
                            <#if gdTabOrder=='td'>
                                <li>
                                    <a data-toggle="tab" id="tdTab" href="#td">
                                        纯土地证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='lq'>
                                <li>
                                    <a data-toggle="tab" id="lqTab" href="#lq">
                                        林权证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='cq'>
                                <li>
                                    <a data-toggle="tab" id="cqTab" href="#cq">
                                        草权证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='hy'>
                                <li>
                                    <a data-toggle="tab" id="hyTab" href="#hy">
                                        海域使用证
                                    </a>
                                </li>
                            <#else>
                                <li>
                                    <a data-toggle="tab" id="fwTab" href="#fw">
                                        房产证
                                    </a>
                                </li>

                            </#if>

                        </#list>

                    </ul>
                    <div class="tab-content">
                        <div id="fw" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                                   data-watermark="请输入业务号/业务名称/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="fw_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="save_fw">
                                                缮证
                                            </a>
                                        </td>
                                    <#--<td>-->
                                    <#--<button type="button" class="btn01 AdvancedButton" id="opnSearchBtn">高级搜索-->
                                    <#--</button>-->
                                    <#--</td>-->
                                    </tr>
                                </table>
                            </div>
                            <table id="fw-grid-table"></table>
                            <div id="fw-grid-pager"></div>
                        </div>
                    <#--林权-->
                        <div id="lq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="lq_search_qlr"
                                                   data-watermark="请输入业务号/业务名称/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="lq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="save_lq">
                                                缮证
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table id="lq-grid-table"></table>
                            <div id="lq-grid-pager"></div>
                        </div>
                    <#--草权-->
                        <div id="cq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="cq_search_qlr"
                                                   data-watermark="请输入业务号/业务名称/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="cq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="save_cq">
                                                缮证
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>

                            <table id="cq-grid-table"></table>
                            <div id="cq-grid-pager"></div>
                        </div>
                    <#--海域-->
                        <div id="hy" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="hy_search_qlr"
                                                   data-watermark="请输入业务号/业务名称/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="hy_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="save_hy">
                                                缮证
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>

                            <table id="hy-grid-table"></table>
                            <div id="hy-grid-pager"></div>
                        </div>
                    <#--土地-->
                        <div id="td" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                                                   data-watermark="请输入业务号/业务名称/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="td_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="save_td">
                                                缮证
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table id="td-grid-table"></table>
                            <div id="td-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" id="dyhTab" href="#file">
                                不动产项目
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="file" class="tab-pane in active ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="dyh_search_qlr"
                                                   data-watermark="请输入受理编号/创建人/申请类型名称">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="dyh_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table id="dyh-grid-table"></table>
                            <div id="dyh-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="djId" name="djId">
    <input type="hidden" id="proid" name="proid">
    <input type="hidden" id="bdcdyh" name="bdcdyh">
    <input type="hidden" id="gdproid" name="gdproid"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
