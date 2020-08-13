<@com.html title="不动产登记业务管理系统" import="bootstrap,public">
    <script type="text/javascript">
        var bdcdjUrl = "${bdcdjUrl!}";
        var bdclx = "${bdclx!}"
        var ml_proid = "";
        var ml_bdcqzh = "";
        var ml_bdcdyh = "";
        var ml_bdclx = "";
        var djsjbdcdy_bdcdyh = "";
        var djsjbdcdy_id = "";
        var td_bdcqzh = "";
        var td_id = "";
        var proids = "${proids!}";
        var bdcqzhs = "${bdcqzhs!}";
    </script>
    <script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcFghb/bdcCombine.js"></script>
    <script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcFghb/bdcCombine_grid.js"></script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/bdcPic.css" />
    <div class="container-fluid sjpp_content">
        <div class="row-fluid row1">
            <div class="span2 left">
                <h2>目录</h2>
                <ul>
                    <#list bdcPicQoList! as bdcPicQo>
                        <li onclick="changeMl('${bdcPicQo.proid!}','${bdcPicQo.bdcqzh!}','${bdcPicQo.bdclx!}','${bdcPicQo.bdcdyh!}')" id="ml-${bdcPicQo_index}">
                            <img src="static/img/wenjianjia.png"/>
                            <i class="icon-folder-open icon-white"></i>
                            <span>${bdcPicQo.bdcqzh!}</span>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="span10 right">
                <a href="#" class="showMore" id="showMore"><i class="icon-chevron-right"></i></a>
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#tab1" data-toggle="tab" id="bdcdyTab">不动产单元号</a>
                        </li>
                        <li>
                            <a href="#tab3" data-toggle="tab" id="combineTab">合并</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="bdcdyModel">
                            <div class="row-fluid top">
                                <div class="span3">
                                    <span class="help-inline">权利人</span>
                                    <input type="text" id="djsjbdcdy_qlr">
                                </div>
                                <div class="span3">
                                    <span class="help-inline">坐落</span>
                                    <input type="text" id="djsjbdcdy_zl">
                                </div>
                                <div class="span3">
                                    <span class="help-inline">不动产单元号</span>
                                    <input type="text" id="djsjbdcdy_bdcdyh">
                                </div>
                                <div class="query_btn">
                                    <button class="btn btn-primary" type="button" id="djsjbdcdy_searchBtn">查询</button>
                                </div>
                            </div>
                            <div class="bottom">
                                <table id="djsj-grid-table"></table>
                                <div id="djsj-grid-pager"></div>
                            </div>
                        </div>
                        <div class="tab-pane" id="combineModel">
                            <div class="tab-content" id="iframeTab" style="height: 650px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@com.html>
