<@com.html title="不动产登记业务管理系统" import="ace,public">
<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var wwslbh = "${wwslbh!}";
    var $mulRowid = new Array();
</script>
<script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcPic/showPpgx.js"></script>
<style type="text/css">
    .SelectBG {
        background-color: #5cb85c;
    }
</style>
<link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/djsjBdcdyQlShow.css"/>
<div class="main-container">
    <div class="row-fluid top">
        <form class="form advancedSearchTable" id="SearchForm">
            <div class="row"></div>
            <div class="row">
                <div class="col-xs-1" style="width: 130px;text-align: right;">
                    <label>不动产权证号：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="bdcqzh" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 130px;text-align: right;">
                    <label>产权证号简称：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="cqzhjc" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 80px;text-align: right;">
                    <label>坐落：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="zl" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 120px;text-align: right;">
                    <label>不动产类型：</label>
                </div>
                <div class="col-xs-2" style="width: 120px;height: 30px;">
                    <select id="bdclxSelect" name="bdclx" class="form-control"
                            style="margin-left: 5px;width: 120px;height: 30px;">
                        <#list bdclxList! as bdclx>
                            <option value="${bdclx.DM!}">${bdclx.MC}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-1" style="width: 130px;text-align: right;">
                    <label>房屋代码：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="fwbm" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 130px;text-align: right;">
                    <label>不动产单元号：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="bdcdyh" value="${bdcdyh!}" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 80px;text-align: right;">
                    <label>权利人：</label>
                </div>
                <div class="col-xs-2" style="width: 190px;height: 30px;">
                    <input type="text" id="qlr" style="width: 190px;height: 30px;">
                </div>
                <div class="col-xs-1" style="margin-left: 10px">
                    <button type="button" class="btn btn-sm btn-primary"
                            id="search_btn">搜&nbsp;&nbsp;索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </button>
                </div>
            </div>
            <div class="row">
                <div id="combineButton" class="col-xs-3" style="margin-left: 20px">
                </div>
            </div>
        </form>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdxxClear" onclick="bdcPicMul()">
                    <span>匹配</span>
                </button>
            </li>
        </ul>
    </div>
    <div class="bottom">
        <div class="bottom">
            <table id="grid-table"></table>
            <div id="grid-pager"></div>
        </div>
    </div>
    <div class="fade" id="loadingModal" style="position:absolute!important">
        <div style="width: 200px;height:38px!important; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
            <div class="progress progress-striped active" style="margin-bottom: 0;height:38px!important">
                <div class="progress-bar" style="width: 100%;"><h5 style="color:black"><strong>正在撤销匹配请稍等！</strong></h5>
                </div>
            </div>

        </div>
    </div>
    </@com.html>
