<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">

    function saveTdcb() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcTdcbnydsyqxx/saveBdcTdcbnydsyqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#tdcbForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("保存成功");
                        var contentFrame=window.parent.document.getElementById("contentFrame");
                        if(contentFrame!=null){
                            var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                            if(frameMain!=null){
                                var contentPane=frameMain.contentWindow;
                                if(contentPane!=null)
                                    contentPane.refreshGrid('qlxx');
                            }
                        }
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button id=""  type="button" class="btn btn-info save" onclick="saveBdcTdcbnydsyqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="土地承包经营权、农用地的其他使用权登记信息（非林地）">
        <@f.form id="tdcbForm" name="tdcbForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcTdcbnydsyqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcTdcbnydsyq.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh"name="bdcdyh" value="${bdcSpxx.bdcdyh!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="发包方"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlr"name="fbfmc" value="${bdcTdcbnydsyq.fbfmc!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh"name="ywh" value="${bdcTdcbnydsyq.ywh!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList"defaultValue="${bdcXm.djlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th style="text-align:center; background: #f9f9f9;" >
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.th style="text-align:center; background: #f9f9f9;" >
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th colspan="2" style="text-align:center; background: #f9f9f9;">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <#if (bdcQlrList?size > 0)>
                        <#list bdcQlrList as qlr>
                            <@f.td style="background:#ffffff;" >
                                <@f.text id="qlrmc"name="qlrmc"value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.select id="qlrsfzjzl" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${qlr.qlrsfzjzl!}"/>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="qlrzjh" name="qlrzjh"value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
                            </@f.td>
                        </#list>
                    <#else>
                        <@f.td style="background:#ffffff;">
                            <@f.text></@f.text>
                        </@f.td>
                        <@f.td>
                            <@f.text></@f.text>
                        </@f.td>
                        <@f.td colspan="2" style="background:#ffffff;">
                            <@f.text></@f.text>
                        </@f.td>
                    </#if>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djyy"name="djyy" value="${bdcXm.djyy!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk"name="gyqk" value="${bdcTdcbnydsyq.gyqk!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="承包（使用）开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syksqx"name="syksqx" value="${syksqx!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="承包（使用权）面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syqmj"name="syqmj" value="${bdcTdcbnydsyq.syqmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="承包（使用）结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syjsqx"name="syjsqx" value="${syjsqx!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="土地所有权性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdsyqmj"name="tdsyqxz" value="${bdcTdcbnydsyq.tdsyqxz!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="水域滩涂类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syttlx"name="syttlx" value="${bdcTdcbnydsyq.syttlx!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="养殖方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="yzyfs"name="yzyfs" value="${bdcTdcbnydsyq.yzyfs!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="草原质量"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cyzl"name="cyzl" value="${bdcTdcbnydsyq.cyzl!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="适宜载畜量"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syzcl"name="syzcl" value="${bdcTdcbnydsyq.syzcl!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr"name="dbr" value="${bdcTdcbnydsyq.dbr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="djsj"name="djsj" value="${djsj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="bdcqzh"name="bdcqzh" value="${bdcqzh!}"  readonly="true"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  id="fj"name="fj" value="${bdcTdcbnydsyq.fj!}">
                        </@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
    <@f.hidden id="wiid" value="${wiid!}"/>
    <@f.hidden id="proid" value="${proid!}"/>
</@com.html>