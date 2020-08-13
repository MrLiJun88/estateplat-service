<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveQl(){
        if ($mulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var proid = "";
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var bdcXmRelList = "";
        var bdcdyhs = "";
        var yxmids = "";
        var bdcdyh = "";
        for (var i = 0; i < $mulData.length; i++) {
            bdcXmRelList += "&bdcXmRelList[" + i + "].proid=" + proid;
            yxmids += "&yxmids[" + i + "]=" + $mulData[i].PROID;
            bdcXmRelList += "&bdcXmRelList[" + i + "].yproid=" + $mulData[i].PROID + "&bdcXmRelList[" + i + "].ydjxmly=1";
            bdcdyhs += "&bdcdyhs[" + i + "]=" + $mulData[i].BDCDYH;
        }
        $("#bdcdyh").val($mulData[0].BDCDYH);
        $("#yxmid").val($mulData[0].PROID);
        creatBdcXm(yxmids, bdcXmRelList);
    }
    function addQl(){
        var bdcdyid=$("#bdcdySelect  option:selected").val();
        creatBdcXmForAdd(bdcdyid);
    }
    function glQl(yproid,bdcdyh){
        var proid = "";
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var bdcXmRelList = "";
        var bdcdyhs = "";
        var yxmids = "";
            bdcXmRelList += "&bdcXmRelList[0].proid=" + proid;
            yxmids += "&yxmids[0]=" + yproid;
            bdcXmRelList += "&bdcXmRelList[0].yproid=" + yproid + "&bdcXmRelList[0].ydjxmly=1";
            bdcdyhs += "&bdcdyhs[0]=" + bdcdyh;
        $("#bdcdyh").val(bdcdyh);
        $("#yxmid").val(yproid);
        creatBdcXm(yxmids, bdcXmRelList);
    }
    function qllxCz(cellvalue, options, rowObject) {
        return '<div >' +
                '<div><button type="button"  class="btn btn-primary" onclick="glQl(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\')">关联</button></div>' +
                '</div>'
    }
    function idChange(){
        var id =  $("#bdcdySelect  option:selected").val();
        var dataUrl = "${bdcdjUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}&bdcdyid="+id;
        tableReload("qlxx-grid-table", dataUrl, '', '', '');
    }
    function tableReload(table, Url, data, colModel, loadComplete) {
        var index = 0;
        var jqgrid = $("#" + table);
        if (colModel == '' && loadComplete == '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        } else if (loadComplete == '' && colModel != '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel});
        } else if (loadComplete != '' && colModel != '') {
            jqgrid.setGridParam({
                url: Url,
                datatype: 'json',
                page: 1,
                postData: data,
                colModel: colModel,
                loadComplete: loadComplete
            });
        } else if (loadComplete != '' && colModel == '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, loadComplete: loadComplete});
        }

        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function getSqlxByDjlxAndBdclx() {
        var djlx = $("#djlxSelect  option:selected").text();
        var bdclx = $("#bdclx").val();
        var bdcdyid = $("#bdcdySelect  option:selected").val();
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcdjQlxx/getBdclxByBdcdyid",
            data: {bdcdyid: bdcdyid},
            dataType: "json",
            success: function (data) {
                if (data != null && data != '') {
                    bdclx = data.bdclx;
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/bdcJgSjgl/getSqlxByDjlx",
                        data: {djlx: djlx, bdclx: bdclx},
                        dataType: "json",
                        success: function (result) {
                            //清空
                            $("#sqlxSelect").html("");
                            if (result != null && result != '') {
                                $.each(result, function (index, data) {
                                    if (data.DM == "24D768DE8B8F4CD59F70E621C2CAB2E2" || data.DM == "19D3CDE088174478943341B32EF3238C")
                                        $("#sqlxSelect").append('<option value="' + data.DM + '" selected="selected">' + data.MC + '</option>');
                                    else
                                        $("#sqlxSelect").append('<option value="' + data.DM + '" >' + data.MC + '</option>');
                                })
                            }
                            $("#sqlxSelect").trigger("chosen:updated");
                            var sqlx = $("#sqlxSelect").val();
                        },
                        error: function (data) {
                        }
                    });
                }
            },
            error: function (data) {
                }
            });
    }
    //修改项目信息的函数
    function ywsjEditXm(yxmids, bdcdyhs, bdcXmRelList) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }

        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/wfProject/checkMulBdcXm?proid=' + proid + bdcdyhs + yxmids + bdcXmRelList,
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                if (data.length > 0) {
                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    var alertCount = 0;
                    $.each(data, function (i, item) {
                        if (item.checkModel == "alert") {
                            alertCount++;
                        }
                    })

                    $.each(data, function (i, item) {
                        if (item.checkModel == "confirm") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "confirmAndCreate") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if(item.checkCode=='199'){
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            }else{
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    })
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
                if (alertSize == 0 && confirmSize == 0) {
                    creatBdcXm(proid, bdcXmRelList);
                } else if (alertSize == 0 && confirmSize > 0) {
                    $("span[name='hlBtn']").click(function () {
                        $(this).parent().remove();
                        if ($("#csdjConfirmInfo > div").size() == 0) {
                            creatBdcXm(proid, bdcXmRelList);
                        }
                    })
                }
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                $("#modal-backdrop-mul").hide();
            }
        };
        $.ajax(options);
    }

    //创建不动产项目
    function creatBdcXm(yxmids, bdcXmRelList) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        $("#sqlx").val($("#sqlxSelect  option:selected").text());
        $("#djlx").val($("#djlxSelect  option:selected").val());
        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/bdcdjQlxx/creatBdcXm',
            type: 'post',
            dataType: 'json',
            data: $("#slxxForm").serialize(),
            success: function (data) {
                    ywsjInitVoFromOldData(proid, bdcXmRelList);
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }

    //创建不动产项目(新增权利)
    function creatBdcXmForAdd(bdcdyid) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        $("#bdcdyid").val(bdcdyid);
        $("#sqlx").val($("#sqlxSelect  option:selected").text());
        $("#djlx").val($("#djlxSelect  option:selected").val());
        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/bdcdjQlxx/creatQlxx',
            type: 'post',
            dataType: 'json',
            data: $("#slxxForm").serialize(),
            success: function (data) {
                window.parent.hideModel();
                window.parent.resourceRefresh();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }

    function ywsjInitVoFromOldData(proid, bdcXmRelList) {
        $.ajax({
            type: 'get',
            url: '${bdcdjUrl}/bdcdjQlxx/initVoFromOldData?proid=' + proid + bdcXmRelList,
            success: function (data) {
                if (data == '成功') {
                    $(".mulSelectPop").parent().hide();
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                } else {
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    alert(data);
                }
                $("#modal-backdrop-mul").hide();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#modal-backdrop-mul").hide();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }
            }
        });
    }
</script>
    <@f.contentDiv title="新增权利页面">
    <!--错误提示-->
    <div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
        <div class="modal-dialog tipPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                    <button type="button" id="tipHide" class="proHide"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <div id="csdjAlertInfo"></div>
                    <div id="csdjConfirmInfo"></div>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
                </div>
            </div>
        </div>
    </div>
        <@f.form id="slxxForm" name="slxxForm">
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="bdclx" name="bdclx"  value="${bdclx!}"/>
            <@f.hidden id="sqlx" name="sqlxMc"/>
            <@f.hidden id="djlx" name="djlx"/>
            <@f.hidden id="bdcdyh" name="bdcdyh"/>
            <@f.hidden id="bdcdyid" name="bdcdyid"/>
            <@f.hidden id="yxmid" name="yxmid"/>
            <@f.table style="width: 100%;margin: 0px 0px 10px 0px;">
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元选择"></@f.label>
                        </@f.th>
                    <@f.td>
                        <@f.select id="bdcdySelect" name="bdcdySelect"   showFieldName="ZL" valueFieldName="ID" source="bdcdyList" handler="idChange"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                        </@f.th>
                    <@f.td>
                        <@f.select id="djlxSelect" name="djlxSelect"   showFieldName="MC" valueFieldName="DM" source="djlxList" handler="getSqlxByDjlxAndBdclx"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="申请类型"></@f.label>
                        </@f.th>
                    <@f.td>
                        <@f.select id="sqlxSelect" name="sqlxSelect"   showFieldName="MC" valueFieldName="DM" source="sqlxList"/>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
        <section id="sqrxx">
            <div class="row">
                <div class="col-xs-2 demonstrative">关联权利信息</div>
            </div>
        <#--列表按钮-->
            <@p.toolBars>
                <@p.toolBar text="确定" handler="saveQl" iClass="ace-icon fa fa-file-o"/>
                <@p.toolBar text="新增" handler="addQl" iClass="ace-icon fa fa-file-o"/>
            </@p.toolBars>
            <@p.list tableId="qlxx-grid-table" pageId="qlxx-grid-pager" keyField="QLID" dataUrl="" rowNum="3"  multiboxonly="false"multiselect="true">
                <@p.field fieldName="XH" header="序号" width="5%"/>
                <@p.field fieldName="BDCDYH" header="不动产单元号" width="25%"/>
                <@p.field fieldName="QLLX" header="权利类型" width="25%"/>
                <@p.field fieldName="ZL" header="坐落" width="30%"/>
                <@p.field fieldName="CZ" header="操作" width="10%"   renderer="qllxCz"/>
                <@p.field fieldName="PROID" header="PROID" hidden="true"/>
                <@p.field fieldName="QLID" header="QLID" hidden="true"/>
            </@p.list>
            <table id="qlxx-grid-table"></table>
            <div id="qlxx-grid-pager"></div>
        </section>
    </@f.contentDiv>
</@com.html>