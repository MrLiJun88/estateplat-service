<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function rendererQlrlx(cellvalue, options, rowObject) {
        var value = cellvalue;
        if (value == "qlr") {
            value = "权利人";
        } else {
            value = "义务人";
        }
        return value;
    }
    function qllxCz(cellvalue, options, rowObject) {
        return '<div >' +
                '<div><button type="button"  class="btn btn-primary" onclick="delQlrxx(\'' + cellvalue + '\')">关联</button></div>' +
                '</div>'
    }
</script>
<div class="container">
    <div class="row">
        <div class="space9">
            <@f.contentDiv title="关联申请人">
                <section id="glsqr">
                    <@p.list tableId="glsqr-grid-table" pageId="glsqr-grid-pager" keyField="QLID" dataUrl="${bdcdjUrl}/bdcdjSqrxx/getSqrxxPagesJson?wiid=${wiid!}">
                        <@p.field fieldName="XH" header="序号" width="2%"/>
                        <@p.field fieldName="SQRMC" header="申请人名称" width="5%"/>
                        <@p.field fieldName="SQRZJH" header="申请人证件号" width="5%"/>
                        <@p.field fieldName="SQZJLX" header="申请证件类型" width="5%"/>
                        <@p.field fieldName="LX" header="类型" width="5%"   renderer="rendererQlrlx"/>
                        <@p.field fieldName="GYFS" header="共有方式" width="5%"/>
                        <@p.field fieldName="GYBL" header="共有比例" width="5%"/>
                        <@p.field fieldName="CZ" header="操作" width="5%" renderer="qllxCz"  />
                    </@p.list>
                    <@f.table id="glsqr-grid-table"></@f.table>
                    <div id="glsqr-grid-pager"></div>
                </section>
            </@f.contentDiv>
        </div>
    </div>
</div>
    <@f.hidden id="wiid" value="${wiid!}" />
    <@f.hidden id="proid" value="${proid!}" />
</@com.html>