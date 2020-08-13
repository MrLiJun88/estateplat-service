<@com.html title="不动产登记业务管理系统" import="ace">
<style>
        /*bootbox弹出框的ie8居中样式解决*/
    .modal-dialog{
        width:650px;
    }

        /*新建弹出框的样式修改及ie8居中问题*/
    #myModal .modal-dialog {
        width: 1000px;
        margin:30px auto;
    }
    #zydjModal .modal-dialog {
        width: 1000px;
        margin:30px auto;
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


        /*去掉表格横向滚动条*/
        /*.ui-jqgrid-bdiv{
            overflow-x: hidden!important;
        }*/

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;100% !important;
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

        /*日期表单样式*/
    .dropdown-menu {
        z-index: 10000 !important;
    }
    .input-icon {
        width: 100%;
    }
    .chosen-container-single .chosen-single {
        height: 35px;
    }
</style>
<script type="text/javascript">

$(function () {
    /*表单校验 begin*/
    $.validator.setDefaults({
        showErrors:function (map, list) {
            // there's probably a way to simplify this
            this.currentElements.removeAttr("title").removeAttr("style");
            $(this.currentElements).tooltip("destroy");
            $.each(list, function (index, error) {
                $(error.element).attr("title", error.message).attr("style", "border:1px dotted red;");
                $(error.element).tooltip({placement:"bottom"});
            });
        }
    });
    jQuery.extend(jQuery.validator.messages, {
        required:"必填字段"
    });
    /*表单校验 end*/

    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject){
        if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
            $(window).resize(function(){
                $.each($(".moveModel > .modal-dialog"),function(){
                    $(this).css("left",($(window).width()-$(this).width())/2);
                    $(this).css("top","40px");
                })
            })
        }
    }

    /*   文字水印  */
    $(".watermarkText").watermark();

    //时间控件
    $('.date-picker').datepicker({
        autoclose:true,
        todayHighlight:true,
        language:'zh-CN'
    }).next().on(ace.click_event, function () {
                $(this).prev().focus();
            });

    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
        $("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/bdcSjgl/getBdcXmPagesJson?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {xmmc:xmmc});
    })

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcSjgl/getBdcXmPagesJson?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {xmmc:""});
    })

    //新建:初始登记
    $("#addXm").click(function () {
        showModal(200);
    })

    //项目高级搜索关闭事件
    $("#proHide").click(function () {
        $("#gjSearchPop").hide();
        $("#gjSearchForm")[0].reset();
    });
    //项目高级查询按钮点击事件
    $("#show").click(function () {
        $("#gjSearchPop").show();
    });
});

function tableLocalReload(table,data){
    var jqgrid = $("#"+table);
    jqgrid.setGridParam({url:"", datatype:'local',page:1,postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
//转移登记
function creatXm(val,text) {
    $('#zydjModal').show();
    $('#modal-backdrop').show();
    //根据登记类型判断引入不同的表单
    $.get("${bdcdjUrl}/bdcSjgl/getFormByDjlx?djlxdm="+val,function(data){
        $("#zydjRight").html(data);
        getQllx("", val, "zydj_qllx");
        getSqlx(val,"zydj_sqlx");
        $("#zydj_djlx").append('<option value="' + val + '" selected>' + text + '</option>');
        $("#zydj_djlx_text").val(val);
        $("#zydjTitle").text(text);
    })
    //初始化了的话会重加载
    //tableReload("zydj-grid-table", "${bdcdjUrl}/bdcSjgl/getBdcBjxmListByPage",{});
    //初始化了的话不会再初始化
    zydjTableInit();
    $("#newData > span").html("新数据"+$("#zydj_djlx option:selected").text());
    $("#oldData > span").html("老数据"+$("#zydj_djlx option:selected").text());
}

/* 显示初始登记modal  */
function showModal(val) {
    getQllx("", val, "csdjQllx");
    getSqlx(val,"csdjSqlx");
    $('#myModal').show();
    $('#modal-backdrop').show();
    $("#tdTab").click();
    $("#djlx").val(val);
    $("#djlx_text").val(val);
    $("#csdjTitle").text($("#djlx option:selected").text());
}


function hideModal(proid) {
    $('.newPro').hide();
    //隐藏高级搜索框
    $('.Pop-upBox.moveModel').hide();
    $('#modal-backdrop').hide();
    //关闭时重置表格数据和表单数据
    $("#xjForm")[0].reset();
    //清空表单部分
    $("#zydjRight").html("");
    $("#xjForm").find(".form-control").tooltip("destroy").removeAttr("aria-invalid").removeAttr("title").removeAttr("style");
    //$("#zydjXjForm").find(".form-control").tooltip("destroy").removeAttr("aria-invalid").removeAttr("title").removeAttr("style");
    $("#tdSearchText,#fcSearchText,#dyhSearchText,#zydjSarchText").val("");
    clearCheckInfo();
    /*刷新表格*/
    tableLocalReload("td-grid-table");
    tableLocalReload("dyh-grid-table");
    tableLocalReload("fw-grid-table");
    tableLocalReload("zydj-grid-table");
    tableLocalReload("zydj-td-grid-table");
    tableLocalReload("zydj-dyh-grid-table");
    tableLocalReload("zydj-fw-grid-table");
    tableReload("grid-table", "${bdcdjUrl}/bdcSjgl/getBdcXmPagesJson",{});
    if (proid && proid != undefined) {
        openWin('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid);
    }
}
function openWin(url){
    var w_width=screen.availWidth-10;
    var w_height= screen.availHeight-32;
    window.open(url, "", "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
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
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

$(function () {
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
        url:"${bdcdjUrl}/bdcSjgl/getBdcXmPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'PROID'},
        colNames:["不动产单元号", '项目名称', '权利人', '登记类型', '坐落', '创建日期', '产权证号', '操作'],
        colModel:[
            {name:'BDCDYH', index:'BDCDYH', width:'20%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                return value;
            }},
            {name:'XMMC', index:'XMMC', width:'15%', sortable:false},
            {name:'QLR', index:'QLR', width:'15%', sortable:false, hidden:true},
            {name:'DJLX', index:'DJLX', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                $.each(${djListJson!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                return value;
            }},
            {name:'ZL', index:'ZL', width:'15%', sortable:false},
            {name:'CJSJ', index:'CJSJ', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                var data = new Date(value).Format("yyyy-MM-dd");
                return data;
            }},
            {name:'BDCQZH', index:'BDCQZH', width:'20%', sortable:false},
            {name:'myac', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;"> <div title="编辑所选记录" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ui-icon-pencil"></span></div>' +
                        '<div title="删除所选记录" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="DelXm(\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ui-icon-trash"></span></div></div>'
            }
            }
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
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
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
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+":this.getMonth() + 1, //月份
            "d+":this.getDate(), //日
            "h+":this.getHours(), //小时
            "m+":this.getMinutes(), //分
            "s+":this.getSeconds(), //秒
            "q+":Math.floor((this.getMonth() + 3) / 3), //季度
            "S":this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
});

//修改项目信息的函数
function EditXm(proid) {
    openWin("${bdcdjUrl}/bdcSjgl/formTab?proid=" + proid);
}
//删除项目
function DelXm(proid) {
    $.blockUI({ message:"请稍等……" });
    bootbox.dialog({
        message:"是否删除？",
        title:"",
        closeButton:false,
        buttons:{
            success:{
                label:"确定",
                className:"btn-success",
                callback:function () {
                    $.getJSON("${bdcdjUrl!}/bdcSjgl/delBdcXm?id=" + proid, {}, function (jsonData) {
                        setTimeout($.unblockUI, 10);
                        alert(jsonData.result);
                        $("#grid-table").trigger("reloadGrid");//重新加载JqGrid
                    })
                }
            },
            main:{
                label:"取消",
                className:"btn-primary",
                callback:function () {
                    setTimeout($.unblockUI, 10);
                }
            }
        }
    });
}

//根据不动产类型和登记类型获取权利数据 需要传下拉列表id
function getQllx(bdclxdm, djlxdm, selectId) {
    var options = {
        url:'${bdcdjUrl}/bdcSjgl/getqllxByDjlxOrBdclx?bdclxdm=' + bdclxdm + '&djlxdm=' + djlxdm,
        type:'get',
        dataType:'json',
        success:function (result) {
            var qllxSelect = $("#" + selectId);
            qllxSelect.html("");
            if (result.length > 0) {
                $.each(result, function (index, data) {
                    //这句判断本身应该没用  数据库有问题才会用到
                    if (data.QLLXDM != '' && data.QLLXDM != undefined && data.MC != '' && data.MC != undefined) {
                        qllxSelect.append('<option value="' + data.QLLXDM + '">' + data.MC + '</option>');
                    }
                })
            }
        },
        error:function (result) {
        }
    };
    $.ajax(options);
    return false;
}

//根据登记类型获取申请数据 需要传下拉列表id
function getSqlx(djlxdm, selectId) {
    var options = {
        url:'${bdcdjUrl}/bdcSjgl/getsqlxByDjlx?djlxdm=' + djlxdm,
        type:'get',
        dataType:'json',
        success:function (result) {
            var qllxSelect = $("#" + selectId);
            qllxSelect.html("");
            if (result.length > 0) {
                $.each(result, function (index, data) {
                    //这句判断本身应该没用  数据库有问题才会用到
                    if (data.dm != '' && data.dm != undefined && data.mc != '' && data.mc != undefined) {
                        qllxSelect.append('<option value="' + data.dm + '">' + data.mc + '</option>');
                    }
                })
            }
        },
        error:function (result) {
        }
    };
    $.ajax(options);
    return false;
}
//清空验证信息
function clearCheckInfo() {
    $("#alertInfo,#confirmInfo").html("");
    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
    $("#zydjXjBtn,#xjBtn").attr("disabled", false);
}
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入项目名称">
                    </td>
                    <td class="Search">
                        <a href="#" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="addXm">
                        <i class="ace-icon fa fa-file"></i>
                        <span>首次登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(400,'变更登记')">
                        <i class="ace-icon fa fa-arrow-left"></i>
                        <span>变更登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(300,'转移登记')">
                        <i class="ace-icon fa fa-arrow-left"></i>
                        <span>转移登记</span>
                    </button>
                </li>
            <#--  <li>
                  <button type="button" onclick="creatXm(1100,'抵押登记')">
                      <i class="ace-icon fa fa-lock"></i>
                      <span>抵押登记</span>
                  </button>
              </li>-->
                <li>
                    <button type="button" onclick="creatXm(900,'查封登记')">
                        <i class="ace-icon fa fa-check"></i>
                        <span>查封登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(800,'预告登记')">
                        <i class="ace-icon fa fa-tasks"></i>
                        <span>预告登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(700,'异议登记')">
                        <i class="ace-icon fa fa-pencil-square-o"></i>
                        <span>异议登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(600,'更正登记')">
                        <i class="ace-icon fa fa-pencil-square-o"></i>
                        <span>更正登记</span>
                    </button>
                </li>
                <li>
                    <button type="button" onclick="creatXm(500,'注销登记')">
                        <i class="ace-icon fa fa-pencil-square-o"></i>
                        <span>注销登记</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<#--新建-->
<div class="Pop-upBox bootbox modal fade bootbox-prompt in newPro" style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-file-o bigger-110" id="csdjTitle"></i></h4>
                <button type="button" onclick="hideModal()"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="bootbox-body">
                <#include "addBdcxm.ftl">
            </div>
        </div>
    </div>
</div>
<#--转移登记-->
<div class="Pop-upBox bootbox modal fade bootbox-prompt in newPro" style="display: none;" id="zydjModal">
    <div class="modal-dialog newPro-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-file-o bigger-110" id="zydjTitle"></i></h4>
                <button type="button" onclick="hideModal()"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="bootbox-body">
                <#include "zydj.ftl">
            </div>
        </div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>产权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cqzh" class="form-control">
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
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>登记类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="djlx" class="form-control">
                                <option></option>
                                <#list djList as djlx>
                                    <option value="${djlx.dm}">${djlx.mc}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>起始日期：</label>
                        </div>
                        <div class="col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="qsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                        </div>
                        <div class="col-xs-2">
                            <label>结束日期：</label>
                        </div>
                        <div class="col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="jsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
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
<!--房产证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="fcSearchPop">
    <div class="modal-dialog fcSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>房产证高级查询</h4>
                <button type="button" id="fcHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fcSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>房产证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zjgczl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fcGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--不动产单元号高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="dyhSearchPop">
    <div class="modal-dialog dyhSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>不动产单元高级查询</h4>
                <button type="button" id="dyhHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="dyhSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
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
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="dyhGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--土地证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="tdSearchPop">
    <div class="modal-dialog tdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>土地证高级查询</h4>
                <button type="button" id="tdHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="tdSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tdGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--转移登记高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="zydjSearchPop">
    <div class="modal-dialog zydjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="zydjHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="zydjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>产权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>项目名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="xmmc" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>权利类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="qllx" class="form-control" >
                                <option></option>
                                <#list qlList as qllx>
                                    <option value="${qllx.dm}">${qllx.mc}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="zydjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<!--其他登记的不动产单元号高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="zydjDyhSearchPop">
    <div class="modal-dialog zydjDyhSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>不动产单元高级查询</h4>
                <button type="button" id="zydjDyhHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="zydjDyhSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
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
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="zydjDyhGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--其他登记的土地证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="zydjTdSearchPop">
    <div class="modal-dialog tdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>土地证高级查询</h4>
                <button type="button" id="zydjTdHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="zydjTdSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="zydjTdGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--其他登记的房产证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="zydjFcSearchPop">
    <div class="modal-dialog fcSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>房产证高级查询</h4>
                <button type="button" id="zydjFcHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="zydjFcSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>房产证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zjgczl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="zydjFcGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
</@com.html>
