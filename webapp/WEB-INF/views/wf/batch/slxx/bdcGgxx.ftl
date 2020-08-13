<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveGgxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveGgxx",
            type: 'POST',
            dataType: 'json',
            data: $("#ggxxForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (data != null && data != "") {
                    if (data.msg == "true") {
                        alert("保存成功");
                        var contentFrame=window.parent.document.getElementById("contentFrame");
                        if(contentFrame!=null){
                            var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                            if(frameMain!=null){
                                var contentPane=frameMain.contentWindow;
                                if(contentPane!=null)
                                    contentPane.refreshGrid('ggxx');
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
    function addGgxx() {
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveGgxx",
            type: 'POST',
            dataType: 'json',
            data: $("#ggxxForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (data != null && data != "") {
                    if (data.msg == "true") {
                        alert("保存成功");
                        var contentFrame=window.parent.document.getElementById("contentFrame");
                        if(contentFrame!=null){
                            var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                            if(frameMain!=null){
                                var contentPane=frameMain.contentWindow;
                                if(contentPane!=null)
                                    contentPane.refreshGrid('ggxx');
                            }
                        }
                        document.location.reload();
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }
</script>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
    <@f.contentDiv  title="公告信息">
        <@f.form id="ggxxForm">
            <@f.buttons>
                <@f.button id="saveGgxx" name="saveGgxx"   handler="saveGgxx" text="保存" ></@f.button>
                <#--<@f.button id="addGgxx" name="addGgxx"  handler="saveGgxx" text="增加" ></@f.button>-->
            </@f.buttons>
            <@f.hidden id="proid" name="proid"  value="${bdcGg.proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${bdcGg.wiid!}"/>
            <@f.hidden id="ggid" name="ggid"  value="${ggid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="公告编号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ggbh" name="ggbh" value="${bdcGg.ggbh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="公告类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select name="gglx"   showFieldName="MC" valueFieldName="DM" source="bdcGglxList"defaultValue="${gglx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="公告名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ggmc" name="ggmc" value="${bdcGg.ggmc!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="是否发布"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="sffb" valueFieldName="1"  saveValue="${bdcGg.sffb!}" defaultValue="1">是</@f.radio>
                        <@f.radio name="sffb" valueFieldName="0" saveValue="${bdcGg.sffb!}"  defaultValue="1">否</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="申请人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sqr" name="sqr" value="${bdcGg.sqr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="开始时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="kssj" name="kssj" value="${kssj!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="结束时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="jssj" name="jssj" value="${jssj!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="公告期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ggqx" name="ggqx" value="${bdcGg.ggqx!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="创建人名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cjrmc" name="cjrmc" value="${bdcGg.cjrmc!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="创建时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="cjsj" name="cjsj" value="${cjsj!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="公告内容"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea id="ggnr" name="ggnr" value="${bdcGg.ggnr!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</@com.html>