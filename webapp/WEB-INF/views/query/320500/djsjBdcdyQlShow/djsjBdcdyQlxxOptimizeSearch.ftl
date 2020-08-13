<#if (bdcdyly==3 && sqlxdm != "8009902" && sqlxdm != "8009904")>
    <div id="qlxx" class="tab-pane in active">
<#else>
    <div id="qlxx" class="tab-pane">
</#if>
    <form class="form advancedSearchTable" id="qlxxSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产权证号：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="bdcqzhqlxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权证号简称：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cqzhjcqlxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="zlqlxx" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>被查封权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="bzxrqlxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhqlxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 100px;text-align: right;">
                <label>查封文号：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="cfwhqlxx" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>房屋代码：</label>
            </div>
            <div class="col-xs-2" style="width: 180px;height: 30px;">
                <input type="text" id="fwbmqlxx" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>产权权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="qlr" style="width: 200px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="qlxx_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <#--<label><input type="checkbox" id="exactQueryqlxx"/>精确查询</label>-->
            </div>
        </div>
    </form>

    <table id="qlxx-grid-table"></table>
    <div id="qlxx-grid-pager"></div>
</div>