<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveTdsyq() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcTdsyqxx/saveBdcTdsyqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#tdsyqForm").serialize(),
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
        <button id=""  type="button" class="btn btn-info save" onclick="saveBdcTdsyqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="土地所有权登记信息">
        <@f.form id="tdsyqForm" name="tdsyqForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcTdsyqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcTdsyq.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh"name="bdcdyh" value="${bdcSpxx.bdcdyh!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="单位"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select  id="dw" name="mjdw"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'平方米','dm':'1'},{'mc':'亩','dm':'2'},{'mc':'公顷','dm':'3'}]" defaultValue="${mjdw!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh"name="ywh" value="${ywh!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlxMc" name="djlxMc" value="${djlxMc!}" readonly="true"></@f.text>
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
                                <@f.text id="qlr"name="qlr"value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.select id="qlrsfzjzl" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${qlr.qlrsfzjzl!}"/>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="qlrzjh"name="qlrzjh"value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
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
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk"name="gyqk" value="${bdcTdsyq.gyqk!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djyy"name="djyy" value="${bdcXm.djyy!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="农用地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="nydmj"name="nydmj" value="${bdcTdsyq.nydmj!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="耕地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gdmj"name="gdmj" value="${bdcTdsyq.gdmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="建设用地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jsydmj"name="jsydmj" value="${bdcTdsyq.jsydmj!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="林地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ldmj"name="ldmj" value="${bdcTdsyq.ldmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="未利用地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="wlydmj"name="wlydmj" value="${bdcTdsyq.wlydmj!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="草地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cdmj"name="cdmj" value="${bdcTdsyq.cdmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh"name="bdcqzh" value="${bdcqzsh!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="其它面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qtmj"name="qtmj" value="${bdcTdsyq.qtmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr"name="dbr" value="${bdcTdsyq.dbr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登簿时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="djsj"name="djsj" value="${djsj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  id="fj"name="fj" value="${bdcTdsyq.fj!}">
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