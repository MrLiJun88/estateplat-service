<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@script name="static/js/wf.js"></@script>
<script type="text/javascript">
    function saveSjclxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveSjclxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sjclxxForm").serialize(),
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
                                    contentPane.refreshGrid('sjclxx');
                            }
                        }
                        window.returnValue="1";
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
    function addSjclxx() {
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveSjclxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sjclxxForm").serialize(),
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
                                    contentPane.refreshGrid('sjclxx');
                            }
                        }

                        if(top.window.parent.document) {
                            //加入一个调用收件单父页面的保存功能
                            var  sjdxxSave=contentPane.document.getElementById("sjdxxSaveButton");
                            if(sjdxxSave!=null){
                                sjdxxSave.onclick();
                            }
                            top.window.parent.document.location.reload();//刷新父页面，并关闭子页面。
                        }
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }
</script>
    <@f.contentDiv  title="收件材料信息">
        <@f.form  id="sjclxxForm" >
            <@f.buttons>
                <@f.button   handler="saveSjclxx" text="保存" ></@f.button>
                <@f.button   handler="addSjclxx" text="增加" ></@f.button>
            </@f.buttons>
            <@f.table >
                <@f.tr>
                    <@f.th>
                        <@f.label name="材料名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="clmc" value="${clmc!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="cllx" name="cllx"  showFieldName="mc" valueFieldName="dm" source="[{'mc':'原件','dm':'1'},{'mc':'复印件','dm':'2'}]"defaultValue="${cllx!}" noEmptyValue="true"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="份数"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="fs" value="${fs!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="页数"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text name="ys" value="${ys!}"/>
                    </@f.td>
                </@f.tr>
                <#--<@f.tr>-->
                    <#--<@f.td>-->
                        <#--<@f.textarea></@f.textarea>-->
                    <#--</@f.td>-->
                <#--</@f.tr>-->
            </@f.table>
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="sjxxid" name="sjxxid"  value="${sjxxid!}"/>
            <@f.hidden id="sjclid" name="sjclid"  value="${sjclid!}"/>
        </@f.form>
    </@f.contentDiv>
</@com.html>