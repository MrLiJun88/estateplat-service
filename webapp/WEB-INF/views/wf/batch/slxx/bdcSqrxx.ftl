<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveSlxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveSlxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sqrxxForm").serialize(),
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
    <@f.contentDiv  title="申请人信息">
        <@f.form id="sqrxxForm" name="sqrxxForm">
            <@f.buttons>
                <@f.button id=""   handler="saveSlxx" text="保存" ></@f.button>
            </@f.buttons>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="xm" value="${xm!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="是否持证人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="sfczr" defaultValue="1"  valueFieldName="${bdcQlr.sfczr!}">是</@f.radio>
                        <@f.radio name="sfczr" defaultValue="0" valueFieldName="${bdcQlr.sfczr!}">否</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="身份证件种类"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${bdcQlr.qlrsfzjzl!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="zjh" value="${zjh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="共有方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select name="gyfs"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'单独所有','dm':'0'},{'mc':'共同共有','dm':'1'},{'mc':'按份共有','dm':'2'},{'mc':'其他共有','dm':'3'}]"defaultValue="${bdcQlr.qlrsfzjzl!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有比例"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="gybl" value="${gybl!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="通讯地址"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="txdz" value="${txdz!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="申请人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="sqrlxdh" value="${sqrlxdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="法定代表人或负责人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="fddbrhfzr" value="${fddbrhfzr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代表人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="dbrlxdh" value="${dbrlxdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理人姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="dlrxm" value="${dlrxm!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代理人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="dlrlxdh" value="${dlrlxdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理机构名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="dljgmc" value="${dljgmc!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="其余共有人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="qygyr" value="${qygyr!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="4">
                        <@f.textarea></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@f.hidden id="wiid" value="${wiid!}"/>
    <@f.hidden id="proid" value="${proid!}"/>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
</@com.html>