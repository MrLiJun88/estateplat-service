<@com.html title="关联附属设施" import="ace,public">
<style>
    span.label {
        border-radius: 3px !important;
    }

    .SSinput {
        width: 310px
    }

    iframe {
        position: relative;
    }

    .filesub {
        display: block;
        width: 90px;
        line-height: 34px;
        height: 34px;
        text-align: center;
        background: #155e96;
        color: #fff;
        word-spacing: 10px;
    }

    .col-xs-11 {
        width: 100%
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    .tableHeader {
        width: 800px;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    .SSinput {
        min-width: 100px !important;
    }

    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 1175px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput .modal-dialog {
        width: 500px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    /*高级搜索的样式修改*/
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .title {
        font-size: 18pt;
        text-align: center;
        padding-left: 2px;
        position: absolute;
        margin-left: 40px;
        margin-top: 5px;
    }
    html {
        background-color: white;
    }
</style>
<script type="text/javascript">

    Array.prototype.remove = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };
    if (!Array.prototype.forEach) {
        Array.prototype.forEach = function (callback, thisArg) {
            var T, k;
            if (this == null) {
                throw new TypeError(" this is null or not defined");
            }
            var O = Object(this);
            var len = O.length >>> 0; // Hack to convert O.length to a UInt32
            if ({}.toString.call(callback) != "[object Function]") {
                throw new TypeError(callback + " is not a function");
            }
            if (thisArg) {
                T = thisArg;
            }
            k = 0;
            while (k < len) {
                var kValue;
                if (k in O) {
                    kValue = O[k];
                    callback.call(T, kValue, k, O);
                }
                k++;
            }
        };
    }
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '320px';
    //全局的不动产类型
    $bdclx = 'TDFW';
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
        $("#qllxSelect").find("option[value='${cpt!}']").attr("selected", true);
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

        /*   文字水印  */
        $(".watermarkText").watermark();
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });
        $("#fileHide").click(function () {
            $("#fileInput").hide();
        });
        //初始化表单
        fwTableInit();
        fwfsssTableInit();

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/bdcFwfsss/getBdcFwxxJson?wiid=${wiid!}&zl=" + hhSearch;
            tableReload("fw-grid-table", fwUrl, {hhSearch:hhSearch},'', '');
        });
        $("#fwfsss_search").click(function () {
            var hhSearch = $("#fwfsss_search_qlr").val();
            var dyhUrl = "${bdcdjUrl}/bdcFwfsss/getBdcDyhPagesJson";
            tableReload("fwfsss-grid-table", dyhUrl, {hhSearch: hhSearch}, '', '');
        });
        //关联主房和附属设施
        $("#glFsss").click(function () {
            var fwfsssidArr = new Array();
            fwfsssidArr=getFwfsssids();
            var fwfsssids =  fwfsssidArr.join(",");
            var bdcdyh = $("#bdcdyh").val();
            var wiid = $("#wiid").val();
            var proid = $("#proid").val();
            $.ajax({
                url: '${bdcdjUrl!}/bdcFwfsss/glFsss',
                type: 'POST',
                dataType: 'json',
                data: {bdcdyh: bdcdyh, fwfsssids: fwfsssids,wiid:wiid,proid:proid},
                success: function (data) {
                    tipInfo(data.result);
                    var jqgrid = $("#fw-grid-table");
                    jqgrid.trigger("reloadGrid");//重新加载JqGrid
                    var jqgridFsss = $("#fwfsss-grid-table");
                    jqgridFsss.trigger("reloadGrid");//重新加载JqGrid
                },
                error: function (data) {
                }
            });
        });

        //取消关联主房和附属设施
        $("#disGlFsss").click(function () {
            var fwfsssidArr = new Array();
            fwfsssidArr=getFwfsssids();
            var fwfsssids =  fwfsssidArr.join(",");
            var proid = $("#proid").val();
            var wiid = $("#wiid").val();
            $.ajax({
                url: '${bdcdjUrl!}/bdcFwfsss/disGlFsss',
                type: 'POST',
                dataType: 'json',
                data: {fwfsssids: fwfsssids,wiid:wiid,proid:proid},
                success: function (data) {
                    tipInfo(data.result);
                    var jqgrid = $("#fw-grid-table");
                    jqgrid.trigger("reloadGrid");//重新加载JqGrid
                    var jqgridFsss = $("#fwfsss-grid-table");
                    jqgridFsss.trigger("reloadGrid");//重新加载JqGrid
                },
                error: function (data) {
                }
            });
        });

        //删除关联主房和附属设施
        $("#delFsss").click(function () {
            var fwfsssidArr = new Array();
            fwfsssidArr=getFwfsssids();
            var fwfsssids =  fwfsssidArr.join(",");
            var wiid = $("#wiid").val();
            var proid = $("#proid").val();
            $.ajax({
                url: '${bdcdjUrl!}/bdcFwfsss/delFsss',
                type: 'POST',
                dataType: 'json',
                data: {fwfsssids: fwfsssids,wiid:wiid,proid:proid},
                success: function (data) {
                    tipInfo(data.result);
                    var jqgrid = $("#fw-grid-table");
                    jqgrid.trigger("reloadGrid");//重新加载JqGrid
                    var jqgridFsss = $("#fwfsss-grid-table");
                    jqgridFsss.trigger("reloadGrid");//重新加载JqGrid
                },
                error: function (data) {
                }
            });
        });

        //删除关联主房和附属设施
        $("#disDy").click(function () {
            var fwfsssidArr = new Array();
            fwfsssidArr=getFwfsssids();
            var fwfsssids =  fwfsssidArr.join(",");
            $.ajax({
                url: '${bdcdjUrl!}/bdcFwfsss/delWithZf',
                type: 'POST',
                dataType: 'json',
                data: {fwfsssids: fwfsssids,qllx:"18"},
                success: function (data) {
                    tipInfo(data.result);
                    var jqgrid = $("#fw-grid-table");
                    jqgrid.trigger("reloadGrid");//重新加载JqGrid
                    var jqgridFsss = $("#fwfsss-grid-table");
                    jqgridFsss.trigger("reloadGrid");//重新加载JqGrid
                },
                error: function (data) {
                }
            });
        });

        //删除关联主房和附属设施
        $("#disCf").click(function () {
            var fwfsssidArr = new Array();
            fwfsssidArr=getFwfsssids();
            var fwfsssids =  fwfsssidArr.join(",");
            $.ajax({
                url: '${bdcdjUrl!}/bdcFwfsss/delWithZf',
                type: 'POST',
                dataType: 'json',
                data: {fwfsssids: fwfsssids,qllx:"21"},
                success: function (data) {
                    tipInfo(data.result);
                    var jqgrid = $("#fw-grid-table");
                    jqgrid.trigger("reloadGrid");//重新加载JqGrid
                    var jqgridFsss = $("#fwfsss-grid-table");
                    jqgridFsss.trigger("reloadGrid");//重新加载JqGrid
                },
                error: function (data) {
                }
            });
        });

    })

    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }

    function hideModal(proid) {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
    }

    //提示信息
    function tipInfo(msg) {
        $.Prompt(msg,1500);
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

    function fwSel(bdcdyid, wiid,bdcdyh) {
        $("#bdcdyh").val(bdcdyh);
        //遮罩
        $.blockUI({message: "请稍等……"});
        selFsss(bdcdyid, wiid);
        setTimeout($.unblockUI, 10);
    }

    //通过不动产单元级联不动产单元
    var selFsss = function(bdcdyid, wiid) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcFwfsss/getBdcFwFsssJson";
        var data = {bdcdyid: bdcdyid,wiid:wiid};
        var jqgrid = $("#fwfsss-grid-table");
        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel:  [
                {
                    name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.FWFSSSID + '" name="fsssXl"/> '
                }
                },
                {name: 'FWZL', index: 'FWZL', width: '10%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '12%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '5%', sortable: false},
                {name: 'GLZT', index: 'GLZT', width: '3%', sortable: false ,
                    formatter: function (cellvalue, options, rowObject) {
                        var value = '<span class="label label-danger">未关联</span>';
                        if (cellvalue != null && cellvalue != "") {
                            value='<span class="label label-success">已关联</span>';
                        }
                        return value;
                    }},
                {name: 'FWFSSSID', index: 'FWFSSSID', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(table).jqGrid("getRowData").length == 7) {
                    $(table).jqGrid('setGridWidth', 550);
                    $(table).jqGrid("setGridHeight", $pageHight);
                } else {
                    $(table).jqGrid('setGridWidth', 550);
                    $(table).jqGrid("setGridHeight", $pageHight);
                }
            }
        });
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
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function getFwfsssids() {
        var chk_value = [];
        $('input[name="fsssXl"]:checked').each(function () {
            chk_value.push($(this).val());
        });
        return chk_value;
    }
    function resetFwfsssids() {
        $("input[name='fsssXl']").attr("checked", false);
        $("#fwfsssids").val('');
    }

    function fwfsssTableInit() {
        var grid_selector = "#fwfsss-grid-table";
        var pager_selector = "#fwfsss-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/bdcFwfsss/getBdcFwFsssJson?proid=${proid!}",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'FWFSSSID'},
            colNames: ['序列', '坐落','不动产单元号', '建筑面积','关联状态','FWFSSSID'],
            colModel: [
                {
                    name: 'XL', index: '', width: '2%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.FWFSSSID + '" name="fsssXl"/> '
                }
                },
                {name: 'FWZL', index: 'FWZL', width: '8%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '12%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '4%', sortable: false},
                {name: 'GLZT', index: 'GLZT', width: '3%', sortable: false ,
                    formatter: function (cellvalue, options, rowObject) {
                        var value = '<span class="label label-danger">未关联</span>';
                        if (cellvalue != null && cellvalue != "") {
                            value='<span class="label label-success">已关联</span>';
                        }
                        return value;
                    }},
                {name: 'FWFSSSID', index: 'FWFSSSID', width: '0%', sortable: false, hidden: true}
            ],
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
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
            },
            onSelectAll: function (aRowids, status) {
                var $myGrid = $(this);
                aRowids.forEach(function (e) {
                    var cm = $myGrid.jqGrid('getRowData', e);
                })
            },
            onSelectRow: function (rowid, status) {
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    function fwTableInit() {
        var grid_selector = "#fw-grid-table";
        var pager_selector = "#fw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/bdcFwfsss/getBdcFwxxJson?wiid=${wiid!}",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'BDCDYID'},
            colNames: ['序列', '坐落', '房屋用途', '建筑面积', '宗地面积','操作','BDCDYID','WIID','BDCDYH'],
            colModel: [
                {
                    name: 'XL', index: '', width: '2%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.BDCDYID + '\',\'' + rowObject.WIID + '\',\'' + rowObject.BDCDYH + '\')"/>'
                }
                },
                {name: 'FWZL', index: 'FWZL', width: '8%', sortable: false},
                {name: 'FWYT', index: 'FWYT', width: '4%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '4%', sortable: false},
                {name: 'ZDZHMJ', index: 'ZDZHMJ', width: '4%', sortable: false},
                {name:'CZ', index:'CZ', width:'3%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;">' +
                            '<div><button type="button"  class="btn btn-minier btn-primary" onclick="addFsss(\'' + rowObject.WIID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')">添加</button></div>' +
                            '</div>'
                }},
                {name: 'BDCDYID', index: 'BDCDYID', width: '0%', sortable: false, hidden: true},
                {name: 'WIID', index: 'WIID', width: '0%', sortable: false, hidden: true},
                {name: 'BDCDYH', index: 'BDCDYH', width: '0%', sortable: false, hidden: true}
            ],
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
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    function addFsss(wiid,bdcdyh,bdcdyid) {
        var url = '${bdcdjUrl}/bdcFwfsss/addIndex?wiid=' + wiid+'&bdcdyh='+bdcdyh+'&bdcdyid='+bdcdyid;
        var k=window.showModalDialog(url,window,"dialogWidth=900px;dialogHeight=600px");
        if(k==1){
            window.location.reload();
        }
    }
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container" style="display: none;">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
        <div class="row" style="border-bottom: 1px solid #ddd;">
            <div class="col-xs-6"><h2>关联附属设施</h2></div>
            <div class="col-xs-6" style="text-align: right;">
                <button type="button" class="btn btn-sm btn-primary" id="glFsss"
                        style="margin-top: 20px; margin-bottom: 10px;">关联
                </button>
                <button type="button" class="btn btn-sm btn-primary" id="disGlFsss"
                        style="margin-top: 20px; margin-bottom: 10px;">取消关联
                </button>
            </div>
        </div>
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a data-toggle="tab" id="fwTab" href="#fw">
                                主房信息
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                               data-watermark="请输入坐落">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="fw_search">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="fw"  class="tab-pane in active ">
                            <table id="fw-grid-table"></table>
                            <div id="fw-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a data-toggle="tab" id="fwfsssTab" href="#file">
                                附属设施
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tableHeader">
                            <ul>
                                <li>
                                    <button type="button" id="disDy">
                                        <span>取消抵押</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" id="disCf">
                                        <span>取消查封</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" id="delFsss">
                                        <span>删除</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <div id="file" class="tab-pane in active ">
                            <table id="fwfsss-grid-table"></table>
                            <div id="fwfsss-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="proid" name="proid" value="${proid!}">
    <input type="hidden" id="wiid" name="wiid" value="${wiid!}">
    <input type="hidden" id="xmid" name="xmid">
    <input type="hidden" id="fwfsssids" name="fwfsssids">
    <input type="hidden" id="bdcdyh" name="bdcdyh">
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
