<@com.html title="楼盘表" import="ace,init,public">
<style type="text/css">
    .breadcrumb {
        border: 0px;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    select {
        width: 100%;
    }

    .center, .align-center {
        text-align: center !important;
    }

    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

    /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    /*移动modal样式*/
    #tipPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    .alert {
        font-size: 12px;
        border-radius: 4px;
        padding: 5px;
        margin-bottom: 5px;
    }

    /*移动modal样式*/
    #ywsjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    /*高级搜索的样式修改*/

    /*去掉表格横向滚动条*/
    .ui-jqgrid-bdiv {
        overflow-x: hidden !important;
    }

    /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
    }

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }
</style>
<script type="text/javascript">
    var mulSelectJson = {};
    if ($.cookie('lpb_cookie') != null) {
        mulSelectJson = JSON.parse($.cookie('lpb_cookie'));
               // alert($.cookie('lpb_cookie'));
    }

    $(document).ready(function () {
        //去掉遮罩
        setTimeout($.unblockUI, 10);
    });

    var replaceStr = "^&*";//全局变量，用来代替空的不动产单元号存储在bs中，与fjhs对应顺序，来提醒用户那些房间没有不动产单元号
    function queryZh(lszd, zh) {
        location.href = "${bdcdjUrl}/lpb/lpb?lszd=" + lszd + "&zh=" + zh + "&proid=${proid!}";
    }

    function chooseBdcdy(proid, bdclx, bdcdyh, fjh, a, djid) {
        var checkZmd =${chcekZmd?c};
        if (checkZmd) {
            var checkResult = checkIfZmd(bdcdyh);
            if (checkResult == 'false') {
                alert('该户室未发信息表！');
                return false;
            }
        }
        selectOneHs(a);
        $("#proid").val(proid);
        $("#bdclx").val(bdclx);
        $("#bdcdyh").val(bdcdyh);
        $("#djid").val(djid);
        $("#fjh").val(fjh);
        if ("${showQl!}" == "true" && "${openQlWay!}" != "win") {
            showQl(proid, bdclx, bdcdyh, fjh, djid);
        } else if ("${showQl!}" == "true") {
            showQlWin(proid, bdclx, bdcdyh, fjh, djid);
        }

        var ljzName="${fwmc!}"+"(${lszd!})"
        if(mulSelectJson!=null&&mulSelectJson[ljzName]){
            if(mulSelectJson[ljzName].property.length==0){
                delete mulSelectJson[ljzName];
                if($.isEmptyObject(mulSelectJson )==true){
                    $.cookie('lpb_cookie', '', { expires: -1 });
                }
                else{
                    $.cookie('lpb_cookie', JSON.stringify(mulSelectJson));
                }
            }
        }
    }

    function checkIfZmd(bdcdyh) {
        if (bdcdyh == '' || bdcdyh == 'undefined') {
            tipInfo('不动产单元号为空！');
            return false;
        }
        var checkResult = '';
        var options = {
            url: '${bdcdjUrl}/lpb/checkIfZmd',
            type: 'post',
            async: false,
            dataType: 'json',
            data: {bdcdyh: bdcdyh},
            success: function (data) {
                checkResult = data ? 'true' : 'false';
            },
            error: function (data) {
                return false;
            }
        };
        $.ajax(options);
        return checkResult;
    }

    //修改项目信息的函数
    function djsjEditXm(id, bdclx, bdcdyh) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var options = {
            url: '${bdcdjUrl}/wfProject/checkBdcXm',
            type: 'post',
            dataType: 'json',
            data: {proid: proid, bdcdyh: bdcdyh, djId: id},
            success: function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                if (data.length > 0) {
                    var islw = false;
                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                    $.each(data, function (i, item) {
                        if (item.checkModel == "confirm") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if (isNotBlank(item.wiid)) {
                                islw = true;
                                confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                            } else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    })
                    if (!islw) {
                        $("#tipPop").show();
                        $("#modal-backdrop").show();
                    }
                }
                if (alertSize == 0 && confirmSize == 0) {
                    djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                } else if (alertSize == 0 && confirmSize > 0) {
                    $("span[name='hlBtn']").click(function () {
                        $(this).parent().remove();
                        if ($("#csdjConfirmInfo > div").size() == 0) {
                            djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                        }
                    })
                }
            },
            error: function (data) {

            }
        };
        $.ajax(options);
    }

    //zdd 选择不多不动产单元修改项目信息
    function mulSelectDjsjEditXm() {
        //遮罩
        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/wfProject/checkBdcXm',
            type: 'post',
            dataType: 'json',
            data: $("#projectForm").serialize(),
            success: function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                if (data.length > 0) {
                    var islw = false;
                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                    $.each(data, function (i, item) {
                        if (item.checkModel == "confirm") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if (isNotBlank(item.wiid)) {
                                islw = true;
                                confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                            } else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    })
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    if (!islw) {
                        $("#tipPop").show();
                        $("#modal-backdrop").show();
                    }
                }
                if (alertSize == 0 && confirmSize == 0) {
                    mulSelectDjsjInitVoFromOldData();
                } else if (alertSize == 0 && confirmSize > 0) {
                    $("span[name='hlBtn']").click(function () {
                        $(this).parent().remove();
                        if ($("#csdjConfirmInfo > div").size() == 0) {
                            mulSelectDjsjInitVoFromOldData();
                        }
                    })
                }
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }

    function openProjectInfo(proid) {
        if (proid && proid != undefined) {
            $.ajax({
                url: "${bdcdjUrl}/qllxResource/getViewUrl?proid=" + proid,
                type: 'post',
                success: function (data) {
                    if (data && data != undefined) {
                        openWin(data);
                    }
                },
                error: function (data) {
                    tipInfo("查看失败！");
                }
            });
        }
    }

    function djsjInitVoFromOldData(proid, id, bdclx, bdcdyh) {
        //                   独幢或者是多幢时直接创建 返回的是不动产单元号
        if (bdcdyh != null && bdcdyh != "") {
            $.ajax({
                type: 'get',
                url: '${bdcdjUrl!}/zydjBdcdy/getYxmid?proid=' + proid + "&bdcdyh=" + bdcdyh,
                success: function (data) {
                    if (data != 'error') {
                        $.ajax({
                            type: 'get',
                            url: '${bdcdjUrl!}/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh + "&yxmid=" + data,
                            success: function (data) {

                                if (data == '成功') {
//                    opener.location.reload();
//                    window.close();
                                    getBdcXtConfigFj();
                                    hideModelAndRefreshParant();
                                } else {
                                    alert(data);
                                }
                            },
                            error: function (_ex) {
                                alert("保存失败!失败原因:" + _ex);
                            }
                        });
                    } else {
                        alert("该不动产单元号" + bdcdyh + "没有不动产权证！");
                    }
                },
                error: function (_ex) {
                    alert("保存失败!失败原因:" + _ex);
                }
            });
        } else {
            alert("没有不动产单元号");
        }
    }

    function hideModelAndRefreshParant() {
        window.parent.hideModel();
        window.parent.resourceRefresh();
    }

    function getBdcXtConfigFj() {
        $.ajax({
            type: 'get',
            url: '${bdcdjUrl!}/wfProject/getBdcXtConfigFj?wiid=' + '${wiid!}',
            datatype: 'json',
            success: function () {
                alert("成功");
            },
            error: function () {
            }
        })
    }

    function mulSelectDjsjInitVoFromOldData() {
        //                   独幢或者是多幢时直接创建 返回的是不动产单元号
        $.unblockUI();
        var fjh = $("#fjhs").val();
        var bs = $("#bs").val();
        var fjhs = fjh.split("${splitStr!}");
        var bss = bs.split("${splitStr!}");
        var alertinfo = "户室";
        var isalert = true;
        /*  if(fjhs.length > bss.length){
              alertinfo="";
              isalert=false;
          }else{
              for(var i=0;i<fjhs.length;i++){
                  if(bss[i]==replaceStr){
                      alertinfo=alertinfo+"["+fjhs[i]+"]";
                      isalert=false;
                  }
              }
          }*/
        if (isalert) {
            //遮罩
            $.blockUI({message: "请稍等……"});
            $.ajax({
                type: 'post',
                data: $("#projectForm").serialize(),
                url: '${bdcdjUrl!}/wfProject/initVoFromOldData',
                success: function (data) {

                    if (data == '成功') {
                        //                    opener.location.reload();
                        //                    window.close();
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();

                    } else {
                        alert(data);
                    }
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                },
                error: function (_ex) {
                    //去掉遮罩
                    //alert("error");
                    setTimeout($.unblockUI, 10);
                    alert("保存失败!失败原因:" + _ex);
                }
            });
        } else {
            alertinfo = alertinfo + "没有不动产单元号";
            alert(alertinfo);
        }
    }

    function createWf() {
        var proid = $("#proid").val();
        var bdclx = $("#bdclx").val();
        var bdcdyh = $("#bdcdyh").val();
        var djid = $("#djid").val();
        var fjh = $("#fjh").val();
        var mulSelect = "${mulSelect!}";

        if (mulSelect == "true")
            mulSelectDjsjEditXm();
        else
            djsjEditXm(djid, bdclx, bdcdyh);

    }

    function backZdList() {
        var type = "${type!}";
        if (type == "ljz") {
            window.location.href = "${bdcdjUrl!}/lpb/selectLjz?proid=${proid!}&mulSelect=${mulSelect!}";
        } else if (type == "lpb") {
            window.location.href = "${bdcdjUrl!}/lpb/queryZdList?proid=${proid!}&mulSelect=${mulSelect!}";
        } else {
            window.location.href = "${bdcdjUrl!}/lpb?openQlWay=${openQlWay!}&dcxc=" + encodeURI(encodeURI("${dcxc!}"));
        }

    }

    $(function () {
        if ("${showQl!}" == "true" && "${openQlWay!}" != "win") {
            var width = $(window).width() / 1.4;
            $("#ace-settings-box").css("width", width).css("height", "850px");
            $(window).resize(function () {
                var width = $(window).width() / 1.4;
                $("#ace-settings-box").css("width", width).css("height", "850px");
            })
        }
        $("#djsjHide,#ywsjHide,#tipHide,#tipCloseBtn").click(function () {
            if (this.id == "djsjHide") {
                $("#djsjSearchPop").hide();
                $("#djsjSearchForm")[0].reset();
            } else if (this.id == "ywsjHide") {
                $("#ywsjSearchPop").hide();
                $("#ywsjSearchForm")[0].reset();
            } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
                $("#modal-backdrop").hide();
                $("#tipPop").hide();
            }
        });
        $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    });

    //查看权利
    function showQl(proid, bdclx, bdcdyh, fjh) {
        var djbid = "${djbid!}";
        var iframeDiv = document.getElementById("iframe");
        //存储选中的户室url
        var newIframe = $("#newIframeUrl");
        if (bdcdyh != null && bdcdyh != "") {
            newIframe.val("${bdcdjUrl!}/bdcDjb/showQL?bdcdyh=" + bdcdyh + "&fjh=" + fjh + "djbid=" + djbid + "&djid=" + djbid);
        } else {
            newIframe.val("");
            alert("没有不动产单元号");
        }

    }

    function showQlModel() {
        var iframeDiv = document.getElementById("iframe");
        var newSrc = $("#newIframeUrl").val();
        if (!$("#ace-settings-box").hasClass("open") && iframeDiv.src != newSrc) {
            iframeDiv.src = newSrc;
        }
    }

    function showQlWin(proid, bdclx, bdcdyh, fjh, djid) {
        if (bdcdyh == null || bdcdyh == "")
            bdcdyh = $("#dzBdcdyh").val();
        if (bdcdyh != null && bdcdyh != "")
            openWin("${bdcdjUrl!}/bdcDjb/showQL?bdcdyh=" + bdcdyh + "&fjh=" + fjh + "&djid=" + djid + "&showFcfht=${showFcfht!}&djbid=${djbid!}", "户室信息");

        else {
            alert("没有不动产单元号");
        }
    }

    //选中一个单元
    function selectOneHs(a) {
        var mulSelect = "${mulSelect!}";
        var isFrist = $("#isFrist").val();
        if (isFrist == "true") {
            if ($(".active").length > 0)
                $(".active").removeClass("active");
            $("#djIds").val('');
            $("#bdcdyhs").val('');
        }

        if (mulSelect == "true") {
            //多选
            if (a != null && a.className.indexOf("active") < 0) {
//                alert(a.title);
                //没有选中处理
                $(a).addClass("active");
                changeBdcdyhs();
            } else if (a != null) {
                //选中处理
                $(a).removeClass("active");
                changeBdcdyhs();
            }

        } else if (a != null) {
            if ($(".active").length > 0)
                $(".active").removeClass("active");
            $(a).addClass("active");
            $("#djIds").val('');
            $("#bdcdyhs").val('');
        }
        $("#isFrist").val("false");
    }

    //选中一行
    function selectRowHs(a) {
        var mulSelect = "${mulSelect!}";
        var isFrist = $("#isFrist").val();
        if (isFrist == "true") {
            if ($(".active").length > 0)
                $(".active").removeClass("active");
            $("#djIds").val('');
            $("#bdcdyhs").val('');
        }

        if (mulSelect == "true") {
            //多选
            if (a != null && a.className.indexOf("active") < 0) {
//                alert(a.title);
                //没有选中处理
                var $td = $(a).parents('tr').children('td');
                $td.addClass("active");
                changeBdcdyhs();
            } else if (a != null) {
                //选中处理
                var $td = $(a).parents('tr').children('td');
                $td.removeClass("active");
                changeBdcdyhs();
            }

        } else if (a != null) {
            if ($(".active").length > 0)
                $(".active").removeClass("active");
            $(a).addClass("active");
            $("#djIds").val('');
            $("#bdcdyhs").val('');
        }
        $("#isFrist").val("false");
    }

    //改变不动产单元值
    function changeBdcdyhs() {
        var splitStr = "${splitStr!}";
        var djIds = '';
        var bdcdyhs = '';
        var bs = '';
        var fjhs = '';
        var bdclx = 'TDFW';
        var hsArray=new Array();
        if ($(".active").length > 0) {
            for (var i = 0; i < $(".active").length; i++) {
                var hsxx="";
                if (("djid") in ($(".active")[i].attributes)) {
                    if (djIds != null && djIds != "") {
                        djIds = djIds + "${splitStr!}" + $(".active")[i].attributes.djId.value;
                    } else {
                        djIds = $(".active")[i].attributes.djId.value;
                    }
                    if (fjhs != null && fjhs != "") {
                        fjhs = fjhs + "${splitStr!}" + $(".active")[i].innerHTML;
                    } else {
                        fjhs = $(".active")[i].innerHTML;
                    }
                    hsxx=$(".active")[i].attributes.djid.value;
                }
                if (("id") in ($(".active")[i].attributes)) {
                    if (bdcdyhs != null && bdcdyhs != "") {
                        bdcdyhs = bdcdyhs + "${splitStr!}" + $(".active")[i].id;
                    } else {
                        bdcdyhs = $(".active")[i].id;
                    }
                    if (bs != null && bs != "") {
                        if ($(".active")[i].id == null || $(".active")[i].id == "") {
                            bs = bs + "${splitStr!}" + replaceStr;
                        } else {
                            bs = bs + "${splitStr!}" + $(".active")[i].id;
                        }
                    } else {
                        if ($(".active")[i].id == null || $(".active")[i].id == "") {
                            bs = replaceStr;
                        } else {
                            bs = $(".active")[i].id;
                        }
                    }
                    hsxx=hsxx+"${splitStr!}"+$(".active")[i].id;
                }
                hsArray.push(hsxx)
            }
        }

        $("#fjhs").val(fjhs);

        $("#djIds").val(djIds);

        $("#bs").val(bs);

        $("#bdcdyhs").val(bdcdyhs);
        $("#bdclx").val(bdclx);

        if(hsArray!=null){
            var ljzName="${fwmc!}"+"(${lszd!})"
            addSelectJson(mulSelectJson,ljzName,"${dcbId!}","item","false",hsArray);
        }
    }

    function selectRow(proid, bdclx, bdcdyh, fjh, a, djid) {
        selectRowHs(a);
        $("#proid").val(proid);
        $("#bdclx").val(bdclx);
        $("#bdcdyh").val(bdcdyh);
        $("#djid").val(djid);
        $("#fjh").val(fjh);

    }

    function selectAll() {
        var mulSelect = "${mulSelect!}";
        if (mulSelect == "true") {
            //多选,没有选中处理
            var tdArray = new Array();
            tdArray = $("td[id][id!='']");
            if (tdArray != null && tdArray.length > 0) {
                for (var i = 0; i < tdArray.length; i++) {
                    var td = $("#" + tdArray[i].id);
                    td.addClass("active");
                    //td.prepend("<i class=\"ace-icon fa fa-check\" style=\"margin-right:10%\"></i>");
                }
            }
            changeBdcdyhs();
        }
    }

    function cancelSelectAll() {
        var mulSelect = "${mulSelect!}";
        if (mulSelect == "true") {
            //多选,没有选中处理
            var tdArray = new Array();
            tdArray = $("td[id][id!='']");
            if (tdArray != null && tdArray.length > 0) {
                for (var i = 0; i < tdArray.length; i++) {
                    var td = $("#" + tdArray[i].id);
                    td.removeClass("active");
                    //td.children('i').remove();
                }
            }
            changeBdcdyhs();
        }
    }
</script>
<div class="main-container">
    <form id="projectForm">
        <input type="hidden" id="proid" name="proid" value="${proid!}"/>
        <input type="hidden" id="bdclx" name="bdclx"/>
        <input type="hidden" id="bdcdyh" name="bdcdyh"/>
        <input type="hidden" id="djid" name="djid"/>
        <input type="hidden" id="fjh" name="fjh"/>
        <input type="hidden" id="djIds" name="djIds" value="${djIds!}"/>
        <input type="hidden" id="bdcdyhs" name="bdcdyhs" value="${bdcdyhs!}"/>
        <input type="hidden" id="dzBdcdyh" name="dzBdcdyh" value="${dzBdcdyh!}"/>
    </form>
    <input type="hidden" id="fjhs" name="fjhs"/><#--用来存储所有选中的房间号，与bs对应顺序，来提醒用户那些房间没有不动产单元号-->
    <input type="hidden" id="bs" name="bs"/><#--用来存储所有选中的不动产单元号(空值用特殊字符串变量replaceStr代替)，与fjhs对应顺序，来提醒用户那些房间没有不动产单元号-->
    <input type="hidden" id="isFrist" name="isFrist" value="true"/>
    <input type="hidden" id="splitStr" name="splitStr" value="${splitStr!}"/>
<#--权利信息-->
    <#if showQl=="true" && openQlWay!="win">
        <div class="ace-settings-container" id="ace-settings-container">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn"
                 onclick="showQlModel()">
                <i class="ace-icon fa fa-globe bigger-150"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <input type="hidden" id="newIframeUrl">
                <iframe src="" style="width: 100%;height: 100%" id="iframe" frameborder="0" scrolling="no"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
    </#if>
    <!--  业务内容  -->
    <div class="row-fluid searchPop">

        <!--  右侧内容部分  -->
        <div class="col-xs-12">
            <!--  步骤条01  -->
            <#if isNotWf!="true">
                <div class="flow-steps-style1">
                    <ul class="unstyled">
                        <li class="first current-prev" style="width: 455px"><i class="icon-ok"></i><font>1.</font>查询项目名称
                        </li>
                        <li class="last current" style="width: 455px"><i
                                class="icon-edit icon-white"></i><font>2.</font>选择楼盘表
                        </li>
                    </ul>
                </div>
            </#if>
            <!--end 步骤条01  -->

            <!--  已选条件  -->
            <div class="TaobaoSearch">
                <div class="container-fluid">
                    <div class="row-fluid SelectedConditions">
                        <div style="margin-top: 6px;width: 200px;float: left;">
                            <label class="label1" style="padding: 0 40px 0 20px;">已选条件</label>
                        </div>
                        <div class="col-xs-8">
                            <ul class=" breadcrumb">
                                <li>小区名称：${fwmc!}</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--end  已选条件 -->
            <div style="position:relative; height:50px; width:100%;overflow:auto">
                <!--  楼盘表状态栏  -->
                <div class="houseStatus">
                    <ul>
                    <#--<li>
                        <i class="icon_none"></i> 未设权利
                    </li>
                    <li>
                        <i class="icon_green"></i> 已设权利
                    </li>
                    <li>
                        <i class="icon_orange"></i> 权利抵押
                    </li>
                    <li>
                        <i class="icon_red"></i> 权利查封
                    </li>-->
                        <#if bdcZdQlztList?size gt 0>
                            <#list bdcZdQlztList as qlzt>
                                <li>
                                    <i class="icon_red" style="background: ${qlzt.color!}"></i> ${qlzt.mc!}
                                </li>
                            </#list>
                        </#if>
                    </ul>
                </div>
                <!--end  楼盘表状态栏  -->
            </div>

            <!--  楼盘表  -->
            <div style="position:absolute; height:400px; width:100%;overflow:auto">

                <!--  固定格式表格  -->
                <form class="UItable houseTable" style="overflow: auto;width:100%">
                    <table cellpadding="0" cellspacing="0" border="0" class="">
                        <tbody>
                            ${tableTr!}
                        </tbody>
                    </table>
                </form>
                <!--end  固定格式表格  -->
                <div class="center" style="text-align: center">
                    <button class="btn01 btn01-primary" type="button" onclick="selectAll()">全选</button>
                    <button class="btn01 btn01-primary" type="button" onclick="cancelSelectAll()">取消全选</button>
                    <#if isNotBack!="true">
                        <button class="btn01 btn01-primary" type="button" onclick="backZdList()">上一步</button>
                    </#if>
                    <#if isNotWf!="true">
                        <button class="btn01 btn01-primary" type="button" onclick="createWf()">确定</button>
                    </#if>
                <#--<button class="btn01 " type="button">清除</button>-->
                </div>
            </div>
            <!--end  楼盘表  -->

        </div>
        <!--end  右侧内容部分  -->
    </div>
    <!--end  业务内容  -->
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo"></div>
                <div id="csdjConfirmInfo"></div>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
</@com.html>
