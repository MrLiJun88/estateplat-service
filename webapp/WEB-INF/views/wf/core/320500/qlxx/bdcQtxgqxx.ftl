<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveQlxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjQlxx/saveQlxx",
            type: 'POST',
            dataType: 'json',
            data: $("#fdcqForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        tipInfo("保存成功");
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }
</script>
    <@f.contentDiv  title="其他相关权利登记信息（取水权、探矿权、采矿权等）">
        <@f.form  id="qtxgqlxxForm">
            <@f.buttons>
                <@f.button id=""   handler="saveQlxx" text="保存" ></@f.button>
            </@f.buttons>
            <@f.table >
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="权利类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qllx" name="qllx" value="${qllx!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${ywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlx" name="djlx" value="${djlx!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td>
                        <@f.label name="权利人"></@f.label>
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
                    <@f.td>
                        <@f.text id="qlr"name="qlr"value=""></@f.text>
                    </@f.td>
                    <@f.td>
                        <@f.text  id="qlrsfzjzl"name="qlrsfzjzl"value=""></@f.text>
                    </@f.td>
                    <@f.td>
                        <@f.text  id="qlrzjh"name="qlrzjh"value=""></@f.text>
                    </@f.td>
                    <@f.td>
                        <@f.text  id="qlrlx"name="qlrlx"value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="djyy" name="djyy" value="${djyy!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="qlksqx" name="qlksqx" value=""></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk" name="gyqk" value="${gyqk!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="qljsqx" name="qljsqx" value=""></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="取水方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qsfs" name="qsfs" value="${qsfs!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="水源类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sylx" name="sylx" value="${sylx!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="取水量"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qsl" name="qsl" value="${qsl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="勘察面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="kcmj" name="kcmj" value="${kcmj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="取水用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qsyt" name="qsyt" value="${qsyt!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="开采矿种"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="kckz" name="kckz" value="${kckz!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="开采方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="kcfs" name="kcfs" value="${kcfs!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="生产规模"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="scgm" name="scgm" value="${scgm!}"></@f.text>
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
                        <@f.text id="dbr" name="dbr" value="${dbr!}"></@f.text>
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
                        <@f.textarea  name="fj" value="${fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>