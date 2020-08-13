<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcHysyqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdchysyqxx/saveBdcHysyqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#hysyqForm").serialize(),
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
        <button id="" type="button" class="btn btn-info save" onclick="saveBdcHysyqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="海域（含无居民海岛）使用权登记信息">
        <@f.form id="hysyqForm">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcHysyqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcHysyq.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcHysyq.ywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk" name="gyqk" value="${bdcHysyq.gyqk!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList"defaultValue="${bdcXm.djlx!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djyy" name="djyy" value="${bdcXm.djyy!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th class="center">
                        <@f.label name="权利人"></@f.label>
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
                                <@f.text value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="30%">
                                <@f.text value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="20%" class="color-white">
                                <@f.text  value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="30%">
                                <@f.text  value="${qlr.qlrmc}" style="text-align:center" ></@f.text>
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
                        <@f.label name="使用开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syksqx" name="syksqx" value="${syksqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="使用权面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syqmj" name="syqmj" value="${bdcHysyq.syqmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="使用结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syjsqx" name="syjsqx" value="${syjsqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="使用总金额"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syzje" name="syzje" value="${bdcHysyq.syzje!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="使用金标准依据"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="syjyj" name="syjyj" value="${bdcHysyq.syjyj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="使用金缴纳情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syjjnqk" name="syjjnqk" value="${bdcHysyq.syjjnqk!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh" name="bdcqzh" value="${bdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr" name="dbr" value="${bdcHysyq.dbr!}"></@f.text>
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
                    <@f.td colspan="3">
                        <@f.textarea  name="fj" value="${bdcHysyq.fj!}">
                        </@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>