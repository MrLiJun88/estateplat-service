<div id="lq" class="tab-pane">
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="lq_search_qlr"
                           data-watermark="请输入权利人/坐落/林权证号">
                </td>
                <td class="Search">
                    <a href="#" id="lq_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="lqgjss">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
<#if "${editFlag!}"=="true">
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdLqAdd">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>新增</span>
                </button>
            </li>
            <li>
                <button type="button" id="gdLqUpdate">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>修改</span>
                </button>
            </li>
        </ul>
    </div>
</#if>
    <table id="lq-grid-table"></table>
    <div id="lq-grid-pager"></div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="lqgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="lqgjssHide" class="lqgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="lqgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="lqgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>