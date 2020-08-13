<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveSfxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjQlxx/saveSfxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sfxxForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (data != null && data != "") {
                    if (data.msg == "success") {
                        alert("保存成功");
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });

    }
</script>
<div class="main-container">
    <div class="space-10"></div>
    <@f.contentDiv title="不动产登记收费信息">
        <@f.form id="sfxxForm" name="sfxxForm">
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="proid"  name="proid" value="${proid!}"/>
            <div class="secondTitle">
                <section id="jbxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">
                            收费基本信息：
                        </div>
                        <div class=" col-xs-6  ">
                        </div>
                    </div>
                </section>
            </div>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="执收单位名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zsdwmc" name="zsdwmc" value="${zsdwmc!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="发票号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fph" name="fph" value="${fph!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理银行网点代码"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dlyhwddm" name="dlyhwddm" value="${dlyhwddm!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="收款人账号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="skrzh" name="skrzh" value="${skrzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="开票方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="kpfs" name="kpfs" value="${kpfs!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="收款人开户银行"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="skrkhyh" name="skrkhyh" value="${skrkhyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利人姓名（名称）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrxm" name="qlrxm" value="${qlrxm!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="缴款方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jkfs" name="jkfs" value="${jkfs!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlx" name="djlx" value="${djlx!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdmj" name="tdmj" value="${tdmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${zl!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="建筑面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jzmj" name="jzmj" value="${jzmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="yt" name="yt" value="${yt!}"></@f.text>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@f.contentDiv>
        <section id="sfxx">
            <div class="row">
                <div class="col-xs-2 demonstrative">
                    收费明细列表：
                </div>
                <div class="col-xs-8 ">
                </div>
                <div class="rowLabel col-xs-2">
                </div>
            </div>
            <@p.list tableId="sfxx-grid-table" pageId="sfxx-grid-pager" keyField="QLRID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getSqrxxPagesJson?wiid=${wiid!}">
                <@p.field fieldName="XH" header="序号" width="3%"/>
                <@p.field fieldName="SFXM" header="收费项目" width="5%"/>
                <@p.field fieldName="BZ" header="标准" width="5%"/>
                <@p.field fieldName="SL" header="数量" width="5%"/>
                <@p.field fieldName="DW" header="单位" width="5%"/>
                <@p.field fieldName="JCJE" header="基础金额" width="5%"/>
                <@p.field fieldName="SSJE" header="实收金额" width="5%"/>
                <@p.field fieldName="CZ" header="操作" width="5%"  renderer="sqrxxCz"/>
            </@p.list>
            <table id="sfxx-grid-table"></table>
            <div id="sfxx-grid-pager"></div>
        </section>
    </@f.contentDiv>
</div>
</@com.html>