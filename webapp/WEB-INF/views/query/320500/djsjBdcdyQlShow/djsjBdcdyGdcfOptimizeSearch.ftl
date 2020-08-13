<#if bdcdyly==3 || bdcdyly==4 >
    <#if (sqlxdm == "8009902" || sqlxdm == "8009904")>
        <div id="gdcfsj" class="tab-pane in active">
    <#else>
        <div id="gdcfsj" class="tab-pane">
    </#if>
<#else>
    <div id="qlxx" class="tab-pane">
</#if>
    <form class="form advancedSearchTable" id="gdcfSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>查封文号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cfwhgdcf" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>房屋坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="fwzlgdcf" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>土地坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="tdzlgdcf" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhgdcf" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>原权证号：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="yqzhgdcf" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="gdcfsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <label><input type="checkbox" id="exactQuerygdcf"/>精确查询</label>
            </div>
        </div>
    </form>
    <table id="gdcfsj-grid-table"></table>
    <div id="gdcfsj-grid-pager"></div>
</div>