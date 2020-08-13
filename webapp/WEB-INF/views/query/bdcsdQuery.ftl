<@com.html title="不动产登记业务管理系统" import="ace,bootstrap,public">
    <script type="text/javascript">
        var bdcdjUrl = "${bdcdjUrl!}";
        var $mulRowid = [];
    </script>
    <script type="text/javascript" src="${bdcdjUrl!}/static/js/query/bdcsdQuery.js"></script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/djsjBdcdyQlShow.css" />

    <div class="row-fluid top">
        <form class="form advancedSearchTable" id="SearchForm">
            <div class="row"></div>
            <div class="row">
                <div class="col-xs-1" style="width: 100px;text-align: right;margin-top: 5px">
                    <label>收件号：</label>
                </div>
                <div class="col-xs-2" style="width: 150px;height: 30px;">
                    <input type="text" id="sjh" style="width: 150px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 100px;text-align: right;margin-top: 5px">
                    <label>房屋代码：</label>
                </div>
                <div class="col-xs-2" style="width: 150px;height: 30px;">
                    <input type="text" id="fwbm" style="width: 150px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 100px;text-align: right;margin-top: 5px">
                    <label>坐落：</label>
                </div>
                <div class="col-xs-2" style="width: 200px;height: 30px;">
                    <input type="text" id="zl" style="width: 200px;height: 30px;">
                </div>
                <div class="col-xs-1" style="margin-left: 20px">
                    <button type="button" class="btn btn-sm btn-primary"
                            id="search_btn">搜&nbsp;&nbsp;索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </button>
                </div>
                <div class="row">
                    <label><input type="checkbox" id="exactQuery"/>精确查询</label>
                </div>
            </div>
        </form>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdxxClear" onclick="checkGdxxByProid()">
                    <span>归档</span>
                </button>
            </li>
        </ul>
    </div>
    <div class="bottom">
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>

    <div class="Pop-upBox moveModel" style="display: none;" id="tsxxPop">
        <div class="modal-dialog tsxxPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="bigger-110"></i>提示信息</h4>
                    <button type="button" id="tsxxHide" class="proHide"><i
                                class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="tsxxForm">
                        <div class="row">
                            <h3></h3>
                        </div>
                    </form>
                </div>
                <div class="modelFooter" style="text-align:right"">
                    <button type="button" class="btn btn-sm btn-primary" id="yesBtn">是</button>
                    <button type="button" class="btn btn-sm btn-primary" id="noBtn">否</button>
                </div>
            </div>
        </div>
    </div>
</@com.html>
