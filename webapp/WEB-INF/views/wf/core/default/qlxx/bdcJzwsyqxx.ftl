<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcJzwsyqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcJzwsyqxx/saveBdcJzwsyqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#jzwsyqForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        tipInfo("保存成功");
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
        <button id="saveBdcJzwsyqxx"   type="button" class="btn btn-info save" onclick="saveBdcJzwsyqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="构（建）筑物所有权登记信息">
        <@f.form  id="jzwsyqForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcJzwsyqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcJzwsyq.ywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList"defaultValue="${bdcXm.djlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th class="center">
                        <@f.label name="构（建）筑物所有权人"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="权利性质"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <#if (bdcQlrList?size > 0)>
                        <#list bdcQlrList as qlr>
                            <@f.td width="20%" class="color-white" >
                                <@f.text id="qlr"name="qlr"value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="30%">
                                <@f.text id="qlrsfzjzl"name="qlrsfzjzl"value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="20%" class="color-white">
                                <@f.text  id="qlrzjh"name="qlrzjh"value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="30%">
                                <@f.text  id="qlrxz"name="qlrxz"value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                        </#list>
                    <#else>
                        <@f.td width="20%" class="color-white">
                            <@f.text ></@f.text>
                        </@f.td>
                        <@f.td width="30%">
                            <@f.text ></@f.text>
                        </@f.td>
                        <@f.td width="20%" class="color-white">
                            <@f.text ></@f.text>
                        </@f.td>
                        <@f.td width="30%">
                            <@f.text ></@f.text>
                        </@f.td>
                    </#if>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="构（建）筑物共有情况"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="gyqk" name="gyqk" value="${bdcJzwsyq.gyqk!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地/海域使用权人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdhysyqr" name="tdhysyqr" value="${bdcJzwsyq.tdhysyqr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="土地/海域使用面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdhysymj" name="tdhysymj" value="${bdcJzwsyq.tdhysymj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地/海域使用开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syksqx" name="syksqx" value="${syksqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="构（建）筑物类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="gzwlx" name="gzwlx"  showFieldName="MC" valueFieldName="DM" source="djlxList"defaultValue="${bdcJzwsyq.gzwlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地/海域使用结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syjsqx" name="syjsqx" value="${syjsqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="构（建）筑物规划用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="jzwghyt" name="dzwyt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcJzwsyq.dzwyt!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="构（建）筑物面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gzwmj" name="gzwmj" value="${bdcJzwsyq.gzwmj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="竣工时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="jgsj" name="jgsj" value="${jgsj!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh" name="bdcqzh" value="${bdcqzh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djyy" name="djyy" value="${bdcXm.djyy!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr" name="dbr" value="${bdcJzwsyq.dbr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djsj" name="djsj" value="${djsj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" >
                        <@f.textarea  name="fj" id="fj" value="${bdcJzwsyq.fj!}">
                        </@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>