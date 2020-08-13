<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveQlrxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjQlrxx/saveQlrxx",
            type: 'POST',
            dataType: 'json',
            data: $("#qlrForm").serialize(),
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
        <@f.form id="qlrForm" name="qlrForm">
            <@f.buttons>
                <@f.button   handler="saveQlrxx" text="保存" ></@f.button>
            </@f.buttons>
            <@f.hidden id="proid" name="proid"  value="${bdcQlr.proid!}"/>
            <@f.hidden id="qlrid"  name="qlrid" value="${bdcQlr.qlrid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrmc" name="qlrmc" value="${bdcQlr.qlrmc!}"></@f.text>
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
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${bdcQlr.qlrsfzjzl!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  id="qlrzjh" name="qlrzjh" value="${bdcQlr.qlrzjh!}"></@f.text>
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
                        <@f.text  id="qlbl" name="qlbl"   value="${bdcQlr.qlbl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="性别"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="xb" defaultValue="男"  valueFieldName="">男</@f.radio>
                        <@f.radio  name="xb" defaultValue="女" valueFieldName="">女</@f.radio>
                    </@f.td>
                    <@f.th>
                        <@f.label name="所属行业"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sshy" name="sshy"value="${bdcQlr.sshy!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="申请人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrlxdh"  name="qlrlxdh" value="${bdcQlr.qlrlxdh!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="权利面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlmj" name="qlmj" value="${bdcQlr.qlmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="通讯地址"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="qlrtxdz" name="qlrtxdz"   value="${bdcQlr.qlrtxdz!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="法定代表人或负责人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrfddbr"   name="qlrfddbr"   value="${bdcQlr.qlrfddbr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代表人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrfddbrdh" name="qlrfddbrdh"   value="${bdcQlr.qlrfddbrdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理人姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrdlr"  name="qlrdlr"   value="${bdcQlr.qlrdlr!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代理人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrdlrdh" name="qlrdlrdh"   value="${bdcQlr.qlrdlrdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理机构名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrdljg" name="qlrdljg"   value="${bdcQlr.qlrdljg!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="其余共有人"></@f.label>
                    </@f.th>
                    <@f.td>
                    <@f.text id="qygyr" name="qygyr" value="${bdcQlr.qygyr!}"/>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</@com.html>