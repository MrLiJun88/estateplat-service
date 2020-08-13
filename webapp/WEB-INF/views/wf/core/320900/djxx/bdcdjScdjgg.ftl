<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->
<#--<script src="../js/sjd.js"></script>-->
<#--<#assign path="${request.getContextPath()}">-->
<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}";
    var analysisUrl = "${analysisUrl!}";
    var taskid = "${taskid!}";
    var from = "${from!}";
    var rid = "${rid!}";
    e = 1;
    function saveGg() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjGg/saveGg",
            type: 'POST',
            dataType: 'json',
            data: $("#ggForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("保存成功!");
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });
    }
    function printGg() {
        var proid = $('#proid').val();
        var wiid = $('#wiid').val();
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_scdjgg.cpt&proid=" + proid+'&wiid='+wiid;
        openWin(url);
    }
</script>

<style type="text/css">
    span {
        border-bottom: 1px solid black;
    }

    .toparea {
        width: 1300px;
        position: relative;
        text-align: left;
    }

    .bottomarea {
        width: 1300px;
        text-align: right;
        position: relative;
        margin-top: 20px;
    }

    .bottomarea textarea {
        border-bottom: 1px solid black;
    }

    .toparea .cldz {
        position: relative;
        text-align: center;
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .toparea .info {
        margin-top: 5px;
        margin-bottom: 10px;
    }

    .toparea .bh {
        position: relative;
        margin-top: 40px;
        text-align: right;
    }

    .formBody {
        width: 1300px;
    }

    #clsddz{
        position: relative;
        border-bottom: 1px solid black;
        width: 300px;
    }


</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveGg()">保存</button>
        <button type="button" class="btn btn-info save" onclick="printGg()">打印</button>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="${bdcGg.ggmc!}" >
        <@f.form id="ggForm" name="ggForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="userid" name="userid"  value="${userid!}"/>
            <@f.hidden id="ggid" name="ggid"  value="${bdcGg.ggid!}"/>
            <@f.hidden id="ggbh" name="ggbh"  value="${bdcGg.ggbh!}"/>
            <@f.hidden id="ggdw" name="ggdw"  value="${bdcGg.ggdw!}"/>
            <div class="toparea">
                <div class="bh"><p>编号：<span id="bh">${bdcGg.ggbh!}</span></p></div>
                <div class="info">
                    <p> 经初步审定，我机构拟对下列不动产权利予以首次登记，根据《不动产登记暂行条例实施细则》第十七条的规定，现予公告。有异议者请自本公告之日起十五个工作日内（${date!}
                        之前）将异议书面材料送达我机构，逾期我机构将予以登记。
                    </p>
                </div>
                <div class="cldz">
                    <div class="cldz"><p>异议书面材料送达地址:<span><input id="clsddz" name="clsddz" value="${bdcGg.clsddz!}"/></span></p></div>
                </div>
            </div>
            <@f.table style="width:1300px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:200px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                </@f.tr>
                <@f.tr>

                    <@f.th colspan="1" >
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="不动产权利类型"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="不动产坐落(名称)"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="宗地/宗海号"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="不动产面积(㎡)"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="用途"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="公告名称"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="开始时间"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="结束时间"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="公告期限"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                </@f.tr>
                <tbody id="bdcqk">
                    <#list resultList as value>
                        <@f.tr>

                        <@f.td colspan="1" >
                            <#list value.bdcQlrList as qlr>
                                <@f.hidden id="qlrid_${qlr.qlrid}" name="qlrid"  value="${qlr.qlrid!}"/>
                                <@f.text  id="qlrmc_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" style="text-align:center"></@f.text>
                            </#list>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.select  id="qllx_${value.bdcXm.proid!}" name="qllx"   showFieldName="mc" valueFieldName="dm" source="qllxList"  defaultValue="${value.bdcXm.qllx!}" noEmptyValue="true" ></@f.select>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="zl_${value.bdcSpxx.spxxid!}" name="zl" value="${value.bdcSpxx.zl!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="zdzhh_${value.bdcSpxx.spxxid!}" name="zdzhh" value="${value.bdcSpxx.bdcdyh!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="mj_${value.bdcSpxx.spxxid!}" name="mj" value="${value.bdcSpxx.mj!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.select  id="dzwyt_${value.bdcdyh!}" name="dzwyt"  showFieldName="mc" valueFieldName="dm" source="fwytList" defaultValue="${value.bdcSpxx.yt!}"></@f.select>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="ggmc" name="ggmc" value="${bdcGg.ggmc!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.date  id="kssj" name="kssj" value="${bdcGg.kssj?date}"></@f.date>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.date  id="jssj" name="jssj" value="${bdcGg.jssj?date}"></@f.date>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="ggqx" name="ggqx" value="${bdcGg.ggqx!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="bz_${bdcGg.ggid}" name="bz" value="${bdcGg.bz!}" style="text-align:center"></@f.text>
                        </@f.td>
                    </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
            <div class="bottomarea">
                <p>公告单位：<span>${bdcGg.ggdw!}</span></p>
            </div>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>