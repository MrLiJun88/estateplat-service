<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<div class="main-container">
    <div class="space-10"></div>
    <@f.contentDiv title="不动产登记档案信息">
        <@f.form id="daxxForm" name="daxxForm">
            <@f.hidden id="proid" name="proid"  value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="sjxxid" name="sjxxid"  value="${bdcSjxx.sjxxid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="proid"  name="proid" value="${proid!}"/>
            <div class="secondTitle">
                <section id="jbxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">
                            档案基本信息：
                        </div>
                        <div class=" col-xs-6  ">
                        </div>
                    </div>
                </section>
            </div>
        <@f.table>
            <@f.tr>
                <@f.th>
                    <@f.label name="档案号"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="dah" name="dah" value="${dah!}"></@f.text>
                </@f.td>
                <@f.th>
                    <@f.label name="目录号"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="mlh" name="mlh" value="${mlh!}"></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr>
                <@f.th>
                    <@f.label name="案卷号"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="ajh" name="ajh" value="${ajh!}"></@f.text>
                </@f.td>
                <@f.th>
                    <@f.label name="权利人"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="qlr" name="qlr" value="${qlr!}"></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr>
                <@f.th>
                    <@f.label name="不动产权证号"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="bdcqzh" name="bdcqzh" value="${bdcqzh!}"></@f.text>
                </@f.td>
                <@f.th>
                    <@f.label name="不动产单元号"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="bdcdyh" name="bdcdyh" value="${bdcdyh!}"></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr>
                <@f.th>
                    <@f.label name="面积"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="mj" name="mj" value="${mj!}"></@f.text>
                </@f.td>
                <@f.th>
                    <@f.label name="用途"></@f.label>
                </@f.th>
                <@f.td>
                    <@f.text id="yt" name="yt" value="${yt!}"></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr>
                <@f.th>
                    <@f.label name="发证日期"></@f.label>
                </@f.th>
                <@f.td colspan="9">
                    <@f.date id="fzrq" name="fzrq" value="${fzrq!}"></@f.date>
                </@f.td>
            </@f.tr>
        </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@f.contentDiv>
        <section id="daxx">
            <div class="row">
                <div class="col-xs-2 demonstrative">
                    档案材料列表：
                </div>
                <div class="col-xs-8 ">
                </div>
                <div class="rowLabel col-xs-2">
                <#--<button type="button" class="btn btn-primary" id="addQlr" onclick="addQlr()">新增</button>-->
                </div>
            </div>
            <div class="page-content">
                <@p.list tableId="daxx-grid-table" pageId="daxx-grid-pager" keyField="QLRID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getSqrxxPagesJson?wiid=${wiid!}">
                    <@p.field fieldName="XH" header="序号" width="3%"/>
                    <@p.field fieldName="CLMC" header="材料名称" width="5%"/>
                    <@p.field fieldName="FS" header="份数" width="5%"/>
                    <@p.field fieldName="YS" header="页数" width="5%"/>
                    <@p.field fieldName="LX" header="类型" width="5%"/>
                    <@p.field fieldName="CZ" header="操作" width="5%"/>
                </@p.list>
                <table id="daxx-grid-table"></table>
                <div id="daxx-grid-pager"></div>
            </div>
        </section>
    </@f.contentDiv>
</@com.html>