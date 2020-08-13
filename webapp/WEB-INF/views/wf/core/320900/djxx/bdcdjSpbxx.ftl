<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--<#include "../../../common/rightsManagement.ftl">-->
    <#include "../../../common/fieldColorManagement.ftl">
<script type="text/javascript">
    i = 1;
    e = 1;
    var arry = [];
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var wiid = "${wiid!}";
    var etlUrl = "${etlUrl!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}"
    var analysisUrl = "${analysisUrl!}";
    var taskid = "${taskid!}";
    var from = "${from!}";
    var rid = "${rid!}";
</script>
<style type="text/css">
    .leftLabel {
        margin: 0 auto;
        width: 20px;
        line-height: 24px;
        border: 0px solid #333
    }
</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="saveBdcSpbxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" onclick="openSpb()">打印</a>
        </div>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="不动产登记审批表">
        <@f.form id="spbForm" name="spbForm">
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="qllx" name="qllx" value="${bdcXm.qllx!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${qllxVo.qlid!}"/>
            <@f.hidden id="ywh" name="ywh" value="${bdcXm.bh!}"/>
            <@f.hidden id="rowsize" name="rowsize" value="${rowsize!}"/>
            <@f.hidden id="csSignId"  name="csSignId" value="${csrSign.signId!}"/>
            <@f.hidden id="fsSignId"  name="fsSignId" value="${fsrSign.signId!}"/>
            <@f.hidden id="hdSignId"  name="hdSignId" value="${hdrSign.signId!}"/>
            <@f.hidden id="csSignKey"  name="csSignKey" value="csr"/>
            <@f.hidden id="fsSignKey"  name="fsSignKey" value="fsr"/>
            <@f.hidden id="hdSignKey"  name="hdSignKey" value="hdr"/>
            <div class="secondTitle" style="width:780px">
                <section id="jbxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">
                        </div>
                        <div class=" col-xs-6  ">
                        </div>
                        <div class=" col-xs-2 demonstrative dw ">
                            单位：
                        </div>
                        <div class="rowLabel col-xs-2 mjdwCol">
                            <@f.select  id="mjdw" name="mjdw"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'平方米','dm':'1'},{'mc':'亩','dm':'2'},{'mc':'公顷','dm':'3'}]" defaultValue="${mjdw!}"></@f.select>
                        </div>
                    </div>
                </section>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:40px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:40px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:75px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:75px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
                <#assign showRows = "${showRows?c}">
                <#include "top.ftl">
                <#include "qlr.ftl">
                <@f.tr id="tr_qlr">
                    <@f.th   style="text-align:center; background: #f9f9f9;border-right-style:none;"  colspan="11" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" style="border-left-style:none;background: #f9f9f9;">
                        <a onclick="ywrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <#include "ywr.ftl">
                <@f.tr id="tr_ywr">
                    <@f.th rowspan="6" colspan="1">
                        <@f.label class="leftLabel" name="                    不动产情况                   "></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text  id="bdcdyh" name="bdcdyh" value="${bdcSpxx.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="不动产类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.select id="bdclx" name="bdclx"  showFieldName="MC" valueFieldName="DM" source="bdclxList"defaultValue="${bdcSpxx.bdclx!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="宗地/宗海面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text  id="zdzhmj" name="zdzhmj" value="${bdcSpxx.zdzhmj!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="宗地/宗海用途"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdzhytList"defaultValue="${bdcSpxx.zdzhyt!}" ></@f.select>
                    </@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="宗地/宗海权利性质"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select id="zdzhqlxz" name="zdzhqlxz"  showFieldName="MC" valueFieldName="DM" source="zdzhqlxzList"defaultValue="${bdcSpxx.zdzhqlxz!}" ></@f.select>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="用海类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.select id="yhlx" name="yhlx"  showFieldName="MC" valueFieldName="DM" source="yhlxList"defaultValue="${bdcSpxx.yhlx!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="构筑物类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select id="gzwlx" name="gzwlx"  showFieldName="MC" valueFieldName="DM" source="gzwlxList"defaultValue="${bdcSpxx.gzwlx!}" ></@f.select>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="林种"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.select id="lz" name="lz"  showFieldName="MC" valueFieldName="DM" source="lzList"defaultValue="${bdcSpxx.lz!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="原不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="ybdcqzh" name="ybdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="7" colspan="1" >
                        <@f.label class="leftLabel" name="抵押情况"></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="被担保债权数额"></@f.label>
                    </@f.th>
                    <@f.td rowspan="2" colspan="3">
                        <@f.text  id="bdbzzqse" name="bdbzzqse" value="${bdcDyaq.bdbzzqse!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="债务履行期限"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.date id="zwlxksqx" name="zwlxksqx" value="${zwlxksqx!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="（最高债权数额：万元）"></@f.label>
                    </@f.th>
                    <@f.th colspan="2">
                        <@f.label name="(债权确定期限)"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.date  id="zwlxjsqx" name="zwlxjsqx" value="${zwlxjsqx!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr >
                    <@f.th colspan="3" rowspan="2">
                        <@f.label name="担保范围"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" rowspan="2">
                        <@f.text  id="dbfw" name="dbfw" value="${bdcDyaq.dbfw!}"></@f.text>
                    </@f.td>

                    <@f.th colspan="2" rowspan="2">
                        <@f.label name="抵押面积"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="房产"></@f.label>
                    </@f.th>
                    <@f.td  colspan="3">
                        <@f.text  id="fwdymj" name="fwdymj" value="${bdcDyaq.fwdymj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="1">
                        <@f.label name="土地"></@f.label>
                    </@f.th>
                    <@f.td  colspan="3">
                        <@f.text  id="tddymj" name="tddymj" value="${bdcDyaq.tddymj!}"></@f.text>
                    </@f.td>
                </@f.tr>

                <@f.tr >
                    <@f.th colspan="3" rowspan="3">
                        <@f.label name="在建建筑物抵押范围"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" rowspan="3">
                        <@f.text  id="zjgcdyfw" name="zjgcdyfw" value="${bdcDyaq.zjgcdyfw!}"></@f.text>
                    </@f.td>

                    <@f.th colspan="2" rowspan="3">
                        <@f.label name="价格（作价）"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="房产"></@f.label>
                    </@f.th>
                    <@f.td  colspan="3">
                        <@f.text  id="fwdyjg" name="fwdyjg" value="${bdcDyaq.fwdyjg!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="1">
                        <@f.label name="土地"></@f.label>
                    </@f.th>
                    <@f.td  colspan="3">
                        <@f.text  id="tddyjg" name="tddyjg" value="${bdcDyaq.tddyjg!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="1">
                        <@f.label name="总价"></@f.label>
                    </@f.th>
                    <@f.td  colspan="3">
                        <@f.text  id="zj" name="zj" value="${zj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="2">
                        <@f.label class="leftLabel" name="地役权情况"></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="需役地坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="xydzl" name="xydzl" value="${bdcDyq.xydzl!}"></@f.text>                        </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="需役地不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="xydbdcdyh" name="xydbdcdyh" value="${bdcDyq.xydbdcdyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <#--<#include "qlqtzk.ftl">-->
                <#--<#include "fj.ftl">-->
                <#include "spyj.ftl">
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                    <@f.td colspan="12">
                        <@f.text  id="bz" name="bz" value="${bdcXm.bz!}"></@f.text>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
    <@script name="static/js/${dwdm!}/sqsAndSpbCommon.js"></@script>
    <@script name="static/js/${dwdm!}/spb.js"></@script>
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/sign.js"></@script>
</@com.html>