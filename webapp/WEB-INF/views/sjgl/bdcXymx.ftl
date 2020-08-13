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

    .col-xs-6 {
        width: 83%;
        padding: 0px;
    }

    #xymxSearchForm .col-xs-3 {
        width: 22%;
        padding-left: 0px;
        padding-right: 0px;
    }

    #xymxSearchForm .col-xs-1 {
        width: 3%;
        padding-left: 3px;
        padding-right: 3px;
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
</script>
    <@script name="static/js/bdcXymx.js"></@script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">
            <div class="tabbable">
                <div class="tab-content">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="sd_search"
                                           data-watermark="请输入审核人">
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
                                <button type="button" id="addXymx" onclick="showModal()">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                    <table id="sd-grid-table"></table>
                    <div id="sd-grid-pager"></div>
                </div>
            </div>
        </div>
    </div>
    <!--高级搜索-->
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
                    <form class="form advancedSearchTable" id="xymxSearchForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>审核人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="shr" id='shr' class="form-control">
                            </div>
                            <div class="col-xs-2">
                                <label>状态：</label>
                            </div>
                            <div class="col-xs-4">
                                <select id="sfsx" class="chosen-select">
                                    <option value="0">未生效</option>
                                    <option value="1">生效</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">
                                <label>审核时间：</label>
                            </div>
                            <div class="col-xs-3">
                                <input type="text" name="shsjStart" id='shsjStart' class="date-picker form-control"
                                       data-date-format="yyyy-mm-dd">
                            </div>
                            <div class="col-xs-1">
                                <label>：</label>
                            </div>
                            <div class="col-xs-3">
                                <input type="text" name="shsjEnd" id='shsjEnd' class="date-picker form-control"
                                       data-date-format="yyyy-mm-dd">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="xymxGjSearchBtn">搜索</button>
                </div>
            </div>
        </div>
    </div>
    <!--修改-->
    <div style="display: none;">
        <form class="form advancedSearchTable" id="updateJsxxDataForm">
            <div style="display: none">
                <input type="text" name="xymxid" id='xymxid' class="form-control">
                <input type="text" name="xyglid" id='xyglid' class="form-control" value="${xyglid}">
            </div>
        </form>
    </div>
    <!--添加-->
    <div class="Pop-upBox moveModel  ui-draggable" style="display: none;" id="addData">
        <div class="modal-dialog gjSearchPop-modal ui-draggable">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>新增</h4>
                    <button type="button" id="sdHide" class="proHide" onclick="closeDialog('addData')"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="addDataForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>内容：</label>
                            </div>
                            <div class="col-xs-10">
                                <input type="text" name="nr" id='nr' class="form-control">
                            </div>
                        </div>
                        <div style="display: none;">
                            <input type="hidden" name="xyglid" id='addXyglid' value="${xyglid}">
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="addXymx" onclick="addXymx()">添加</button>
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
    <div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
    <div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
