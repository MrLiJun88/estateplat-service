<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
    <#include "../../../batch/qtdjxx/defaultOpinion.ftl">
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/sign.js"></@script>
<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    i = 1;
    e = 1;
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
    //保存按钮
    function saveBdcSpbxx() {
        var arry = [];//放入权利人对象
        var ywr = [];//放入义务人对象
        var a = 1;//计算权利人序号
        var y = 1;//计算义务人序号
        var msgarry = [];//放入验证消息
        $("input[name='qlrzjh']").each(function (i, n) {
            var index = $(n).attr('id');
            var y = index.lastIndexOf("_");
            var id = index.substr(y + 1);
            var qlrsfzjzl = $("#qlr_qlrsfzjzl_" + id).val();
            var sId = $("#qlr_qlrzjh_" + id).val();
            if (qlrsfzjzl == 1) {
                var zjmsg = ValidateIdCard(sId);
                if (zjmsg != null && zjmsg != undefined) {
                    msgarry.push(zjmsg);
                }
            }
        });
        var dysj = Dysj();
        if (dysj != null && dysj != undefined) {
            msgarry.push(dysj);
        }
        //验证定着物值存在于字典表中
        var dzw=Dzwyt();
        if(dzw!=null&&dzw!=undefined){
            msgarry.push(dzw);
        }
        if (msgarry != null && msgarry != undefined && msgarry.length > 0) {
            alert(msgarry[0]);
        } else {
            var ms = beforeSave();
            if (ms != null && ms != undefined) {
                alert(ms);
            }
            else {
                $.blockUI({message: "请稍等......"});
                $("input[name='qlrid']").each(function (i, n) {
                    var id = $(n).val();
                    var o = new Object();
                    o['qlrid'] = $(n).val();
                    o['proid'] = $("#proid").val();
                    o['qlrlx'] = $("#qlrlx_" + id).val();
                    o['qlbl'] = $("#qlr_gybl_" + id).val();
                    o['qlrmc'] = $("#qlr_qlr_" + id).val();
                    o['qlrsfzjzl'] = $("#qlr_qlrsfzjzl_" + id).val();
                    o['qlrzjh'] = $("#qlr_qlrzjh_" + id).val();
                    o['sxh'] = a;
                    ++a;
                    arry.push(o);
                });
                var s = JSON.stringify(arry);
                $.ajax({
                    url: "${bdcdjUrl}/bdcdjSpbxx/saveBdcSpbxx",
                    type: 'POST',
                    dataType: 'json',
                    data: $("#spbForm").serialize() + '&' + $.param({s: s}),
                    success: function (data) {
                        setTimeout($.unblockUI, 10);
                        if (isNotBlank(data)) {
                            if (data.msg == "success") {
                                $("input[name='ywrid']").each(function (i, n) {
                                    var id = $(n).val();
                                    var o = new Object();
                                    o['qlrid'] = $(n).val();
                                    o['proid'] = $("#proid").val();
                                    o['qlrlx'] = $("#ywrlx_" + id).val();
                                    o['qlbl'] = $("#ywr_gybl_" + id).val();
                                    o['qlrmc'] = $("#ywr_qlr_" + id).val();
                                    o['qlrsfzjzl'] = $("#ywr_qlrsfzjzl_" + id).val();
                                    o['qlrzjh'] = $("#ywr_qlrzjh_" + id).val();
                                    o['qlrtxdz'] = $("#ywr_qlrtxdz_" + id).val();
                                    o['qlryb'] = $("#ywr_qlryb_" + id).val();
                                    o['qlrfddbr'] = $("#ywr_qlrfddbr_" + id).val();
                                    o['qlrfddbrdh'] = $("#ywr_qlrfddbrdh_" + id).val();
                                    o['qlrdlr'] = $("#ywr_qlrdlr_" + id).val();
                                    o['qlrdlrdh'] = $("#ywr_qlrdlrdh_" + id).val();
                                    o['qlrdljg'] = $("#ywr_qlrdljg_" + id).val();
                                    o['sxh'] = y;
                                    ++y;
                                    ywr.push(o);
                                });
                                var y = JSON.stringify(ywr);
                                $.ajax({
                                    url: "${bdcdjUrl}/bdcdjSpbxx/saveBdcSpbxx",
                                    type: 'POST',
                                    dataType: 'json',
                                    data: $("#sqsForm").serialize() + '&' + $.param({s: y}),
                                    success: function (data) {
                                        setTimeout($.unblockUI, 10);
                                        if (isNotBlank(data)) {
                                            if (data.msg == "success") {
                                                //保存签名
                                                $.ajax({
                                                    url: "${bdcdjUrl}/sign/updateSignOpinon",
                                                    type: 'POST',
                                                    dataType: 'json',
                                                    data: $("#spbForm").serialize(),
                                                    success: function () {
                                                        alert("保存成功");
                                                        window.location.reload();
                                                    },
                                                    error: function () {
                                                        window.location.reload();
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    error: function (data) {
                                        alert("保存失败");
                                    }
                                });
                            }
                        }
                    },
                    error: function (data) {
                        alert("保存失败");
                    }
                });
            }
        }
    }
    //保存前验证共有方式，持证方式,共有比例是否为1
    function beforeSave() {
        $("input[name='fbcz']").each(function (i, n) {
            var qlrid = $(n).val();
            var qlbl = $("#qlr_gybl_" + qlrid).val();
            if (qlbl.replace(/\s/g, "") != "") {
                var msg = ValidateGybl(qlbl);
                if (msg != null || msg != undefined) {
                    return msg;
                }
            }
        });
        var msggyfs = changGyfs();
        if (msggyfs != null || msggyfs != undefined) {
            return msggyfs;
        }
        else {
            var sqfbcz = $("input[name='sqfbcz']:checked").val();
            var gyfs = $("input[name='gyfs']:checked").val();
            var a = 0;
            $("input[name='fbcz']").each(function (i, n) {
                ++a;
            });
            if (a == 1 && sqfbcz == "是") {
                return "持证方式选择错误！";
            }
            if (a != 1 && gyfs == 0) {
                return "共有方式选择错误！";
            }
        }
    }
    function initSpb() {
        //验证身份证证件号合法性
        $("input[name='qlrzjh']").change(function () {
            var index = $(this).attr('id');
            var y = index.lastIndexOf("_");
            var id = index.substr(y + 1);
            var qlrsfzjzl = $("#qlr_qlrsfzjzl_" + id).val();
            var sId = $("#qlr_qlrzjh_" + id).val();
            if (qlrsfzjzl == 1) {
                var msg = ValidateIdCard(sId);
                if (msg != null) {
                    alert(msg);
                }
            }
        });
        //填写共有比例计算权利人共有比例是否总计为1
        $("input[name='qlbl']").change(function () {
            var gybl = $(this).val();
            var msg = ValidateGybl(gybl);
            if (msg != null) {
                alert(msg);
            }
            else {
                var msggyfs = changGyfs();
                if (msggyfs != null) {
                    alert(msggyfs);
                }
            }
        });
        //计算权利人数量并对分别持证方式进行更改
        changFbcz();
        limGyfs();
    }
    $(document).ready(function () {
        $("#sqrqk").attr("rowspan", sqrQk(i));
        initDyqk();
        initSpb();
    });
    //初始化是否有共有比例并改变共有方式
    function changGyfs() {
        var blnumber = 0;
        $("input[name='fbcz']").each(function (i, n) {
            var qlrid = $(n).val();
            var qlbl = $("#qlr_gybl_" + qlrid).val();
            if (qlbl.replace(/\s/g, "") == "") {
                qlbl = 0;
            }
            else {
                $("#afgy").attr("checked", "checked");
            }
            blnumber = blnumber + parseFloat(qlbl);
        });
        if (blnumber != 1 && blnumber != 0) {
            return "权利人比例之和不为1！";
        }
    }
    //验证共有比例是否为整数
    function ValidateGybl(gybl) {
        var gz = /^0+(\.\d{1,2})?$/;
        if (!gz.test(gybl))
            return "请正确输入两位小数！";
    }
    //计算权利人数量并对分别持证方式进行更改
    function changFbcz() {
        var a = 0;
        $("input[name='fbcz']").each(function (i, n) {
            ++a;
        });
        if (a == 1) {
            $("#sqfbcz").attr("disabled", true);
        }
        else {
            $("#sqfbcz").attr("disabled", false);
        }
    }
    //计算权利人数量并对共有方式进行权限限制
    function limGyfs() {
        var a = 0;
        $("input[name='fbcz']").each(function (i, n) {
            ++a;
        });
        if (a == 1) {
            $("[name='gyfs']").attr("disabled", true);
        }
        else {
            $("[name='gyfs']").attr("disabled", false);
        }
    }
    //删除行
    function qlrDel(o) {
        var row = "tr_" + o;
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSpbxx/delBdcQlr",
            type: 'POST',
            dataType: 'json',
            data: $.param({s: o}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        $("tr[id='" + row + "']").remove();//删除当前行
                        i = i - 2;
                        $("#sqrqk").attr("rowspan", sqrQk(i));
                        changFbcz();
                        limGyfs();
                    }
                }
            },
            error: function (data) {
                alert("删除失败");
            }
        });
    }
    //初审意见签名
    //    function csyjQm() {
    //        var proid = $("#proid").val();
    //        var url=bdcdjUrl+"/sign?proid=" + proid + "&wiid=" + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    //        showIndexModel(url,"初审意见签名",500,300,false);
    //    }
    //重定义申请人情况合并行数
    function sqrQk(i) {
        return parseInt($("#rowsize").val()) + i;
    }
    //权利人增加行
    function qlrAdd() {
        var row_num = "tr_" + e;
        var tr = '<tr id="' + row_num + '" >'
                + '<input type="hidden" id="fbcz" name="fbcz" value="' + e + '" />'
                + '<input type="hidden" id="qlrid" name="qlrid" value="' + e + '" />'
                + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="qlr"/>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">权利人姓名（名称）</label></th>'
                + '<td colspan="3">'
                + '<input type="text" style="display:block"id="qlr_qlr_' + e + '" name="qlrmc" value="" />'
                + '<select style="display:none" id="qlr_qlr_' + e + '" name="qlrmc"  showFieldName="YHMC" valueFieldName="YHMC" source="yhList"defaultValue="" >'
                + '<#list yhList as item> <option value="${item.YHMC!}">${item.YHMC!}</option></#list>'
                + '</select></td>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
                + '<label name="">共有比例</label></th>'
                + '<td colspan="2" style="border-right-style:none;">'
                + '<input type="text" id="qlr_gybl_' + e + '" name="qlbl" value="" /></td>'
                + '<td colspan="1" style="border-left-style:none;text-align:center;">'
                + '<a   id="qlr_del"name="qlr_del"onclick="qlrDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
                + '</td></tr>'
                + '<tr id="' + row_num + '" >'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">身份证件种类</label></th>'
                + '<td colspan="3">'
                + '<select name="qlrsfzjzl" id="qlr_qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
                + '<label name="">证件号</label></th>'
                + '<td colspan="3">'
                + '<input type="text" id="qlr_qlrzjh_' + e + '" name="qlrzjh" value="" />'
                + '</td></tr>';
        $("#tr_qlr").before(tr);//确定增加行数的位置
        i = i + 2;
        ++e;
        $("#sqrqk").attr("rowspan", sqrQk(i));
        initSpb();
        reloadrights();
        reloadcolor();
    }
    //义务人增加行
    function ywrAdd() {
        var row_num = "tr_" + e;
        var tr = '<tr id="' + row_num + '" >'
                + '<input type="hidden" id="ywrid" name="ywrid" value="' + e + '" />'
                + '<input type="hidden" id="ywrlx_' + e + '" name="ywrlx" value="ywr" />'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">义务人姓名（名称）</label></th>'
                + '<td colspan="3">'
                + '<input type="text" id="ywr_qlr_' + e + '" name="ywrmc" value="" /></td>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
                + '<label name="">共有比例</label></th>'
                + '<td colspan="2" style="border-right-style:none;">'
                + '<input type="text" id="ywr_gybl_' + e + '" name="ywrqlbl" value=" " /></td>'
                + '<td colspan="1" style="border-left-style:none;text-align:center;">'
                + '<a  id="ywr_del"name="ywr_del"onclick="qlrDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
                + '</td></tr>'
                + '<tr id="' + row_num + '" >'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
                + '<label name="">身份证件种类</label></th>'
                + '<td colspan="3">'
                + '<select name="ywrsfzjzl" id="ywr_qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
                + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
                + '<label name="">证件号</label></th>'
                + '<td colspan="3">'
                + '<input type="text" id="ywr_qlrzjh_' + e + '" name="ywrzjh" value="" />'
                + '</td></tr>';
        $("#tr_ywr").before(tr);//确定增加行数的位置
        i = i + 2;
        ++e;
        $("#sqrqk").attr("rowspan", sqrQk(i));
        reloadrights();
        reloadcolor();
    }
    //根据权利类型判断显示抵押情况信息
    function initDyqk() {
        var qllx = $("#qllx").val();
        if (qllx == "18") {
            $("#ygdyqk").hide();
        }
        else if (qllx == "19") {
            $("#dyqk").hide();
        }
        else {
            $("#dyqk").hide();
            $("#ygdyqk").hide();
        }
    }
    //判断抵押开始时间必须小于抵押结束时间
    function Dysj() {
        var zwlxksqx = $("#zwlxksqx").val();
        var zwlxjsqx = $("#zwlxjsqx").val();
        if (zwlxksqx != null && zwlxksqx != undefined && zwlxksqx != "") {
            if (zwlxjsqx != null || zwlxjsqx != "" || zwlxjsqx != undefined) {
                var ksqx = new Date(zwlxksqx);
                var jsqx = new Date(zwlxjsqx);
                if (ksqx.getTime() > jsqx.getTime()) {
                    return "抵押开始期限大于抵押结束期限！";
                }
            }
        }
    }
    //验证填写的定着物用途是否存在于字典表中
    function  Dzwyt() {
        var yt =$("#dzwyt").val();
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSqsxx/validateDzwyt",
            type: 'POST',
            dataType: 'json',
            data: $.param({yt: yt}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "faile") {
                        return "定着物用途请按字典表填写！";
                    }
                }
            },
            error:function (data) {
            }
        });
    }
    //审批表打印
    function printSpb(){
        var proid = $("#proid").val();
        var url=reportUrl+"/ReportServer?reportlet=print%2Fbdc_spb.cpt&op=write&proid="+proid;
        // window.open(url,"弹出窗口");
        showIndexModel(url,"打印审批表",1000,550,false);
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" id="提交" name="提交"onclick="saveBdcSpbxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" id="打印预览"name="打印预览"onclick="printSpb()">打印</a>
        <#--<ul class='dropdown-menu'>-->
        <#--<li><a onclick="openSpb()">审批表 </a></li>-->
        <#--</ul>-->
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
                        <div class=" col-xs-4  ">
                        </div>
                        <div class=" col-xs-2 demonstrative dw ">
                            单位：
                        </div>
                        <div class="rowLabel col-xs-4 mjdwCol">
                        <#--<@f.select  id="mjdw" name="mjdw"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'平方米','dm':'1'},{'mc':'亩','dm':'2'},{'mc':'公顷','dm':'3'}]" defaultValue="${mjdw!}"></@f.select>-->
                            <@f.radio id="dw"name="mjdw" saveValue="${mjdw!}" defaultValue="1" valueFieldName="1">
                                平方米</@f.radio>
                            <@f.radio  id="dw"name="mjdw" saveValue="${mjdw!}" valueFieldName="2">亩</@f.radio>
                            <@f.radio  id="dw"name="mjdw" saveValue="${mjdw!}" valueFieldName="3">公顷、万元</@f.radio>
                        </div>
                    </div>
                </section>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th  colspan="3">
                        <@f.label name="申请登记事由"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.select id="djsy" name="djsy"  showFieldName="MC" valueFieldName="DM" source="djsyMcList" defaultValue="${bdcXm.djsy}" noEmptyValue="true" ></@f.select>
                    </@f.td>
                    <@f.td colspan="3">
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList" defaultValue="${bdcXm.djlx!}" noEmptyValue="true" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th  colspan="3">
                        <@f.label name="共有方式"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.radio id="ddsy" name="gyfs" valueFieldName="0"  saveValue="${gyfs!}" defaultValue="0">单独所有&nbsp;&nbsp;</@f.radio>
                        <@f.radio id="gtgy" name="gyfs" valueFieldName="1" saveValue="${gyfs!}"  >
                            共同共有&nbsp;&nbsp;</@f.radio>
                        <@f.radio id="afgy" name="gyfs" valueFieldName="2"  saveValue="${gyfs!}" >
                            按份共有&nbsp;&nbsp;</@f.radio>
                        <@f.radio id="qtgy" name="gyfs" valueFieldName="3" saveValue="${gyfs!}"  >其他共有
                            &nbsp;&nbsp;</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th  colspan="3">
                        <@f.label name="申请证书版式"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.radio id="sqzsbs"name="sqzsbs" valueFieldName="单一版"  saveValue="${bdcXm.sqzsbs!}" defaultValue="单一版">
                            单一版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</@f.radio>
                        <@f.radio id="sqzsbs"name="sqzsbs" valueFieldName="集成板" saveValue="${bdcXm.sqzsbs!}"  >
                            集成板</@f.radio>
                    </@f.td>
                    <@f.th  colspan="2">
                        <@f.label name="申请分别持证"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.radio id="sqfbcz" name="sqfbcz" valueFieldName="是"  saveValue="${bdcXm.sqfbcz!}" defaultValue="是">
                            是&nbsp;&nbsp;</@f.radio>
                        <@f.radio id="sqfbcz"name="sqfbcz" valueFieldName="否" saveValue="${bdcXm.sqfbcz!}">
                            否</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th   colspan="1" id="sqrqk">
                        <@f.label name="                    申<br/>请<br/>人<br/>情<br/>况                   "></@f.label>
                    </@f.th>
                    <@f.th   colspan="10" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" style="border-left-style:none;background: #f9f9f9;">
                        <a id="qlr_add"name="qlr_add"onclick="qlrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <#if (bdcQlrList?size > 0)>
                    <#list bdcQlrList as qlr>
                        <@f.tr id="tr_${qlr.qlrid!}" >
                            <@f.hidden id="fbcz" name="fbcz" value="${qlr.qlrid!}"/>
                            <@f.hidden id="qlrid" name="qlrid" value="${qlr.qlrid!}"/>
                            <@f.hidden id="qlrlx_${qlr.qlrid!}" name="qlrlx" value="${qlr.qlrlx!}"/>
                            <@f.th colspan="3"  >
                                <@f.label name="权利人姓名（名称）"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.text style="display:block"id="qlr_qlr_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" ></@f.text>
                                <@f.select style="display:none" id="qlr_qlr_${qlr.qlrid!}" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue="${qlr.qlrmc!}"/>
                            </@f.td>
                            <@f.th colspan="2"  >
                                <@f.label name="共有比例"></@f.label>
                            </@f.th>
                            <@f.td colspan="2"style="border-right-style:none;">
                                <@f.text id="qlr_gybl_${qlr.qlrid!}" name="qlbl" value="${qlr.qlbl!}" ></@f.text>
                            </@f.td>
                            <@f.td colspan="1" style="border-left-style:none;text-align:center;">
                                <a id="qlr_del"name="qlr_del"onclick="qlrDel('${qlr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                            </@f.td>
                        </@f.tr>
                        <@f.tr  id="tr_${qlr.qlrid!}" >
                            <@f.th colspan="3"  >
                                <@f.label name="身份证件种类"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.select id="qlr_qlrsfzjzl_${qlr.qlrid!}" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${qlr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
                            </@f.td>
                            <@f.th colspan="2"  >
                                <@f.label name="证件号"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.text id="qlr_qlrzjh_${qlr.qlrid!}" name="qlrzjh" value="${qlr.qlrzjh!}" ></@f.text>
                            </@f.td>
                        </@f.tr>
                    </#list>
                <#else>
                    <@f.tr id="tr_def1" >
                        <@f.hidden id="fbcz" name="fbcz" value="def1"/>
                        <@f.hidden id="qlrid" name="qlrid" value="def1"/>
                        <@f.hidden id="qlrlx_def1" name="qlrlx" value="qlr"/>
                        <@f.th colspan="3"  >
                            <@f.label name="权利人姓名（名称）"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.text style="display:block"id="qlr_qlr_def1" name="qlrmc" value="" ></@f.text>
                            <@f.select style="display:none" id="qlr_qlr_def1" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue=""/>
                        </@f.td>
                        <@f.th colspan="2"  >
                            <@f.label name="共有比例"></@f.label>
                        </@f.th>
                        <@f.td colspan="2"style="border-right-style:none;">
                            <@f.text id="qlr_gybl_def1" name="qlbl" value="" ></@f.text>
                        </@f.td>
                        <@f.td colspan="1" style="border-left-style:none;text-align:center;">
                            <a id="qlr_del"name="qlr_del"onclick="qlrDel('def1')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                        </@f.td>
                    </@f.tr>
                    <@f.tr  id="tr_def1" >
                        <@f.th colspan="3"  >
                            <@f.label name="身份证件种类"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.select id="qlr_qlrsfzjzl_def1" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
                        </@f.td>
                        <@f.th colspan="2"  >
                            <@f.label name="证件号"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.text id="qlr_qlrzjh_def1" name="qlrzjh" value="" ></@f.text>
                        </@f.td>
                    </@f.tr>
                </#if>
                <@f.tr id="tr_qlr">
                    <@f.th    colspan="10" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" style="border-left-style:none;background: #f9f9f9;">
                        <a id="ywr_add"name="ywr_add"onclick="ywrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <#if (bdcYwrList?size > 0)>
                    <#list bdcYwrList as ywr>
                        <@f.tr id="tr_${ywr.qlrid!}" >
                            <@f.hidden id="ywrid" name="ywrid" value="${ywr.qlrid!}"/>
                            <@f.hidden id="ywrlx_${ywr.qlrid!}" name="ywrlx" value="${ywr.qlrlx!}"/>
                            <@f.th colspan="3"  >
                                <@f.label name="义务人姓名（名称）"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.text id="ywr_qlr_${ywr.qlrid!}" name="ywrmc" value="${ywr.qlrmc!}" ></@f.text>
                            </@f.td>
                            <@f.th colspan="2"  >
                                <@f.label name="共有比例"></@f.label>
                            </@f.th>
                            <@f.td colspan="2" style="border-right-style:none;">
                                <@f.text id="ywr_gybl_${ywr.qlrid!}" name="ywrqlbl" value="${ywr.qlbl!}" ></@f.text>
                            </@f.td>
                            <@f.td colspan="1" style="border-left-style:none;text-align:center;">
                                <a id="ywr_del"name="ywr_del"onclick="qlrDel('${ywr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                            </@f.td>
                        </@f.tr>
                        <@f.tr id="tr_${ywr.qlrid!}" >
                            <@f.th colspan="3"  >
                                <@f.label name="身份证件种类"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.select id="ywr_qlrsfzjzl_${ywr.qlrid!}" name="ywrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${ywr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
                            </@f.td>
                            <@f.th colspan="2"  >
                                <@f.label name="证件号"></@f.label>
                            </@f.th>
                            <@f.td colspan="3">
                                <@f.text id="ywr_qlrzjh_${ywr.qlrid!}" name="ywrzjh" value="${ywr.qlrzjh!}" ></@f.text>
                            </@f.td>
                        </@f.tr>
                    </#list>
                <#else>
                    <@f.tr id="tr_def2" >
                        <@f.hidden id="ywrid" name="ywrid" value="def2"/>
                        <@f.hidden id="ywrlx_def2" name="ywrlx" value="ywr"/>
                        <@f.th colspan="3"  >
                            <@f.label name="义务人姓名（名称）"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.text id="ywr_qlr_def2" name="ywrmc" value="" ></@f.text>
                        </@f.td>
                        <@f.th colspan="2"  >
                            <@f.label name="共有比例"></@f.label>
                        </@f.th>
                        <@f.td colspan="2" style="border-right-style:none;">
                            <@f.text id="ywr_gybl_def2" name="ywrqlbl" value="" ></@f.text>
                        </@f.td>
                        <@f.td colspan="1" style="border-left-style:none;text-align:center;">
                            <a id="ywr_del"name="ywr_del"onclick="qlrDel('def2')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                        </@f.td>
                    </@f.tr>
                    <@f.tr id="tr_def2" >
                        <@f.th colspan="3"  >
                            <@f.label name="身份证件种类"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.select id="ywr_qlrsfzjzl_def2" name="ywrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
                        </@f.td>
                        <@f.th colspan="2"  >
                            <@f.label name="证件号"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.text id="ywr_qlrzjh_def2" name="ywrzjh" value="" ></@f.text>
                        </@f.td>
                    </@f.tr>
                </#if>
                <@f.tr id="tr_ywr">
                    <@f.th rowspan="7" colspan="1">
                        <@f.label name="                    不<br/>动<br/>产<br/>情<br/>况                   "></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="8">
                        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>                        </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">
                        <@f.text  id="bdcdyh" name="bdcdyh" value="${bdcSpxx.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="1">
                        <@f.label name="类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
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
                    <@f.td colspan="3">
                        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdzhytList"defaultValue="${bdcSpxx.zdzhyt!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="定着物面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text  id="djzwmj" name="mj" value="${bdcSpxx.mj!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="2">
                        <@f.label name="定着物用途"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select id="dzwyt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcSpxx.yt!}" ></@f.select>
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
                    <@f.td colspan="3">
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
                    <@f.td colspan="3">
                        <@f.select id="lz" name="lz"  showFieldName="MC" valueFieldName="DM" source="lzList"defaultValue="${bdcSpxx.lz!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="原不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td colspan="8">
                        <@f.text  id="ybdcqzh" name="ybdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <tbody id="dyqk">
                    <@f.tr>
                        <@f.th rowspan="3" colspan="1" >
                            <@f.label name="抵<br/>押<br/>情<br/>况"></@f.label>
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
                        <@f.td colspan="3">
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
                    <@f.td colspan="3">
                        <@f.date  id="zwlxjsqx" name="zwlxjsqx" value="${zwlxjsqx!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="在建建筑物抵押范围"></@f.label>
                    </@f.th>
                    <@f.td colspan="8">
                        <@f.text  id="zjgcdyfw" name="zjgcdyfw" value="${bdcDyaq.zjgcdyfw!}"></@f.text>
                    </@f.td>
                </@f.tr>
                </tbody>
                <tbody id="ygdyqk">
                    <@f.tr>
                        <@f.th rowspan="2" colspan="1" >
                            <@f.label name="抵押<br/>情况"></@f.label>
                        </@f.th>
                        <@f.th colspan="3">
                            <@f.label name="被担保债权数额"></@f.label>
                        </@f.th>
                        <@f.td rowspan="2" colspan="3">
                            <@f.text  id="bdbzzqse" name="qdjg" value="${bdcYg.qdjg!}"></@f.text>
                        </@f.td>
                        <@f.th colspan="2">
                            <@f.label name="债务履行期限"></@f.label>
                        </@f.th>
                        <@f.td colspan="3">
                            <@f.date id="zwlxksqx" name="zwlxksqx" value="${ygzwlxksqx!}"></@f.date>
                        </@f.td>
                    </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="（最高债权数额：万元）"></@f.label>
                    </@f.th>
                    <@f.th colspan="2">
                        <@f.label name="(债权确定期限)"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.date  id="zwlxjsqx" name="zwlxjsqx" value="${ygzwlxjsqx!}"></@f.date>
                    </@f.td>
                </@f.tr>
                </tbody>
                <@f.tr>
                    <@f.th rowspan="2">
                        <@f.label name="地役权<br/>情况&nbsp;"></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="需役地坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="8">
                        <@f.text  id="xydzl" name="xydzl" value="${bdcDyq.xydzl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="需役地不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="8">
                        <@f.text  id="xydbdcdyh" name="xydbdcdyh" value="${bdcDyq.xydbdcdyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="6" colspan="1" >
                        <@f.label name="不<br/>动<br/>产<br/>登<br/>记<br/>审<br/>批<br/>情<br/>况"></@f.label>
                    </@f.th>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="初审"></@f.label>
                    </@f.th>
                    <@f.td colspan="9" rowspan="1" height="90px" class="center">
                        <@f.textarea  id="csyj" name="csyj" value="${csrSign.signOpinion!}" style="height:75px;position:relative;"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" class="center">
                        <@f.label name="初审人"></@f.label>
                    </@f.td>
                    <@f.td colspan="3" width="145" height="50">
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${csrSign.signId!}"width="145" height="50" signId="${csrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" class="center">
                        <a id="csyj_qm" name="csyj_qm"onclick="sign('${proid!}','csr','${taskid!}','csyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="csyjqm" name="csyjqm" value="${csrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="复审"></@f.label>
                    </@f.th>
                    <@f.td colspan="9" rowspan="1" height="90px">
                        <@f.textarea  id="fsyj" name="fsyj" value="${fsrSign.signOpinion!}" style="height:75px;position:relative;"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" class="center">
                        <@f.label name="复审人"></@f.label>
                    </@f.td>
                    <@f.td colspan="3" width="145" height="50">
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${fsrSign.signId!}"width="145" height="50" signId="${fsrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" class="center">
                        <a id="fsyj_qm"name="fsyj_qm"onclick="sign('${proid!}','fsr','${taskid!}','fsyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="fsyjqm" name="fsyjqm" value="${fsrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="核定"></@f.label>
                    </@f.th>
                    <@f.td colspan="9" rowspan="1" height="90px">
                        <@f.textarea  id="hdyj" name="hdyj" value="${hdrSign.signOpinion!}" style="height:75px;position:relative;"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" class="center">
                        <@f.label name="核定人"></@f.label>
                    </@f.td>
                    <@f.td colspan="3" width="145" height="50">
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${hdrSign.signId!}"width="145" height="50" signId="${hdrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" class="center">
                        <a id="hdyj_qm"name="hdyj_qm"onclick="sign('${proid!}','hdr','${taskid!}','hdyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="hdyjqm" name="hdyjqm" value="${hdrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3" height="100px">
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                    <@f.td colspan="9" >
                        <@f.textarea  id="bz" name="bz" value="${bdcXm.bz!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>