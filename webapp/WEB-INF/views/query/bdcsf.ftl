<@com.html title="不动产登记业务管理系统" import="ace,public,combo">
<style>
    .col-xs-1 {
        padding-left: 0px;
        padding-right: 0px;
        text-align: right;
    }
</style>
  <script type="text/javascript" src="${bdcdjUrl!}/static/js/export/jquery.base64.js"></script>
  <script type="text/javascript" src="${bdcdjUrl!}/static/js/export/tableExport.js"></script>
    <script type="text/javascript">
        $mulRowid = new Array();
        $mulData = new Array();
        $(function () {
            InitTable();
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
            //时间控件
            $('.date-picker').datepicker({
                autoclose: true,
                todayHighlight: true,
                language: 'zh-CN'
            }).next().on(ace.click_event, function () {
                $(this).prev().focus();
            });

            Array.prototype.remove = function (index) {
                if (index > -1) {
                    this.splice(index, 1);
                }
            };
            //项目表搜索事件
            $("#search").click(function () {
                var Url = "${bdcdjUrl}/bdcSf/getBdcSfxxListJsonByPage?" + $("#searchForm").serialize();
                tableReload("grid-table", Url, {dexc: ""});
            });
            $("select[name='jkr']").comboSelect();
            $("select[name='jkr']").hide();
            // document.getElementById("combo_jkr").style.padding-bottom="0px";
        })

        function InitTable() {
            var grid_selector = "#grid-table";
            var pager_selector = "#grid-pager";
            //绑定回车键
            $('#sjh,#fwbm,#zl').keydown(function (event) {
                if (event.keyCode == 13) {
                    $("#search_btn").click();
                }
            });
            $(window).on('resize.jqGrid', function () {
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            });

            var parent_column = $(grid_selector).closest('[class*="col-"]');
            $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
                if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }
            });

            jQuery(grid_selector).jqGrid({
            <#--url: "${bdcdjUrl}/bdcSf/getBdcSfxxListJsonByPage",-->
                datatype: "json",
                height: 'auto',
                jsonReader: {id: 'SFXXID'},
                colNames: ['序号', '受理编号', '缴款人', '不动产权证号(证明号)', '义务(抵押人)', '坐落', '房屋用途', '申请类型', '收费金额', '申请时间', '查询按钮', 'SFXXID', 'proid', 'wiid', 'sfxmid'],
                colModel: [
                    {name: 'XH', index: 'XH', width: '3%', sortable: false},
                    {name: 'SLBH', index: 'SLBH', width: '13%', sortable: false},
                    {name: 'JKR', index: 'JKR', width: '13%', sortable: false},
                    {name: 'BDCQZH', index: 'BDCQZH', width: '13%', sortable: false},
                    {name: 'YWRMC', index: 'YWRMC', width: '13%', sortable: false},
                    {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
                    {name: 'YT', index: 'YT', width: '10%', sortable: false},
                    {name: 'SQLX', index: 'SQLX', width: '10%', sortable: false},
                    {name: 'HJ', index: 'HJ', width: '10%', sortable: false},
                    {
                        name: 'CJSJ',
                        index: 'CJSJ',
                        width: '15%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (!cellvalue) {
                                return "";
                            }
                            var value = cellvalue;
                            var data = new Date(value).Format("yyyy年MM月dd日");
                            return data;
                        }, hidden: true
                    },
                    {
                        name: 'CX',
                        index: 'CX',
                        width: '10%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            cellvalue = '查看详情'
                            cell = '<a href="javascript:Look(\'' + rowObject.SFXXID + '\',\'' + rowObject.PROID + '\',\'' + rowObject.WIID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            return cell;
                        }
                    },
                    {name: 'SFXXID', index: 'SFXXID', width: '8%', sortable: false, hidden: true},
                    {name: 'PROID', index: 'PROID', width: '8%', sortable: false, hidden: true},
                    {name: 'WIID', index: 'WIID', width: '8%', sortable: false, hidden: true},
                    {name: 'SFXMID', index: 'SFXMID', width: '8%', sortable: false, hidden: true}
                ],
                viewrecords: true,
                rowNum: 10,
                rowList: [10, 20, 30, 100, 300, 500],
                pager: pager_selector,
                pagerpos: "left",
                altRows: false,
                multiboxonly: false,
                multiselect: true,
                loadComplete: function () {
                    var table = this;
                    setTimeout(function () {
                        updatePagerIcons(table);
                        enableTooltips(table);

                        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                        // qlrForTable("#grid-table");
                        // bdcqzhForTable("#grid-table");
                        // zlForTable("#grid-table");
                        dataForTable("#grid-table");

                    }, 0);
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    var url = $(grid_selector).getGridParam("url");
                    if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                        tipInfo("未搜索到该数据，请核实！");
                    }

                },
                onSelectAll: function (aRowids, status) {
                    var $myGrid = $(this);
                    //aRowids.forEach(function(e){
                    $.each(aRowids, function (i, e) {
                        var cm = $myGrid.jqGrid('getRowData', e);
                        if (cm.SFXXID == e) {
                            var index = $.inArray(e, $mulRowid);
                            if (status && index < 0) {
                                //混合查封中数据来源的标志
                                $mulData.push(cm);
                                $mulRowid.push(e);
                            } else if (!status && index >= 0) {
                                $mulData.remove(index);
                                $mulRowid.remove(index);
                            }
                        }
                    })
                },
                onSelectRow: function (rowid, status) {
                    var $myGrid = $(this);
                    var cm = $myGrid.jqGrid('getRowData', rowid);
                    if (cm.SFXXID == rowid) {
                        var index = $.inArray(rowid, $mulRowid);
                        if (status && index < 0) {
                            $mulData.push(cm);
                            $mulRowid.push(rowid);
                        } else if (!status && index >= 0) {
                            $mulData.remove(index);
                            $mulRowid.remove(index);
                        }
                    }
                },
                editurl: "",
                caption: "",
                autowidth: true
            });
        }

        function Look(sfxxid, proid, wiid) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSf/getSfdPrintData?proid=" + proid,
                success: function (result) {
                    var sqlxMc = encodeURI(encodeURI(result.sqlxMc));
                    var zl = encodeURI(encodeURI(result.zl));
                    var url = "${reportUrl}/ReportServer?reportlet=print%2Fbdc_sfgzd.cpt&op=write&proid=" + proid + "&sqlxMc=" + sqlxMc + "&wiid=" + wiid + "&isHb=" + result.isHb + "&sfxxid=" + sfxxid + "&zl=" + zl + "&isShowPljf=" + result.isShowPljf;
                    if (result.isSfztVersion == "true") {
                        var url = "${reportUrl}/ReportServer?reportlet=print%2Fbdc_sfgzd_xc.cpt&op=write&proid=" + proid + "&sqlxMc=" + sqlxMc + "&wiid=" + wiid + "&isHb=" + result.isHb + "&sfxxid=" + sfxxid + "&zl=" + zl;
                    }
                    window.open(url, "缴费项目", "height=600px,width=1000px,toolbar=no,menubar=no,scrollbars=no,resizable=no");
                }
            });
        }

        //为表格添加权利人列数据
        function qlrForTable(grid_selector) {
            var jqData = $(grid_selector).jqGrid("getRowData");
            var rowIds = $(grid_selector).jqGrid('getDataIDs');
            $.each(jqData, function (index, data) {
                getQlrByProid(data.PROID, $(grid_selector), rowIds[index]);
            })
        }

        //为表格添加完善数据
        function dataForTable(grid_selector) {
            var jqData = $(grid_selector).jqGrid("getRowData");
            var rowIds = $(grid_selector).jqGrid('getDataIDs');
            $.each(jqData, function (index, data) {
                getQlrByProid(data.PROID, $(grid_selector), rowIds[index]);
                getZlByProid(data.PROID, $(grid_selector), rowIds[index]);
                getBdcqzhByProid(data.PROID, $(grid_selector), rowIds[index]);
                getOtherByProid(data.PROID, $(grid_selector), rowIds[index]);
            })
        }

        //获取权利人
        function getQlrByProid(proid, table, rowid) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSf/getQlrByProid?proid=" + proid,
                success: function (result) {
                    var ywr = result.ywr;
                    if (ywr == null || ywr == "undefined")
                        ywr = "";
                    var cellVal = "";
                    cellVal += '<span>' + ywr + '</span>';
                    table.setCell(rowid, "YWRMC", cellVal);
                }
            });
        }

        //获取不动产权证号
        function getBdcqzhByProid(proid, table, rowid) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSf/getBdcqzhByProid?proid=" + proid,
                success: function (result) {
                    var bdcqzh = result.bdcqzh;
                    if (bdcqzh == null || bdcqzh == "undefined")
                        bdcqzh = "";
                    var cellVal = "";
                    cellVal += '<span>' + bdcqzh + '</span>';
                    table.setCell(rowid, "BDCQZH", cellVal);
                }
            });
        }

        //获取不动产权证号
        function getOtherByProid(proid, table, rowid) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSf/getOtherByProid?proid=" + proid + "&sfxxid=" + rowid,
                data: $("#searchForm").serialize(),
                success: function (result) {
                    table.setCell(rowid, "YT", result.dzwyt);
                    table.setCell(rowid, "SQLX", result.sqlxmc);
                    table.setCell(rowid, "HJ", result.hj);
                    table.setCell(rowid, "SFXMID", result.sfxmid);
                }
            });
        }

        //获取坐落
        function getZlByProid(proid, table, rowid) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSf/getZlByProid?proid=" + proid,
                success: function (result) {
                    var zl = result.zl;
                    if (zl == null || zl == "undefined")
                        zl = "";
                    var cellVal = "";
                    cellVal += '<span>' + zl + '</span>';
                    table.setCell(rowid, "ZL", cellVal);
                }
            });
        }

        //添加节点名称
        function getJdmc(grid_selector) {
            var jqData = $(grid_selector).jqGrid("getRowData");
            var rowIds = $(grid_selector).jqGrid('getDataIDs');
            $.each(jqData, function (index, data) {
                getJdmcByproid(data.PROID, $(grid_selector), rowIds[index]);
            })

        }

        function Trim(str, is_global) {
            var result;
            result = str.replace(/(^\s+)|(\s+$)/g, "");
            if (is_global.toLowerCase() == "g") {
                result = result.replace(/\s/g, "");
            }
            return result;
        }

        function getJdmcByproid(proid, table, rowid) {
            if (proid == null || proid == "undefined")
                proid = "";
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcXmQuery/getActivityNameByProid?proid=" + proid,
                async: false,
                success: function (result) {
                    if (result != "" && result != null) {
                        if (result == '缮证') {
                            $("#" + rowid).hide();
                        } else {
                            var cellVal = "";
                            cellVal += '<span>' + result + '</span>';
                            table.setCell(rowid, "JDMC", cellVal);
                        }
                    }

                }
            })
        }

        var timer;
        var winOpen;

        function printMul() {
            if ($mulRowid.length > 0) {
                var sfxxid = "";
                var sfxmid = "";
                for (var i = 0; i < $mulRowid.length; i++) {
                    if (sfxxid == "") {
                        sfxxid = $mulRowid[i];
                    } else {
                        sfxxid += "," + $mulRowid[i];
                    }
                }
                for (var i = 0; i < $mulData.length; i++) {
                    if (sfxmid == "") {
                        sfxmid = $mulData[i].SFXMID;
                    } else {
                        sfxmid += "," + $mulData[i].SFXMID;
                    }
                }
                if (sfxxid != "") {
                    $.ajax({
                        type: "POST",
                        url: "${bdcdjUrl}/bdcSf/printMul",
                        data: $("#searchForm").serialize() + '&' + $.param({sfxxid: sfxxid, sfxmid: sfxmid}),
                        success: function (result) {
                            var cxrq = encodeURI(encodeURI(result.cxrq));
                            var skrzh = encodeURI(encodeURI(result.skrzh));
                            var skrkhyh = encodeURI(encodeURI(result.skrkhyh));
                            var uuid = result.uuid;
                            if (sfxxid != "") {
                                var url = "${reportUrl}/ReportServer?reportlet=print%2Fbdc_sfgzd_mul.cpt&op=write&uuid=" + uuid + "&cxrq=" + cxrq + "&skrkhyh=" + skrkhyh + "&skrzh=" + skrzh;
                                winOpen = window.open(url, "缴费项目", "height=600px,width=1000px,toolbar=no,menubar=no,scrollbars=no,resizable=no");
                                timer = window.setInterval("IfWindowClosed('" + uuid + "')", 500);//每0.5秒监听一次
                            }
                        }
                    });
                }
            } else {
                $.ajax({
                    type: "POST",
                    url: "${bdcdjUrl}/bdcSf/printMul",
                    data: $("#searchForm").serialize(),
                    success: function (result) {
                        var sfxxid = result.sfxxid;
                        var sfxmid = result.sfxmid;
                        var cxrq = encodeURI(encodeURI(result.cxrq));
                        var skrzh = encodeURI(encodeURI(result.skrzh));
                        var skrkhyh = encodeURI(encodeURI(result.skrkhyh));
                        if (sfxxid != "") {
                            var url = "${reportUrl}/ReportServer?reportlet=print%2Fbdc_sfgzd_mul.cpt&op=write&sfxxid=" + sfxxid + "&sfxmid=" + sfxmid + "&cxrq=" + cxrq + "&skrkhyh=" + skrkhyh + "&skrzh=" + skrzh;
                            window.open(url, "缴费项目", "height=600px,width=1000px,toolbar=no,menubar=no,scrollbars=no,resizable=no");
                        }
                    }
                });
            }
        }

        function tableReload(table, Url, data) {
            var jqgrid = $("#" + table);
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
            jqgrid.trigger("reloadGrid");//重新加载JqGrid
        }


        function IfWindowClosed(uuid) {
            if (winOpen.closed == true) {
                //此处做关闭后的操作
                winOpen = null;
                window.clearInterval(timer)
                if (uuid != "") {
                    $.ajax({
                        type: "POST",
                        url: "${bdcdjUrl}/bdcSf/delPrintBdcSfxxByUuid",
                        data: {uuid: uuid},
                        success: function (result) {
                        }
                    });
                }
            }
        }

        function exportExcel() {
            var titleName = $("#jkr").val();
            if ($("#cjqssj").val() != null && $("#cjzzsj").val() != null) {
                titleName += '（' + new Date($("#cjqssj").val()).Format("yyyy/MM/dd") + '-' + new Date($("#cjzzsj").val()).Format("yyyy/MM/dd") + '）';
            }
            var tableid = "grid-table";
            var dd = $("#gbox_" + tableid + ' .ui-jqgrid-htable thead');
            var ee = $('#' + tableid);
            ee.find('.jqgfirstrow').remove();//干掉多余的无效行
            ee.find('tbody').before(dd);//合并表头和表数据
            ee.find('tr.ui-search-toolbar').remove();//干掉搜索框
            ee.tableExport({
                type: 'excel',
                escape: 'false',
                fileName: '导出' + new Date().getTime(),
                ignoreColumn: [0, 3, 11],
                titleName: titleName,
                countColumn: [9]
            });
            // var a = $("#" + tableid).find('thead');//把合并后的表头和数据拆分
            // $("#gbox_" + tableid + ' .ui-jqgrid-htable').append(a);
        }

        function print(){
            var sflx=$("#sflx").val();
            var sfxmmc=getSfxmmc(sflx);
            var jkr=$("#jkr").val();
            var cjqssj=$("#cjqssj").val();
            var cjzzsj=$("#cjzzsj").val();
            var url = "${reportUrl!}/ReportServer?reportlet=print%2FsfxxcxExport.cpt&op=write&jkr="+encodeURI(encodeURI(jkr))+"&cjqssj="+cjqssj+"&cjzzsj="+cjzzsj+"&sfxmmc="+encodeURI(encodeURI(sfxmmc));
            openWin(url);
        }
        function getSfxmmc(sflx){
            var sfxmmc="";
            if(isNotBlank(sflx)){
                $.ajax({
                    url: "${bdcdjUrl}/bdcSf/getSfxmmc",
                    type: "get",
                    async:false,
                    data: {sflx: sflx},
                    success: function (result) {
                        if(result!=null){
                            sfxmmc= result.sfxmmc;
                        }
                    }
                });
            }
            return sfxmmc;
        }

    </script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/djsjBdcdyQlShow.css"/>

    <div class="main-container">
        <input type="hidden" id="proid" value="${proid!}">

        <div class="space-10"></div>
        <div class="page-content">
            <form class="form" id="searchForm" style="width:1600px">

                <div class="row">
                    <div class="col-xs-1" style="width: 84px;text-align: right;">
                        <label>缴款人姓名：</label>
                    </div>
                    <div class="col-xs-2" style="width: 300px;height: 30px;">
                        <select id="jkr" name="jkr" style="width:180px;height: 30px;margin-left: 10px">
                            <#list bdcXtYhList as yh>
                                <option value="${yh.YHMC!}">${yh.YHMC!}</option>
                            </#list>
                        </select>

                    </div>
                    <div class="col-xs-1" style="width: 90px;text-align: right;">
                        <label>收费大类：</label>
                    </div>
                    <div class="col-xs-2" style="width: 125px;height: 30px;">
                        <select id="sflx" name="sflx" style="width:110px;height: 30px;margin-left: 10px">
                            <option value=""></option>
                            <#list jkrList as jkr>
                                <option value="${jkr.jkrmc!}">${jkr.jkrmc!}</option>
                            </#list>
                        </select>

                    </div>
                    <div class="col-xs-1" style="width: 110px;text-align: right;">
                        <label>申请起日期：</label>
                    </div>
                    <div class="col-xs-2" style="width: 135px;height: 30px;text-align: left">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" id="cjqssj" name="cjqssj"
                                    data-date-format="yyyy-mm-dd" style="width: 190px;height: 30px;">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                    </div>
                    <div class="col-xs-1" style="width: 100px;text-align: right;">
                        <label>申请止日期：</label>
                    </div>
                    <div class="col-xs-2" style="width: 135px;height: 30px;text-align: left">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" id="cjzzsj" name="cjzzsj"
                                    data-date-format="yyyy-mm-dd" style="width: 190px;height: 30px;">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                    </div>
                    <div class="col-xs-1" style="width: 1px;padding-left: 10px">
                        <button type="button" class="btn btn-sm btn-primary"
                                id="search">搜&nbsp;&nbsp;索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </button>
                    </div>
                </div>
            </form>
            <div class="tableHeader">
                <ul>
                    <li>
                        <button type="button" id="export" onclick="exportExcel()">
                            <span>导出</span>
                        </button>
                    </li>
                    <li>
                        <button type="button" id="export" onclick="print()">
                            <span>打印</span>
                        </button>
                    </li>
                    <li>
                        <button type="button" id="gdxxClear" onclick="printMul()">
                            <span>批量打印</span>
                        </button>
                    </li>
                </ul>
            </div>
            <table id="grid-table"></table>
            <div id="grid-pager"></div>
        </div>
    </div>
</@com.html>
