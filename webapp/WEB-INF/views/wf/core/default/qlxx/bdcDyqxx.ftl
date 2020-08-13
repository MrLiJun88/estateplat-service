<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveDyqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcDyqxx/saveDyqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#dyqxxForm").serialize(),
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
        <button id="" type="button" class="btn btn-info save" onclick="saveDyqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="地役权登记信息">
        <@f.form  id="dyqxxForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveDyqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcDyq.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号（供役地）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="xydbdcdyh" value="${bdcSpxx.bdcdyh!}" readonly="true"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="需役地坐落" ></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcDyq.ywh!}" readonly="true"></@f.text>
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
                        <@f.label name="地役权人（需地役权人）"></@f.label>
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
                                <@f.text id="dyqr_qlr"name="dyqr_qlr"value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.text  id="dyqr_qlrsfzjzl"name="dyqr_qlrsfzjzl"value="${qlr.qlrsfzjzl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="dyqr_qlrzjh"name="dyqr_qlrzjh"value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
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
                    <@f.th style="text-align:center; background: #f9f9f9;" >
                        <@f.label name="供地役权人"></@f.label>
                    </@f.th>
                    <@f.th style="text-align:center; background: #f9f9f9;" >
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th colspan="2" style="text-align:center; background: #f9f9f9;">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <#if (bdcYwrList?size > 0)>
                        <#list bdcYwrList as ywr>
                            <@f.td style="background:#ffffff;" >
                                <@f.text id="gyd_qlr"name="gyd_qlr"value="${ywr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.text id="gyd_qlrsfzjzl"name="gyd_qlrsfzjzl"value="${ywr.qlrsfzjzl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="gyd_qlrzjh"name="gyd_qlrzjh"value="${ywr.qlrzjh!}" style="text-align:center" ></@f.text>
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
                        <@f.label name="地役权开始时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="dyqksqx"name="dyqksqx" value="${dyqksqx!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr"name="dbr" value="${bdcDyq.dbr!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="地役权结束时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="dyqjsqx"name="dyqjsqx" value="${dyqjsqx!}"/>
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
                        <@f.label name="不动产登记证明号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh"name="bdcqzh" value="${bdcdjzmh!}"  readonly="true"/>
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
                        <@f.label name="地役权内容"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="dyqnr"name="dyqnr" value="${bdcDyq.dyqnr!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  id="fj"name="fj" value="${bdcDyq.fj!}">
                        </@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>