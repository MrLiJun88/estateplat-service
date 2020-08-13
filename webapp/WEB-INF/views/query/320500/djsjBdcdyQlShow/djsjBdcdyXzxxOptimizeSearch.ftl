<#if bdcdyly==11>
    <div id="xzxx" class="tab-pane in active">
<#else>
    <div id="xzxx" class="tab-pane">
</#if>
    <form class="form advancedSearchTable" id="xzxxSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产权证号：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="bdcqzhxzxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权证号简称：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cqzhjcxzxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="zlxzxx" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>被限制权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="bzxrxzxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhxzxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>房屋代码：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="fwbmxzxx" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="cqqlr" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="xzxx_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <#--<label><input type="checkbox" id="exactQueryxzxx"/>精确查询</label>-->
            </div>
        </div>
    </form>

    <table id="xzxx-grid-table"></table>
    <div id="xzxx-grid-pager"></div>
</div>