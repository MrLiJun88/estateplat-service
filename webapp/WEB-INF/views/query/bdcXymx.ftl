<@com.html title="不动产项目查询" import="ace,public">
<style>
    body {
        background-color: #fff;
    }

    .title {
        text-align: center;
        font-size: 18pt;
        font-family: 微软雅黑;
        padding-top: 20px;
    }

    table {
        margin: auto;
    }

    td {
        width: 90px;
        text-align: center;
    }

    tr {
        height: 35px;
    }

    td > input {
        width: 100%;
        border-width: 0px;
        text-align: center;
    }

    td > input:focus {
        outline: none;
    }

    .titleColor {
        background-color: rgb(238, 238, 238);
    }

    .btn-position {
        text-align: center;
        padding-top: 20px;
    }
</style>
<script type="text/javascript">
    $(function () {
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据", width: "100%"});
        //resize the chosen on window resize
        $(window).on('resize.chosen', function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
            $.each($('.chosen-single-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var e = $(obj).parent().width();
                $(obj).next().css("width", e);
            })
        }).trigger('resize.chosen');
        $("input").height($(this).parent().height());

        //把值放入输入框
        var qlrzjlx = "${(bdcXygl.qlrsfzjzl)!''}";
        $("#qlrzjlx").find("option[value='" + qlrzjlx + "']").prop("selected", 'selected');
        $("#qlrzjlx").trigger("chosen:updated");


        $("clearXymxZt").click(function () {
            var ids = $("#xymxid").val();
            var resaon = $("#reason").val();
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/changeZtToHs",
                type: "post",
                data: {xymxid: ids, reason: resaon},
                success: function (data) {
                    alert(data);
                    $("#cleaZt").reset();
                    $("#cleaZt").hide();

                },
                error: function (data) {
                    alert(data);
                }
            })
        });

        $("#save").click(function () {
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/saveData",
                type: "get",
                data: $("#tableForm").serialize(),
                success: function (data) {
                    if (data && data != 'undefined' && data == 'success') {
                        alert("保存成功");
                        opener.tableReload("grid-table", "${bdcdjUrl}/bdcXygl/getBdcXyglListJsonByPage?", {dcxc: ""});
                        window.close();
                    } else {
                        alert("保存失败");
                    }
                },
                error: function (data) {
                    alert(data);
                }
            })
        });

        $("#sure").click(function () {
            var ids = "${(bdcXymx.xymxid)!''}";
            if (ids == '') {
                alert("没有相关信用明细记录");
                return;
            }
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/changeZtToXs?xymxid=" + ids,
                type: "get",
                success: function (data) {
                    alert(data);
                }
            });
        });

        $("#clear").click(function () {
            var ids = "${(bdcXymx.xymxid)!''}";
            if (ids == '') {
                alert("没有相关信用明细记录");
                return;
            }
            $("#cleaZt").show();
        });

        $("#clearXymxZt").click(function () {
            var ids = "${(bdcXymx.xymxid)!''}";
            var resaon = $("#reason").val();
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/changeZtToHs",
                type: "post",
                data: {xymxid: ids, reason: resaon},
                success: function (data) {
                    alert(data);
                    $("#cleaZtForm")[0].reset();
                    $("#cleaZt").hide();
                },
                error: function (data) {
                    alert(data);
                }
            })
        });
    });

    function closeDialog(id) {
        var x = '#' + id;
        $(x).hide();
        clearAddData();
    }
</script>
<div>
    <div class="title">信用记录</div>
    <div class="btn-position" style="margin-left: 420px;margin-bottom: 4px">
        <button class="btn btn-sm btn-grey" style="margin-right: 5px" id="sure">确认</button>
        <button class="btn btn-sm btn-grey" id="clear">清除</button>
    </div>
    <form id="tableForm">
        <input type="hidden" id="xyglid" name="xyglid" value="${(bdcXygl.xyglid)!''}">
        <input type="hidden" id="xymxid" name="xymxid" value="${(bdcXymx.xymxid)!''}">
        <table border="1" bgcolor="#000" cellspacing="0">
            <tr style="height: 0px">
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td class="titleColor">权利人</td>
                <td colspan="2">
                    <input id="qlr" name="qlrmc" value="${(bdcXygl.qlrmc)!''}">
                </td>
                <td class="titleColor">通讯地址</td>
                <td colspan="2">
                    <input id="txdz" name="qlrtxdz" value="${(bdcXygl.qlrtxdz)!''}">
                </td>
            </tr>
            <tr>
                <td class="titleColor">证件类型</td>
                <td colspan="2">
                    <select id="qlrzjlx" name="qlrsfzjzl" class="chosen-select">
                        <option value="">请选择证件类型</option>
                        <#list qlrzjhList as qlrzjh>
                            <option value="${qlrzjh.dm!}">${qlrzjh.mc!}</option>
                        </#list>
                    </select>
                </td>
                <td class="titleColor">证件编号</td>
                <td colspan="2">
                    <input id="zjbh" name="qlrzjh" value="${(bdcXygl.qlrzjh)!''}">
                </td>
            </tr>
            <tr>
                <td colspan="6" class="titleColor">内容</td>
            </tr>
            <tr>
                <td colspan="6">
                    <input id="nr" name="nr" value="${(bdcXymx.nr)!''}">
                </td>
            </tr>
            <tr>
                <td class="titleColor">备注</td>
                <td colspan="6">
                    <input id="bz" name="bz" value="${(bdcXygl.bz)!''}">
                </td>
            </tr>
        </table>
    </form>
    <div class="btn-position">
        <#if type!='view'>
            <button id="save" class="btn btn-sm btn-grey">保存</button>
        </#if>
    </div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="cleaZt">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>新增</h4>
                <button type="button" id="sdHide" class="proHide" onclick="closeDialog('cleaZt')"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="cleaZtForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>解除生效原因：</label>
                        </div>
                        <div class="col-xs-8">
                            <input type="text" name="reason" id='reason' class="form-control">
                        </div>
                        <div class="col-xs-2" style="display: none;">
                            <input type="hidden" name="xymxid" id='xymxid'>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="clearXymxZt">消除</button>
            </div>
        </div>
    </div>
</div>

</@com.html>