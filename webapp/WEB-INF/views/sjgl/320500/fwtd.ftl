<div id="fwTd" class="tab-pane">
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="fwTd_search_qlr"
                           data-watermark="请输入坐落/土地证号">
                </td>
                <td class="Search">
                    <a href="#" id="fwTd_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="fwtdgjss">高级搜索</button>
                </td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="hjhtdzsearch">查询户籍化土地证</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdFwTdMulXx">
                    <span>已选择</span>
                </button>
            </li>
            <li>
                <button type="button" id="gdFwTdclean">
                    <i class="ace-icon fa fa-mail-forward"></i>
                    <span>清空</span>
                </button>
            </li>
        </ul>
    </div>
    <table id="fwTd-grid-table" style="width: 800px"></table>
    <div id="fwTd-grid-pager" style="width: 800px"></div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="fwtdgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="fwtdgjssHide" class="fwtdgjssHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fwtdgjSearchForm">

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
                <button type="button" class="btn btn-sm btn-primary" id="fwtdgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>