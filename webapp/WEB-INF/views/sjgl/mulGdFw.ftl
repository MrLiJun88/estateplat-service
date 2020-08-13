<@com.html title="不动产登记业务管理系统" import="ace">
<style type="text/css">
    /*高级搜索的样式修改*/
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

    /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width: 100% !important;
        margin: 0px 5px 0px 0px
    }

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

    .chosen-container > .chosen-single, [class*="chosen-container"] > .chosen-single {
        height: 34px;

    }
</style>

<script type="text/javascript">
    $(function () {

        //下拉框
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据", width: "100%"});
        $(window).on('resize.chosen', function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
        }).trigger('resize.chosen');


        try {
            Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
            content = $("#search_xmmc");
        } catch (err) {
            alert("请安装Active X控件CaptureVideo.cab");
        }
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
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});


        //绑定回车键
        $('#search_xmmc').keydown(function (event) {
            if (event.keyCode == 13) {
                searchGdFw();
            }
        });

        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcCfql/getbdcCfPagesJsonace?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc: ""});
        })
        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(window).trigger('resize.chosen');
            $(".modal-dialog").css({"_margin-left": "25%"});
        });
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
            url: "${bdcdjUrl!}/bdcJgSjgl/getGdFwJson",
            datatype: "json",
            postData: {qlids: parent.window.opener.$("#qlids").val(), checkMulGdFw: "true"},
            height: 'auto',
            jsonReader: {id: 'QLID'},
            colNames: ['权利人', '证书类型', "房产证号", '坐落', '匹配状态', '权利状态', '操作', 'QLZT', 'PROID', 'QLID'],
            colModel: [
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
                {name: 'ZSLX', index: 'ZSLX', width: '15%', sortable: false},
                {name: 'FCZH', index: 'FCZH', width: '18%', sortable: false},
                {name: 'FWZL', index: 'FWZL', width: '26%', sortable: false},
                {name: 'PPZT', index: '', width: '13%', sortable: false},
                {name: 'STATUS', index: '', width: '18%', sortable: false},
                {
                    name: 'mydy',
                    index: '',
                    width: '5%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div style="margin-left:7px;"><div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="delFw(\'' + rowObject.PROID + '\',\'' + rowObject.QLID + '\',\'' + rowObject.FCZH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div>'
                    }
                },
                {name: 'QLZT', index: 'QLZT', width: '0%', sortable: false, hidden: true},
                {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            rownumbers: true,
            rownumWidth: 50,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    qlrForFwTable("#grid-table");
                    var replacement =
                    {
                        'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
                    };
                    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                        var icon = $(this);
                        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                    })
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
            },
            onCellSelect: function (rowid) {

            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
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

    });
    function delFw(gdproid, qlid, fczh) {


        var table = $("#grid-table");
        var delConfirm = confirm("是否确定删除？");
        if (delConfirm == true) {
            //删除原房产证号和土地证号
            var array = parent.window.opener.$selectArray;
            for (var i = 0; i != array.length; i++) {
                if (array[0] == fczh) {
                    array.splice(i, 1);
                }
            }
            table.jqGrid("delRowData", gdproid);
            var gdproids = parent.window.opener.$("#gdproids").val();
            var qlids = parent.window.opener.$("#qlids").val();
            if (gdproids.indexOf(",") < 0) {
                gdproids = gdproids.replace(gdproid, "");
            } else if (gdproids.indexOf("," + gdproid) > -1) {
                gdproids = gdproids.replace("," + gdproid, "");
            } else {
                gdproids = gdproids.replace(gdproid + ",", "");
            }
            if (qlids.indexOf(",") < 0) {
                qlids = qlids.replace(qlid, "");
            } else if (qlids.indexOf("," + qlid) > -1) {
                qlids = qlids.replace("," + qlid, "");
            } else {
                qlids = qlids.replace(qlid + ",", "");
            }
            parent.window.opener.$("#gdproids").val(gdproids);
            parent.window.opener.$("#qlids").val(qlids);
            window.location.reload();
        }
    }
    function qlrForFwTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
        })
    }
    //获取土地林权匹配状态
    function getQtPpzt(ppzt, table, rowid) {
        if (ppzt == "2")
            ppzt = '<span class="label label-success">已匹配</span>';
        else if (ppzt == "1")
            ppzt = '<span class="label label-warning">部分匹配</span>';
        else
            ppzt = '<span class="label label-danger">待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }
    //获取过渡房屋权利人
    function getfwQlrByQlid(djid, table, rowid) {
        if (djid == null || djid == "undefined")
            djid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/getFwQlrByQlid?djid=" + djid,
            success: function (result) {
                var qlr = result.qlr;
                if (qlr == null || qlr == "undefined")
                    qlr = "";
                var cellVal = "";
                cellVal += '<span>' + qlr + '</span>';
                table.setCell(rowid, "RF1DWMC", cellVal);
            }
        });
    }
    //获取过渡房屋坐落
    function getfwZlByQlid(djid, table, rowid) {
        if (djid == null || djid == "undefined")
            djid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/getFwZlByQlid?djid=" + djid,
            success: function (result) {
                var fwzl = result.fwzl;
                if (fwzl == null || fwzl == "undefined")
                    fwzl = "";
                var cellVal = "";
                cellVal += '<span>' + fwzl + '</span>';
                table.setCell(rowid, "FWZL", cellVal);
            }
        });
    }
    //lst 获取房产证抵押 查封 预告 异议 状态
    function getFczQlzt(bdcid, table, rowid, qlzt, qlid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/getFczQlzt?bdcid=" + bdcid + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                if (qlzt != "" && qlzt != 0) {
                    cellVal += '<span class="label label-danger">注销</span><span> </span>';
                } else {
                    if (result.fwsyq) {
                        if (result.cf && result.dy && result.yg && result.yy) {
                            cellVal = '<span class="label label-success">正常</span>';
                        } else {//有查封 预告 或 抵押
                            if (!result.cf) {
                                cellVal += '<span class="label label-warning">查封</span><span> </span>';
                            }
                            if (!result.dy) {
                                cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                            }
                            if (!result.yg) {
                                cellVal += '<span class="label label-info">预告</span><span> </span>';
                            }
                            if (!result.yy) {
                                cellVal += '<span class="label label-error">异议</span><span> </span>';
                            }
                        }
                    } else {
                        cellVal = '<span class="label label-success">正常</span>';
                    }
                }
                table.setCell(rowid, "STATUS", cellVal);
            }
        });
    }
    //zx 获取房产证抵押 查封 预告 异议 状态
    function asyncGetGdFwxx(bdcid, table, rowid, qlzt, qlid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=TDFW&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                //正常

                if (result.qlzts == null || result.qlzts == "") {
                    cellVal = '<span class="label label-success">正常</span>';
                } else {
                    var qlzts = result.qlzts.split(",");
                    for (var i = 0; i < qlzts.length; i++) {
                        if (qlzts[i] == "DY") {
                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        } else if (qlzts[i] == "CF") {
                            cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        } else if (qlzts[i] == "YG") {
                            cellVal += '<span class="label label-info">预告</span><span> </span>';
                        } else if (qlzts[i] == "YY") {
                            cellVal += '<span class="label label-info">异议</span><span> </span>';
                        } else if (qlzts[i] == "ZX") {
                            cellVal += '<span class="label label-danger">注销</span><span> </span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }

                table.setCell(qlid, "STATUS", cellVal);
                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
                    table.setCell(qlid, "RF1DWMC", result.qlrs);
                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
                    table.setCell(qlid, "FCZH", result.cqzhs);
                if (result.zls != null && result.zls != "" && result.zls != "null")
                    table.setCell(qlid, "FWZL", result.zls);

                getQtPpzt(result.ppzt, table, qlid);
            }
        });
    }
    /* 调用子页面方法  */
    function showModal() {

        var frame = window.parent;
        while (frame != frame.parent) {
            frameframe = frame.parent;
        }
        frame.postMessage("childCall", "*");
    }

    function searchGdFw() {
        var dcxc = $("#search_xmmc").val();
        var qlids = parent.window.opener.$("#qlids").val();
        var Url = "${bdcdjUrl}/bdcJgSjgl/getGdFwJson?qlids=" + qlids;
        tableReload("grid-table", Url, {hhSearch: dcxc});
    }

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function save() {
        window.opener.document.getElementById('save').click();
        window.close();
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
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }
</script>

<div class="space-10"></div>
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入权利人/坐落/房产证号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="searchGdFw()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td>
                        <a href="#" onclick="save()">
                            <button type="button" class="btn btn-sm btn-primary">创建</button>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                <#--<td>-->
                <#--<button type="button" class="btn01 AdvancedButton" onclick="openDevices_onclick(0)">扫描</button>-->
                <#--</td>-->
                <#--<td style="border: 0px">&nbsp;</td>-->
                <#--<td>-->
                <#--<button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>-->
                <#--</td>-->
                </tr>
            </table>
        </div>
        <!--end  高级搜索 -->
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<#--<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">-->
<#--<div class="modal-dialog gjSearchPop-modal">-->
<#--<div class="modal-content">-->
<#--<div class="modal-header">-->
<#--<h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>-->
<#--<button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>-->
<#--</button>-->
<#--</div>-->
<#--<div class="bootbox-body" style="background: #fafafa;">-->
<#--<form class="form advancedSearchTable" id="gjSearchForm">-->
<#--<div class="row">-->
<#--<div class="col-xs-2">-->
<#--<label>不动产单元号：</label>-->
<#--</div>-->
<#--<div class="col-xs-4">-->
<#--<input type="text" name="bdcdyh" class="form-control" onclick="">-->
<#--</div>-->

<#--<div class="col-xs-2">-->
<#--<label>查封文号：</label>-->
<#--</div>-->
<#--<div class="col-xs-4">-->
<#--<input type="text" name="cfwh" class="form-control">-->
<#--</div>-->
<#--</div>-->
<#--<div class="row">-->
<#--<div class="col-xs-2">-->
<#--<label>查封类型：</label>-->

<#--</div>-->
<#--<div class="col-xs-4">-->
<#--<select name="cflx" class="form-control chosen-select" data-placeholder=" ">-->
<#--<option></option>-->
<#--<#list bdcCflxlist as bdcCflx>-->
<#--<option value="${bdcCflx}">${bdcCflx}</option>-->
<#--</#list>-->
<#--</select>-->
<#--</div>-->

<#--<div class="col-xs-2">-->
<#--<label>查封机关：</label>-->
<#--</div>-->
<#--<div class="col-xs-4">-->
<#--<input type="text" name="cfjg" class="form-control">-->
<#--</div>-->
<#--</div>-->
<#--<div class="row">-->
<#--<div class="col-xs-2">-->
<#--<label>被执行人：</label>-->
<#--</div>-->
<#--<div class="col-xs-4">-->
<#--<input type="text" name="bzxr" class="form-control">-->
<#--</div>-->
<#--</div>-->
<#--</form>-->
<#--</div>-->
<#--<div class="modelFooter">-->
<#--<button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->



<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>