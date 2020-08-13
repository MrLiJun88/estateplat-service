
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
function selectBdcdy() {
    var proid = $("#proid").val();
    var url=bdcdjUrl+"/selectBdcdy?proid=" + proid + "&wiid=" + wiid;
    showIndexModel(url,"选择不动产单元",1000,550,false);
}

function chooseLpb() {
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/lpb/queryZdList?proid='+proid,
        "选择楼盘表","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function chooseLjz() {
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/lpb/selectLjz?proid='+proid+'&mulSelect=true',
        "选择逻辑幢","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function chooseYcjz() {
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/lpb/selectYcLjz?proid='+proid+'&mulSelect=true',
        "选择预测逻辑幢","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function printSjd(){
    var proid = $("#proid").val();
    var sjxxNum = $("#sjxxNum").val();
    var djzxdm = $("#djzxdm").val();
    var url=reportUrl+"/ReportServer?reportlet=print%2Fbdc_sjd.cpt&op=write&proid="+proid+"&sjxxnum="+sjxxNum+"&djzxdm="+djzxdm;
    window.open(url,"弹出窗口")
}

function plChooseBdcdy(){
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/selectBdcdy?proid='+proid+'&multiselect=true',
        "批量选择不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function getJyxx(){
    var proid = $("#proid").val();
    window.open(etlUrl+'/gxjy?proid='+proid,
        "查询交易信息","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function glBdcdy(){
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/selectBdcdy?glbdcdy=true&proid='+proid,
        "选择不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function importZip(){
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/ont/toOntSjgl?proid='+proid+"&wiid="+wiid,
        "选择外网收件不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function glFcz(){
    window.open(bdcdjUrl+'/glZs/glFcz?wiid='+wiid,
        "关联房产证","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function glTdz(){
    window.open(bdcdjUrl+'/glZs?wiid='+wiid,
        "关联土地证","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}

function delGlZs(){
    $.ajax({
        type:'get',
        async:true,
        url:bdcdjUrl+'/glZs/deleteGlzs?wiid=' + wiid,
        success:function (data) {
            alert(data);
            location.reload();
        }
    });
}

//权利人增加行
function qlrAdd() {
    var row_num = "tr_" + e;
    var tr = '<tr id="' + row_num + '" >'
        + '<input type="hidden" id="qlrid" name="qlrid" value="' + e + '" />'
        + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="qlr"/>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">权利人</label></th>'
        + '<td colspan="8" style="border-right-style:none;">'
        + '<input type="text" id="qlrmc_' + e + '" name="qlrmc" value="" /></td>'
        + '<th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">'
        + '<a  onclick="qlrAdd(' + e + ')"><i class="ace-icon glyphicon glyphicon-plus"></i></a>'
        + '</th></tr>'
        + '<tr id="' + row_num + '" >'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">身份证件种类</label></th>'
        + '<td colspan="3">'
        + '<select name="qlrsfzjzl" id="qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
        + '<label name="">证件号</label></th>'
        + '<td colspan="3" style="border-right:none">'
        + '<input type="text" id="qlrzjh_' + e + '" name="qlrzjh" value="" /></td>'
        + '<th colspan="1" style="border-left-style:none;background: #ffffff;border-left:none">'
        + '<a  onclick="qlrDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>'
        + '</th></tr>'
        + '<tr id="' + row_num + '" >'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">联系电话</label></th>'
        + '<td colspan="9">'
        + '<input type="text" id="qlrlxdh' + e + '" name="qlrlxdh" value="" />'
        + '</td></tr>';
    $("#tr_qlr").before(tr);//确定增加行数的位置
    ++e;
    initlisten();
}
//删除行
function qlrDel(o) {
    var row="tr_" + o;
    $.blockUI({message: "请稍等......"});
    $.ajax({
        url: "${bdcdjUrl}/bdcdjSqsxx/delBdcQlr",
        type: 'POST',
        dataType: 'json',
        data: $.param({s:o}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    $("tr[id='"+row+"']").remove();//删除当前行
                    changFbcz();
                }
            }
        },
        error: function (data) {
            alert("删除失败");
        }
    });
}
//选择框判断删除
function  sjclDel(){
    sjclid=[];
    clmc=[];
    $.ajax({
        type:'post',
        url:'/estateplat-server/bdcdjSlxx/creatSjcl',
        data:{proid: proid,wiid:wiid},
        success: function (data) {
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    $("input[name='checkbox']:checked").each(function (i, n) {
                        var id = $(n).val();
                        var mc = $("#clmc_" + id).val();
                        sjclid.push(id);
                        clmc.push(mc);
                    });
                    var s = JSON.stringify(sjclid);
                    var o = JSON.stringify(clmc);
                    delSjcl(o, s);
                }
            }
        },
        error: function (data) {
            alert("删除失败,请重新尝试！");
        }
    });
}
//勾选checkbox动作
function checkboxOnclick(checkbox) {
    if (checkbox.checked == true) {
        var checkbox = $("input[name='checkbox']");
        $.each(checkbox,function (){
            this.checked="checked";
        });
    } else {
        var checkbox = $("input[name='checkbox']");
        $.each(checkbox,function (){
            this.checked = !this.checked;
        });
    }
}
//计算权利人数量并对分别持证方式进行更改
function  changFbcz() {
    var a=0;
    $("input[name='qlrid']").each(function(i,n){
        ++a;
    });
    if(a==1){
        $("#sqfbcz").attr("disabled",true);
    }
    else{
        $("#sqfbcz").attr("disabled",false);
    }
}
//初始化一些监听事件
function initlisten() {
    //集成身份证读卡器qlrmc
    $("input[name='qlrmc']").on("click",function() {
        var index = $(this).attr('id');
        var y = index.indexOf("_");
        var id = index.substr(y + 1);
        var qlrzjh="qlrzjh_"+id
        ReadIDCardNew(index,qlrzjh);
    });
    //权利人身份证件号填写验证
    $("input[name='qlrzjh']").change(function () {
        var index = $(this).attr('id');
        var y = index.indexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl=$("#qlrsfzjzl_"+id).val();
        var sId=$("#qlrzjh_"+id).val();
        if(qlrsfzjzl==1){
            var msg=ValidateIdCard(sId);
            if(msg!=null){
                alert(msg);
            }
        }
    });
    changFbcz();
}
$(function () {
    initlisten();
    //登记子项改变收件材料随之改变
    $("select[id='djzx']").change(function () {
        var djzxdm = $("#djzx").val();
        $.ajax({
            type: 'post',
            url: '/estateplat-server/bdcdjSjdxx/changeDjzx',
            data: {proid: proid, wiid: wiid,djzxdm:djzxdm},
            success: function (data) {
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        $.ajax({
                            type:'post',
                            url:'/estateplat-server/bdcdjSjdxx/getDjzxByAjax',
                            data:{proid: proid,wiid:wiid,djzx:djzxdm},
                            success: function (data) {
                                if (data.msg == "success") {
                                    var json = eval(data.sjclList);
                                    var html="";
                                    $.each(json, function (index, item) {
                                        var sjclid= json[index].SJCLID;
                                        var rownum= json[index].ROWNUM;
                                        var sjclmc= json[index].SJCLMC;
                                        var fs= json[index].FS;
                                        var ys= json[index].YS;
                                        var context= '<tr>'
                                            + '<input type="hidden" id="sjclid" name="sjclid" value="' + sjclid + '" />'
                                            + '<td colspan="1">'
                                            +'<input role="checkbox" id="checkbox" name="checkbox" class="cbox" type="checkbox"value="' + sjclid + '"></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="xh" name="xh" value="' + rownum + '"  /></td>'
                                            + '<td colspan="4">'
                                            + '<input type="text" id="clmc_'+sjclid+'" name="clmc" value="' + sjclmc + '"  /></td>'
                                            + '<td colspan="2">'
                                            +'<select name="cllx" id="cllx"> <option value="1" >原件</option> <option value="2">复印件</option></select>'
                                            + '</select></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="fs" name="fs" value="' + fs + '"  /></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="ys" name="ys" value="' + ys + '"  /></td>'
                                            + '<th colspan="2">'
                                            +'<a class="detail" href="javascript:editSjcl('+sjclid+')">详细</a>'
                                            +'<a onclick="fileUp('+sjclmc+')">'
                                            +'<i class="ace-icon glyphicon glyphicon-arrow-up"></i></a>'
                                            + '</th></tr>';
                                        html=html+context;
                                    });
                                    $('#sjcl').empty();
                                    $('#sjcl').append(html);
                                }
                            },
                            error: function (data) {
                            }
                        });
                    }
                }
            },
        });
    });
});

function  saveSjclList() {
    $.blockUI({message: "请稍等......"});
    var arry=[];
    $("input[name='sjclid']").each(function(i,n){
        var id=$(n).val();
        var o=new Object();
        o['sjclid']=$(n).val();
        o['sjxxid']=$("#sjxxid").val();
        o['xh']=$("#xh_"+id).val();
        o['clmc']=$("#clmc_"+id).val();
        o['cllx']=$("#cllx_"+id).val();
        o['fs']=$("#fs_"+id).val();
        o['ys']=$("#ys_"+id).val();
        arry.push(o);
    });
    var s=JSON.stringify(arry);
    $.ajax({
        url: bdcdjUrl+"/bdcdjSjdxx/saveSjclList",
        type: 'POST',
        dataType: 'json',
        data:$.param({s:s}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "true") {
                    window.location.reload();
                    alert("保存成功!");
                }
            }
        },
        error: function (data) {
            alert("保存收件材料失败!");
        }
    });
}
