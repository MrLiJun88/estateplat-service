<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

        /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
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

        /*移动modal样式*/
    #tipPop .modal-dialog {
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

    .alert {
        font-size: 12px;
        border-radius: 4px;
        padding: 5px;
        margin-bottom: 5px;
    }

        /*移动modal样式*/
    #ywsjSearchPop .modal-dialog {
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
        width;
        100% !important;
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

</style>
<script type="text/javascript">
$(function () {
    /*   文字水印  */
    $(".watermarkText").watermark();

    $('#djsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });

    //搜索事件
    $("#djsj_search_btn").click(function () {
        var djsjUrl = "${bdcdjUrl}/lpb/getLpbPagesJson?" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});
    })


    /*高级按钮点击事件 begin*/
    $("#djsjShow,#ywsjShow").click(function () {
        if (this.id == "ywsjShow") {
            $("#ywsjSearchPop").show();
        } else if (this.id == "djsjShow") {
            $("#djsjSearchPop").show();
        }
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function () {
        var djsjUrl = "${bdcdjUrl}/lpb/getLpbPagesJson?" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc:""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    })

    $("#djsjHide,#ywsjHide,#tipHide,#tipCloseBtn").click(function () {
        if (this.id == "djsjHide") {
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        } else if (this.id == "ywsjHide") {
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        }
    });
    $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
    //默认初始化表格
<#--if("${bdcdyly!}"=='2' || "${bdcdyly!}"=='0'){-->
<#--djsjInitTable();-->
<#--}else if("${bdcdyly!}"=='1'){-->
<#--ywsjInitTable();-->
<#--}-->
    djsjInitTable();
});
//地籍数据
function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";

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
        url:"",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'LSZD'},
        colNames:[ '小区名称', '坐落', '宗地号'],
        colModel:[
            {name:'FWMC', index:'FWMC', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:selectZd(\'' + rowObject.FW_DCB_INDEX + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }},
            {name:'ZLDZ', index:'ZLDZ', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:selectZd(\'' + rowObject.FW_DCB_INDEX + '\')" title="' + cellvalue + '">' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }},
            {name:'LSZD', index:'LSZD', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:selectZd(\'' + rowObject.FW_DCB_INDEX + '\')" title="' + cellvalue + '">' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }}
        ],
        viewrecords:true,
        rowNum:7,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        ondblClickRow:function (rowid) {
//            var rowData = $(grid_selector).getRowData(rowid);
//            if(rowData!=null)
//                djsjEditXm(rowid,rowData.BDCLX,rowData.BDCDYH);
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}


function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

//修改项目信息的函数
function djsjEditXm(id, bdclx, bdcdyh) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    var options = {
        url:'${bdcdjUrl}/wfProject/checkBdcXm',
        type:'post',
        dataType:'json',
        data:{proid:proid, bdcdyh:bdcdyh, djId:id},
        success:function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                var islw = false;
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                $.each(data, function (i, item) {
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                        }else {
                            if(item.checkCode=='199'){
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            }else{
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    }
                })
                if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
            } else if (alertSize == 0 && confirmSize > 0) {
                $("span[name='hlBtn']").click(function () {
                    $(this).parent().remove();
                    if ($("#csdjConfirmInfo > div").size() == 0) {
                        djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                    }
                })
            }
        },
        error:function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);
}
function djsjInitVoFromOldData(proid, id, bdclx, bdcdyh) {
    //                   独幢或者是多幢时直接创建 返回的是不动产单元号
    $.ajax({
        type:'get',
        url:'${bdcdjUrl!}/zydjBdcdy/getYxmid?proid=' + proid + "&bdcdyh=" + bdcdyh,
        success:function (data) {
            if (data != 'error') {
                $.ajax({
                    type:'get',
                    url:'${bdcdjUrl!}/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + bdcdyh + "&bdclx=TDFW&bdcdyh=" + bdcdyh + "&yxmid=" + data,
                    success:function (data) {
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                        if (data == '成功') {
                            $.ajax({
                                type:'get',
                                async:true,
                                url:'${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                                success:function (data) {

                                }
                            });
                            window.parent.hideModel();
                            window.parent.resourceRefresh();

                        } else {
                            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
                        }
                    },
                    error:function (_ex) {
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                        //alert("保存失败!失败原因:" + _ex);
                        tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
                    }
                });
            } else {
                alert("该不动产单元号" + bdcdyh + "没有不动产权证！");
            }
        },
        error:function (_ex) {
            //alert("保存失败!失败原因:" + _ex);
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });
}
//修改项目信息的函数
function ywsjEditXm(id, bdcdyh) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    var options = {
        url:'${bdcdjUrl}/wfProject/checkBdcXm',
        type:'post',
        dataType:'json',
        data:{proid:proid, yxmid:id, bdcdyh:bdcdyh},
        success:function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                var islw = false; if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                $.each(data, function (i, item) {
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                        }else {
                            if(item.checkCode=='199'){
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            }else{
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    }
                })
                if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                ywsjInitVoFromOldData(proid, id, bdcdyh);
            } else if (alertSize == 0 && confirmSize > 0) {
                $("span[name='hlBtn']").click(function () {
                    $(this).parent().remove();
                    if ($("#csdjConfirmInfo > div").size() == 0) {
                        ywsjInitVoFromOldData(proid, id, bdcdyh);
                    }
                })
            }
        },
        error:function (data) {

        }
    };
    $.ajax(options);
}
function ywsjInitVoFromOldData(proid, id, bdcdyh) {
    $.ajax({
        type:'get',
        url:'${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id,
        success:function (data) {

            if (data == '成功') {
                $.ajax({
                    type:'get',
                    async:true,
                    url:'${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                    success:function (data) {

                    }
                });
                window.parent.hideModel();
                window.parent.resourceRefresh();

            } else {
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        },
        error:function (_ex) {
            //alert("保存失败!失败原因:" + _ex);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
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
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}
//修改项目信息的函数
function selectZd(id) {
    if (id != "") {
        //遮罩
        $.blockUI({ message:"请稍等……" });
        $.ajax({
            type:'get',
            url:'${bdcdjUrl}/lpb/getBdcdyFwlx?dcbId=' + id,
            success:function (bdcdyh) {
                var proid = "${proid!}";
                //返回是房屋类型，
                if (bdcdyh == "2") {
                    location.href = "${bdcdjUrl}/lpb/lpb?dcbId=" + id + "&proid=" + proid+"&mulSelect=${mulSelect!}&type=${type!}";
                } else {
                    //进行验证
                    djsjEditXm(bdcdyh, "TDFW", bdcdyh);
                }
                //去掉遮罩
                //setTimeout($.unblockUI, 10);
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (XMLHttpRequest.readyState == 4) {
                    alert("选择失败!");
                }
            }
        });

    }
}
function openProjectInfo(proid) {
    if (proid && proid != undefined) {
        window.open('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid);
    }
}
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">

    <div class="page-content">
        <div class="row">
            <div class="tabbable">
                <div class="tab-content">
                    <div id="djsj" class="tab-pane in active">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input tabindex="1" type="text" class="SSinput watermarkText" id="djsj_search"
                                               data-watermark="请输入小区名称/宗地号/坐落">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="djsj_search_btn">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                    <td style="border: 0px">&nbsp;</td>
                                    <td>
                                        <div type="button" class="btn01 AdvancedButton" id="djsjShow">高级搜索</div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <table id="djsj-grid-table"></table>
                        <div id="djsj-grid-pager"></div>
                    </div>

                    <table id="ywsj-grid-table"></table>
                    <div id="ywsj-grid-pager"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<!--地籍高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="djsjSearchPop">
    <div class="modal-dialog djsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>地籍高级查询</h4>
                <button type="button" id="djsjHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="djsjSearchForm">
                    <input id="zdtzm" name="zdtzm" type="hidden" value="${zdtzm!}"/>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>小区名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwmc" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>宗地号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zdh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
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
                <button class="btn btn-sm btn-primary" id="djsjGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo"></div>
                <div id="csdjConfirmInfo"></div>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
