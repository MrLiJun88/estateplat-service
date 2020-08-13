<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
    <@script name="static/js/wf.js"></@script>
<script src="js/sjd.js"      type="text/javascript"></script>
<script src="js/readCard.js"      type="text/javascript"></script>
<script type="text/javascript" >
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl="${path_platform!}";
    var analysisUrl ="${analysisUrl!}";
    var taskid ="${taskid!}";
    var from="${from!}";
    var rid="${rid!}";
    e = 1;
   num = 0;//计算新增材料次数
    //权利人增加行
    function qlrAdd() {
        var row_num = "tr_" + e;
        var tr = '<tr id="' + row_num + '" >'
                + '<input type="hidden" id="qlrid" name="qlrid" value="' + e + '" />'
                + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="qlr"/>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">权利人</label></th>'
                + '<td colspan="8" style="border-right-style:none;">'
                + '<input type="text" style="display:block" id="qlr_qlr_' + e + '" name="qlrmc" value="" />'
                + '<select style="display:none" id="qlr_qlr_' + e + '" name="qlrmc"  showFieldName="YHMC" valueFieldName="YHMC" source="yhList"defaultValue="" >'
                + '<#list yhList as item> <option value="${item.YHMC!}">${item.YHMC!}</option></#list>'
                + '</select></td>'
                + '<th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">'
                + '<a  id="qlr_add"name="qlr_add"  onclick="qlrAdd(' + e + ')"><i class="ace-icon glyphicon glyphicon-plus"></i></a>'
                + '</th></tr>'
                + '<tr id="' + row_num + '" >'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">身份证件种类</label></th>'
                + '<td colspan="3">'
                + '<select name="qlrsfzjzl" id="qlr_qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
                + '<label name="">证件号</label></th>'
                + '<td colspan="3" style="border-right:none">'
                + '<input type="text" id="qlr_qlrzjh_' + e + '" name="qlrzjh" value="" /></td>'
                + '<th colspan="1" style="border-left-style:none;background: #ffffff;text-align: center;">'
                + '<a  id="qlr_del"name="qlr_del" onclick="qlrDel(' + e + ')"><i  class="ace-icon glyphicon glyphicon-remove"></i></a>'
                + '</th></tr>'
                + '<tr id="' + row_num + '" >'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">联系电话</label></th>'
                + '<td colspan="9">'
                + '<input type="text" id="qlr_qlrtxdz_' + e + '" name="qlrlxdh" value="" />'
                + '</td></tr>';
        $("#tr_qlr").before(tr);//确定增加行数的位置
        ++e;
        initlisten();
        reloadrights();
        reloadcolor();
        xtyh();
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="提交" name="提交" onclick="saveSjdxx()">保存</button>
        <#--<button type="button" class="btn btn-info save" onclick="selectBdcdy()">选择</button>-->
        <div class='btn-group'>
            <a class='btn btn-info' class="dropdown-toggle" data-toggle="dropdown">选择<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a id="选择不动产单元"name="选择不动产单元"onclick="selectBdcdy()">选择不动产单元</a></li>
                <li><a id="选择楼盘表"name="选择楼盘表"onclick="chooseLpb()">选择楼盘表</a></li>
                <li><a id="选择逻辑幢"name="选择逻辑幢"onclick="chooseLjz()">选择逻辑幢</a></li>
                <li><a id="选择预测逻辑幢"name="选择预测逻辑幢" onclick="chooseYcjz()">选择预测逻辑幢</a></li>
                <li><a id="批量选择不动产单元"name="批量选择不动产单元"onclick="plChooseBdcdy()">批量选择不动产单元</a></li>
            </ul>
        </div>
        <div class='btn-group'>
            <a class='btn btn-info' class="dropdown-toggle" data-toggle="dropdown">获取<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a id="获取交易信息"name="获取交易信息"onclick="getJyxx()">获取交易信息</a></li>
                <li><a id="导入ZIP"name="导入ZIP"onclick="importZip()">导入ZIP</a></li>
            </ul>
        </div>
        <div class='btn-group'>
            <a class='btn btn-info' class="dropdown-toggle" data-toggle="dropdown">关联<span class="caret"></span></a>
            <ul class='dropdown-menu'>
                <li><a  id="关联不动产单元"name="关联不动产单元"onclick="glBdcdy()">关联不动产单元</a></li>
                <li><a id="关联房产证"name="关联房产证"onclick="glFcz()">关联房产证</a></li>
                <li><a id="关联土地证"name="关联土地证"onclick="glTdz()">关联土地证</a></li>
                <li><a id="删除关联证书"name="删除关联证书"onclick="delGlZs()">删除关联证书</a></li>
            </ul>
        </div>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" id="打印预览"name="打印预览" onclick="printSjd()">打印</a>
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
            <@f.hidden id="qllx" name="qllx" value="${bdcXm.qllx!}"/>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:40px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:25px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:40px;"></@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="3" style="border:none;">
                        <img src="${bdcdjUrl!}/bdcPrint/getEwm?proid=${proid}"style="width:70px;height:70px;"/>
                    </@f.td>
                    <@f.td colspan="9" style="border:none;height:0px;">
                    </@f.td>
                    <#--<@f.td colspan="2" style="border:none;text-align:right">-->
                        <#--<@f.label name="收件编号："></@f.label>-->
                    <#--</@f.td>-->
                    <#--<@f.td colspan="3" style="border:none;">-->
                        <#--<@f.text id="bh" name="bh" value="${bdcXm.bh!}"></@f.text>-->
                    <#--</@f.td>-->
                </@f.tr>
                <@f.tr style="height:15px">
                    <@f.td colspan="6" style="border:none;height:0px;">
                    </@f.td>
                    <@f.td colspan="2" style="border:none;text-align:right">
                        <@f.label name="收件编号："></@f.label>
                    </@f.td>
                    <@f.td colspan="4" style="border:none;text-align:left">
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
                    <@f.td colspan="9">
                        <@f.select id="djzx" name="djzx"   showFieldName="MC" valueFieldName="DM" source="djzxList" defaultValue="${bdcXm.djzx!}"/>
                    </@f.td>
                    <#--<@f.th colspan="2" style="background: #ffffff;">-->
                        <#--<@f.label></@f.label>-->
                    <#--</@f.th>-->
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
                        <@f.text style="display:block" id="qlr_qlr_${bdcQlr.qlrid!}" name="qlrmc" value="${bdcQlr.qlrmc!}"></@f.text>
                        <@f.select style="display:none" id="qlr_qlr_${bdcQlr.qlrid!}" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue="${bdcQlr.qlrmc!}"/>
                    </@f.td>
                    <@f.th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">
                        <a id="qlr_add"name="qlr_add" onclick="qlrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <@f.tr id="tr_${bdcQlr.qlrid!}">
                    <@f.th colspan="3">
                        <@f.label name="证件类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select id="qlr_qlrsfzjzl_${bdcQlr.qlrid!}" name="qlrsfzjzl"   showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${bdcQlr.qlrsfzjzl!}"/>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="证件号码"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" style="border-right:none">
                        <@f.text  id="qlr_qlrzjh_${bdcQlr.qlrid!}" name="qlrzjh" value="${bdcQlr.qlrzjh!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="1" style="border-left-style:none;background: #ffffff;text-align:center;">
                        <a id="qlr_del"name="qlr_del"onclick="qlrDel('${bdcQlr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                    </@f.th>
                </@f.tr>
                <@f.tr id="tr_${bdcQlr.qlrid!}">
                    <@f.th colspan="3">
                        <@f.label name="联系电话"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="qlr_qlrtxdz_${bdcQlr.qlrid!}" name="qlrlxdh" value="${bdcQlr.qlrlxdh!}"></@f.text>
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
                        <@f.radio id="sqzsbs"name="sqzsbs" saveValue="${bdcXm.sqzsbs!}" defaultValue="单一版" valueFieldName="单一版">单一版</@f.radio>
                        <@f.radio  id="sqzsbs"name="sqzsbs" saveValue="${bdcXm.sqzsbs!}" valueFieldName="集成版">集成版</@f.radio>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="申请分别持证"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.radio id="sqfbcz" name="sqfbcz" saveValue="${bdcXm.sqfbcz!}"  valueFieldName="是">是</@f.radio>
                        <@f.radio  id="sqfbcz"name="sqfbcz" saveValue="${bdcXm.sqfbcz!}" defaultValue="否" valueFieldName="否">否</@f.radio>
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
                        <@f.date id="sjrq" name="sjdsjrq" value="${sjrq!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="4" style="border-right:none;text-align:left">
                        <a id="addsjcl"name="addsjcl"onclick="sjclAdd()"><i class="ace-icon glyphicon glyphicon-plus-sign">插入行</i></a>&nbsp;
                        <a id="delsjcl"name="delsjcl"onclick="sjclDel()"><i class="ace-icon glyphicon glyphicon-remove-sign">删除</i></a>&nbsp;
                        <a id="plsc"name="plsc"onclick="upFile()"><i class="ace-icon glyphicon glyphicon-arrow-up">批量上传</i></a>
                    </@f.th>
                    <@f.th colspan="8" style="border-left:none;text-align:left">
                        <@f.label name="  材  料  清  单  "></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="1"  class="bg-color-white">
                        <input role="checkbox" id="allcheckbox" class="cbox" type="checkbox" onclick="checkboxOnclick(this)" />
                    </@f.th>
                    <@f.th colspan="1" class="bg-color-white">
                        <@f.label name="序号"></@f.label>
                    </@f.th>
                    <@f.th colspan="4"  class="bg-color-white">
                        <@f.label name="材料名称"></@f.label>
                    </@f.th>
                    <@f.th colspan="2"  class="bg-color-white">
                        <@f.label name="材料类型"></@f.label>
                    </@f.th>
                    <@f.th colspan="1"  class="bg-color-white">
                        <@f.label name="材料份数"></@f.label>
                    </@f.th>
                    <@f.th colspan="1"  class="bg-color-white">
                        <@f.label name="材料页数"></@f.label>
                    </@f.th>
                    <@f.th colspan="2"  class="bg-color-white">
                        <@f.label name="操作"></@f.label>
                    </@f.th>
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
                                <@f.text  id="sjcl_xh_${sjcl.SJCLID!}" name="xh" value="${sjcl.ROWNUM!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="4" >
                                <@f.text  id="sjclmc_${sjcl.SJCLID!}" name="clmc" value="${sjcl.SJCLMC!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="2" >
                                <@f.select  id="cllx_${sjcl.SJCLID!}" name="cllx"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'原件','dm':'1'},{'mc':'复印件','dm':'2'}]" defaultValue="${sjcl.CLLX!}" noEmptyValue="true" ></@f.select>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="fs_${sjcl.SJCLID!}" name="fs" value="${sjcl.FS!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="ys_${sjcl.SJCLID!}" name="ys" value="${sjcl.YS!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.th colspan="2" >
                            <!--<a class="detail" href="javascript:editSjcl('${sjcl.SJCLID!}')">详细</a>-->
                            <a id="sc_${sjcl.SJCLID!}"onclick="fileUp('${sjcl.SJCLMC!}')"><i class="ace-icon glyphicon glyphicon-arrow-up"></i></a>
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