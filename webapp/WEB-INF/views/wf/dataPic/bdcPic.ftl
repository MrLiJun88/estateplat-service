<@com.html title="不动产登记业务管理系统" import="bootstrap,public">
    <script type="text/javascript">
        var bdcdjUrl = "${bdcdjUrl!}";
        var bdclx = "${bdclx!}"
        var ml_proid = "";
        var ml_bdcqzh = "";
        var ml_bdcdyh = "";
        var ml_bdclx = "";
        var djsjbdcdy_zl = "";
        var djsjbdcdy_qlr = "";
        var djsjbdcdy_bdcdyh = "";
        var djsjbdcdy_fttdmj = "";
        var djsjbdcdy_dytdmj = "";
        var djsjbdcdy_id = "";
        var td_bdcqzh = "";
        var td_zl = "";
        var td_qlr = "";
        var td_id = "";
        var ppbdcdyh = "";
        var wwslbh="${wwslbh!}";
        var td_proid="";
    </script>

    <script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcPic/bdcPic.js"></script>
    <script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcPic/bdcPic_grid.js"></script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/bdcPic.css" />
    <div class="container-fluid sjpp_content">
        <div class="row-fluid row1">
            <div class="span2 left">
                <h2>目录</h2>
                <ul>
                    <#list bdcPicQoList! as bdcPicQo>
                        <li onclick="changeMl('${bdcPicQo.proid!}','${bdcPicQo.bdcqzh!}','${bdcPicQo.bdclx!}','${bdcPicQo.bdcdyh!}','${bdcPicQo.ppbdcdyhid!}','${bdcPicQo.ppbdcdyh!}','${bdcPicQo.fwbm!}','${bdcPicQo.tdid!}','${bdcPicQo.tdbdcqzh!}')" id="ml-${bdcPicQo_index}">
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
                        <#if '${bdclx!}' =='TDFW'>
                            <li>
                                <a href="#tab2" data-toggle="tab" id="tdTab">土地证</a>
                            </li>
                        </#if>
                        <li>
                            <a href="#tab3" data-toggle="tab" id="picTab">匹配</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="bdcdyModel">
                            <div class="row-fluid top">
                                <div class="span3">
                                    <span class="help-inline">房屋编码</span>
                                    <input type="text" id="djsjbdcdy_fwbm">
                                </div>
                                <div class="span3">
                                    <span class="help-inline">坐落</span>
                                    <input type="text" id="djsjbdcdy_zl">
                                </div>
                                <div class="span4">
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
                        <#if '${bdclx!}'=='TDFW'>
                            <div class="tab-pane" id="tdModel">
                                <div class="top">
                                    <div class="row-fluid">
                                        <div class="span3">
                                            <span class="help-inline">权利人</span>
                                            <input type="text" id="td_qlr">
                                        </div>
                                        <div class="span3">
                                            <span class="help-inline">坐落</span>
                                            <input type="text" id="td_zl">
                                        </div>
                                        <div class="span4">
                                            <span class="help-inline">不动产权证(明)号</span>
                                            <input type="text" id="td_bdcqzh">
                                        </div>
                                        <div class="query_btn">
                                            <button class="btn btn-primary" type="button" id="td_searchBtn">查询</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="bottom">
                                    <table id="td-grid-table"></table>
                                    <div id="td-grid-pager"></div>
                                </div>
                            </div>
                        </#if>
                        <div class="tab-pane" id="picModel">
                            <p>其它</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="fade" id="loadingModal" style="position:absolute!important">
        <div style="width: 200px;height:38px!important; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
            <div class="progress progress-striped active" style="margin-bottom: 0;height:38px!important">
                <div class="progress-bar" style="width: 100%;"> <h5 style="color:black"> <strong>正在匹配请稍等！</strong> </h5></div>
            </div>

        </div>
    </div>
</@com.html>
