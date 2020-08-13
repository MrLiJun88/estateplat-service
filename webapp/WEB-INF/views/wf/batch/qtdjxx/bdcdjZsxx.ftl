<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<div class="main-container">
    <div class="space-10"></div>
    <@f.contentDiv title="不动产登记证书（明）信息">
        <div class="secondTitle">
            <section id="zsxx">
                <div class="row">
                    <div class=" col-xs-2 demonstrative">
                        证书（明）列表：
                    </div>
                </div>
                <@p.list tableId="zsxx-grid-table" pageId="zsxx-grid-pager" keyField="QLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}">
                    <@p.field fieldName="XH" header="序号" width="1%"/>
                    <@p.field fieldName="ZMH" header="证（明）号" width="5%"/>
                    <@p.field fieldName="QLR" header="权利人" width="5%"/>
                    <@p.field fieldName="LX" header="类型" width="5%"/>
                    <@p.field fieldName="CZ" header="操作" width="5%"   renderer="qllxCz"/>
                </@p.list>
                <table id="zsxx-grid-table"></table>
                <div id="zsxx-grid-pager"></div>
            </section>
        </div>
    </@f.contentDiv>
</div>
    <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
    <@f.hidden id="proid"  name="proid" value="${proid!}"/>
</@com.html>