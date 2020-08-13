<div id="fw" class="tab-pane " xmlns="http://www.w3.org/1999/html">
<#if showOptimize == "true">
   <div class="tab-content">
            <div class="row">
                <div class="col-xs-1" style="width:24%;text-align: right;">
                    <label>权利人：</label>
                </div>
                <div class="col-xs-2" style="width: 28%;height: 30px;">
                    <input type="text" id="fw_search_qlrmc" style="width: 100%;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 20%;text-align: right;">
                    <label>坐落：</label>
                </div>
                <div class="col-xs-2" style="width: 28%;height: 30px;">
                    <input type="text" id="fw_search_zl" style="width: 100%;height: 30px;">
                </div>
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-xs-1" style="width: 24%;text-align: right;">
                    <label>房产证号：</label>
                </div>
                <div class="col-xs-2" style="width: 32%;height: 30px;">
                    <input type="text" id="fw_search_fczh" style="width: 100%;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 24%;margin-left: 2%;">
                    <button type="button" class="btn btn-sm btn-primary"
                            id="fw_search">搜&nbsp;&nbsp;索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </button>
                </div>
                <label style="margin-left:2%;"><input type="checkbox" id="fuzzyQueryfw"/>模糊查询</label>
            </div>
    </div>
<#else>
    <div class="simpleSearch">
                 <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                           data-watermark="请输入权利人/坐落/房产证号">
                </td>
                <td class="Search">
                    <a href="#" id="fw_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="fwgjss">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
</#if>

<#if "${editFlag!}"=="true">
    <div class="tableHeader">
        <ul>
            <#if "${showFcsjUpdate!}" == "true">
                <li>
                    <button type="button" id="gdFwAdd">
                        新增
                    </button>
                </li>
                <li>
                    <button type="button" id="gdFwUpdate">
                        修改
                    </button>
                </li>
            </#if>
            <#if "${zxVisible!}"=="true">
                <li>
                    <button type="button" id="gdFwZx">
                        注销
                    </button>
                </li>
            </#if>
            <li>
                <button type="button" id="gdfwMulXx">
                    已选择
                </button>
            </li>
            <li>
                <button type="button" id="clean">
                    清空
                </button>
            </li>
            <#if "${showFcsjUpdate!}" == "true">
                <li>
                    <button type="button" id="splitData">
                        拆分数据
                    </button>
                </li>

                <li>
                    <button type="button" id="gdfwMergeData">
                        合并数据
                    </button>
                </li>
            </#if>
        </ul>
    </div>
</#if>
    <table id="fw-grid-table"></table>
    <div id="fw-grid-pager"></div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="fwgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="fwgjssHide" class="fwgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fwgjSearchForm">

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
                            <input type="text" name="fwzl" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>房产证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fwgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>