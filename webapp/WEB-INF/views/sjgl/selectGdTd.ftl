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

<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
    $mulData = new Array();
    $mulRowid = new Array();
    $bdclx = 'TD';
    tdColModel = [
        {name:'XL', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '</span>'
        }
        },
        {name: 'SDF', index: 'SDF', width: '10%', sortable: false},
        {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
        {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
        {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
        {name: 'PPZT', index: '', width: '13%', sortable: false},
        {name: 'STATUS', index: '', width: '13%', sortable: false},
        {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
        {name: 'RF1DWMC', index: 'RF1DWMC', width: '0%', sortable: false, hidden: true},
        {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
        {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
    ];
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
                serch();
            }
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
            url:"${bdcdjUrl}/bdcJgSjgl/getGdTdJson?qlids=${qlids!}&checkMulGdFw=true",
            datatype: "json",
            height:400,
            jsonReader:{id:'QLID'},
            colNames:['序列', '锁定否', '地籍号', '坐落', "土地证号", '匹配状态', '权利状态', 'QLID', '权利人', 'PROID','TDID'],
            colModel:tdColModel,
            viewrecords:true,
            rowNum:10,
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:false,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                var jqData = $(grid_selector).jqGrid("getRowData");
                var rowIds = $(grid_selector).jqGrid('getDataIDs');
                $.each(jqData, function (index, data) {
                    asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
                    getSdStatus(data.QLID, data.TDZH, $(grid_selector), data.PROID, "", "TD");
                })
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });
    //zx 获取房产证抵押 查封 预告 异议 状态
    function asyncGetGdFwxx(bdcid, table, rowid, qlzt, qlid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                //正常
                var zls = result.zls;
                if (result.qlzts == null || result.qlzts == "") {
                    cellVal += '<span class="label label-success">正常</span>';
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?bdclx=" + $bdclx + "&zl=" + zls,
                        dataType: "json",
                        async: false,
                        success: function (result) {
                            if (result.rows != '') {
                                $.ajax({
                                    type: "GET",
                                    url: "${bdcdjUrl}/queryBdcdy/getBdcdyhQlxx?bdcdyh=" + result.rows[0].BDCDYH,
                                    dataType: "json",
                                    async: false,
                                    success: function (result) {
                                        if (!result.dy) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                                        }
                                        if (!result.cf) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">查封</span><span> </span>';
                                        }


                                    }
                                });
                            }
                        }
                    });
                } else {
                    var qlzts = result.qlzts.split(",");
                    for (var i = 0; i < qlzts.length; i++) {
                        //zhouwanqing 防止其他权利与注销同在
                        if (qlzts[i] == "ZX") {
                            cellVal = '<span class="label label-danger">注销</span><span> </span>';
                            break;
                        } else if (qlzts[i] == "DY") {
                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        } else if (qlzts[i] == "CF") {
                            cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        } else if (qlzts[i] == "YG") {
                            cellVal += '<span class="label label-info">预告</span><span> </span>';
                        } else if (qlzts[i] == "YY") {
                            cellVal += '<span class="label label-info">异议</span><span> </span>';
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
    //zx 获取土地证抵押 查封 预告 异议 状态
    function asyncGetGdTdxx(table, qlid, proid) {
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdTdxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid,
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
                        } else if (qlzts[i] == "DI") {
                            cellVal += '<span class="label label-danger">地役</span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }

                table.setCell(qlid, "STATUS", cellVal);
                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
                    table.setCell(qlid, "RF1DWMC", result.qlrs);
                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
                    table.setCell(qlid, "TDZH", result.cqzhs);
                if (result.zls != null && result.zls != "" && result.zls != "null")
                    table.setCell(qlid, "ZL", result.zls);
                if (result.djhs != null && result.djhs != "" && result.djhs != "null")
                    table.setCell(qlid, "LDJH", result.djhs);
                if (result.xdjhs != null && result.xdjhs != "" && result.xdjhs != "null")
                    table.setCell(qlid, "DJH", result.xdjhs);

                getQtPpzt(result.ppzt, table, qlid);
            }
        });
    }
    //zx 获取锁定状态
    function getSdStatus(qlid, cqzh, table, proid, index, lx) {
        var cellVal = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getXzyy",
            dataType: "json",
            data: {cqzh: cqzh},
            async: false,
            success: function (jsonData) {
                if(jsonData.msg == "false"){
                    var xzyy = jsonData.xzyy;
                    if (lx == "FW") {
                        cellVal = '<div title="'+xzyy+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span style="font-family: cursive;"> ' + index + '</span>' +
                        '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                        '</div>';
                        table.setCell(qlid, "XL", cellVal);
                    }

                    if (lx == "TD") {
                        cellVal = '<div title="'+xzyy+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                        '</div>';
                        table.setCell(qlid, "SDF", cellVal);
                    }

                }
            },
            error: function (data) {
            }
        });
    }

    //获取匹配状态
    function getQtPpzt(ppzt, table, rowid) {
        if (ppzt == "2")
            ppzt = '<span class="label label-success" value="2">已匹配</span>';
        else if (ppzt == "1")
            ppzt = '<span class="label label-warning" value="1">部分匹配</span>';
        else
            ppzt = '<span class="label label-danger" value="0">待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }
    /* 调用子页面方法  */
    function showModal() {

        var frame = window.parent;
        while (frame != frame.parent) {
            frameframe = frame.parent;
        }
        frame.postMessage("childCall", "*");
    }

    function serch() {
        var dcxc = $("#search_xmmc").val();
        var Url = "${bdcdjUrl}/bdcJgSjgl/getGdTdJson";
        tableReload("grid-table", Url, {hhSearch: dcxc,selqlids:"${qlids!}"});
    }

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
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
                               data-watermark="请输入坐落/土地证号/地籍号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                </tr>
            </table>
        </div>
        <div class="bootbox-body" style="background: #fafafa;">

            <table id="grid-table"></table>
            <div id="grid-pager"></div>
        </div>
    </div>


<#--无用div 防止ace报错-->
    <div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<#--隐藏查询条件，方便再此查询-->
    <input type="hidden" id="formHidden" name="formHidden"/>
</@com.html>