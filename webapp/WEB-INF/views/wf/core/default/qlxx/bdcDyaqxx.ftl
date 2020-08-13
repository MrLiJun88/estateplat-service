<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    <#--$(document).ready(function () {-->
    <#--var v = "${bdcXm.djsy!}";//这个为保存的值，自己从数据库读取来赋值给v变量-->
    <#--if (v != "" && v != null) {-->
    <#--var arr = v.split(',');-->
    <#--for (var i = 0; i < arr.length; i++) {-->
    <#--// 设置选中项-->
    <#--$("#djsy").find("option[value='"+arr[i]+"']").attr("selected", true);-->
    <#--}-->
    <#--}-->
    <#--$('#djsy').multiselect({-->
    <#--enableFiltering: true,-->
    <#--filterPlaceholder: 'Search...'-->
    <#--});-->
    <#--});-->
    function saveBdcDyaqxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcDyaqxx/saveBdcDyaqxx",
            type: 'POST',
            dataType: 'json',
            data: $("#bdcDyaqxxForm").serialize(),
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
        <button id="saveBdcDyaqxx" type="button" class="btn btn-info save" onclick="saveBdcDyaqxx()">保存</button>
    </div>
</div>
<div class="space-10"></div>
    <@f.contentDiv title="抵押权登记信息">
        <@f.form id="bdcDyaqxxForm" name="bdcDyaqxxForm">
        <#--<@f.buttons>-->
        <#--<@f.button   handler="saveBdcDyaqxx" text="保存"></@f.button>-->
        <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcDyaq.qlid!}"/>
            <@f.table>
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:154px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:140px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:154px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:140px;"></@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djlxMc" name="djlxMc" value="${djlxMc!}" readonly="true"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="申请类型"></@f.label>
                    </@f.th>
                    <@f.td>  ${sqlxMc!}</@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记子项"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.select id="djzx" name="djzx"  showFieldName="MC" valueFieldName="DM" source="djzxList" defaultValue="${bdcXm.djzx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记事由"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="djsy" name="djsy" value="${djsy!}"></@f.text>
                    <#--<@f.select id="djsy" name="djsy"  showFieldName="mc" valueFieldName="dm" source="djsyList" defaultValue="${bdcXm.djsy!}"></@f.select>-->
                    <#--<select id="djsy" name="djsy" multiple="multiple"><#list djsyList as item><option value="${item.DM!}">${item.MC!}</option></#list></select>-->
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td> ${bdcBdcdy.bdcdyh!}</@f.td>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>${bdcSpxx.zl!}</@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th style="text-align:center" >
                        <@f.label name="抵押权人"></@f.label>
                    </@f.th>
                    <@f.th style="text-align:center" >
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th colspan="6" style="text-align:center">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                </@f.tr>
                <#if (bdcQlrList?size > 0)>
                    <#list bdcQlrList as qlr>
                        <@f.tr>
                            <@f.td >
                                <@f.text value="${qlr.qlrmc!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.text value="${qlr.qlrsfzjzl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td colspan="6">
                                <@f.text value="${qlr.qlrzjh!}" style="text-align:center" ></@f.text>
                            </@f.td>
                        </@f.tr>
                    </#list>
                <#else>
                    <@f.tr>
                        <@f.td>
                            <@f.text></@f.text>
                        </@f.td>
                        <@f.td>
                            <@f.text></@f.text>
                        </@f.td>
                        <@f.td colspan="6">
                            <@f.text></@f.text>
                        </@f.td>
                    </@f.tr>
                </#if>
                <@f.tr>
                    <@f.th>
                        <@f.label name="抵押不动产类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="dybdclx" name="dybdclx"  showFieldName="MC" valueFieldName="BM" source="dybdclxList"defaultValue="${bdcBdcdy.bdclx!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="抵押人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcYwr" name="bdcYwr" value="${bdcYwr!}"  readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="抵押方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="dyfs" name="dyfs"  showFieldName="MC" valueFieldName="DM" source="dyfsList"defaultValue="${bdcDyaq.dyfs!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="在建建筑物抵押范围"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zjjzwdyfw" name="zjgcdyfw" value="${bdcDyaq.zjgcdyfw!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="债务履行开始时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="zwlxkssj" name="zwlxksqx" value="${zwlxksqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="被担保主债权数额（最高债权数额）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zqse" name="bdbzzqse" value="${bdcDyaq.bdbzzqse!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="债务履行结束时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="zwlxjssj" name="zwlxjsqx" value="${zwlxjsqx!}"></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产价值"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcjz" name="zgzqqdse" value="${bdcDyaq.zgzqqdse!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="贷款方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="dkfs" name="dkfs"  showFieldName="mc" valueFieldName="dm" source="[{'mc':'公积金','dm':'1'},{'mc':'商业','dm':'2'},{'mc':'组合','dm':'3'},{'mc':'其他','dm':'4'}]"defaultValue="${bdcDyaq.dkfs!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="抵押顺位"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dysw" name="dysw" value="${bdcDyaq.dysw!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="解押抵押面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  name="sydymj" value="${bdcDyaq.sydymj!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="剩余解押金额"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  name="sydyje" value="${bdcDyaq.sydyje!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  name="dbr" value="${bdcDyaq.dbr!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登薄时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  id="djsj"  name="djsj" value="${djsj!}" readonly="true"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="注销登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  name="zxdbr" value="${bdcDyaq.zxdbr!}" readonly="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="注销登薄时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  id="zxsj"  name="zxsj" value="${zxsj!}" readonly="true"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="注销抵押原因"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.textarea name="zxdyyy" id="zxdyyy">${bdcDyaq.zxdyyy!}</@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.textarea  name="fj" id="fj">${bdcDyaq.fj!}</@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>