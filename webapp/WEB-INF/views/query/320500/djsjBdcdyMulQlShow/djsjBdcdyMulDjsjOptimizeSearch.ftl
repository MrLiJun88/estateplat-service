<div id="djsj" class="tab-pane">
    <form class="form advancedSearchTable" id="djsjSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="qlrdjsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="tdzldjsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhdjsj" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>房屋代码：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="fwbmdjsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>地籍号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="djhdjsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产类型：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <select id="bdclxSelect" name="bdclx" class="form-control" style="margin-left: 5px;width: 200px;height: 30px;">
                    <#list bdclxList! as bdclx>
                        <option value="${bdclx.DM!}">${bdclx.MC}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="djsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <#--<label><input type="checkbox" id="exactQuerydjsj"/>精确查询</label>-->
            </div>
        </div>
    </form>
    <div class="simpleSearch" id="bdclxZxShow" style="display:none">
    <#--<input type="text" id="checkboxSelect" style="width: 200px;height: 34px;"/>-->
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="border: 0px">
                    <select name="bdclx" id="bdclxSelect" class="form-control" style="width: 150px;"
                            onchange="changeBdclx()">
                                            <#list bdclxList! as bdclx>
                                                <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                            </#list>
                    </select>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td style="border: 0px">
                    <select name="bdclxZx" id="bdclxZxSelect" class="form-control"
                            style="width: 150px;">
                        <option value="">请选择...</option>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="djsjSure">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>确定</span>
                </button>
            </li>
            <li>
                <button type="button" id="djsjMulXx">
                    <span>已选择</span>
                </button>
            </li>
                                <#if bdcdyly==4>
                                    <li>
                                        <button type="button" name="addHhcf" id="addBdcdy">
                                            <span name="shoppingCartButton"></span>
                                        </button>
                                    </li>
                                </#if>
        </ul>
    </div>
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>