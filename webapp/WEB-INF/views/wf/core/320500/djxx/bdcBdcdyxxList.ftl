<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--authorize-->
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->
<#--<script src="../js/sjd.js"></script>-->
<#--<#assign path="${request.getContextPath()}">-->
<script src="js/sjd.js"      type="text/javascript"></script>
<script src="js/readCard.js"      type="text/javascript"></script>
<script type="text/javascript" >
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl="${path_platform!}"
    var analysisUrl ="${analysisUrl!}";
    var taskid ="${taskid!}";
    var from="${from!}";
    var rid="${rid!}";
    e = 1;

    var qlrName='';
    function changeFzlx(bdcdyh,fzlx){
        if(fzlx=='3'&&(bdcdyh!=''&&bdcdyh!='undefined')){
            var idvalue='qlrid_'+bdcdyh;
            changeQlrName('全体业主',idvalue,bdcdyh);
        }else if(qlrName!==''&&(fzlx!='3')&&(bdcdyh!=''&&bdcdyh!='undefined')){
            var idvalue='qlrid_'+bdcdyh;
            changeQlrName(qlrName,idvalue,bdcdyh);
        }
    }

    function changeQlrName(qlrname,idvalue,bdcdyh){
        $("input[id='"+idvalue+"']").each(function(i,n){
            var qlrid=$(n).val();
            var qlrmc=$("#qlrmc_"+qlrid+"_"+bdcdyh).val();
            if(qlrmc!='全体业主'){
                qlrName=qlrmc;
            }
            $("#qlrmc_"+qlrid+"_"+bdcdyh).val(qlrname);
        });
    }

    function saveBdcdyxx(){
        $.blockUI({message: "请稍等......"});
        var spxx=new Array();
        var qlr=new Array();
        var fdcq=new Array();
        addInfo(spxx,fdcq,qlr);
        var fdcq=JSON.stringify(fdcq);
        var spxx=JSON.stringify(spxx);
        var qlr=JSON.stringify(qlr);
        $.ajax({
            url: bdcdjUrl+"/bdcdjBdcdyxxList/saveBdcdyxx",
            type: 'POST',
            dataType: 'json',
            data:$.param({fdcq:fdcq,spxx:spxx,qlr:qlr}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.message == "success") {
                        alert("保存成功!");
                        window.location.reload();
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });
    }
    function addInfo(spxx,fdcq,qlr){
        $("input[name='bdcdyh']").each(function(i,n){
            var bdcdyh=$(n).val();
            if($("#checkbox_"+bdcdyh)[0].checked){
            var o=new Object();
            o['bdcdyh']=$(n).val();
            o['zl']=$("#zl_"+bdcdyh).val();
            o['mj']=$("#dzwmj_"+bdcdyh).val();
            o['yt']=$("#dzwyt_"+bdcdyh).val();
            o['spxxid']=$("#spxxid_"+bdcdyh).val();
            spxx.push(o);
            addFzlx(fdcq,bdcdyh);
            addQlr(qlr,bdcdyh);
            }
        });
    }
    function addQlr(qlr,bdcdyh){
        var idvalue='qlrid_'+bdcdyh;
        $("input[id='"+idvalue+"']").each(function(i,n){
            var qlrid=$(n).val();
            var q=new Object();
            q['qlrid']=$(n).val();
            q['qlrmc']=$("#qlrmc_"+qlrid+"_"+bdcdyh).val();
            if(q['qlrmc']!=''&&q['qlrmc']!='undefined'){
                qlr.push(q);
            }
        });
    }

    function addFzlx(fdcq,bdcdyh){
        var idvalue='fdcqid_'+bdcdyh;
        $("input[id='"+idvalue+"']").each(function(i,n){
            var qlid=$(n).val();
            var f=new Object();
            f['qlid']=$(n).val();
            f['fzlx']=$("#fzlx_"+qlid+"_"+bdcdyh).val();
            if( f['fzlx']!=''&& f['fzlx']!='undefined'){
                fdcq.push(f);
            }
        });
    }

    function printSpb(proid){
        proid ="in('"+proid+"')";
        var url = "${reportUrl!}/ReportServer?reportlet=print%2F${spbreportName!}&__cutpage__=v&op=write&proid="+proid;
        openWin(url);
    }
    function printSqs(proid){
        proid ="in('"+proid+"')";
        var url ="${reportUrl!}/ReportServer?reportlet=print%2F${sqsreportName!}&__cutpage__=v&op=write&proid="+ proid;
        openWin(url);
    }

    function openDialog(){
        $("#update").show();
    }
    function closeDialog(){
        $("#update").hide();
        $('#mj').val('');
        $('#yt').val('');
        $('#zdzhyt').val('');
        $('#zdzhmj').val('');
        $('#zdzhqlxz').val('');
    }

    function updateAll(){
        var spxx=new Array();
        var mj=$('#mj').val();
        var yt=$('#yt').val();
        var zdzhyt=$('#zdzhyt').val();
        var zdzhmj=$('#zdzhmj').val();
        var zdzhqlxz=$('#zdzhqlxz').val();
        $("input[name='bdcdyh']").each(function(i,n){
            var bdcdyh=$(n).val();
            var o=new Object();
            o.spxxid=$("#spxxid_"+bdcdyh).val();
            o.mj=mj;
            o.yt=yt;
            o.zdzhyt=zdzhyt;
            o.zdzhmj=zdzhmj;
            o.zdzhqlxz=zdzhqlxz;
            spxx.push(o);
        });
        var spxx=JSON.stringify(spxx);
        $.ajax({
            url: bdcdjUrl+"/bdcdjBdcdyxxList/updateAllBdcdyxx",
            type: 'POST',
            dataType: 'json',
            data:$.param({spxx:spxx}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (data.message == "success") {
                    alert("修改成功!");
                    window.location.reload();
                }
            },
            error: function (data) {
                alert("修改失败!");
            }
        });
    }

</script>
<style type="text/css">
    .tableA>tbody>tr>td:nth-child(odd) {
        text-align: center;
        width: 20%;
        background: #fff;
    }

    .tableA>tbody>tr>td{
        text-align: center;
        width: 20%;
        background: #fff;
    }
    .formBody {
        width: 1200px;
    }

</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveBdcdyxx()">保存</button>
        <button type="button" class="btn btn-primary save" id="sjdxxUpdateAllButton" onclick="openDialog()">批量修改</button>
    </div>
</div>
<div class="main-container" >
    <@f.contentDiv  title="不动产单元信息表" >
        <@f.form id="bdcdyxxForm" name="bdcdyxxForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <#assign showFzlxColumn = "${showFzlxColumn?c}">
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:200px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:150px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:150px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:250px;"></@f.td>
                    <#if showFzlxColumn=="true">
                    <@f.td style="border:none;height:0px;width:80px;"></@f.td><!--发证类型-->
                    </#if>
                    <@f.td style="border:none;height:0px;width:90px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:90px;"></@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th colspan="1" >
                        <input role="checkbox" id="allcheckbox" class="cbox" type="checkbox" onclick="checkboxOnclick(this)" />
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="不动产单元"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="定着物面积"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="定着物用途"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="${columnName!}"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="原不动产权证号"></@f.label>
                    </@f.th>
                    <#if showFzlxColumn=="true">
                    <@f.th colspan="1">
                        <@f.label name="发证类型"></@f.label>
                    </@f.th>
                    </#if>
                    <@f.th colspan="1">
                        <@f.label name="打印（审批表）"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="打印（申请书）"></@f.label>
                    </@f.th>
                </@f.tr>
                <tbody id="bdcdyxx">
                    <#list returnValueList as value>
                        <@f.tr>
                            <@f.hidden id="fdcqid_${value.bdcdyh!}" name="fdcqid"  value="${value.qlid!}"/>
                            <@f.td colspan="1">
                            <input role="checkbox" id="checkbox_${value.bdcdyh!}" name="checkbox" class="cbox" type="checkbox">
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="bdcdyh_${value.bdcdyh!}" name="bdcdyh" value="${value.bdcdyh!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="zl_${value.bdcdyh!}" name="zl" value="${value.bdcSpxx.zl!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="dzwmj_${value.bdcdyh!}" name="dzwmj" value="${value.bdcSpxx.mj!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.select  id="dzwyt_${value.bdcdyh!}" name="dzwyt"  showFieldName="mc" valueFieldName="dm" source="fwytList" defaultValue="${value.bdcSpxx.yt!}"></@f.select>
                            </@f.td>
                            <@f.td colspan="1" >
                                <#list value.bdcQlrList as qlr>
                                <@f.hidden id="qlrid_${value.bdcdyh}" name="qlrid"  value="${qlr.qlrid!}"/>
                                <@f.text  id="qlrmc_${qlr.qlrid!}_${value.bdcdyh!}" name="qlrmc" value="${qlr.qlrmc!}" style="text-align:center"></@f.text>
                                </#list>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="ybdcqzh_${value.bdcdyh!}" name="ybdcqzh" value="${value.bdcXm.ybdcqzh!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <#if showFzlxColumn=="true">
                            <@f.td colspan="1" >
                            <select id="fzlx_${value.qlid!}_${value.bdcdyh!}" value="${value.fzlx!}"
                                    name="fzlx" onchange="changeFzlx('${value.bdcdyh!}',this.value)">
                                    <#assign default="${value.fzlx!}"/>
                                    <option value="1" <#if "1"==default>selected="selected" </#if>>证书</option>
                                    <option value="2" <#if "2"==default>selected="selected" </#if>>信息表</option>
                                    <option value="3" <#if "3"==default>selected="selected" </#if>>不发证</option>
                            </select>
                                <#--<select showFieldName="mc" valueFieldName="dm"-->
                                <#--source="[{'mc':'证书','dm':'1'},{'mc':'信息表','dm':'2'},{'mc':'不发证','dm':'3'}]"-->
                                <#--defaultValue ="${value.fdcq.fzlx!}" noEmptyValue="true" handler="changeFzlx" args="${value.bdcdyh!}"><select>-->
                            </@f.td>
                            </#if>
                            <@f.td colspan="1">
                            <a href="javascript:void(0)" id="printspb_${value.bdcdyh!}" onclick="printSpb('${value.bdcXm.proid!}')">打印</a>
                            </@f.td>
                            <@f.td colspan="1">
                            <a href="javascript:void(0)" id="printsqs_${value.bdcdyh!}" onclick="printSqs('${value.bdcXm.proid!}')">打印</a>
                            </@f.td>
                            <@f.hidden id="spxxid_${value.bdcdyh!}" name="spxxid"  value="${value.bdcSpxx.spxxid!}"/>
                        </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>

<div class="Pop-upBox moveModel  ui-draggable" style="display: none;" id="update">
    <div class="modal-dialog sdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>批量修改</h4>
                <button type="button" id="sdHide" class="proHide" onclick="closeDialog()"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="sdDataForm">
                    <div class="row">
                        <div class="col-xs-3">
                            <label>宗地面积：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="zdzhmj" id='zdzhmj' class="form-control">
                        </div>
                        <div class="col-xs-3">
                            <label>宗地用途：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="zdzhyt" id='zdzhyt' class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-3">
                            <label>宗地权利性质：</label>
                        </div>
                        <div class="col-xs-3">
                            <#--<input type="text" name="zdzhqlxz" id='zdzhqlxz' class="form-control">-->
                            <@f.select  id="zdzhqlxz" name="zdzhqlxz"  showFieldName="MC" valueFieldName="DM" source="zdqlxz"></@f.select>
                        </div>
                        <div class="col-xs-3">
                            <label>定着物用途：</label>
                        </div>
                        <div class="col-xs-3">
                            <@f.select  id="yt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList" ></@f.select>
                            <#--<input type="text" name="yt" id='yt' class="form-control">-->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-3">
                            <label>定着物面积：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="mj" id='mj' class="form-control">
                        </div>
                    </div>
                    <div style="display: none;">
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="update" onclick="updateAll()">修改</button>
            </div>
        </div>

    </div>
</div>
</@com.html>