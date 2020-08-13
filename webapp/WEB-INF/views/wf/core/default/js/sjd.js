function saveSjdxx() {
    var proid = $("#proid").val();
    var arry=[];
    var  msgarry=[];
    var a=1;
    var sjdsjrq=$("#sjdsjrq").val();
    $("input[name='qlrzjh']").each(function (i,n) {
        var index = $(n).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl=$("#qlr_qlrsfzjzl_"+id).val();
        var sId=$("#qlr_qlrzjh_"+id).val();
        if(qlrsfzjzl==1){
            var zjmsg=ValidateIdCard(sId);
            if(zjmsg!=null&&zjmsg!=undefined){
                msgarry.push(zjmsg);
            }
        }
    });
    if(msgarry!=null && msgarry!=undefined && msgarry.length>0){
        alert(msgarry[0]);
    }else{
        $.blockUI({message: "请稍等......"});
        $("input[name='qlrid']").each(function(i,n){
            var id=$(n).val();
            var o=new Object();
            o['qlrid']=$(n).val();
            o['proid']=$("#proid").val();
            o['qlrlx']=$("#qlrlx_"+id).val();
            o['qlrmc']=$("#qlr_qlr_"+id).val();
            o['qlrsfzjzl']=$("#qlr_qlrsfzjzl_"+id).val();
            o['qlrzjh']=$("#qlr_qlrzjh_"+id).val();
            o['qlrlxdh']=$("#qlr_qlrtxdz_"+id).val();
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
            data: $("#sjdForm").serialize()+'&'+$.param({s:s,"sjdsjrq":sjdsjrq}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        saveSjclList();
                        $.ajax({
                            type: 'get',
                            async: true,
                            url: bdcdjUrl+'/wfProject/updateWorkFlow?proid=' + proid,
                            success: function (data) {
                            },
                        });
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });
    }
}
function selectBdcdy() {
    var proid = $("#proid").val();
    var url=bdcdjUrl+"/selectBdcdy?proid=" + proid + "&wiid=" + wiid;
    showIndexModel(url,"选择不动产单元",1000,550,false);
}

function chooseLpb() {
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/lpb/queryZdList?proid='+proid;
    // window.open(bdcdjUrl+'/lpb/queryZdList?proid='+proid,
    //     "选择楼盘表","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"选择楼盘表",1000,550,false);
}

function chooseLjz() {
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/lpb/selectLjz?proid='+proid+'&mulSelect=true';
    // window.open(bdcdjUrl+'/lpb/selectLjz?proid='+proid+'&mulSelect=true',
    //     "选择逻辑幢","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"选择逻辑幢",1000,550,false);
}

function chooseYcjz() {
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/lpb/selectYcLjz?proid='+proid+'&mulSelect=true';
    // window.open(bdcdjUrl+'/lpb/selectYcLjz?proid='+proid+'&mulSelect=true',
    //     "选择预测逻辑幢","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"选择预测逻辑幢",1000,550,false);
}

function printSjd(){
    var proid = $("#proid").val();
    var sjxxNum = $("#sjxxNum").val();
    var djzxdm = $("#djzxdm").val();
    var url=reportUrl+"/ReportServer?reportlet=print%2Fbdc_sjd.cpt&op=write&proid="+proid+"&sjxxnum="+sjxxNum+"&djzxdm="+djzxdm;
    // window.open(url,"弹出窗口");
    showIndexModel(url,"打印收件单",1000,550,false);
}

// function openSjd() {
//     alert("11");
//     var proid = $("#proid").val();
//     var url = bdcdjUrl+'/bdcdjSpbxx/bdcdjSpbList?wiid=' + wiid +'&taskid='+taskid+'&from='+from+'&rid='+rid+'&proid='+proid;
//     showIndexModel(url,"收件单列表",1000,800,false);
// }

function plChooseBdcdy(){
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/selectBdcdy?proid='+proid+'&multiselect=true';
    // window.open(bdcdjUrl+'/selectBdcdy?proid='+proid+'&multiselect=true',
    //     "批量选择不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"批量选择不动产单元",1000,550,false);
}

function getJyxx(){
    var proid = $("#proid").val();
    var url=etlUrl+'/gxjy?proid='+proid;
    // window.open(etlUrl+'/gxjy?proid='+proid,
    //     "查询交易信息","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"查询交易信息",1000,550,false);
}

function glBdcdy(){
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/selectBdcdy?glbdcdy=true&proid='+proid;
    // window.open(bdcdjUrl+'/selectBdcdy?glbdcdy=true&proid='+proid,
    //     "选择不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");

    showIndexModel(url,"选择不动产单元",1000,550,false);
}

function importZip(){
    var proid = $("#proid").val();
    var url=bdcdjUrl+'/ont/toOntSjgl?proid='+proid+"&wiid="+wiid;
    // window.open(bdcdjUrl+'/ont/toOntSjgl?proid='+proid+"&wiid="+wiid,
    //     "选择外网收件不动产单元","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"选择外网收件不动产单元",1000,550,false);
}

function glFcz(){
    var url=bdcdjUrl+'/glZs/glFcz?wiid='+wiid;
    // window.open(bdcdjUrl+'/glZs/glFcz?wiid='+wiid,
    //     "关联房产证","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"关联房产证",1000,550,false);
}

function glTdz(){
    var url=bdcdjUrl+'/glZs?wiid='+wiid;
    // window.open(bdcdjUrl+'/glZs?wiid='+wiid,
    //     "关联土地证","height=500, width=900, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    showIndexModel(url,"关联土地证",1000,550,false);
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


//删除行
function qlrDel(o) {
    var row="tr_" + o;
    $.blockUI({message: "请稍等......"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqsxx/delBdcQlr",
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
                        var mc = $("#sjclmc_" + id).val();
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
    $("input[name='qlrmc']").on("dblclick",function() {
        var index = $(this).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrzjh="qlr_qlrzjh_"+id;
        ReadIDCardNew(index,qlrzjh);
    });
    //权利人身份证件号填写验证
    $("input[name='qlrzjh']").change(function () {
        var index = $(this).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl=$("#qlr_qlrsfzjzl_"+id).val();
        var sId=$("#qlr_qlrzjh_"+id).val();
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
                                        var context= '<tr id="tr_'+sjclid+'">'
                                            + '<input type="hidden" id="sjclid" name="sjclid" value="' + sjclid + '" />'
                                            + '<td colspan="1">'
                                            +'<input role="checkbox" id="checkbox" name="checkbox" class="cbox" type="checkbox"value="' + sjclid + '"></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="sjcl_xh_'+sjclid+'" name="xh" value="' + rownum + '" style="text-align:center" /></td>'
                                            + '<td colspan="4">'
                                            + '<input type="text" id="sjclmc_'+sjclid+'" name="clmc" value="' + sjclmc + '"  /></td>'
                                            + '<td colspan="2">'
                                            +'<select name="cllx" id="cllx_'+sjclid+'"> <option value="1" >原件</option> <option value="2">复印件</option></select>'
                                            + '</select></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="fs_'+sjclid+'" name="fs" value="' + fs + '" style="text-align:center" /></td>'
                                            + '<td colspan="1">'
                                            + '<input type="text" id="ys_'+sjclid+'" name="ys" value="' + ys + '" style="text-align:center" /></td>'
                                            + '<th colspan="2">'
                                            // +'<a class="detail" href="javascript:editSjcl('+sjclid+')">详细</a>'
                                            +'<a id="sc_${sjcl.SJCLID!}"onclick="fileUp('+sjclmc+')">'
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
function sjclAdd(){
    var proid = $("#proid").val();
    var sjxxid = $("#sjxxid").val();
    $.ajax({
        type:'post',
        url:'/estateplat-server/bdcdjSlxx/creatSjcl',
        data:{proid: proid,wiid:wiid},
        success: function (data) {
            if (data.msg == "success") {
                $.ajax({
                    type:'post',
                    url:'/estateplat-server/bdcdjSjdxx/sjclAdd',
                    data:{proid: proid,wiid:wiid,sjxxid:sjxxid},
                    success: function (data) {
                        if (data.msg == "success") {
                            var json = JSON.parse(data.bdcSjcl);
                            var html="";
                                var sjclid= json.sjclid;
                                var rownum= json.xh;
                                var maxnum=rownum+num;
                                var sjclmc= json.clmc;
                                var fs= json.fs;
                                var ys= json.ys;
                                var context= '<tr id="tr_'+sjclid+'">'
                                    + '<input type="hidden" id="sjclid" name="sjclid" value="' + sjclid + '" />'
                                    + '<td colspan="1">'
                                    +'<input role="checkbox" id="checkbox" name="checkbox" class="cbox" type="checkbox"value="' + sjclid + '"></td>'
                                    + '<td colspan="1">'
                                    + '<input type="text" id="sjcl_xh_'+sjclid+'" name="xh" value="' + maxnum + '" style="text-align:center" /></td>'
                                    + '<td colspan="4">'
                                    + '<input type="text" id="sjclmc_'+sjclid+'" name="clmc" value="' + sjclmc + '"  /></td>'
                                    + '<td colspan="2">'
                                    +'<select name="cllx" id="cllx_'+sjclid+'"> <option value="1" >原件</option> <option value="2">复印件</option></select>'
                                    + '</select></td>'
                                    + '<td colspan="1">'
                                    + '<input type="text" id="fs_'+sjclid+'" name="fs" value="' + fs + '" style="text-align:center" /></td>'
                                    + '<td colspan="1">'
                                    + '<input type="text" id="ys_'+sjclid+'" name="ys" value="' + ys + '" style="text-align:center" /></td>'
                                    + '<th colspan="2">'
                                    // +'<a class="detail" href="javascript:editSjcl('+sjclid+')">详细</a>'
                                    +'<a id="sc_${sjcl.SJCLID!}"onclick="fileUp('+sjclmc+')">'
                                    +'<i class="ace-icon glyphicon glyphicon-arrow-up"></i></a>'
                                    + '</th></tr>';
                                ++num;
                                html=html+context;
                            $('#sjcl').append(html);
                        }
                    },
                    error: function (data) {
                        alert("创建收件材料失败！");
                    }
                });
            }
        },
        error: function (data) {
            alert("创建收件材料失败！");
        }
    });
}

function  saveSjclList() {
    $.blockUI({message: "请稍等......"});
    var arry=[];
    $("input[name='sjclid']").each(function(i,n){
        var id=$(n).val();
        var o=new Object();
        o['sjclid']=$(n).val();
        o['sjxxid']=$("#sjxxid").val();
        o['xh']=$("#sjcl_xh_"+id).val();
        o['clmc']=$("#sjclmc_"+id).val();
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
                    alert("保存成功!");
                }
            }
        },
        error: function (data) {
            alert("保存收件材料失败!");
        }
    });
}