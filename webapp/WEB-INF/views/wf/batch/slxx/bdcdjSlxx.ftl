<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@script name="static/js/wf.js"></@script>
<script type="text/javascript">
    var zjlxListJosn=${zjlxListJosn!};
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl="${path_platform!}"
    var analysisUrl ="${analysisUrl!}";
    var wiid ="${wiid!}";
    var taskid ="${taskid!}";
    var from="${from!}";
    var rid="${rid!}";
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="saveSlxx()">保存</button>
        <button type="button" class="btn btn-info save" onclick="addBdcdy()">选择</button>
        <button type="button" class="btn btn-info save" onclick="creatBdcqz()">生成证书</button>
        <div class='btn-group'>
            <a class='btn btn-info' class="dropdown-toggle" data-toggle="dropdown">新增<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a onclick="addQlxx()">新增权利信息</a></li>
                <li><a onclick="addQlr()">新增申请人</a></li>
                <li><a onclick="addFsss()">新增附属设施</a></li>
                <#--<li><a onclick="addSjcl()">新增材料信息</a></li>-->
                <li><a onclick="addGgxx()">新增公告信息</a></li>
            </ul>
        </div>
        <button type="button" class="btn btn-info save" onclick="upFile()">上传材料</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a onclick="openSjd()">收件单</a></li>
                <li><a onclick="openSqs()">申请书 </a></li>
                <li><a onclick="openSpb()">审批表 </a></li>
                <li><a onclick="openBdcqz()">不动产权证书 </a></li>
                <li><a onclick="openBdcqzm()">不动产权证明 </a></li>
            </ul>
        </div>
        <button type="button" class="btn btn-success " onclick="fileView()">材料预览</button>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span9">
            <div id="jbxxMao"></div>
            <@f.contentDiv title="不动产登记受理信息">
                <@f.form id="slxxForm" name="slxxForm">
                    <@f.hidden id="proid" name="proid"  value="${bdcXm.proid!}"/>
                    <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
                    <@f.hidden id="sjxxid" name="sjxxid"  value="${bdcSjxx.sjxxid!}"/>
                    <div>
                        <section id="jbxx">
                            <div class="row">
                                <div class=" col-xs-2 demonstrative">基本信息</div>
                                <div class=" col-xs-6  ">
                                </div>
                                <div class=" col-xs-2 demonstrative dw ">
                                    单位：
                                </div>
                                <div class="rowLabel col-xs-2 mjdwCol">
                                    <@f.select  id="mjdw" name="mjdw"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'平方米','dm':'1'},{'mc':'亩','dm':'2'},{'mc':'公顷','dm':'3'}]" defaultValue="${mjdw!}" noEmptyValue="true"></@f.select>
                                </div>
                            </div>
                        </section>
                    </div>
                    <@f.table style="width: 100%;margin: 0px 0px 10px 0px;">
                        <@f.tr>
                            <@f.th width="25%">
                                <@f.label name="收件编号"></@f.label>
                            </@f.th>
                            <@f.td  width="25%">
                                <@f.text id="bh" name="bh" value="${bdcXm.bh!}" readonly="true"></@f.text>
                            </@f.td>
                            <@f.th  width="25%">
                                <@f.label name="承诺期限"></@f.label>
                            </@f.th>
                            <@f.td  width="25%">
                                <@f.date id="lzrq" name="lzrq" value="${lzrq!}"></@f.date>
                            </@f.td>
                        </@f.tr>
                        <@f.tr>
                            <@f.th>
                                <@f.label name="登记原因"></@f.label>
                            </@f.th>
                            <@f.td>
                                <@f.text id="djyy" name="djyy" value="${bdcXm.djyy!}"></@f.text>
                            </@f.td>
                            <@f.th>
                                <@f.label name="申请证书版式"></@f.label>
                            </@f.th>
                            <@f.td>
                                <@f.radio name="sqzsbs" saveValue="${bdcXm.sqzsbs!}"   defaultValue="单一版" valueFieldName="单一版">
                                    单一版</@f.radio>
                                <@f.radio name="sqzsbs" defaultValue="${bdcXm.sqzsbs!}" valueFieldName="集成版">
                                    集成版</@f.radio>
                            </@f.td>
                        </@f.tr>
                        <@f.tr>
                            <@f.th>
                                <@f.label name="收件人"></@f.label>
                            </@f.th>
                            <@f.td>
                                <@f.text id="sjr" name="sjr" value="${bdcSjxx.sjr!}"></@f.text>
                            </@f.td>
                            <@f.th>
                                <@f.label name="收件时间"></@f.label>
                            </@f.th>
                            <@f.td>
                                <@f.text id="sjrq" name="sjrq" value="${sjrq!}"readonly="true"></@f.text>
                            </@f.td>
                        </@f.tr>
                        <@f.tr>
                            <@f.th>
                                <@f.label name="备注"></@f.label>
                            </@f.th>
                            <@f.td colspan="9">
                                <@f.textarea name="bz" id="bz" value="${bdcXm.bz!}"></@f.textarea>
                            </@f.td>
                        </@f.tr>
                    </@f.table>
                </@f.form>
                <div id="sqrxxMao"></div>
            </@f.contentDiv>
            <@p.listDiv>
                <section   id="sqrxx">
                        <div class="row">
                            <div class="col-xs-2 demonstrative">申请人信息</div>
                            <div class="col-xs-8 "></div>
                            <div class="rowLabel col-xs-2"></div>
                    </div>
                    <@p.list tableId="qlr-grid-table" pageId="qlr-grid-pager" keyField="SQRID" dataUrl="${bdcdjUrl}/bdcdjSqrxx/getSqrxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                            <@p.field fieldName="XH" header="序号" width="2%"/>
                            <@p.field fieldName="SQRMC" header="申请人名称" width="5%"/>
                            <@p.field fieldName="ZJH" header="申请人证件号" width="5%"/>
                            <@p.field fieldName="ZJZL" header="申请证件类型" width="5%"  renderer="rendererZjlx"/>
                            <@p.field fieldName="SQRLB" header="类型" width="5%"  renderer="rendererSqrlx"/>
                            <@p.field fieldName="GYFS" header="共有方式" width="5%" renderer="rendererGyfs"/>
                            <@p.field fieldName="QLBL" header="共有比例" width="5%"/>
                            <@p.field fieldName="SQRID" header="操作" width="5%"  renderer="sqrxxCz"/>
                        </@p.list>
                        <table id="qlr-grid-table"></table>
                        <div id="qlr-grid-pager"></div>
                    <div id="bdcdyxxMao"></div>
                </section>
            </@p.listDiv>
            <@p.listDiv>
                <section id="bdcdyxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">不动产单元信息</div>
                        <div class="col-xs-8 ">
                        </div>
                        <div class="rowLabel col-xs-2">
                        <#--<button type="button" class="btn btn-primary" id="addBdcdy" onclick="addBdcdy()">新增</button>-->
                        </div>
                    </div>
                    <@p.list tableId="bdcdy-grid-table" pageId="bdcdy-grid-pager" keyField="BDCDYID" dataUrl="${bdcdjUrl}/bdcdjBdcdyxx/getBdcdyxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="XH" header="序号" width="3%"/>
                        <@p.field fieldName="FWFSSSID" header="附属设施" width="4%"  renderer="fsssCz"/>
                        <@p.field fieldName="BDCDYH" header="不动产单元号" width="8%"/>
                        <@p.field fieldName="ZL" header="坐落" width="10%"/>
                        <@p.field fieldName="ZDZHMJ" header="宗地/宗海<br>面积" width="5%"/>
                        <@p.field fieldName="ZDZHYT" header="宗地/宗海<br>用途" width="5%"/>
                        <@p.field fieldName="DZWMJ" header="定着物<br>面积" width="4%"/>
                        <@p.field fieldName="DZWYT" header="定着物<br>用途" width="4%"/>
                        <@p.field fieldName="BDCDYID" header="操作" width="12%"   renderer="bdcdyxxCz"/>
                    </@p.list>
                    <table id="bdcdy-grid-table"></table>
                    <div id="bdcdy-grid-pager"></div>
                </section>
                <div id="sjxxMao"></div>
            </@p.listDiv>
            <@p.listDiv>
                <section id="sjd">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">收件信息列表</div>
                        <div class="col-xs-8 ">
                        </div>
                    </div>
                    <@p.list tableId="sjxx-grid-table" pageId="sjxx-grid-pager" keyField="PROID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getBdcXmxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="XH" header="序号" width="1%"/>
                        <@p.field fieldName="BH" header="收件单编号" width="5%"/>
                        <@p.field fieldName="DJZX" header="登记子项" width="5%"/>
                        <@p.field fieldName="SQLX" header="申请类型" width="5%"/>
                        <@p.field fieldName="ZL" header="坐落" width="5%"/>
                        <@p.field fieldName="PROID" header="操作" width="5%"  renderer="sjdCz"/>
                        <@p.field fieldName="QLLXDM" header="权利类型代码" hidden="true"/>
                        <@p.field fieldName="PROID" header="项目ID" hidden="true"/>
                    </@p.list>
                    <table id="sjxx-grid-table"></table>
                    <div id="sjxx-grid-pager"></div>
                </section>
                <div id="qlxxMao"></div>
            </@p.listDiv>
            <@p.listDiv>
                <section id="qlxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">权利信息</div>
                        <div class="col-xs-8 ">
                        </div>
                        <div class="rowLabel col-xs-2">
                        <#--<button type="button" class="btn btn-primary" id="addQlxx" onclick="addQlxx()">新增</button>-->
                        </div>
                    </div>
                    <@p.list tableId="qlxx-grid-table" pageId="qlxx-grid-pager" keyField="QLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="XH" header="序号" width="1%"/>
                        <@p.field fieldName="BDCDYH" header="不动产单元号" width="5%"/>
                        <@p.field fieldName="QLLX" header="权利类型" width="5%"/>
                        <@p.field fieldName="ZL" header="坐落" width="5%"/>
                        <@p.field fieldName="PROID" header="操作" width="5%"  renderer="qllxCz"/>
                        <@p.field fieldName="QLLXDM" header="权利类型代码" hidden="true"/>
                        <@p.field fieldName="PROID" header="项目ID" hidden="true"/>
                    </@p.list>
                    <table id="qlxx-grid-table"></table>
                    <div id="qlxx-grid-pager"></div>
                </section>
                <div id="yqlxxMao"></div>
            </@p.listDiv>
            <@p.listDiv>
                <section id="yqlxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">原权利信息</div>
                        <div class="col-xs-10 ">
                        </div>
                    </div>
                    <@p.list tableId="yqlxx-grid-table" pageId="yqlxx-grid-pager" keyField="QLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getYqlxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="XH" header="序号" width="2%"/>
                        <@p.field fieldName="BDCDYH" header="不动产单元号" width="7%"/>
                        <@p.field fieldName="QLLX" header="权利类型" width="6%"/>
                        <@p.field fieldName="ZL" header="坐落" width="7%"/>
                        <@p.field fieldName="CZ" header="操作" width="3%"   renderer="yqllxCz"/>
                        <@p.field fieldName="PROID" header="PROID" hidden="true"/>
                    </@p.list>
                    <table id="yqlxx-grid-table"></table>
                    <div id="yqlxx-grid-pager"></div>
                </section>
                <div id="ggxxMao"></div>
            </@p.listDiv>
            <#--<@p.listDiv>-->
                <#--<section id="sqcllb">-->
                    <#--<div class="row">-->
                        <#--<div class=" col-xs-2 demonstrative">申请材料列表</div>-->
                        <#--<div class="col-xs-8 ">-->
                        <#--</div>-->
                        <#--<div class="rowLabel col-xs-2">-->
                        <#--&lt;#&ndash;<button type="button" class="btn btn-primary" id="addSjcl" onclick="addSjcl()">添加材料</button>&ndash;&gt;-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<@p.list tableId="sjclxx-grid-table" pageId="sjclxx-grid-pager" keyField="SJCLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getSjclPagesJson?wiid=${wiid!}&proid=${proid!}" multiboxonly="false"multiselect="false">-->
                        <#--<@p.field fieldName="XH" header="序号" width="2%"/>-->
                        <#--<@p.field fieldName="CLMC" header="材料名称" width="13%"/>-->
                        <#--<@p.field fieldName="FS" header="份数" width="3%"/>-->
                        <#--<@p.field fieldName="FS" header="页数" width="3%"/>-->
                        <#--<@p.field fieldName="LX" header="类型" width="3%"/>-->
                        <#--<@p.field fieldName="CZ" header="操作" width="6%"   renderer="sjclCz"/>-->
                        <#--<@p.field fieldName="SJCLID" header="收件材料ID"  width="0%" hidden="true"/>-->
                    <#--</@p.list>-->
                    <#--<table id="sjclxx-grid-table"></table>-->
                    <#--<div id="sjclxx-grid-pager"></div>-->
                <#--</section>-->
                <#--<div id="ggxxMao"></div>-->
            <#--</@p.listDiv>-->
            <@p.listDiv>
                <section id="ggxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">公告信息</div>
                        <div class="col-xs-8 ">
                        </div>
                        <div class="rowLabel col-xs-2">
                        <#--<button type="button" class="btn btn-primary" id="addQlr" onclick="addGgxx()">新增</button>-->
                        </div>
                    </div>
                    <@p.list tableId="ggxx-grid-table" pageId="ggxx-grid-pager" keyField="GGID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getGgxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="GGID" header="公告ID" width="0%" hidden="true"/>
                        <@p.field fieldName="XH" header="序号" width="3%"/>
                        <@p.field fieldName="GGLX" header="公告类型" width="5%"/>
                        <@p.field fieldName="GGMC" header="公告名称" width="7%"/>
                        <@p.field fieldName="KSSJ" header="开始时间" width="5%"/>
                        <@p.field fieldName="JSSJ" header="结束时间" width="5%"/>
                        <@p.field fieldName="GGQX" header="公告期限" width="3%"/>
                        <@p.field fieldName="SFFB" header="是否发布" width="3%"/>
                        <@p.field fieldName="CZ" header="操作" width="7%"  renderer="ggxxCz"/>
                    </@p.list>
                    <table id="ggxx-grid-table"></table>
                    <div id="ggxx-grid-pager"></div>
                </section>
            </@p.listDiv>
            <@p.listDiv>
                <section id="zsxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">证书信息</div>
                    </div>
                    <@p.list tableId="zsxx-grid-table" pageId="zsxx-grid-pager" keyField="ZSID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getZsxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
                        <@p.field fieldName="ZSID" header="ZSID" width="0%" hidden="true"/>
                        <@p.field fieldName="PROID" header="PROID" width="0%" hidden="true"/>
                        <@p.field fieldName="XH" header="序号" width="1%"/>
                        <@p.field fieldName="BDCQZH" header="不动产权证号" width="7%"/>
                        <@p.field fieldName="ZL" header="座落" width="7%"/>
                        <@p.field fieldName="QLR" header="权利人" width="5%"/>
                        <@p.field fieldName="CZ" header="操作" width="3%"  renderer="zsxxCz"/>
                    </@p.list>
                    <table id="zsxx-grid-table"></table>
                    <div id="zsxx-grid-pager"></div>
                </section>
            </@p.listDiv>
        </div>
        <!-- 右侧导航 -->
        <div class="rightNav span3 -docs-sidebar">
            <a href="#jbxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>1</em>基本信息</a>
            <a href="#sqrxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>2</em>申请人信息</a>
            <a href="#bdcdyxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>3</em>不动产单元信息</a>
            <a href="#sjxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>4</em>收件信息列表</a>
            <a href="#qlxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>5</em>权利信息</a>
            <a href="#yqlxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>6</em>原权利信息</a>
            <#--<a href="#sqcllbMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>6</em>申请材料列表</a>-->
            <a href="#ggxxMao" class="ui-corner-all" tabindex="-1" role="menuitem"><em>7</em>公告信息</a>
            <a href="#zsxx" class="ui-corner-all" tabindex="-1" role="menuitem"><em>8</em>证书信息</a>
        </div>
        <#--<div class="blog blog-article">-->
                <#--<div class="blog-wrapper">-->
                    <#--<div class="sidebar md-hide" style="top: 401.05px; opacity: 1;">-->
                        <#--<div class="catalogWrap" id="catalogWrap">-->
                            <#--<span class="sidebar-scrollbar" style="top: 10px;"></span>-->
                            <#--<ul class="anchor-content" id="catalogWrapMenu">-->
                                <#--<li class="blog-catalogue-h2"><a href="#jbxxMao">基本信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#sqrxxMao">申请人信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#bdcdyxxMao">不动产单元信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#qlxxMao">权利信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#yqlxxMao">原权利信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#sqcllbMao">申请材料列表</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#ggxxMao">公告信息</a></li>-->
                                <#--<li class="blog-catalogue-h2"><a href="#zsxx">证书信息</a></li>-->
                            <#--</ul>-->
                        <#--</div>-->
                    <#--</div>-->
            <#--</div>-->
        <#--</div>-->
        <div class="toolbar">
            <a href="javascript:scroll(0,0)" id="top" class="toolbar-item toolbar-item-top"></a>
        </div>
        <script type="text/javascript">
            var btb=$(".rightNav");
            var tempS;
            $(".rightNav").hover(function(){
                var thisObj = $(this);
                tempS = setTimeout(function(){
                    thisObj.find("a").each(function(i){
                        var tA=$(this);
                        setTimeout(function(){ tA.animate({right:"0"},300);},50*i);
                    });
                },200);

            },function(){
                if(tempS){ clearTimeout(tempS); }
                $(this).find("a").each(function(i){
                    var tA=$(this);
                    setTimeout(function(){ tA.animate({right:"-110"},300,function(){
                    });},50*i);
                });

            });
            var isIE6 = !!window.ActiveXObject&&!window.XMLHttpRequest;
            if( isIE6 ){ $(window).scroll(function(){ btb.css("top", $(document).scrollTop()+100) }); }
        </script>
    </div>
</div>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
</@com.html>