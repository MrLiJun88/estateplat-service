<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<div class="main-container">
    <div class="space9">
        <@f.contentDiv>
            <section id="hqjy">
                <div class="row">
                    <div class="col-xs-2 demonstrative">
                        合同编号：
                    </div>
                    <div class="col-xs-8 ">
                    </div>
                    <div class="rowLabel col-xs-2">
                        <@f.buttons>
                    <@f.button   handler="search" text="查询" ></@f.button>
                </@f.buttons>
                    </div>
                </div>
                <@p.list tableId="hqjy-grid-table" pageId="hqjy-grid-pager" keyField="QLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}">
                    <@p.field fieldName="XZQMC" header="行政区名称" width="5%"/>
                    <@p.field fieldName="HTBH" header="合同编号" width="5%"/>
                    <@p.field fieldName="QLR" header="权利人" width="5%"/>
                    <@p.field fieldName="JYJE" header="交易金额" width="5%"/>
                    <@p.field fieldName="CZ" header="操作" width="5%"   renderer="qllxCz"/>
                </@p.list>
                <@f.table id="hqjy-grid-table"></@f.table>
                <div id="hqjy-grid-pager"></div>
            </section>
        </@f.contentDiv>
    </div>
</div>
    <@f.hidden id="wiid" value="${wiid!}" />
    <@f.hidden id="proid" value="${proid!}" />
</@com.html>