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
    #sdSearchPop .modal-dialog {
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
    .modal-content{
        width: 500px;
    }
</style>

<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var portalUrl = "${portalUrl!}";
    var zdtzm = "${zdtzm!}";
    var dyfs = "${dyfs!}";
    var bdclxdm = "${bdclxdm!}";
    var qlxzdm = "${qlxzdm!}";
    var bdcqzh = "${bdcqzh!}";
</script>
    <@script name="static/js/bdcqzsjsd.js"></@script>
<div class="main-container">

    <div class="page-content">
        <div class="row">
            <div class="tabbable">
                <div class="tab-content">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="sd_search"
                                           data-watermark="请输入产权证号/坐落" value="${bdcqzh!}">
                                </td>
                                <td class="Search">
                                    <a href="#" id="sd_search_btn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="sdShow"
                                            onclick="showDialog(this.id)">高级搜索
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="addSel" onclick="showDialog(this.id)">
                                    增加锁定
                                </button>
                            </li>
                            <li>
                                <button type="button" id="modifylock" onclick="modifylock()">
                                    锁定
                                </button>
                            </li>
                            <li>
                                <button type="button" id="unlock" onclick="unlockbefore()">
                                    解除锁定
                                </button>
                            </li>
                            <li>
                                <button type="button" id="modifySd" onclick="modifySd()">
                                    修改
                                </button>
                            </li>
                        </ul>
                    </div>
                    <table id="sd-grid-table"></table>
                    <div id="sd-grid-pager"></div>
                    <form id="form" hidden="hidden">
                        <input type="hidden" id="sdIds" name="sdIds">
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--地籍高级搜索-->
    <div class="Pop-upBox moveModel" style="display: none;" id="sdSearchPop">
        <div class="modal-dialog sdSearchPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                    <button type="button" id="sdHide" class="sdSearchPop" onclick="closeDialog('sdSearchPop')"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="sdSearchForm">
                        <#--<div class="row">-->
                            <#--<div class="col-xs-2">-->
                                <#--<label>不动产单元：</label>-->
                            <#--</div>-->
                            <#--<div class="col-xs-4">-->
                                <#--<input type="text" name="bdcdyh" id='searchBdcdyh' class="form-control">-->
                            <#--</div>-->
                            <#--<div class="col-xs-2">-->
                                <#--<label>不动产权证号：</label>-->
                            <#--</div>-->
                            <#--<div class="col-xs-4">-->
                                <#--<input type="text" name="bdcqzh" id='searchBdcqzh' class="form-control">-->
                            <#--</div>-->
                        <#--</div>-->
                        <div class="row" style="align-content: center">
                            <#--<div class="col-xs-2">-->
                                <#--<label>坐落：</label>-->
                            <#--</div>-->
                            <#--<div class="col-xs-4">-->
                                <#--<input type="text" name="zl" id='searchZl' class="form-control">-->
                            <#--</div>-->
                            <div class="col-xs-2">
                                <label>限制状态：</label>
                            </div>
                            <div class="col-xs-4">
                                <select id="searchXzzt" class="chosen-select" >
                                    <option value="0">正常</option>
                                    <option value="1">锁定</option>
                                </select>
                            </div>
                        </div>
                        <#--<div class="row">-->
                            <#--<div class="col-xs-2">-->
                                <#--<label>锁定人：</label>-->
                            <#--</div>-->
                            <#--<div class="col-xs-4">-->
                                <#--<input type="text" name="sdr" id='searchSdr' class="form-control">-->
                            <#--</div>-->
                            <#--<div class="col-xs-2">-->
                                <#--<label>解锁人：</label>-->
                            <#--</div>-->
                            <#--<div class="col-xs-4">-->
                                <#--<input type="text" name="jsr" id='searchJsr' class="form-control">-->
                            <#--</div>-->
                        <#--</div>-->
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="sdGjSearchBtn">搜索</button>
                </div>
            </div>
        </div>
    </div>

    <!--添加-->
    <div class="Pop-upBox moveModel" style="display: none;" id="addSdData">
        <div class="modal-dialog sdSearchPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>新增</h4>

                    <button type="button" id="sdHide" class="proHide" onclick="closeDialog('addSdData')"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="sdDataForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" id='bdcdyh' class="form-control" readonly="readonly">
                            </div>
                            <div class="col-xs-2">
                                <label>不动产权证号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcqzh" id='bdcqzh' class="form-control" readonly="readonly">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zl" id='zl' class="form-control" readonly="readonly">
                            </div>
                            <div class="col-xs-2">
                                <label>限制原因：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="xzyy" id='xzyy'class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">
                                <label>房产证号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fczh" id='fczh' class="form-control" readonly="readonly">
                            </div>
                            <div class="col-xs-2">
                                <label>土地证号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="tdzh" id='tdzh' class="form-control" readonly="readonly">
                            </div>
                        </div>
                        <div class="row" hidden="hidden">
                            <div class="col-xs-4">
                                <input type="text" name="qlid" id="qlid" class="form-control"  readonly="readonly">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="tdfwid" id='tdfwid' class="form-control">
                            </div>
                        </div>

                        <div class="row" hidden="hidden">
                            <div class="col-xs-4">
                                <input type="text" name="proid" id="proid" class="form-control"  readonly="readonly">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="wiid" id='wiid' class="form-control">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" style='margin-right: 30px' id="selectBdcdy"
                            onclick="showModal()">选择限制类型
                    </button>
                    <button type="button" class="btn btn-sm btn-primary" id="add">添加</button>
                </div>
            </div>
        </div>
    </div>

        <!--解除锁定-->
    <div class="Pop-upBox moveModel" style="display: none;" id="unlockSdData">
        <div class="modal-dialog sdSearchPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>解锁</h4>

                    <button type="button" id="updatesdHide" class="proHide" onclick="closeDialog('unlockSdData')"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="updatesdDataForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>解锁原因：</label>
                            </div>
                            <div class="col-xs-10">
                                <input type="text" name="xzyy" id='jsyy'class="form-control">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="unlockSdSave">解锁</button>
                </div>
            </div>
        </div>
    </div>

    <!--修改-->
    <div class="Pop-upBox moveModel" style="display: none;" id="modifySdData">
        <div class="modal-dialog sdSearchPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>修改</h4>

                    <button type="button" id="updatesdHide" class="proHide" onclick="closeDialog('modifySdData')"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="updatesdDataForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>限制原因：</label>
                            </div>
                            <div class="col-xs-10">
                                <input type="text" name="xzyy" id='xgxzyy'class="form-control">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="modifySdSave">修改</button>
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
                    <button type="button" id="tipHide" class="proHide"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <div id="csdjAlertInfo"></div>
                    <div id="csdjConfirmInfo"></div>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
                </div>
            </div>
        </div>
    </div>
<#--不动产单元-->
    <div class="Pop-upBox moveModel" style="display: none;" id="myModal1">
        <div class="modal-dialog newPro-modal">
            <div class="modal-content" style="width:900px;">
                <div class="modal-header">
                    <button type="button" onclick="closeModal()"><i class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body"  style="overflow-y:auto;overflow-x:auto;height:500px;width:900px">
                    <#include "bdcqzListForSd.ftl">
                </div>
            </div>
        </div>
    </div>
    <div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
    <div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
