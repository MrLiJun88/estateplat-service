<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcFdcqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcFdcqxx/saveBdcFdcqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#fdcqForm").serialize(),
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
    $(function () {
        $("input[id='tdsyksqx']").change(function () {
            var proid=$("#proid").val();
            var wiid=$("#wiid").val();
            var tdsyksqx=$("#tdsyksqx").val();
            $.ajax({
                type: 'post',
                url: '/estateplat-server/bdcdjQlxx/getTdsyjsqx',
                data: {proid: proid, wiid: wiid,tdsyksqx:tdsyksqx},
                success: function (data) {
                    if(data.tdsyjsqx!=""&&data.tdsyjsqx!=null&&data.tdsyjsqx!=undefined){
                        $("#tdsyjsqx").val(data.tdsyjsqx);
                    }
                }
            });
        });
    });
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button i type="button" class="btn btn-info save" onclick="saveBdcFdcqxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="房地产权信息（独幢、层、套、间房屋）">
        <@f.form id="fdcqForm" name="fdcqForm">
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcFdcq.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房地坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sjdywh" name="ywh" value="${bdcXm.bh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlx" name="djlx" value="${djlxMc!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th class="center">
                        <@f.label name="房屋所有权人"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="权利人性质"></@f.label>
                    </@f.th>
                </@f.tr>
                    <#if (bdcQlrList?size > 0)>
                        <#list bdcQlrList as qlr>
                            <@f.tr>
                            <@f.td width="154px">
                                <@f.text id="qlr"name="qlr"value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td class="color-white" width="154px">
                                <@f.select style="text-align:center" id="qlrsfzjzl" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${qlr.qlrsfzjzl!}"/>
                            </@f.td>
                            <@f.td width="140px">
                                <@f.text  id="qlrzjh" name="qlrzjh"value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td width="140px">
                                <@f.text id="qlrxz"name="qlrxz"value="${qlr.qlrxz!}" style="text-align:center" ></@f.text>
                            </@f.td>
                        </@f.tr>
                        </#list>
                    <#else>
                        <@f.tr>
                            <@f.td width="154px" class="color-white">
                                <@f.text ></@f.text>
                            </@f.td>
                            <@f.td width="154px" class="color-white">
                                <@f.text ></@f.text>
                            </@f.td>
                            <@f.td width="140px">
                                <@f.text ></@f.text>
                            </@f.td>
                            <@f.td width="140px">
                                <@f.text ></@f.text>
                            </@f.td>
                        </@f.tr>
                    </#if>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋编号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fwdah" name="fwdah" value="${bdcFdcq.fwdah!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="房屋类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="fwlx" name="fwlx"  showFieldName="MC" valueFieldName="DM" source="fwlxList"defaultValue="${bdcFdcq.fwlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="fwxz" name="fwxz"  showFieldName="MC" valueFieldName="DM" source="fwxzList"defaultValue="${bdcFdcq.fwxz!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="产权来源"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cqly" name="cqly" value="${bdcFdcq.cqly!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="另有临时用地"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lsyd" name="lylsyd" value="${bdcFdcq.lylsyd!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="土地使用权人"></@f.label>
                    </@f.th>
                    <@f.td style="text-align:right">
                        <@f.text id="tdsyqr" name="tdsyqr" value="${bdcFdcq.tdsyqr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="独用土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dytdmj" name="dytdmj" value="${bdcFdcq.dytdmj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="房屋结构"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="fwjg" name="fwjg"  showFieldName="MC" valueFieldName="DM" source="fwjgList"defaultValue="${bdcFdcq.fwjg!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="分摊土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fttdmj" name="fttdmj" value="${bdcFdcq.fttdmj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="规划用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="ghyt" name="ghyt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcFdcq.ghyt!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="所在层"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="szc" name="szc" value="${bdcFdcq.szc!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="总层数"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zcs" name="zcs" value="${bdcFdcq.zcs!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地使用开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="tdsyksqx" name="tdsyksqx" value="${tdsyksqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="建筑面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jzmj" name="jzmj" value="${bdcFdcq.jzmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地使用结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="tdsyjsqx" name="tdsyjsqx" value="${tdsyjsqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="专有建筑面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tnjzmj" name="tnjzmj" value="${bdcFdcq.tnjzmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房地产交易价格"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jyjg" name="jyjg" value="${bdcFdcq.jyjg!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="分摊建筑面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ftjzmj" name="ftjzmj" value="${bdcFdcq.ftjzmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="竣工时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="jgsj" name="jgsj" value="${jgsj!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dnr" name="dbr" value="${bdcFdcq.dbr!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产权证号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh" name="bdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djsj" name="djsj" value="${djsj!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="djyy" name="djyy" value="${bdcXm.djyy!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋共有情况"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="gyqk" name="gyqk" value="${bdcFdcq.gyqk!}" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea name="fj" id="fj" value=" ${bdcFdcq.fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>