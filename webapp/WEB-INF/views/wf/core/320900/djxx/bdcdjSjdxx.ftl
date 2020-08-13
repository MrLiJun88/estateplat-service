<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
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

    function selectBdcdy() {
        var proid = $("#proid").val();
        var url=bdcdjUrl+"/selectBdcdy?proid=" + proid + "&wiid=" + wiid;
        showIndexModel(url,"选择不动产单元",1000,550,false);
    }
    function saveSjdxx() {
        var proid = $("#proid").val();
        var arry=[];
        var a=1;
        $.blockUI({message: "请稍等......"});
        $("input[name='qlrid']").each(function(i,n){
            var id=$(n).val();
            var o=new Object();
            o['qlrid']=$(n).val();
            o['proid']=$("#proid").val();
            o['qlrlx']=$("#qlrlx_"+id).val();
            o['qlrmc']=$("#qlrmc_"+id).val();
            o['qlrsfzjzl']=$("#qlrsfzjzl_"+id).val();
            o['qlrzjh']=$("#qlrzjh_"+id).val();
            o['qlrlxdh']=$("#qlrlxdh_"+id).val();
            o['sxh']=a;
            ++a;
            arry.push(o);
        });
        var s=JSON.stringify(arry);
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: bdcdjUrl+"/bdcdjSjdxx/saveSlxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sjdForm").serialize()+'&'+$.param({s:s}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        saveSjclList();
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });

    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveSjdxx()">保存</button>
        <button type="button" class="btn btn-info save" onclick="selectBdcdy()">选择</button>
        <div class='btn-group'>
            <a class='btn btn-info' class="dropdown-toggle" data-toggle="dropdown">操作<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a onclick="chooseLpb()">选择楼盘表</a></li>
                <li><a onclick="chooseLjz()">选择逻辑幢</a></li>
                <li><a onclick="chooseYcjz()">选择预测逻辑幢</a></li>
                <li><a onclick="printSjd()">打印预览</a></li>
                <li><a onclick="plChooseBdcdy()">批量选择不动产单元</a></li>
                <li><a onclick="getJyxx()">获取交易信息</a></li>
                <li><a onclick="glBdcdy()">关联不动产单元</a></li>
                <li><a onclick="importZip()">导入ZIP</a></li>
                <li><a onclick="glFcz()">关联房产证</a></li>
                <li><a onclick="glTdz()">关联土地证</a></li>
                <li><a onclick="delGlZs()">删除关联证书</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="main-container" >
    <@f.contentDiv  title="不动产登记收件单" >
        <@f.form id="sjdForm" name="sjdForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="sjxxid" name="sjxxid"  value="${bdcSjxx.sjxxid!}"/>
            <@f.hidden id="spxxid" name="spxxid"  value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="sjxxNum" name="sjxxNum"  value="${sjxxNum!}"/>
            <@f.hidden id="djzxdm" name="djzxdm"  value="${djzxdm!}"/>
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
                <@f.tr>
                    <@f.td colspan="3" style="border:none;">
                        <img src="${bdcdjUrl!}/bdcPrint/getEwm?proid=${proid}"style="width:70px;height:70px;"/>
                    </@f.td>
                    <@f.td colspan="4" style="border:none;height:0px;">
                    </@f.td>
                    <@f.td colspan="2" style="border:none;text-align:right">
                        <@f.label name="收件编号："></@f.label>
                    </@f.td>
                    <@f.td colspan="3" style="border:none;">
                        <@f.text id="bh" name="bh" value="${bdcXm.bh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产登记类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="bdcdjlx" name="bdcdjlx" value="${bdcdjlx!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="登记子项"></@f.label>
                    </@f.th>
                    <@f.td colspan="7">
                        <@f.select id="djzx" name="djzx"   showFieldName="MC" valueFieldName="DM" source="djzxList" defaultValue="${bdcXm.djzx!}"/>
                    </@f.td>
                    <@f.th colspan="2" style="background: #ffffff;">
                        <@f.label></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="djyy" name="djyy" value="${bdcXm.djyy!}"></@f.text>
                    </@f.td>
                </@f.tr>
            <#list bdcQlrList as bdcQlr>
                <@f.tr id="tr_${bdcQlr.qlrid!}">
                    <@f.hidden id="qlrid" name="qlrid" value="${bdcQlr.qlrid!}"/>
                    <@f.hidden id="qlrlx_${bdcQlr.qlrid!}" name="qlrlx" value="qlr"/>
                    <@f.th colspan="3">
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.td colspan="8" style="border-right:none">
                        <@f.text id="qlrmc_${bdcQlr.qlrid!}" name="qlrmc" value="${bdcQlr.qlrmc!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">
                        <a onclick="qlrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <@f.tr id="tr_${bdcQlr.qlrid!}">
                    <@f.th colspan="3">
                        <@f.label name="证件类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select id="qlrsfzjzl_${bdcQlr.qlrid!}" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${bdcQlr.qlrsfzjzl!}"noEmptyValue="true"/>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="证件号码"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" style="border-right:none">
                        <@f.text  id="qlrzjh_${bdcQlr.qlrid!}" name="qlrzjh" value="${bdcQlr.qlrzjh!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">
                        <a onclick="qlrDel('${bdcQlr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                    </@f.th>
                </@f.tr>
                <@f.tr id="tr_${bdcQlr.qlrid!}">
                    <@f.th colspan="3">
                        <@f.label name="联系电话"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="qlrlxdh_${bdcQlr.qlrid!}" name="qlrlxdh" value="${bdcQlr.qlrlxdh!}"></@f.text>
                    </@f.td>
                </@f.tr>
            </#list>
                <@f.tr id="tr_qlr">
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="申请证书版式"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.radio name="sqzsbs" saveValue="${bdcXm.sqzsbs!}" defaultValue="单一版" valueFieldName="单一版">单一版</@f.radio>
                        <@f.radio  name="sqzsbs" saveValue="${bdcXm.sqzsbs!}" valueFieldName="集成版">集成版</@f.radio>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="申请分别持证"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.radio id="sqfbcz" name="sqfbcz" saveValue="${bdcXm.sqfbcz!}"  valueFieldName="是">是</@f.radio>
                        <@f.radio  name="sqfbcz" saveValue="${bdcXm.sqfbcz!}" defaultValue="否" valueFieldName="否">否</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="收件人"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text  id="sjr" name="sjr" value="${bdcSjxx.sjr!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="收件时间"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <#--<@f.date id="sjrq" name="sjrq" value="${sjrq!}"></@f.date>-->
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="4" style="border-right:none;text-align:left">
                        <a onclick="addSjcl()"><i class="ace-icon glyphicon glyphicon-plus-sign">插入行</i></a>&nbsp;&nbsp;
                        <a onclick="sjclDel()"><i class="ace-icon glyphicon glyphicon-remove-sign">删除</i></a>&nbsp;&nbsp;
                        <a onclick="upFile()"><i class="ace-icon glyphicon glyphicon-arrow-up">批量上传</i></a>
                    </@f.th>
                    <@f.th colspan="8" style="border-left:none;text-align:left">
                        <@f.label name="  材  料  清  单  "></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" >
                        <input role="checkbox" id="allcheckbox" class="cbox" type="checkbox" onclick="checkboxOnclick(this)" />
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="序号"></@f.label>
                    </@f.td>
                    <@f.td colspan="4" >
                        <@f.label name="材料名称"></@f.label>
                    </@f.td>
                    <@f.td colspan="2" >
                        <@f.label name="材料类型"></@f.label>
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="份数"></@f.label>
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="页数"></@f.label>
                    </@f.td>
                    <@f.td colspan="2" style="text-align:left">
                        <@f.label name="编辑|上传"></@f.label>
                    </@f.td>
                </@f.tr>
                <tbody id="sjcl">
                    <#list sjclList as sjcl>
                        <@f.tr>
                            <@f.hidden id="sjclid" name="sjclid"  value="${sjcl.SJCLID!}"/>
                            <@f.td colspan="1" >
                            <input role="checkbox" id="checkbox" name="checkbox" class="cbox" type="checkbox"
                                   value="${sjcl.SJCLID!}">
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="xh" name="xh" value="${sjcl.ROWNUM!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="4" >
                                <@f.text  id="clmc_${sjcl.SJCLID!}" name="clmc" value="${sjcl.SJCLMC!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="2" >
                                <@f.select  id="cllx" name="cllx"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'原件','dm':'1'},{'mc':'复印件','dm':'2'}]" defaultValue="${sjcl.CLLXDM!}" noEmptyValue="true" ></@f.select>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="fs" name="fs" value="${sjcl.FS!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="ys" name="ys" value="${sjcl.YS!}"></@f.text>
                            </@f.td>
                            <@f.th colspan="2" >
                            <a class="detail" href="javascript:editSjcl('${sjcl.SJCLID!}')">详细</a>
                            <a onclick="fileUp('${sjcl.SJCLMC!}')"><i class="ace-icon glyphicon glyphicon-arrow-up"></i></a>
                            </@f.th>
                        </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
        </@f.form>
        <#--<section id="sjcl">-->
            <#--<div class="row">-->
                <#--<div class="col-xs-2 demonstrative">-->
                    <#--材料清单：-->
                <#--</div>-->
                <#--<div class="col-xs-8 ">-->
                <#--</div>-->
                <#--<div class="rowLabel col-xs-2">-->
                <#--</div>-->
            <#--</div>-->
            <#--<@p.list tableId="sjclxx-grid-table" pageId="sjclxx-grid-pager" keyField="SJCLID" dataUrl="${bdcdjUrl}/bdcdjSjdxx/getSjclxxPagesJson?proid=${proid!}" multiboxonly="false"multiselect="false">-->
                <#--<@p.field fieldName="ROWNUM" header="序号" width="2%"/>-->
                <#--<@p.field fieldName="SJCLMC" header="材料名称" width="15%"/>-->
                <#--<@p.field fieldName="CLLXDM" header="材料类型" width="4%"/>-->
                <#--<@p.field fieldName="MRFS" header="材料份数" width="3%"/>-->
                <#--<@p.field fieldName="FS" header="材料页数" width="3%"/>-->
                <#--<@p.field fieldName="SJCLID" header="操作" width="7%"  renderer="sjclCz"/>-->
            <#--</@p.list>-->
            <#--<table id="sjclxx-grid-table"></table>-->
            <#--<div id="sjclxx-grid-pager"></div>-->
        <#--</section>-->
    </@f.contentDiv>
</div>
</@com.html>