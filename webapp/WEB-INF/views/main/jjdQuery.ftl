<@com.html title="不动产登记业务管理系统" import="ace,public">
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

    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;

    }
</style>

<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
$(function () {

    //下拉框
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
    $(window).on('resize.chosen',function () {
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
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});


    //项目表高级查询的搜索按钮事件
    $("#savejtfw").click(function () {
        var zsid = $("#zsid").val();
        var czmyy = $("#czmyy").val();
        var bz = $("#bz").val();
        if (zsid != '' && zsid != 'undefined') {
            $.ajax({
                type:"POST",
                url:"${bdcdjUrl}/archiveExchange/savejtfw",
                data:{zsid:zsid, czmyy:czmyy, bz:bz},
                dataType:"json",
                success:function (result) {
                    if (result == "成功") {
                        var Url = "${bdcdjUrl}/archiveExchange/getJjd?" + $("#gjSearchForm").serialize();
                        tableReload("grid-table", Url, {dcxc:""});
                        $("#gjSearchPop").hide();
                        $("#gjSearchForm")[0].reset();
                        $(".chosen-select").trigger('chosen:updated');
                    }
                },
                error:function (data) {
                }
            });
        }

    })
    //项目高级搜索关闭事件
    $("#proHide").click(function () {
        $("#gjSearchPop").hide();
        $("#gjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
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
        datatype:"json",
        height:'auto',
        jsonReader:{id:'PROID'},
        colNames:["交接单号","交接单类型", '转发人', '转发时间','操作' ,'jjdid'],
        colModel:[
            {name:'jjdbh', index:'jjdbh', width:'10%', sortable:false},
            {name:'jjdlx', index:'jjdlx', width:'15%', sortable:false},
            {name:'jjr', index:'jjr', width:'15%', sortable:false},
            {name:'jjrq', index:'jjrq', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                var data = new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                return data;
            }},
            {name:'cz', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div><div style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="deljjd(\'' + rowObject.jjdid + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div>'
            }
            },
            {name:'jjdid', index:'jjdid', width:'0', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        rownumbers:true,
        rownumWidth:50,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                var replacement =
                {
                    'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
                };
                $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                    var icon = $(this);
                    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                    if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                })
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            }, 0);
        },
        ondblClickRow:function (rowid) {
            EditXm(rowid);
        },
        onCellSelect:function (rowid) {

        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
});
function deljjd(jjdid){
    var msg = "是否删除";
    showConfirmDialog("提示信息", msg, "deletejjd", "'" + jjdid + "'", "", "");
}

var deletejjd = function (jjdid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/archiveExchange/deletejjd?jjdid=" + jjdid,
        dataType:"json",
        success:function (result) {
            tipInfo(result);
        },
        error:function (data) {
        }
    });
}

function openWin(url, name) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}


function editjtzf(zsid, bdcqzh, qlr, qlrzjh, czmyy, bz) {
    $("#zsid").val(zsid);
    $("#bdcqzh").html(bdcqzh);
    $("#qlr").html(qlr);
    $("#qlrzjh").html(qlrzjh);
    $("#czmyy").val(czmyy);
    $("#czmyy").trigger("chosen:updated");
    if (bz == 'undefined' || bz == '') {
        $("#bz").val();
    } else {
        $("#bz").val(bz);
    }
    $("#gjSearchPop").show();
    $(window).trigger('resize.chosen');
    $(".modal-dialog").css({"_margin-left":"25%"});
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
    if (dcxc != '') {
        var qlr = $("#qlrSerch").val();
        if (qlr != '')
            dcxc = dcxc + ',' + qlr;
    }
    $("#qlrSerch").val(dcxc);
    var Url = "${bdcdjUrl}/archiveExchange/getJjd";
    tableReload("grid-table", Url, {dcxc:dcxc, jjdbhs:dcxc});
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
function qingkong() {
    $("#qlrSerch").val('');
    var Url = "${bdcdjUrl}/archiveExchange/getJjd";
    tableReload("grid-table", Url, {dcxc:'', jjdbhs:'$$$'});
}

function addjjd() {
    var url = "${bdcdjUrl}/archiveExchange";
    openWin(url);
}

function plprintcpt() {
    var grid_selector = "#grid-table";
    var obj = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    var jjdbhs = "";
    $.each(obj, function (index, data) {
        jjdbhs = data.jjdbh;
    });
    if (jjdbhs == '' || jjdbhs == null) {
        tipInfo("请选择项目");
        return false;
    }
    $.ajax({
        type:"POST",
        url:"${bdcdjUrl}/archiveExchange/gdjjd",
        data:{jjdbhs:jjdbhs},
        dataType:"json",
        success:function (result) {
            if (result!="") {
                var url = "${reportUrl}/ReportServer?reportlet=print%2Fbdc_jjd.cpt&jjdid=" + result;
                openWin(url);
            }
        },
        error:function (data) {
        }
    });
}

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
}
function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140',
        'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}
function trimStr(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function dukaqi() {
    var CVR_IDCard = document.getElementById("CVR_IDCard");
    var readCardMessage;
    try {
        readCardMessage = CVR_IDCard.readcard();//通过读卡器获得身份证信息
    } catch (e) {
        return;
    }
    if (readCardMessage != null && readCardMessage != "") {
        var rMessage = readCardMessage.split("||");//身份证信息与照片信息用“||”进行分隔
        var cardMessage = rMessage[0];//身份证属性数据
        var filepath = rMessage[1];//照片路径

        //公民身份证号
        var xm = trimStr(cardMessage.substr(0, 10));
        var gmsfzh = trimStr(cardMessage.substr(61, 18));
        if (gmsfzh != '' && gmsfzh != null) {
            var zjh = $("#zjhSerch").val();
            if (zjh != '')
                gmsfzh = gmsfzh + ',' + zjh;
            $("#zjhSerch").val(gmsfzh);
            var Url = "${bdcdjUrl}/archiveExchange/getJjd";
            tableReload("grid-table", Url, {dcxc:'', qlrzjh:gmsfzh});
        }
    }
}
</script>

<div class="space-10"></div>
<input type="hidden" id="qlrSerch">
<input type="hidden" id="zjhSerch">
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch" style="width: 1000px">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入交接单号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="qingkong()">清空</button>
                    </td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="plprintcpt()">归档</button>
                    </td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="addjjd()">新增交接单</button>
                    </td>
                </tr>
            </table>
        </div>
        <!--end  高级搜索 -->
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>

<OBJECT classid="clsid:24E8FA71-6B30-485B-825E-3C3730AB8CDF" id="CVR_IDCard" name="CVR_IDCard" width=0 height=0
        align=center hspace=0 vspace=0></OBJECT>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>