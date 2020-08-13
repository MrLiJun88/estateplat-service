<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcLqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcLqxx/saveBdcLqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#lqForm").serialize(),
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
        <button id="saveBdcLqxx" type="button" class="btn btn-info save" onclick="saveBdcLqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="林权登记信息">
        <@f.form  id="lqForm" method="post">
            <#--<@f.buttons>-->
            <#--<button type="button" class="btn btn-primary save" onclick="saveBdcLqxx()">保存</button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcLq.qlid!}"/>
            <@f.table >
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="发包方"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fbfmc" name="fbfmc" value="${bdcLq.fbfmc!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcLq.ywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlxMc" name="djlxMc" value="${djlxMc!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td>
                        <@f.label name="林地权利人"></@f.label>
                    </@f.td>
                    <@f.td>
                        <@f.label name="证件种类"></@f.label>
                    </@f.td>
                    <@f.td>
                        <@f.label name="证件号"></@f.label>
                    </@f.td>
                    <@f.td>
                        <@f.label name="权利人性质"></@f.label>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <#if (bdcQlrList?size > 0)>
                        <#list bdcQlrList as qlr>
                    <@f.td>
                        <@f.text id="qlr"name="qlr" value="${qlr.qlrmc!}"></@f.text>
                    </@f.td>
                    <@f.td>
                        <@f.select id="qlrsfzjzl" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${qlr.qlrsfzjzl!}"/>
                    </@f.td>
                    <@f.td>
                        <@f.text id="qlrzjh"name="qlrzjh" value="${qlr.qlrzjh!}"></@f.text>
                    </@f.td>
                    <@f.td>
                        <@f.text id="qlrxz"name="qlrxz" value="${qlr.qlrxz!}"></@f.text>
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
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="djyy" name="djyy" value="${bdcLq.djyy!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="林地共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyfs" name="ldgyqk" value="${bdcLq.ldgyqk!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="使用权（承包）面积（亩）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="syqmj" name="syqmj" value="${bdcLq.syqmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="林权使用开始时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="ldsyksqx" name="ldsyksqx" value="${ldsyksqx!}"></@f.date>
                        <#--<@f.text id="lqsykssj" name="lqsykssj" value="${lqsykssj!}"></@f.text>-->
                    </@f.td>
                    <@f.th>
                        <@f.label name="林地所有权性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ldsyqxz" name="ldsyqxz" value="${ldsyqxz!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="林权使用结束时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="ldsyjsqx" name="ldsyjsqx" value="${ldsyjsqx!}"></@f.date>
                        <#--<@f.text id="lqsyjssj" name="lqsyjssj" value="${lqsyjssj!}"></@f.text>-->
                    </@f.td>
                    <@f.th>
                        <@f.label name="森林、林木所有权人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="slsyqr" name="lmsuqr" value="${bdcLq.lmsuqr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="主要树种"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zysz" name="zysz" value="${bdcLq.zysz!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="森林、林木使用权人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lmsyqr" name="lmsyqr" value="${bdcLq.lmsyqr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="株数"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zs" name="zs" value="${bdcLq.zs!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="林种"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lz" name="lz" value="${bdcLq.lz!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="起源"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qy" name="qy" value="${bdcLq.qy!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="造林年度"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zlnd" name="zlnd" value="${bdcLq.zlnd!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="小地名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="xdm" name="xdm" value="${bdcLq.xdm!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="林班"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lb" name="lb" value="${bdcLq.lb!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="小班"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="xb" name="xb" value="${bdcLq.xb!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzsh" name="bdcqzsh" value="${bdcqzsh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr" name="dbr" value="${bdcLq.dbr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登薄时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="djsj" name="dbsj" value="${dbsj!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利其他状况"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  name="qlqtzk" value="${qlqtzk!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  name="fj" value="${fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>