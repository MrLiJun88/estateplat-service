<@com.html title="" import="ace,public">
<style>
    .filesub {
        display: block;
        width: 90px;
        line-height: 34px;
        height: 34px;
        text-align: center;
        background: #155e96;
        color: #fff;
        word-spacing: 10px;
    }

    a:visited {
        color: #FFF;
    }

    a:hover {
        color: #FFF;
    }

    .col-xs-11 {
        width: 100%
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    .tableHeader {
        width: 100%;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    .SSinput {
        min-width: 100px !important;
    }

    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 1175px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput .modal-dialog {
        width: 500px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput1 .modal-dialog {
        width: 620px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    /*高级搜索的样式修改*/
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .title {
        font-size: 18pt;
        text-align: center;
        padding-left: 2px;
        position: absolute;
        margin-left: 40px;
        margin-top: 5px;
    }

    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>

<script >
    var bdcdjUrl="${bdcdjUrl!}";
    var reportUrl="${reportUrl!}";
    var portalUrl="${portalUrl!}";
    var wfids="${wfids!}";
    var matchTdzh="${matchTdzh!}";
    var filterFwPpzt="${filterFwPpzt!}";
    var gdTabOrder="${gdTabOrder!}";
    var gdTabLoadData="${gdTabLoadData!}";
    var bppwfids="${bppwfids!}";
    var sflw="${sflw!}";
</script>
    <@script name="static/js/default/jgDataPic.js"></@script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container" style="display: none;">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
        <div class="row">
        <div class="col-xs-2" style="min-width: 230px">
            <#if "${onlyShowDatePic!}"!='true'>
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 登记类型</div>
                    <div class="profile-info-value">
                        <select id="djlxSelect" class="chosen-select" style="width:100px">
                            <#list djlxList as djlx>
                                <option value="${djlx.DM!}">${djlx.MC!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </#if>
        </div>
            <div class="col-xs-3" style="min-width:370px">
            <#if "${onlyShowDatePic!}"!='true'>
            <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 申请类型</div>
                    <div class="profile-info-value">
                        <select id="sqlxSelect" class="chosen-select" style="width:250px" onchange="changeSqlx()">
                            <#list sqlxList as sqlx>
                                <option value="${sqlx.DM!}">${sqlx.MC!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </#if>
            </div>

            <div class="col-xs-3">
                <#if "${onlyShowDatePic!}"!='true'>
                    <button type="button" class="btn btn-sm btn-primary" id="save">创建</button>
                </#if>
                <button type="button" class="btn btn-sm btn-primary" id="ylzs" style="display: none;">预览证书</button>
                <#if "${showZjfzBtn!}"=='true'>
                    <button type="button" class="btn btn-sm btn-primary" id="fz">缮证</button>
                </#if>
                <button type="button" class="btn btn-sm btn-primary" id="match">匹配</button>
                <button type="button" class="btn btn-sm btn-primary" id="dismatch">取消匹配</button>
                <div></div>
                 <#if "${onlyShowDatePic!}"!='true'>
                     <button type="button" class="btn btn-sm btn-primary" id="ppxm" style="margin-top: 5px">关联项目</button>
                     <button type="button" class="btn btn-sm btn-primary" id="deletexmgl" style="margin-top: 5px;">删除项目关联
                     </button>
                 </#if>
            </div>
        </div>
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <#list gdTabOrderList as gdTabOrder>
                            <#if gdTabOrder=='td'>
                                <li>
                                    <a data-toggle="tab" id="tdTab" href="#td">
                                        纯土地证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='lq'>
                                <li>
                                    <a data-toggle="tab" id="lqTab" href="#lq">
                                        林权证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='cq'>
                                <li>
                                    <a data-toggle="tab" id="cqTab" href="#cq">
                                        草原证
                                    </a>
                                </li>
                            <#else>
                                <li>
                                    <a data-toggle="tab" id="fwTab" href="#fw">
                                        房产证
                                    </a>
                                </li>

                            </#if>

                        </#list>

                    </ul>
                    <div class="tab-content">
                        <div id="fw" class="tab-pane ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                                   data-watermark="请输入权利人/坐落/房产证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="fw_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="fwgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdFwAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdFwUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                        <#if "${zxVisible!}"=="true">
                                            <li>
                                                <button type="button" id="gdFwZx">
                                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                                    <span>注销</span>
                                                </button>
                                            </li>
                                            <#--<li>
                                                 <button type="button" id="gdFwJcZx">
                                                     <i class="ace-icon fa fa-pencil-square-o"></i>
                                                     <span>解除注销</span>
                                                 </button>
                                             </li>-->
                                        </#if>
                                        <#--<li>
                                            <button type="button" id="addSel">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>添加</span>
                                            </button>
                                        </li>-->
                                        <li>
                                            <button type="button" id="gdfwMulXx">
                                                <span>已选择</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="clean">
                                                <i class="ace-icon fa fa-mail-forward"></i>
                                                <span>清空</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="splitData">
                                                <span>拆分数据</span>
                                            </button>
                                        </li>
                                    <#--<li>-->
                                    <#--<button type="button" id="queryFw">-->
                                    <#--<i class="ace-icon fa fa-search"></i>-->
                                    <#--<span>查看列表</span>-->
                                    <#--</button>-->
                                    <#--</li>&ndash;&gt;
                                        <!-- 锁定，解锁 -->
                                        <#--<li>-->
                                            <#--<button type="button" id="lockFw">-->
                                                <#--<i class="ace-icon fa fa-search"></i>-->
                                                <#--<span>锁定</span>-->
                                            <#--</button>-->
                                        <#--</li>-->
                                        <#--<li>-->
                                            <#--<button type="button" id="unlockFw">-->
                                                <#--<i class="ace-icon fa fa-search"></i>-->
                                                <#--<span>解锁</span>-->
                                            <#--</button>-->
                                        <#--</li>-->
                                    </ul>
                                </div>
                            </#if>
                            <table id="fw-grid-table"></table>
                            <div id="fw-grid-pager"></div>
                        </div>
                    <#--林权-->
                        <div id="lq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="lq_search_qlr"
                                                   data-watermark="请输入权利人/坐落/林权证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="lq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="lqgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdLqAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdLqUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="lq-grid-table"></table>
                            <div id="lq-grid-pager"></div>
                        </div>
                    <#--草原-->
                        <div id="cq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="cq_search_qlr"
                                                   data-watermark="请输入权利人/坐落/草原证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="cq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="cqgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdCqAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdCqUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="cq-grid-table"></table>
                            <div id="cq-grid-pager"></div>
                        </div>
                    <#--土地-->
                        <div id="td" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                                                   data-watermark="请输入坐落/土地证号/地籍号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="td_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="tdgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdTdAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdTdUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                        <#if "${zxTdVisible!}"=="true">
                                            <li>
                                                <button type="button" id="gdTdZx">
                                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                                    <span>注销</span>
                                                </button>
                                            </li>
                                        <#--<li>
                                             <button type="button" id="gdFwJcZx">
                                                 <i class="ace-icon fa fa-pencil-square-o"></i>
                                                 <span>解除注销</span>
                                             </button>
                                         </li>-->
                                        </#if>
                                        <li>
                                            <button type="button" id="gdTdLocked">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>锁定</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdTdUnLocked">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>解锁</span>
                                            </button>
                                        </li>
                                        <#--<li>
                                            <button type="button" id="addTdSel">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>添加</span>
                                            </button>
                                        </li>-->
                                        <li>
                                            <button type="button" id="gdTdMulXx">
                                                <span>已选择</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdTdclean">
                                                <i class="ace-icon fa fa-mail-forward"></i>
                                                <span>清空</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="td-grid-table"></table>
                            <div id="td-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" id="dyhTab" href="#file">
                                不动产单元
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="fwTdTab" href="#fwTd" style="display:none;">
                                房屋土地证
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="file" class="tab-pane in active ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="dyh_search_qlr"
                                                   data-watermark="请输入权利人/坐落/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="dyh_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="dyhgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>

                                    </ul>
                                </div>
                            </#if>
                            <table id="dyh-grid-table"></table>
                            <div id="dyh-grid-pager"></div>
                        </div>

                        <div id="fwTd" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fwTd_search_qlr"
                                                   data-watermark="请输入坐落/土地证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="fwTd_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                        <td style="border: 0px">&nbsp;</td>
                                        <td>
                                            <button type="button" class="btn01 AdvancedButton" id="fwtdgjss">高级搜索</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        <#--  <#if "${editFlag!}"=="true">
                              <div class="tableHeader">
                                  <ul>fwTdAdd
                                      <li>
                                          <button type="button" id="fwTdAdd">
                                              <i class="ace-icon fa fa-file-o"></i>
                                              <span>新增</span>
                                          </button>
                                      </li>
                                      <li>
                                          <button type="button" id="fwTdUpdate">
                                              <i class="ace-icon fa fa-pencil-square-o"></i>
                                              <span>修改</span>
                                          </button>
                                      </li>
                                  </ul>
                              </div>
                          </#if>-->
                            <table id="fwTd-grid-table" style="width: 800px"></table>
                            <div id="fwTd-grid-pager" style="width: 800px"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--文件选择框-->
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle">选择文件（请确认文件为.xls格式）</h4>
                <button type="button" id="fileHide" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <td><input type="file" id="fileWindow" name="file"
                                                   style="width: 280px;height:34px"/></td>
                                        <input type="hidden" name="sjlx" id="sjlx">
                                        <td><input type="submit" id="fileSub" class="filesub" value="导入"/></td>
                                        <td><a href="" class="filesub" id="fileDownLoad">模板下载</a></td>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <table id="bdcxm-grid-table"></table>
                    <div id="bdcxm-grid-pager"></div>
                </div>
            </div>

        <#--</div>-->
        </div>
    </div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput1">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle1">关联项目</h4>
                <button type="button" id="fileHide1" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw1">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm1" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="text" style="width: 265px" class="SSinput watermarkText"
                                                       id="bdcxm_search_qlr" data-watermark="受理编号"></td>
                                            <td class="Search"><a href="#" id="bdcxm_search"> 搜索<i
                                                    class="ace-icon fa fa-search bigger-130"></i></a></td>
                                        </tr>
                                    </table>
                                </form>
                            </div>
                        </div>
                        <table id="bdcxm-grid-table1"></table>
                        <div id="bdcxm-grid-pager1"></div>
                    </div>
                </div>
            </div>

        <#--</div>-->
        </div>
    </div>
</div>

<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="fwgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="fwgjssHide" class="fwgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fwgjSearchForm">

                        <div class="row">
                            <div class="col-xs-2">
                                <label>权利人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="qlr" class="form-control">
                            </div>

                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fwzl" class="form-control">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-2">
                                <label>房产证号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fczh" class="form-control">
                            </div>
                        </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fwgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="lqgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="lqgjssHide" class="lqgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="lqgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="lqgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="cqgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="cqgjssHide" class="cqgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="cqgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="cqgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="tdgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="tdgjssHide" class="tdgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="tdgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqzl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqtdzh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jqdjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tdgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="dyhgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="dyhgjssHide" class="dyhgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="dyhgjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="dyhgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="fwtdgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="fwtdgjssHide" class="fwtdgjssHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fwtdgjSearchForm">

                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fwtdgjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>

<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gdXzyyPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    <i class="ace-icon fa fa-search bigger-110"></i>
                    请输入限制原因
                </h4>
                <button type="button" id="proHide" class="proHide">
                    <i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gdXzyyForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>限制原因：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" name="xzyy" id="xzyy" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gdLockSureBtn">确定</button>
                <button type="button" class="btn btn-sm btn-primary" id="gdLockCancelBtn">取消</button>
            </div>
        </div>
    </div>
</div>

<form id="form" hidden="hidden">
    <input type="hidden" id="djlx" name="djlx">
    <input type="hidden" id="fwid" name="fwid">
    <input type="hidden" id="tdid" name="tdid">
    <input type="hidden" id="dah" name="dah">
    <input type="hidden" id="lqid" name="lqid">
    <input type="hidden" id="cqid" name="cqid">
    <input type="hidden" id="djId" name="djId">
    <input type="hidden" id="bdcdyh" name="bdcdyh">
    <input type="hidden" id="workFlowDefId" name="workFlowDefId">
    <input type="hidden" id="sqlx" name="sqlxMc">
    <input type="hidden" id="xmmc" name="xmmc">
    <input type="hidden" id="tdzh" name="tdzh"/>
    <input type="hidden" id="ppzt" name="ppzt"/>
    <input type="hidden" id="dyid" name="dyid"/>
    <input type="hidden" id="ygid" name="ygid"/>
    <input type="hidden" id="cfid" name="cfid"/>
    <input type="hidden" id="yyid" name="yyid"/>
    <input type="hidden" id="gdproid" name="gdproid"/>
    <input type="hidden" id="mulGdfw" name="mulGdfw"/>
    <input type="hidden" id="djIds" name="djIds"/>
    <input type="hidden" id="bdcdyhs" name="bdcdyhs"/>
    <input type="hidden" id="tdids" name="tdids"/>
    <input type="hidden" id="qlid" name="qlid"/>
    <input type="hidden" id="qlzt" name="qlzt"/>
    <input type="hidden" id="gdproids" name="gdproids"/>
    <input type="hidden" id="qlids" name="qlids"/>
    <input type="hidden" id="qlr"/>
    <input type="hidden" id="ybdcqzh" name="ybdcqzh"/>
    <input type="hidden" id="tdqlzt" name="tdqlzt"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
