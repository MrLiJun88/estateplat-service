<div id="td" class="tab-pane">
<#if showOptimize == "true">
    <div class="tab-content">
        <div class="row">
            <div class="col-xs-1" style="width: 24%;text-align: right;">
                <label>地籍号：</label>
            </div>
            <div class="col-xs-2" style="width: 28%;height: 30px;">
                <input type="text" id="td_search_djh" style="width: 100%;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 20%;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 28%;height: 30px;">
                <input type="text" id="td_search_zl" style="width: 100%;height: 30px;">
            </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="col-xs-1" style="width: 24%;text-align: right;">
                <label>土地证号：</label>
            </div>
            <div class="col-xs-2" style="width: 32%;height: 30px;">
                <input type="text" id="td_search_tdzh" style="width: 100%;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 24%;margin-left: 2%;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="td_search">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
            </div>
            <label style="margin-left: 2%"><input type="checkbox" id="fuzzyQueryTd"/>模糊查询</label>
        </div>
    </div>
<#else>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                           data-watermark="请输入坐落/土地证号/地籍号">
                </td>
                <td class="Search">
                    <a href="#" id="td_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="tdgjss">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
</#if>
<#if "${editFlag!}"=="true">
    <div class="tableHeader">
        <ul>
            <#if "${showTdsjUpdate!}" == "true">
                <li>
                    <button type="button" id="gdTdAdd">
                        新增
                    </button>
                </li>
                <li>
                    <button type="button" id="gdTdUpdate">
                        修改
                    </button>
                </li>
                <#if "${zxTdVisible!}"=="true">
                    <li>
                        <button type="button" id="gdTdZx">
                            注销
                        </button>
                    </li>
                </#if>
                <li>
                    <button type="button" id="gdTdLocked">
                        锁定
                    </button>
                </li>
                <li>
                    <button type="button" id="gdTdUnLocked">
                        解锁
                    </button>
                </li>
            </#if>
            <li>
                <button type="button" id="gdTdMulXx">
                    已选择
                </button>
            </li>
            <li>
                <button type="button" id="gdTdclean">
                    清空
                </button>
            </li>

            <#--<li>
                <button type="button" id="splitData">
                    <span>拆分数据</span>
                </button>
            </li>-->
            <#if "${showTdsjUpdate!}" == "true">
                <li>
                    <button type="button" id="gdtdMergeData">
                        合并数据
                    </button>
                </li>
            </#if>
        </ul>
    </div>
</#if>
    <table id="td-grid-table"></table>
    <div id="td-grid-pager"></div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="tdgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="tdgjssHide" class="tdgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="tdgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqzl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqtdzh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqdjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tdgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>