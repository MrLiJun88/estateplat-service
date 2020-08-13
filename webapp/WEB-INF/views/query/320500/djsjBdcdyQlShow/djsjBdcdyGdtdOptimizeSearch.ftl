<#if bdcdyly==6 >
    <div id="gdtdsj" class="tab-pane in active">
<#else>
    <div id="gdtdsj" class="tab-pane">
</#if>
    <form class="form advancedSearchTable" id="gdtdSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>土地证号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="tdzh" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权证号简称：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cqzhjctd" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 70px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="tdzlgdtd" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="qlrgdtd" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhgdtd" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="gdtdsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <label><input type="checkbox" id="exactQuerytd"/>精确查询</label>
            </div>
        </div>
    </form>
    <table id="gdtdsj-grid-table"></table>
    <div id="gdtdsj-grid-pager"></div>
</div>