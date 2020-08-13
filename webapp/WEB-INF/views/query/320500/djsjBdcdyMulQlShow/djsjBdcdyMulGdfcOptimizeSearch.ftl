<div id="gdfcsj" class="tab-pane">
    <form class="form advancedSearchTable" id="gdfcSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>房产证号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="fczh" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权证号简称：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cqzhjcgdfc" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 70px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="fwzlgdfc" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="qlrgdfc" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhgdfc" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="gdfcsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <label><input type="checkbox" id="exactQuerygdfc"/>精确查询</label>
            </div>
        </div>
    </form>

    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdfcSure">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>确定</span>
                </button>
            </li>
            <li>
                <button type="button" id="gdfcsjMulXx">
                    <span>已选择</span>
                </button>
            </li>
                                <#if bdcdyly==4||bdcdyly==7>
                                    <li>
                                        <button type="button" name="addHhcf" id="addFcz">
                                            <span name="shoppingCartButton"></span>
                                        </button>
                                    </li>
                                </#if>
        </ul>
    </div>

    <table id="gdfcsj-grid-table"></table>
    <div id="gdfcsj-grid-pager"></div>
</div>