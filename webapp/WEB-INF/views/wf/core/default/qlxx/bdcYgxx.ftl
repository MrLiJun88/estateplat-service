<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcYgxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcYgxx/saveBdcYgxx",
            type: 'POST',
            dataType: 'json',
            data: $("#ygForm").serialize(),
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
        <button id=""  type="button" class="btn btn-info save" onclick="saveBdcYgxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="预告登记信息">
        <@f.form id="ygForm" name="ygForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcYgxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcYg.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh"name="bdcdyh" value="${bdcSpxx.bdcdyh!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl"name="zl" value="${bdcSpxx.zl!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh"name="ywh" value="${bdcYg.ywh!}"/>
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
                                <@f.text id="qlr"name="qlr"value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.text id="qlr_qlrsfzjzl"name="qlr_qlrsfzjzl"value="${qlr.qlrsfzjzl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="qlr_qlrzjh"name="qlr_qlrzjh"value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
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
                        <@f.label name="义务人"></@f.label>
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
                                <@f.text id="ywr"name="ywr"value="${ywr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.text id="ywr_qlrsfzjzl"name="ywr_qlrsfzjzl"value="${ywr.qlrsfzjzl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td colspan="2" style="background:#ffffff;">
                                <@f.text id="ywr_qlrzjh"name="ywr_qlrzjh"value="${ywr.qlrzjh!}" style="text-align:center" ></@f.text>
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
                        <@f.label name="预告登记种类"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ygdjzl"name="ygdjzl" value="${bdcYg.ygdjzl!}"/>
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
                        <@f.label name="土地使用权人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdqlr"name="tdqlr" value="${bdcYg.tdqlr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="规划用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ghyt"name="ghyt" value="${bdcYg.ghyt!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="所在层"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="szc"name="szc" value="${bdcYg.szc!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="总层数"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zcs"name="zcs" value="${bdcYg.zcs!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fwxz"name="fwxz" value="${bdcYg.fwxz!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="取得价格/被担保主债权数额（万元）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qdjg"name="qdjg" value="${bdcYg.qdjg!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="债务履行期限开始日期"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="zwlxksqx"name="zwlxksqx" value="${zwlxksqx!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产登记证明号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh"name="bdcqzh" value="${bdcqzh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="债务履行期限结束日期"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="zwlxjsqx"name="zwlxjsqx" value="${zwlxksqx!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="建筑面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jzmj"name="mj" value="${bdcSpxx.mj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr"name="dbr" value="${bdcYg.dbr!}"/>
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
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  id="fj"name="fj" value="${bdcYg.fj!}">
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