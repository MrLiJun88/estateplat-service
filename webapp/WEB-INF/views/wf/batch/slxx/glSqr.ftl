<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/glSqr.js"></@script>
<script type="text/javascript">
    var zjlxListJosn=${zjlxListJosn!};
    var bdcdjUrl="${serverUrl!}";
</script>
    <@f.contentDiv title="关联申请人">
    <section id="glsqr">
        <@p.list tableId="glsqr-grid-table" pageId="glsqr-grid-pager"  keyField="SQRID" dataUrl="${bdcdjUrl}/bdcdjSqrxx/getSqrxxPagesJson?wiid=${wiid!}" multiboxonly="false"multiselect="false">
            <@p.field fieldName="XH" header="序号" width="2%"/>
            <@p.field fieldName="SQRMC" header="申请人名称" width="5%"/>
            <@p.field fieldName="ZJH" header="申请人证件号" width="5%"/>
            <@p.field fieldName="ZJZL" header="申请证件类型" width="5%"  renderer="rendererZjlx"/>
            <@p.field fieldName="SQRLB" header="类型" width="5%"  renderer="rendererSqrlx"/>
            <@p.field fieldName="GYFS" header="共有方式" width="5%" renderer="rendererGyfs"/>
            <@p.field fieldName="QLBL" header="共有比例" width="3%"/>
            <@p.field fieldName="SQRID" header="操作" width="7%" renderer="glSqrCz"  />
        </@p.list>
        <@f.table id="glsqr-grid-table"></@f.table>
        <div id="glsqr-grid-pager"></div>
    </section>
    </@f.contentDiv>
    <@p.listDiv>
    <section id="qlrxx">
        <div class="row">
            <div class=" col-xs-2 demonstrative">权利人信息</div>
        </div>
        <@p.list tableId="qlr-grid-table" pageId="qlr-grid-pager" keyField="QLRID" dataUrl="${serverUrl}/bdcdjQlxx/getQlrxxPagesJson?proid=${proid!}" multiboxonly="false"multiselect="false">
            <@p.field fieldName="SXH" header="序号" width="2%"/>
            <@p.field fieldName="QLRMC" header="权利人名称" width="5%"/>
            <@p.field fieldName="QLRZJH" header="证件号" width="5%"/>
            <@p.field fieldName="QLRSFZJZL" header="证件类型" width="5%"  renderer="rendererZjlx"/>
            <@p.field fieldName="QLRLX" header="类型" width="5%"  renderer="rendererQlrlx"/>
            <@p.field fieldName="GYFS" header="共有方式" width="5%" renderer="rendererGyfs"/>
            <@p.field fieldName="QLBL" header="共有比例" width="3%"/>
            <@p.field fieldName="QLRID" header="QLRID" hidden="true"/>
            <@p.field fieldName="QLRID" header="操作" width="7%"  renderer="qlrxxCz"/>
        </@p.list>
        <table id="qlr-grid-table"></table>
        <div id="qlr-grid-pager"></div>
    </section>
    </@p.listDiv>
    <@f.hidden id="wiid" value="${wiid!}" />
    <@f.hidden id="proid" value="${proid!}" />
    <@f.hidden id="qlid" value="${qlid!}" />
    <@f.hidden id="qllxdm" value="${qllxdm!}" />
</@com.html>