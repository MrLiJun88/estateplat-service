<@com.html title="模板配置" import="ace,select">
<style>
    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 80px;
    }

    textarea {
        resize: vertical;
        height: 150px !important;
        color: black !important;
    }

    .col-xs-4 {
        padding: 3px;
        width: 25%;
    }

    /*select样式重写 begin*/
    /*.btn-default, .btn:focus, .btn-default:focus,.btn-default:hover{
         border: 1px solid #d5d5d5!important;
         background-color: #fff!important;
         color: #000!important;
     }
     .btn-group>.btn {
         border-radius: 4px !important;
         height:30px;
     }
     .bootstrap-select.btn-group{
         margin-bottom: 0px!important;
     }
     .open .btn.dropdown-toggle, .open .btn-default.dropdown-toggle {
         border: 1px solid #d5d5d5!important;
         background-color: #fff!important;
     }*/
    /*select样式重写 end*/
</style>
<script>
    $(function () {
        $(".selectpicker").selectpicker();
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据", width: "100%"});
        //resize the chosen on window resize
        $(window).on('resize.chosen', function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
        }).trigger('resize.chosen');

        //提示水印添加
        var tempPlaceholder = "请填写模板内容\n注:需换行时可回车换行,替换的参数名前加@\n例如:分摊面积:@ftmj\n       房产证号:@fczh";
        var sqlPlaceholder = "请填写正确sql语句\n注:多条语句间用 ; 隔开,替换的参数名前加@,sql语句查询列不要用*\n例如:select bdcdyh from bdc_bdcdy where proid=@proid;select xmmc from bdc_xm where proid=@proid";
        $('#fjTempContent,#qtTempContent').val(tempPlaceholder);
        $('#fjDataSql,#qtDataSql').val(sqlPlaceholder);
        $('#fjTempContent,#fjDataSql,#qtTempContent,#qtDataSql').focus(function () {
            if (this.id.indexOf("DataSql") > -1) {
                if ($(this).val() == sqlPlaceholder) {
                    $(this).val('');
                }
            } else {
                if ($(this).val() == tempPlaceholder) {
                    $(this).val('');
                }
            }
        });
        $('#fjTempContent,#fjDataSql,#qtTempContent,#qtDataSql').blur(function () {
            if (this.id.indexOf("DataSql") > -1) {
                if ($(this).val() == '') {
                    $(this).val(sqlPlaceholder);
                }
            } else {
                if ($(this).val() == '') {
                    $(this).val(tempPlaceholder);
                }
            }
        });

        //申请类型和子类型下拉列表事件
        $("#sqlx,#zlx,#qllx").change(function () {
            var sqlxVal = $("#sqlx option:selected").val();
            var zlxVal = $("#zlx option:selected").val();
            var qllxVal = "";
            //zhouwanqing 根据sqlx联动qllx
            if (this.id == "sqlx") {
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/bdcConfig/getQllxBySqlx?sqlxdm=" + sqlxVal,
                    async: false,
                    success: function (data) {
                        if (data && data != 'undefined') {
                            qllxVal = data;
                            $("#qllx").val(data);
                            $(".chosen-select").trigger('chosen:updated');
                        } else {
                            $("#qllx").val("");
                            $(".chosen-select").trigger('chosen:updated');
                        }
                    }
                });
            }

            //zhouwanqing 未获取需自动选择
            if (qllxVal == null || qllxVal == '')
                qllxVal = $("#qllx option:selected").val();
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcConfig/getQlqtzkData",
                data: {sqlxdm: sqlxVal, qllxzlx: zlxVal, qllxdm: qllxVal},
                dataType: "json",
                success: function (result) {
                    if (result != null) {
                        if (result.length > 1) {
                            alert("数据配置库中有重复记录，请删除！");
                        } else if (result.length == 1) {
                            $("#qtTempContent").val(result[0].qlqtzkmb);
                            $("#qtDataSql").val(result[0].qtdb);
                            $("#fjTempContent").val(result[0].fjmb);
                            $("#fjDataSql").val(result[0].fjdb);
                            $("#fjTempContent,#fjDataSql,#qtTempContent,#qtDataSql").blur();
                        } else {
                            $("#fjTempContent,#fjDataSql,#qtTempContent,#qtDataSql").val("");
                            $("#fjTempContent,#fjDataSql,#qtTempContent,#qtDataSql").blur();
                        }
                    }
                },
                error: function (data) {
                    alert("加载数据失败，请重新加载！");
                }
            });
        })

        //修改或新增
        $("#save").click(function () {
            //去掉水印
            var qtTempContent = $("#qtTempContent").val();
            var qtDataSql = $("#qtDataSql").val();
            var fjTempContent = $("#fjTempContent").val();
            var fjDataSql = $("#fjDataSql").val();
            //判断取的值是否是水印的提示值
            if (qtTempContent == tempPlaceholder) {
                qtTempContent = "";
            }
            if (qtDataSql == sqlPlaceholder) {
                qtDataSql = "";
            }
            if (fjTempContent == tempPlaceholder) {
                fjTempContent = "";
            }
            if (fjDataSql == sqlPlaceholder) {
                fjDataSql = "";
            }

            //其中一种模板为空或者全为空或者全不为空 模板内容没有@参数
            if (((fjDataSql != "" && fjTempContent != "") || (fjDataSql == "" && fjTempContent == "") || (fjTempContent.indexOf("@") < 0 && qtTempContent != "")) && ((qtDataSql != "" && qtTempContent != "") || (qtDataSql == "" && qtTempContent == "") || (qtTempContent.indexOf("@") < 0 && qtTempContent != "") )) {
                //模板全空的时候报错，不保存
                if (fjDataSql == "" && fjTempContent == "" && qtDataSql == "" && qtTempContent == "") {
                    alert("模板不能为空！");
                    return false;
                }
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/bdcConfig/validateSql",
                    data: {sqls: qtDataSql},
                    dataType: "json",
                    success: function (data) {
                        if (!data.result) {
                            alert("权利其他状况数据源:" + data.msg);
                            return false;
                        } else {
                            $.ajax({
                                type: "GET",
                                url: "${bdcdjUrl}/bdcConfig/validateSql",
                                data: {sqls: fjDataSql},
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        alert("附记数据源:" + data.msg);
                                        return false;
                                    } else {
                                        $.ajax({
                                            type: "GET",
                                            url: "${bdcdjUrl}/bdcConfig/saveOrUpdateQlqtzk",
                                            data: {
                                                sqlxdm: $("#sqlx option:selected").val(),
                                                qllxzlx: $("#zlx option:selected").val(),
                                                qllxdm: $("#qllx option:selected").val(),
                                                qlqtzkmb: qtTempContent,
                                                qtdb: qtDataSql,
                                                fjmb: fjTempContent,
                                                fjdb: fjDataSql
                                            },
                                            dataType: "json",
                                            success: function (result) {
                                                if (result) {
                                                    alert("操作成功!");
                                                } else {
                                                    alert("操作失败!");
                                                }
                                            },
                                            error: function (data) {
                                                alert("操作失败！");
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                alert("模板内容和数据源需完整填写！")
            }
        })

        //删除
        $("#delete").click(function () {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcConfig/deleteQlqtzk",
                data: {
                    sqlxdm: $("#sqlx option:selected").val(),
                    qllxzlx: $("#zlx option:selected").val(),
                    qllxdm: $("#qllx option:selected").val()
                },
                dataType: "json",
                success: function (result) {
                    if (result) {
                        alert("删除成功!");
                        //重新获取数据
                        $("#sqlx").change();
                    } else {
                        alert("操作失败!");
                    }
                },
                error: function (data) {
                    alert("操作失败！");
                }
            });
        })

        //默认加载一个模板
        $("#sqlx").change();

    })

</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="row">
            <div class="col-xs-4">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 申请类型</div>
                    <div class="profile-info-value">
                        <select name="sqlx" id="sqlx" class="chosen-select" data-placeholder=" ">
                            <#list sqlxList as sqlx>
                                <option value="${sqlx.dm!}">${sqlx.mc!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>

            <div class="col-xs-4">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 权利类型</div>
                    <div class="profile-info-value">
                        <select name="qllx" id="qllx" class="chosen-select" data-placeholder=" ">
                            <option value="">请选择(非必选)</option>
                            <#list qllxList as qllx>
                                <option value="${qllx.dm!}">${qllx.mc!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>

            <div class="col-xs-4">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 房屋类型</div>
                    <div class="profile-info-value">
                    <#-- <select  name="zlx" id="zlx"   multiple title="请选择(非必选)" class="selectpicker">
                         <#list fwlxList as fwlx>
                             <option value="${fwlx.dm!}" >${fwlx.mc!}</option>
                         </#list>
                     </select>-->
                        <select name="zlx" id="zlx" class="chosen-select" data-placeholder=" ">
                            <option value="">请选择(非必选)</option>
                            <#list fwlxList as fwlx>
                                <option value="${fwlx.dm!}">${fwlx.mc!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <button class="btn-success btn" type="button" id="save">
                    保存
                </button>
                <button class="btn-danger btn" type="button" id="delete">
                    删除
                </button>
            </div>
        </div>
        <div class="space-10"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="widget-box">
                    <div class="widget-header">
                        <h4 class="widget-title">权利其他状况模板</h4>

                        <div class="widget-toolbar">
                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">
                            <div>
                                <label for="qtTempContent">模板内容</label>
                                <textarea class="form-control" id="qtTempContent"></textarea>
                            </div>
                            <hr>
                            <div>
                                <label for="qtDataSql">数据源</label>
                                <textarea class="form-control" id="qtDataSql"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="widget-box">
                    <div class="widget-header">
                        <h4 class="widget-title">附记模板</h4>

                        <div class="widget-toolbar">
                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">
                            <div>
                                <label for="fjTempContent">模板内容</label>
                                <textarea class="form-control" id="fjTempContent"></textarea>
                            </div>
                            <hr>
                            <div>
                                <label for="fjDataSql">数据源</label>
                                <textarea class="form-control" id="fjDataSql"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>