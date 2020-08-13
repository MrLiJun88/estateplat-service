<div id="jcptsj" class="tab-pane">
    <form class="form advancedSearchTable" id="jcptsjSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>收件编号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="businessNo" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="jcptsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <#--                <label><input type="checkbox" id="exactQueryjcptsj"/>精确查询</label>-->
            </div>
        </div>
    </form>
    <table id="jcptsj-grid-table"></table>
    <div id="jcptsj-grid-pager"></div>
</div>