<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcYyxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcYyxx/saveBdcYyxx",
            type: 'POST',
            dataType: 'json',
            data: $("#yyForm").serialize(),
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
                $.Prompt("保存失败",1500);
            }
        });
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button id="" type="button" class="btn btn-info save" onclick="saveBdcYyxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="异议登记信息">
        <@f.form id="yyForm" name="yyForm">
            <#--<@f.buttons>-->
                <#--<@f.button   handler="saveBdcYyxx" text="保存" ></@f.button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcYy.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh"name="bdcdyh" value="${bdcBdcdy.bdcdyh!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh"name="ywh" value="${bdcYy.ywh!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th class="center">
                        <@f.label name="房屋所有权人"></@f.label>
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
                                <@f.text id="qlrxz"name="qlrxz"value="${qlr.qlxz}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="20%" class="color-white">
                                <@f.text  id="qlrsfzjzl"name="qlrsfzjzl"value="${qlr.zjh}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="30%">
                                <@f.text  id="qlrzjh"name="qlrzjh"value="${qlr.qlrsfzjzl}" style="text-align:center" ></@f.text>
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
                        <@f.label name="异议事项"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="yysx"name="yysx" value="${bdcYy.yysx!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产登记证明号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh"name="bdcqzh" value="${bdcqzh!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr"name="dbr" value="${bdcYy.dbr!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="djsj"name="djsj" value="${djsj!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="注销异议业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zxyyh"name="zxyyh" value="${bdcYy.zxyyh!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="注销异议原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zxyyyy"name="zxyyyy" value="${bdcYy.zxyyyy!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="注销登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zxdbr"name="zxdbr" value="${bdcYy.zxdbr!}" />
                    </@f.td>
                    <@f.th>
                        <@f.label name="注销登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="zxsj"name="zxsj" value="${zxsj!}" />
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  name="fj" id="fj" value="${bdcYy.fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>