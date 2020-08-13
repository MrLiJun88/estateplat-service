<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcCfxx(){
        $.blockUI({message:"请稍等......"});
        $.ajax({
            url:"${bdcdjUrl}/bdcCfxx/saveBdcCfxx",
            type:'POST',
            dataType:'json',
            data:$("#cfForm").serialize(),
            success:function(data){
                setTimeout($.unblockUI,10);
                if(isNotBlank(data)){
                    if(data.msg=="success"){
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
            error:function(data){
                alert("保存失败");
            }
        });
    }
    $(function () {
        //查封开始日期自动加三年
        $("input[id='cfksqx']").change(function () {
            var cfksqx=$("#cfksqx").val();
            var rq = new Date(cfksqx);
            var year=parseInt(rq.getFullYear())+3;
            if(cfksqx!=null&&cfksqx!=undefined&&cfksqx!=""){
                var cfjsrq=cfksqx.replace(cfksqx.substring(0,4),year);
                $("#cfjsqx").val(cfjsrq);
            }
        });
    });
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button id="saveBdcCfxx" name="saveBdcCfxx" type="button" class="btn btn-info save" onclick="saveBdcCfxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="查封登记信息">
        <@f.form id="cfForm" name="cfForm">
            <#--<@f.buttons>-->
                <#--<@f.button   handler="saveBdcCfxx" text="保存" ></@f.button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="qlid" name="qlid" value="${bdcCf.qlid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}" readonly="true"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcCf.ywh!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="查封类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="cflx" name="cflx"  showFieldName="MC" valueFieldName="DM" source="cflxList"defaultValue="${bdcCf.cflx!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="查封文件"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cfwj" name="cfwj" value="${bdcCf.cfwj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="查封文号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cfwh" name="cfwh" value="${bdcCf.cfwh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="查封机关"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cfjg" name="cfjg" value="${bdcCf.cfjg!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="查封申请人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cfsqr" name="cfsqr" value="${bdcCf.cfsqr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="被查封权利人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bcfqlr" name="bcfqlr" value="${bcfQlr!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="查封开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="cfksqx" name="cfksqx" value="${cfksqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="查封范围"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cffw" name="cffw" value="${bdcCf.cffw!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="查封结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="cfjsqx" name="cfjsqx" value="${cfjsqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="轮候查封期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lhcfqx" name="lhcfqx" value="${bdcCf.lhcfqx!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="法院送达人姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fysdr" name="fysdr" value="${bdcCf.fysdr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="法院送达人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fysdrlxfs" name="fysdrlxfs" value="${bdcCf.fysdrlxfs!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr" name="dbr" value="${bdcCf.dbr!}" readonly="true"></@f.text>
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
                        <@f.label name="解封业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfywh" name="jfywh" value="${bdcCf.jfywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="解封文件"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfwj" name="jfwj" value="${bdcCf.jfwj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="解封机关"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfjg" name="jfjg" value="${bdcCf.jfjg!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="解封文号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfwh" name="jfwh" value="${bdcCf.jfwh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="解封登簿人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfdbr" name="jfdbr" value="${bdcCf.jfdbr!}" readonly="true"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="解封登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="jfdjsj" name="jfdjsj" value="${jfdjsj!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea id="fj" name="fj" value=" ${bdcCf.fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>