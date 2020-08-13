<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function reSaveZsArbitrary() {
        $.ajax({
            type: 'post',
            async: false,
            url: '${bdcdjUrl!}/wfProject/reSaveZsArbitrary',
            data: $("#bdcqzForm").serialize(),
            success: function (data) {
                if (data.msg == "success") {
                    alert("生成证书成功！");
                    window.parent.hideModel();
                    var contentFrame=window.parent.document.getElementById("contentFrame");
                    if(contentFrame!=null){
                        var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                        if(frameMain!=null){
                            var contentPane=frameMain.contentWindow;
                            if(contentPane!=null)
                                contentPane.refreshGrid('zs');
                        }
                    }
                } else {
                    alert("生成证书失败！");
                }
            }
        });
    }
</script>
    <@f.contentDiv  title="生成证书">
        <@f.form  id="bdcqzForm" >
            <@f.buttons>
                <@f.button   handler="reSaveZsArbitrary" text="生成证书" ></@f.button>
            </@f.buttons>
            <@f.table >
                <@f.tr>
                    <@f.th>
                        <@f.label name="所有权"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="bdcqzFlag" defaultValue="1"  valueFieldName="1"> 一证多房</@f.radio>
                        <@f.radio name="bdcqzFlag" defaultValue="1"  valueFieldName="0">一证一房</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="抵押权"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="bdcqzmFlag" defaultValue="1"  valueFieldName="1"> 多抵一</@f.radio>
                        <@f.radio name="bdcqzmFlag" defaultValue="1"  valueFieldName="0">多抵多</@f.radio>
                    </@f.td>
                </@f.tr>
            </@f.table>
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
        </@f.form>
    </@f.contentDiv>
</@com.html>