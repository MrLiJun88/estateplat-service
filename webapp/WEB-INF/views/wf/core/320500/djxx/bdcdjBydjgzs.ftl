<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->
<#--<script src="../js/sjd.js"></script>-->
<#--<#assign path="${request.getContextPath()}">-->
<script src="js/sjd.js" type="text/javascript"></script>
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

    function saveBydjxx() {
        $.blockUI({message: "请稍等......"});
        var s = checkTzyy();
        $.ajax({
            url: bdcdjUrl + "/bdcdjBydjgzs/saveBydjxx",
            type: 'POST',
            dataType: 'json',
            data: $("#bydjForm").serialize() + '&' + $.param({s: s}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        window.location.reload();
                        alert("保存成功!");
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });
    }

    function checkTzyy() {
        var array = new Array();
        $("input[type='checkbox']").each(function (i, n) {
            var yy = $(n).val();
            if(this.checked){
                array.push(yy);
            }
        });
        var s = JSON.stringify(array);
        return s;
    }

    $(function () {
        initTzyy();
    });
    function initTzyy() {
        var tzyy = $("#tzyy").val();
        if (tzyy != '' && tzyy != null) {
            var tzyys = tzyy.split(",");
            for (var x=0;x<tzyys.length;x++) {
                $("input[type='checkbox']").each(function (i, n) {
                    var y = $(n).val();
                    if (tzyys[x] == y) {
                        this.checked ='true';
                    }
                });
            }
        }
    }

    function printBydj() {
        var proid="${proid!}";
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_bydjgzs.cpt&proid=" + proid;
        openWin(url);
    }
</script>
<style type="text/css">
    span {
        border-bottom: 1px solid black;
    }

    .toparea {
        width: 650px;
        position: relative;
        text-align: left;
        left:50px;
    }

    .bottomarea {
        width: 650px;
        text-align: left;
        position: relative;
        margin-top: 20px;
        left:50px;
    }

    .bottomarea textarea {
        border-bottom: 1px solid black;
    }

    .bottomarea input {
        width: auto;
        margin-left: 12%;
    }

    .toparea .qlrname {
        position: relative;
        text-align: left;
        margin-top: 20px;
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

</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveBydjxx()">保存</button>
        <button type="button" class="btn btn-info save" onclick="printBydj()">打印</button>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="不予登记告知书" >
        <@f.form id="bydjForm" name="bydjForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid"  value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="bh" name="bh"  value="${bdcXm.bh!}"/>
            <@f.hidden id="sqlxdm" name="sqlxdm"  value="${bdcXm.sqlxdm!}"/>
            <@f.hidden id="zl" name="zl"  value="${bdcSpxx.zl!}"/>
            <@f.hidden id="tzdx" name="tzdx"  value="${qlrs!}"/>
            <@f.hidden id="tzyy" name="tzyy"  value="${bdcBydjdjd.tzyy!}"/>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>

                <div class="toparea">
                    <div class="bh"><p>编号：${bdcXm.bh!}</p></div>
                    <div class="qlrname"><p><span style="margin-right:100px;">${qlrs!}:</span></p></div>
                    <div class="info">
                        <p><span>${sjrq!}</span>，收到你（单位）<span>${bdcdjlx!}</span>的登记申请，坐落于<span>${bdcSpxx.zl!}</span>的申请，受理编号为<span>${bdcXm.bh}</span>。因所提交的申请材料尚不足以证明申请登记相关事项，按照《不动产登记暂行条例》第十七条的规定，请补正以下申请材料：
                        </p>
                    </div>
                </div>
                <div class="bottomarea">
                    <p>经审核，因</p>

                    <p><input type="checkbox" value="违反法律、行政法规规定">违反法律、行政法规规定</input></p>

                    <p><input type="checkbox" value="存在尚未解决的权属争议">存在尚未解决的权属争议</input></p>

                    <p><input type="checkbox" value="申请登记的不动产权利超过规定期限">申请登记的不动产权利超过规定期限</input></p>

                    <p><input type="checkbox" value="法律、行政法规规定不予登记的其他情形">法律、行政法规规定不予登记的其他情形</input></p>

                    <p>上述申请因：</p>

                    <p>根据《不动产登记暂行条例》第二十二条的规定，决定不予登记。具体情况如下：</p>
                    <textarea id="bz" name="bz" >${bdcBydjdjd.bz!}</textarea>

                    <div>
                        若对本决定内容不服，可自收到本告知书之日起60日内向苏州市人民政府或者江苏省国土资源厅申请行政复议，或者在收到本告知书之日起6个月内向本不动产登记机构所在地区、县人民法院提起行政诉讼。
                    </div>
                </div>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>