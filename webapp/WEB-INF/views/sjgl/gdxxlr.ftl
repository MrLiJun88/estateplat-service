<@com.html title="过渡信息" import="ace,public">
<style>
    span.label {
        border-radius: 3px !important;
    }

    .SSinput {
        width: 646px
    }

    iframe {
        position: relative;
    }

    .filesub {
        display: block;
        width: 90px;
        line-height: 34px;
        height: 34px;
        text-align: center;
        background: #155e96;
        color: #fff;
        word-spacing: 10px;
    }

    .col-xs-11 {
        width: 100%
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    .tableHeader {
        width: 800px;
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

    .SSinput {
        min-width: 100px !important;
    }

    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 1175px;
        /*position: fixed;*/
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

        /*移动modal样式*/
    #tdgjSearchPop .modal-dialog {
        width: 1175px;
        /*position: fixed;*/
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput .modal-dialog {
        width: 500px;
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
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .title {
        font-size: 18pt;
        text-align: center;
        padding-left: 2px;
        position: absolute;
        margin-left: 40px;
        margin-top: 5px;
    }

</style>
<script type="text/javascript">
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '320px';
    //全局的不动产类型
    $bdclx = 'TDFW';
    //定义公用的基础colModel
    fwColModel = [
        {
            name: 'XL', index: '', width: '8%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            if (rowObject.ROWNUM_ == 1) {
                $("#fwid").val(rowObject.FWID);
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" checked="true" onclick="fwSel(\'' + rowObject.FWID + '\')"/>'
            } else {
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.FWID + '\')"/>'
            }
        }
        },
        {name: 'FWZL', index: 'FWZL', width: '30%', sortable: false},
        {name: 'JZMJ', index: 'JZMJ', width: '14%', sortable: false},
        {name: 'GHYT', index: 'GHYT', width: '16%', sortable: false},
        {name: 'SZC', index: 'SZC', width: '9%', sortable: false},
        {name: 'ZCS', index: 'ZCS', width: '9%', sortable: false}<#if "${editFlag!}"=="true"> ,
            {
                name: 'mydy',
                index: '',
                width: '5%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:7px;"><div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="delFw(\'' + rowObject.FWID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div>'
                }
            }</#if>
    ];
    glfwColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.FWID + '" name="glfwbox"/> '
        }
        },
        {name: 'FWZL', index: 'FWZL', width: '40%', sortable: false},
        {name: 'JZMJ', index: 'JZMJ', width: '13.3%', sortable: false},
        {name: 'SZC', index: 'SZC', width: '10%', sortable: false},
        {name: 'ZCS', index: 'ZCS', width: '10%', sortable: false},
        {
            name: 'mydy',
            index: '',
            width: '15%',
            sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                return '<div style="margin-left:20px;"> <div title="编辑" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="editFw(\'' + rowObject.FWID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div><div  style="float:left;cursor:pointer;margin-left: 10px" class="ui-pg-div ui-inline-edit" id="" onclick="delFw(\'' + rowObject.FWID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div>'
            }
        }
    ];
    yglfwColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.FWID + '" name="yglfwbox"/> '
        }
        },
        {name: 'FWZL', index: 'FWZL', width: '45%', sortable: false},
        {name: 'JZMJ', index: 'JZMJ', width: '15%', sortable: false},
        {name: 'SZC', index: 'SZC', width: '15%', sortable: false},
        {name: 'ZCS', index: 'ZCS', width: '15%', sortable: false}
    ];
    dyhColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>'
        }
        },
        {name: 'DJH', index: 'DJH', width: '25%', sortable: false},
        {
            name: 'BDCDYH',
            index: 'BDCDYH',
            width: '15%',
            sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return "";
                }
                var value = cellvalue.substr(19);
                return value;
            }
        },
        {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
        {name: 'TDZL', index: 'TDZL', width: '35%', sortable: false},
        {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true}
    ];
    dyhLoadComplete = function () {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        //清空
        $("#bdcdyh").val("");
        //如果10条设置宽度为auto,如果少于7条就设置固定高度
        if ($(table).jqGrid("getRowData").length == $rownum) {
            $(table).jqGrid("setGridHeight", "auto");
        } else {
            $(table).jqGrid("setGridHeight", $pageHight);
        }
        //去掉遮罩
        setTimeout($.unblockUI, 10);
    };
    $(function () {
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据"});
        var width = $(window).width() / 2;
        if (width < 795) {
            width = 795;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
        $(window).resize(function () {
            var width = $(window).width() / 2;
            if (width < 795) {
                width = 795;
            }
            var height = $(window).height() - 20;
            $("#ace-settings-box").css("width", width).css("height", height);
        })
        //默认初始化表格
        dyhTableInit();
        fwTdTableInit();
        $("#qllxSelect").find("option[value='${cpt!}']").attr("selected", true);
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
//    fwTableInit();
        //左边房屋林权草权土地
        $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
            if ('${bdclx!}' == 'fw' && '${iscp!}' == 'true') {
                $("#lqTab").hide();
            }
            var url;
            //清空查询内容
            $("#dyh_search_qlr").val("");
            $("#dyh_search_qlr").next().show();
            if (this.id == "dyhTab") {
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: '',
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                $("#dyh-grid-table").trigger("reloadGrid");
                if (this.id == "fwTab") {
                    $("#fw").addClass("active");
                    var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?proid=${proid}";
                    fwTableInit();
                    if (isLoadGrid("fw")) {
                        var proidV = $("#proid").val();
                        tableReload("fw-grid-table", fwUrl, {
                            hhSearch: '',
                            dah: '',
                            filterFwPpzt: "${filterFwPpzt!}"
                        }, fwColModel, '');
                    }
                    var fwid = $("#fwid").val();
                    if (fwid == '' || fwid == 'null')
                        var url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&editFlag=${editFlag!}&fwid=${fwid!}&proid=${proid!}&qrFlag=${qrFlag!}';
                    else
                        var url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&editFlag=${editFlag!}&fwid=' + fwid+"&proid=${proid!}&qrFlag=${qrFlag!}";
                    $("#fwiframe").attr("src", url);
                    $("#dyhTab").click();
                } else if (this.id == "tdTab") {
                    $("#td").addClass("active");
                    $("#dyhTab").click();
                    var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
                    tdTableInit();

                    if (isLoadGrid("td"))
                        tableReload("td-grid-table", tdUrl, {hhSearch: ''}, '', '');
                    var qlidTemp = $("#qlid").val();
                    if (qlidTemp == null || qlidTemp == "")
                        qlidTemp = '${qlid!}';
                    cptTemp = $("#cpt").val();
                    if (cptTemp == null || cptTemp == "")
                        var cptTemp = '${cpt!}';

                    var bdcid="";
                    if("${bdclx!}"=="td")
                        bdcid="${tdid!}";
                    else if("${bdclx!}"=="fw")
                        bdcid="${fwid!}";
                    else if("${bdclx!}"=="lq")
                        bdcid="${lqid!}";
                    else if("${bdclx!}"=="cq")
                        bdcid="${cqid!}";
                    $("#qllxSelect").find("option[value='" + cptTemp + "']").attr("selected", true);
                    removeSelect(cptTemp);
                    if("${bdclx!}"=="td"){
                        url = "${reportUrl!}/ReportServer?reportlet=edit%2F" + cptTemp + ".cpt&op=write&editFlag=${editFlag!}&qlid=" + qlidTemp + "&bdcid=${tdid!}" + "&bdclx=${bdclx!}&tdqlid=${tdqlid!}&proid=${proid!}&qrFlag=${qrFlag!}&qlzt=${qlzt}";
                    }else{
                        url = "${reportUrl!}/ReportServer?reportlet=edit%2F" + cptTemp + ".cpt&op=write&editFlag=${editFlag!}&qlid=" + qlidTemp + "&bdcid="+bdcid + "&bdclx=${bdclx!}&proid=${proid!}&qrFlag=${qrFlag!}";
                    }
                    $("#qliframe").attr("src", url);
                } else if (this.id == "lqTab") {
                    var urlTemp = '';
                    if ('${bdclx!}' == 'td')
                        urlTemp = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&editFlag=${editFlag!}&tdid=${tdid!}&tdqlid=${tdqlid!}&proid=${proid!}&qrFlag=${qrFlag!}';
                    else
                        urlTemp = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_xm.cpt&op=write&editFlag=${editFlag!}&proid=${proid!}&proid=${proid!}&qrFlag=${qrFlag!}';
                    if ('${bdclx!}' == 'fw' && '${iscp!}' == 'true') {
                        $("#lqTab").hide();
                        $("#tdTab").click();
                    } else {
                        $("#xmiframe").attr("src", urlTemp);
                        $("#lq").addClass("active");
                    }
                }
            }
            $("#fwTdTab").hide();
        })

        if ('${msgInfo}' != "null") {
            alert('${msgInfo}');
        }


        //右边不动产单元，房屋土地
        $("#dyhTab,#fwTdTab").click(function () {
            if (this.id == "fwTdTab") {
                $bdclx = 'TDFW';
                $("#fwTd").addClass("active");
                var grid_selector = "#fwTd-grid-table";
                $(grid_selector).jqGrid("setGridWidth", 800);
            } else {
                $("#file").addClass("active");
            }
        })
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        if (gdTabOrderArray != null && gdTabOrderArray.length > 0) {
            if (gdTabOrderArray[0] == 'td')
                $("#tdTab").click();
            else {
                if ('${iscp!}' == 'true')
                    $("#tdTab").click();
                else
                    $("#lqTab").click();
            }
        }
        /*   文字水印  */
        $(".watermarkText").watermark();

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson";
            var proidV = $("#proid").val();
            tableReload("fw-grid-table", fwUrl, {
                hhSearch: hhSearch,
                proid: proidV,
                dah: '',
                filterFwPpzt: '${filterFwPpzt!}'
            }, fwColModel, '');
        })


        //绑定回车键
        $('input').focus(function () {
            var id = $(this).attr('id');
            if (id == 'fw_search_qlr') {
                $('#fw_search_qlr').keydown(function (event) {
                    if (event.keyCode == 13) {
                        $('#fw_search').click();
                    }
                });
            } else if (id == 'td_search_qlr') {
                $('#td_search_qlr').keydown(function (event) {
                    if (event.keyCode == 13) {
                        $('#td_search').click();
                    }
                });
            } else if (id == 'glfw_search_qlr') {
                $('#glfw_search_qlr').keydown(function (event) {
                    if (event.keyCode == 13) {
                        $('#glfw_search').click();
                    }
                });
            } else if (id == 'yglfw_search_qlr') {
                $('#yglfw_search_qlr').keydown(function (event) {
                    if (event.keyCode == 13) {
                        $('#yglfw_search').click();
                    }
                });
            }
        });


        $("#td_search").click(function () {
            var hhSearch = $("#td_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/gdXxLr/getGdQlJson";
            tableReload("td-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', proid: '${proid!}'}, '', '');
        })
        $("#fwTd_search").click(function () {
            var hhSearch = $("#fwTd_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/gdXxLr/getGdQlJson";
            $bdclx = "TDFW";
            tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
        })
        $("#newCpt").click(function () {
            var cpt = $("#cpt").val();
            if (cpt == null || cpt == "") {
                cpt = '${cpt!}'
            }
            var url = "${reportUrl!}/ReportServer?reportlet=edit%2F" + cpt + ".cpt&op=write&editFlag=${editFlag!}&bdcid=${proid!}" + "&bdclx=${bdclx!}&tdqlid=${tdqlid!}";
            $("#qliframe").attr("src", url);
        })

        $("#qxglfw").click(function () {
            alert(1);
        })

        $("#yglfw_search").click(function () {
            var hhSearch = $("#yglfw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + $("#qlid").val();
            tableReload("yglfw-grid-table", fwUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
        })

        $("#ygltd_search").click(function () {
            var hhSearch = $("#ygltd_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + $("#qlid").val();
            tableReload("ygltd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
        })

        /*关闭房屋关联页面*/
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            //$("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });
        /*关闭土地关联页面*/
        $("#tdproHide").click(function () {
            $("#tdgjSearchPop").hide();
            //$("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });

        $("#fileHide").click(function () {
            $("#fileInput").hide();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(window).trigger('resize.chosen');
        });
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth = $(".tab-content").width();
            $("#fw-grid-table,#lq-grid-table,#dyh-grid-table,#cq-grid-table,#td-grid-table,#fwTd-grid-table,#glfw-grid-table").jqGrid('setGridWidth', 800);
        });

//新增按钮点击事件
        $("#gdFwAdd,#gdLqAdd,#gdTdAdd,#gdCqAdd").click(function () {
            if (this.id == "gdFwAdd") {
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write");
            } else if (this.id == "gdTdAdd") {
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write");
            }
        })

//修改按钮点击事件
        $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {

            if (this.id == "gdFwUpdate") {
                var gdproid = $("#gdproid").val();
                if (fwid == null || gdproid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&gdproid=" + gdproid);
            } else if (this.id == "gdTdUpdate") {
                var tdid = $("#tdid").val();
                if (tdid == null || tdid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&tdid=" + tdid+"&qlid=${qlid!}");
            }
        })
    })

    function glfwSsearch() {
        var hhSearch = $("#glfw_search_qlr").val();
        var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson";
        tableReload("glfw-grid-table", fwUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
    }

    function gltdSsearch() {
        var hhSearch = $("#gltd_search_qlr").val();
        var tdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson";
        tableReload("gltd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
    }


    function delFw(fwid) {
        var msg = "是否删除";
        showConfirmDialog("提示信息", msg, "deletefw", "'" + fwid + "'", "", "");
    }

    function delTd(tdid) {
        var msg = "是否删除";
        showConfirmDialog("提示信息", msg, "deletetd", "'" + tdid + "'", "", "");
    }

    var deletefw = function (fwid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/gdXxLr/delFw?fwid=" + fwid,
            success: function (result) {
                if (result != null && result != '') {
                    alert(result);
                    var hhSearch = $("#glfw_search_qlr").val();
                    var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson";
                    tableReload("glfw-grid-table", fwUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
                }
            },
            error: function (data) {
            }
        });
    }

    var deletetd = function (tdid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/gdXxLr/delTd?tdid=" + tdid,
            success: function (result) {
                if (result != null && result != '') {
                    alert(result);
                    var hhSearch = $("#gltd_search_qlr").val();
                    var tdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson";
                    tableReload("gltd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
                }
            },
            error: function (data) {
            }
        });
    }

    function selectQllx(obj) {
        var cpt = $(obj).val();
        $("#cpt").val(cpt);
        var url = "${reportUrl!}/ReportServer?reportlet=edit%2F" + cpt + ".cpt&op=write&editFlag=${editFlag!}&proid=${proid!}" + "&bdclx=${bdclx!}&tdqlid=${tdqlid!}"+"&bdcid=" + "${bdcid!}" + "&qlzt=${qlzt}";
        $("#qliframe").attr("src", url);
    }

    //帆软保存以后返回关联房屋页面
    function glfwsearch(fwzl, fwid) {
        goBack();
        var hhSearch = $("#glfw_search_qlr").val(fwzl);
        $("#glfw_search_qlr").next().css("display", "none");
        glfwSsearch();
    }

    //帆软保存以后返回关联土地页面
    function gltdsearch(zl, fwid) {
        goBacktd();
        var hhSearch = $("#gltd_search_qlr").val(zl);
        $("#gltd_search_qlr").next().css("display", "none");
        gltdSsearch();
    }

    function delQl(qlid) {
        var msg = "是否删除";
        showConfirmDialog("提示信息", msg, "deleteql", "'" + qlid + "'", "", "");
    }
    var deleteql = function (qlid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/gdXxLr/delQl?qlid=" + qlid,
            success: function (result) {
                if (result != null && result != '') {
                    alert(result);
                    var hhSearch = $("#td_search_qlr").val();
                    var tdUrl = "${bdcdjUrl}/gdXxLr/getGdQlJson";
                    tableReload("td-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', proid: '${proid!}'}, '', '');

                    if (qlid == $("#firstqlid").val()) {
                        tdSel($("#secondqlid").val(), $("#secondqllx").val());
                    } else if (qlid == $("#qlid").val()) {
                        tdSel($("#firstqlid").val(), $("#firstqllx").val());
                    } else {
                        $("#tdTab").click();
                    }
                }
            },
            error: function (data) {
            }
        });
    }
    //sc 删除关联房屋 delGlfw
    function delGlfw() {
        var chk_value = "";
        $('input[name="yglfwbox"]:checked').each(function () {
            var glfFwfwid = $(this).val();
            if (chk_value == "") {
                chk_value = chk_value + glfFwfwid;
            } else {
                chk_value = chk_value + "," + glfFwfwid
            }
        });
        if (chk_value == "" || chk_value == null) {
            alert("请选择一条数据");
        } else {
            var qlid = $("#qlid").val()
            $.ajax({
                type: "POST",
                url: "${bdcdjUrl}/gdXxLr/delglfw?qlid=" + qlid,
                data: {"fwids": chk_value},
                success: function (result) {
                    if (result != null && result != '') {
                        alert(result);
                        var yglfwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + qlid;
                        yglfwTableInit();
                        tableReload("yglfw-grid-table", yglfwUrl, {hhSearch: ''}, '', '');
                    }
                },
                error: function (data) {
                }
            });
        }
    }

    //lj 删除关联土地 delGltd
    function delGltd() {
        var chk_value = "";
        $('input[name="ygltdbox"]:checked').each(function () {
            var glTdtdid = $(this).val();
            if (chk_value == "") {
                chk_value = chk_value + glTdtdid;
            } else {
                chk_value = chk_value + "," + glTdtdid
            }
        });
        if (chk_value == "" || chk_value == null) {
            alert("请选择一条数据");
        } else {
            var qlid = $("#qlid").val()
            $.ajax({
                type: "POST",
                url: "${bdcdjUrl}/gdXxLr/delgltd?qlid=" + qlid,
                data: {"tdids": chk_value},
                success: function (result) {
                    if (result != null && result != '') {
                        alert(result);
                        var ygltdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + qlid;
                        ygltdTableInit();
                        tableReload("ygltd-grid-table", ygltdUrl, {hhSearch: ''}, '', '');
                    }
                },
                error: function (data) {
                }
            });
        }
    }

    //sc 关联房屋
    function goGlfw() {
        var chk_value = "";
        $('input[name="glfwbox"]:checked').each(function () {
            var glfFwfwid = $(this).val();
            if (chk_value == "") {
                chk_value = chk_value + glfFwfwid;
            } else {
                chk_value = chk_value + "," + glfFwfwid
            }
        });
        if (chk_value == "" || chk_value == null) {
            alert("请选择一条数据");
        } else {
            var qlid = $("#qlid").val()
            $.ajax({
                type: "POST",
                url: "${bdcdjUrl}/gdXxLr/glfw?qlid=" + qlid,
                dataType: "text",
                data: {"fwids": chk_value, bdclx: $bdclx},
                success: function (result) {
                    if (result != null && result != '') {
                        alert(result);
                        var yglfwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + qlid;
                        yglfwTableInit();
                        tableReload("yglfw-grid-table", yglfwUrl, {hhSearch: ''}, '', '');
                    }
                },
                error: function (data) {
                }
            });
        }
    }
    //lj 关联土地
    function goGltd() {
        var chk_value = "";
        $('input[name="gltdbox"]:checked').each(function () {
            var gltdtdid = $(this).val();
            if (chk_value == "") {
                chk_value = chk_value + gltdtdid;
            } else {
                chk_value = chk_value + "," + gltdtdid;
            }
        });
        if (chk_value == "" || chk_value == null) {
            alert("请选择一条数据");
        } else {
            var qlid = $("#qlid").val()
            $.ajax({
                type: "POST",
                url: "${bdcdjUrl}/gdXxLr/gltd?qlid=" + qlid,
                dataType: "text",
                data: {"tdids": chk_value, bdclx: "TD"},
                success: function (result) {
                    if (result != null && result != '') {
                        alert(result);
                        var ygltdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + qlid;
                        ygltdTableInit();
                        tableReload("ygltd-grid-table", ygltdUrl, {hhSearch: ''}, '', '');
                    }
                },
                error: function (data) {
                }
            });
        }
    }

    //新增房屋页面
    function addfw() {
        $("#glfw").hide()
        $("#editfw").show();
        var title = '<a href="#" style="color: #ffffff;text-decoration: underline" onclick="goBack()">关联房屋</a>　>　新增房屋';
        $("#titlebz").html(title);
        var qlid = $("#qlid").val();
        url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&bjzt=add&editFlag=${editFlag!}&qlid=' + qlid;
        $("#iframepage").attr("src", url);
    }

    //新增土地页面
    function addtd() {
        $("#gltd").hide()
        $("#edittd").show();
        var title = '<a href="#" style="color: #ffffff;text-decoration: underline" onclick="goBacktd()">关联土地</a>　>　新增土地';
        $("#titletd").html(title);
        var qlid = $("#qlid").val();
        url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&bjzt=add&editFlag=${editFlag!}&qlid=' + qlid;
        $("#tdiframepage").attr("src", url);
    }
    //编辑房屋
    function editFw(fwid) {
        $("#glfw").hide()
        $("#editfw").show();
        var title = '<a href="#" style="color: #ffffff;text-decoration: underline" onclick="goBack()">关联房屋</a>　>　编辑房屋';
        $("#titlebz").html(title);
        url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&bjzt=edit&editFlag=${editFlag!}&fwid=' + fwid;
        $("#iframepage").attr("src", url);
    }

    //编辑土地
    function editTd(tdid) {
        $("#gltd").hide()
        $("#edittd").show();
        var title = '<a href="#" style="color: #ffffff;text-decoration: underline" onclick="goBacktd()">关联土地</a>　>　编辑土地';
        $("#titletd").html(title);
        url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&bjzt=edit&editFlag=${editFlag!}&tdid=' + tdid;
        $("#tdiframepage").attr("src", url);
    }

    //返回关联房屋页面
    function goBack() {
        $("#editfw").hide()
        $("#glfw").show();
        $("#titlebz").html("关联房屋");
        var qlid = $("#qlid").val();
        var yglfwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + qlid;
        yglfwTableInit();
        tableReload("yglfw-grid-table", yglfwUrl, {hhSearch: ''}, '', '');
    }

    //返回关联房屋页面
    function goBacktd() {
        $("#edittd").hide();
        $("#gltd").show();
        $("#titletd").html("关联土地");
        var qlid = $("#qlid").val();
        var ygltdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + qlid;
        ygltdTableInit();
        tableReload("ygltd-grid-table", ygltdUrl, {hhSearch: ''}, '', '');
    }
    /*关联房屋*/
    function glfw(qlid) {
    <#--var url = '${bdcdjUrl!}/gdXxLr/goGlfw?proid=${proid!}&qlid='+qlid;-->
    <#--$("#glfw").attr("src", url);-->
        var glfwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + qlid;
        glfwTableInit();
        tableReload("glfw-grid-table", glfwUrl, {hhSearch: ''}, '', '');
        var yglfwUrl = "${bdcdjUrl}/gdXxLr/getGdFwJson?qlid=" + qlid;
        yglfwTableInit();
        tableReload("yglfw-grid-table", yglfwUrl, {hhSearch: ''}, '', '');
        $("#qlid").val(qlid);
        $("#gjSearchPop").show();
    }
    /*关联土地*/
    function gltd(qlid) {
    <#--var url = '${bdcdjUrl!}/gdXxLr/goGlfw?proid=${proid!}&qlid='+qlid;-->
    <#--$("#glfw").attr("src", url);-->
        var gltdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + qlid;
        gltdTableInit();
        tableReload("gltd-grid-table", gltdUrl, {hhSearch: ''}, '', '');
        var ygltdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson?qlid=" + qlid;
        ygltdTableInit();
        tableReload("ygltd-grid-table", ygltdUrl, {hhSearch: ''}, '', '');
        $("#qlid").val(qlid);
        $("#tdgjSearchPop").show();
    }


    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }

    function hideModal(proid) {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
    }

    function openWin(url) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    //提示信息
    function tipInfo(msg) {
        // bootbox.dialog({
        //     message: "<h3><b>" + msg + "</b></h3>",
        //     title: "",
        //     buttons: {
        //         main: {
        //             label: "关闭",
        //             className: "btn-primary"
        //         }
        //     }
        // });
        // return;
        $.Prompt(msg,1500);
    }
    function confirmInfo(msg) {
        bootbox.dialog({
            message: "<h3><b>" + msg + "</b></h3>",
            title: "",
            buttons: {
                OK: {
                    label: "是",
                    className: "btn-primary",
                    callback: function () {
                        return true;
                    }
                },
                Cancel: {
                    label: "否",
                    className: "btn-default",
                    callback: function () {
                        return false;
                    }
                }
            }
        });
    }

    /**
     * 选中单选按钮
     * @param val 值，一般为后台取出传递给前台
     * 调用方式: 在页面加载完成后调用该方法，给定参数，自动根据val勾选单选框
     */
    function transVal2Radio(val) {
        var eles = document.getElementsByTagName('input');
        for (var i = 0; i < eles.length; ++i) {
            if (eles[i].type == 'radio' && eles[i].name == "tdXl") {
                eles[i].checked = false;
                //alert(eles[i].value);
                if (val != '' && eles[i].value == val) {
                    eles[i].checked = true;
                }
            }
        }
    }

    //选择土地证号
    function tdSel(qlid, qllx) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/gdXxLr/getGdqlCptUrl?bdcid=${bdcid!}&qlid=" + qlid,
            success: function (result) {
                if (result != null && result != '') {
                    var cpt = "";
                    if (qllx == '房屋所有权')
                        cpt = "gd_fwsyq";
                    else if (qllx == '查封')
                        cpt = "gd_cf";
                    else if (qllx == '抵押')
                        cpt = "gd_dy";
                    else if (qllx == '预告')
                        cpt = "gd_yg";
                    else if (qllx == '异议')
                        cpt = "gd_yy";
                    else if (qllx == '土地所有权')
                        cpt = "gd_tdsyq";
                    else if (qllx == '土地使用权')
                        cpt = "gd_tdsynq";
                    $("#qllxSelect").find("option[value='" + cpt + "']").attr("selected", true);
                    removeSelect(cpt);
                    result = result + "&editFlag=${editFlag!}" + "&bdclx=${bdclx!}";
                    $("#qliframe").attr("src", result);
                    $("#cpt").val(cpt);
                    $("#qlid").val(qlid);
                }
            },
            error: function (data) {
            }
        });

    }


    function removeSelect(cpt) {
        if (cpt != "gd_fwsyq") {
            $("#qllxSelect").find("option[value='gd_fwsyq']").attr("selected", false);
        }
        if (cpt != "gd_cf") {
            $("#qllxSelect").find("option[value='gd_cf']").attr("selected", false);
        }
        if (cpt != "gd_dy") {
            $("#qllxSelect").find("option[value='gd_dy']").attr("selected", false);
        }
        if (cpt != "gd_yg") {
            $("#qllxSelect").find("option[value='gd_yg']").attr("selected", false);
        }
        if (cpt != "gd_yy") {
            $("#qllxSelect").find("option[value='gd_yy']").attr("selected", false);
        }
        if (cpt != "gd_tdsyq") {
            $("#qllxSelect").find("option[value='gd_tdsyq']").attr("selected", false);
        }
        if (cpt != "gd_tdsynq") {
            $("#qllxSelect").find("option[value='gd_tdsynq']").attr("selected", false);
        }
    }
    //选择房产证
    function fwSel(fwid) {
        var url = '${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&editFlag=${editFlag!}&fwid=' + fwid;
        $("#fwiframe").attr("src", url);
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
        }
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function fwTableInit() {
        var grid_selector = "#fw-grid-table";
        var pager_selector = "#fw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'FWID'},
            colNames: ['序列', '坐落', '建筑面积', '规划用途', '所在层', '总层数',<#if "${editFlag!}"=="true"> '操作'</#if>],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            $("#fwid").val("");
//            $("#dah").val("");
//            $("#xmmc").val("");
//            $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                    $(grid_selector).jqGrid('setGridWidth', 800);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 800);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            ondblClickRow: function (rowid, index) {
                $("#fwid").val(rowid);
                var radionTemp = document.getElementsByName("fwXl")[index - 1];
                radionTemp.checked = true;
                fwSel(rowid);
            },

            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


    function getGdQlr(qlr,table,rowid){
        $.ajax({
            url: "${bdcdjUrl}/gdXxLr/getGdQlr",
            type:"GET",
            data:{qlid:rowid},
            success: function (result) {
                qlr = '<span>' + result + '</span>';
                table.setCell(rowid, "QLR", qlr);
            },
            error: function () {
            }
        });
    }
    //获取匹配状态
    function getPpzt(ppzt, table, rowid) {
        if (ppzt == "1")
            ppzt = '<span class="label label-warning">已匹配未发证</span>';
        else if (ppzt == "3")
            ppzt = '<span class="label label-info">已匹配已发证</span>';
        else if (ppzt == "2")
            ppzt = '<span class="label label-success">已匹配正在发证</span>';
        else
            ppzt = '<span class="label label-danger">待匹配未发证</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }
    //土地证初始化
    function tdTableInit() {
        var grid_selector = "#td-grid-table";
        var pager_selector = "#td-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/gdXxLr/getGdQlJson?proid=${proid!}",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'QLID'},
            colNames: ['序列', '权利类型', '业务类型', "QLID", "权利人",<#if "${editFlag!}"=="true">  "操作" </#if>],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        var qlidTemp = $("#qlid").val();
                        if (qlidTemp == null || qlidTemp == "")
                            qlidTemp = '${qlid!}';

                        if (rowObject.ROWNUM_ == 1) {
                            $("#firstqlid").val(rowObject.QLID);
                            $("#firstqllx").val(rowObject.QLLX);
                        }
                        if (rowObject.ROWNUM_ == 2) {
                            $("#secondqlid").val(rowObject.QLID);
                            $("#secondqllx").val(rowObject.QLLX);
                        }

                        if (rowObject.QLID == qlidTemp)
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" checked="true" value=' + rowObject.QLID + ' onclick="tdSel(\'' + rowObject.QLID + '\',\'' + rowObject.QLLX + '\')"/>'
                        else
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" value=' + rowObject.QLID + ' onclick="tdSel(\'' + rowObject.QLID + '\',\'' + rowObject.QLLX + '\')"/>'
                    }
                },
                {name: 'QLLX', index: 'QLLX', width: '15%', sortable: false},
                {name: 'DJLX', index: 'DJLX', width: '25%', sortable: false},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
                {name: 'QLR', index: 'QLR', width: '25%', sortable: false}
                <#if "${editFlag!}"=="true"> ,
                    {
                        name: 'mydy',
                        index: '',
                        width: '10%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            return '<#if "${bdclx!}"=="fw"><div style="margin-left:20px;"> <div title="关联房屋" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="glfw(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>&nbsp;&nbsp;<div style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="delQl(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div></#if>' +
                                    '<#if "${bdclx!}"=="td"><div style="margin-left:20px;"> <div title="关联土地" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="gltd(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>&nbsp;&nbsp;<div style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="delQl(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div></#if>'
                        }
                    }
                </#if>
            ],
            viewrecords: true,
            rowNum: gridRowNum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            ondblClickRow: function (rowid, index) {
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/gdXxLr/getGdqllx?qlid=" + rowid,
                    success: function (result) {
                        if (result != null && result != '')
                            tdSel(rowid, result);
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });

                var radionTemp = document.getElementsByName("tdXl")[index - 1];
                radionTemp.checked = true;
            },
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < gridRowNum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                    $(grid_selector).jqGrid('setGridWidth', 800);
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    $(grid_selector).jqGrid('setGridWidth', 800);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");

                $.each(jqData, function (index, data) {
//                getDyYgCfStatus(data.TDID,$(grid_selector),data.TDID,'') ;
//                    getPpzt(data.PPZT, $(grid_selector), data.TDID);
                    getGdQlr(data.QLR, $(grid_selector), data.QLID);
                });


            },
            gridComplete: function () {
                //alert($("#qlid").val());


                if ($("#qlid").val() != "") {
                    transVal2Radio($("#qlid").val());
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


    function dyhTableInit() {
        var grid_selector = "#dyh-grid-table";
        var pager_selector = "#dyh-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'ID'},
            colNames: ['序列', '地籍号', "不动产单元号", '权利人', '坐落', '不动产类型'],
            colModel: dyhColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false, /*
            multiboxonly:true,
            multiselect:true,*/
            loadComplete: dyhLoadComplete,
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true

        });
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    /**
     * 根据key获取是否加载数据
     * @param key
     * @returns {boolean}
     */
    function isLoadGrid(key) {
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        var gdTabLoadData = "${gdTabLoadData!}";
        var gdTabLoadDataArray = new Array();
        gdTabLoadDataArray = gdTabLoadData.split(",");
        if (gdTabOrderArray.length == gdTabLoadDataArray.length) {
            for (var i = 0; i < gdTabOrderArray.length; i++) {
                if (gdTabOrderArray[i] == key) {
                    var loadGrid = gdTabLoadDataArray[i];
                    if (loadGrid == 1) {
                        return true;
                        break;
                    } else {
                        return false;
                        break;
                    }
                }
            }
            return false;
        } else
            return false;
    }
    /*已关联房屋页面*/
    function yglfwTableInit() {
        var grid_selector = "#yglfw-grid-table";
        var pager_selector = "#yglfw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'FWID'},
            colNames: ['序列', '坐落', '建筑面积', '所在层', '总层数'],
            colModel: yglfwColModel,
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            $("#fwid").val("");
//            $("#dah").val("");
//            $("#xmmc").val("");
//            $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    $(grid_selector).jqGrid('setGridWidth', 550);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    /*已关联土地页面*/
    function ygltdTableInit() {
        var grid_selector = "#ygltd-grid-table";
        var pager_selector = "#ygltd-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '坐落', '地籍号', '宗地面积', '用途'],
            colModel: [
                {
                    name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.TDID + '" name="ygltdbox"/> '
                }
                },
                {name: 'ZL', index: 'ZL', width: '40%', sortable: false},
                {name: 'DJH', index: 'DJH', width: '10%', sortable: false},
                {name: 'ZDMJ', index: 'ZDMJ', width: '13.3%', sortable: false},
                {name: 'YT', index: 'YT', width: '10%', sortable: false}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            $("#fwid").val("");
//            $("#dah").val("");
//            $("#xmmc").val("");
//            $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    $(grid_selector).jqGrid('setGridWidth', 550);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    //getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    /*房屋关联搜索页面*/
    function glfwTableInit() {
        var grid_selector = "#glfw-grid-table";
        var pager_selector = "#glfw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'FWID'},
            colNames: ['序列', '坐落', '建筑面积', '所在层', '总层数', '编辑'],
            colModel: glfwColModel,
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            $("#fwid").val("");
//            $("#dah").val("");
//            $("#xmmc").val("");
//            $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    $(grid_selector).jqGrid('setGridWidth', 550);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    //getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    /*土地关联搜索页面*/
    function gltdTableInit() {
        var grid_selector = "#gltd-grid-table";
        var pager_selector = "#gltd-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 550);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '坐落', '地籍号', '宗地面积', '用途', '编辑'],
            colModel: [
                {
                    name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" value="' + rowObject.TDID + '" name="gltdbox"/> '
                }
                },
                {name: 'ZL', index: 'ZL', width: '40%', sortable: false},
                {name: 'DJH', index: 'DJH', width: '10%', sortable: false},
                {name: 'ZDMJ', index: 'ZDMJ', width: '13.3%', sortable: false},
                {name: 'YT', index: 'YT', width: '10%', sortable: false},
                {
                    name: 'mydy',
                    index: '',
                    width: '15%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div style="margin-left:20px;"> <div title="编辑" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="editTd(\'' + rowObject.TDID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div><div  style="float:left;cursor:pointer;margin-left: 10px" class="ui-pg-div ui-inline-edit" id="" onclick="delTd(\'' + rowObject.TDID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-trash-o fa-lg blue"></span></div></div>'
                    }
                }
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            $("#fwid").val("");
//            $("#dah").val("");
//            $("#xmmc").val("");
//            $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    $(grid_selector).jqGrid('setGridWidth', 550);
                } else {
                    $(grid_selector).jqGrid('setGridWidth', 550);
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //房屋土地证初始化
    function fwTdTableInit() {
        var grid_selector = "#fwTd-grid-table";
        var pager_selector = "#fwTd-grid-pager";

        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '土地证号', "坐落", '状态', 'ID'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" onclick="fwTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\')"/>';
                    }
                },
                {name: 'TDZH', index: 'TDZH', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '35%', sortable: false},

                {name: 'STATUS', index: '', width: '20%', sortable: false},
                {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: gridRowNum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerPagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getDyYgCfStatus(data.TDID, $(grid_selector), data.TDID, '')
                })

                $(table).jqGrid("setGridWidth", 800);
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


    function fwTdSel(tdzh, tdid) {
        $("#tdzh").val(tdzh);
        $("#tdid").val(tdid);
    }

    //open新窗口
    function addOrUpdate(url) {
        openWin(url);
    }
    function refreshStore() {
        if ($bdclx == "TDFW") {
            $("#fw_search").click();
            $("#fwtd_search").click();
        } else if ($bdclx == "TD")
            $("#td_search").click();
    }


    function hideFwtdGrid() {
        $("#fwTdTab").hide();
        dyhTableInit();
        $("#dyhTab").click();
    }
    function changeSqlx() {
        var sqlx = $("#sqlxSelect").val();
        if (sqlx == "86023BB75E404D918DD31D531DC3DE68") {
            hideFwtdGrid();
            $("#tdid").val();
        }
    }

    function choosefile(filetype) {
        if (filetype == "ql") {
            $("#sjlx").val($("#qllxSelect").val());
        } else if (filetype == "fw") {
            $("#sjlx").val("gd_fw");
        } else if (filetype == "td") {
            $("#sjlx").val("gd_td");
        }
        //alert($("#tempqllxSelect").val());
        $("#fileInput").show();
    }

</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-11">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <#list gdTabOrderList as gdTabOrder>
                            <#if gdTabOrder=='ql'>
                                <li>
                                    <a data-toggle="tab" id="tdTab" href="#td">
                                        权利信息
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='xm' && "${showtd!}" == "true">
                                <li>
                                    <a data-toggle="tab" id="lqTab" href="#lq">
                                        <#if "${iscp!}"!="true"||"${bdclx!}"=="fw">项目信息</#if>
                                        <#if "${bdclx!}"=="td">土地信息</#if>
                                    </a>
                                </li>
                            <#--<#else>-->
                            <#--<li>-->
                            <#--<a data-toggle="tab" id="fwTab" href="#fw">-->
                            <#--房屋信息-->
                            <#--</a>-->
                            <#--</li>-->
                            </#if>
                        </#list>
                    </ul>
                    <div class="tab-content" align="center">
                    <#--<div id="fw" class="tab-pane " align="center">-->
                    <#--<iframe id='fwiframe'-->
                    <#--src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=writeZ&editFlag=${editFlag!}&fwid=${fwidVal!}"-->
                    <#--frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"-->
                    <#--allowtransparency="yes" style="width: 670px;height:500px"></iframe>-->
                    <#--<div class="simpleSearch">-->
                    <#--<table cellpadding="0" cellspacing="0" border="0">-->
                    <#--<tr>-->
                    <#--<td>-->
                    <#--<input type="text" class="SSinput watermarkText" id="fw_search_qlr"-->
                    <#--data-watermark="请输入坐落">-->
                    <#--</td>-->
                    <#--<td class="Search">-->
                    <#--<a href="#" id="fw_search">-->
                    <#--搜索-->
                    <#--<i class="ace-icon fa fa-search bigger-130"></i>-->
                    <#--</a>-->
                    <#--</td>-->
                    <#--</tr>-->
                    <#--</table>-->
                    <#--</div>-->
                    <#--<table id="fw-grid-table" align="center"></table>-->
                    <#--<div id="fw-grid-pager" align="center"></div>-->
                    <#--</div>-->
                    <#--项目-->
                        <#if "${showtd!}"=="true">
                            <#if "${iscp!}"!="true"||"${bdclx!}"=="td">
                                <div id="lq" class="tab-pane" align="center">
                                    <#if "${bdclx!}"=="td">
                                        <div class="simpleSearchIS" style="  width: 280px;margin: 0px 0px 10px 100px;">
                                            <div class="title">土地信息</div>
                                        </div>
                                        <iframe id='xmiframe' src="" frameborder="no" border="0" marginwidth="0"
                                                marginheight="0" scrolling="no" allowtransparency="yes"
                                                style="width: 650px;height:700px"></iframe>
                                    </#if>
                                    <#if "${bdclx!}"=="fw">
                                        <iframe id='xmiframe' src="" frameborder="no" border="0" marginwidth="0"
                                                marginheight="0" scrolling="no" allowtransparency="yes"
                                                style="width: 685px;height:650px"></iframe>
                                    </#if>
                                </div>
                            </#if>
                        </#if>
                    <#--权力-->
                        <div id="td" class="tab-pane" align="center" style="width: 1000px">
                            <div style="margin-bottom: 20px;margin-left:300px;float: left;">
                                <select id="qllxSelect"
                                        style="width: 150px;height: 40px;font-size: 18pt;font-family:微软雅黑; font-weight: 500;color: #000000;border-left:0px;border-top:0px;border-right:0px;border-bottom:1px"
                                        onchange="selectQllx(this)">
                                    <#list qlSelect?keys as k>
                                        <option value="${k}">${qlSelect[k]}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="simpleSearch" style="width: 150px;margin-left: 300px">
                                <table border="0" cellpadding="2" cellspacing="0">
                                    <tr>
                                        <td class="Search">
                                            <a href="#" id="newCpt">
                                                重置
                                                <i class="ace-icon fa fa-file-o bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>

                            <iframe id='qliframe' src="" frameborder="no" border="0" marginwidth="0" marginheight="0"
                                    scrolling="no" allowtransparency="yes" style="width: 1000px;height:500px"></iframe>
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                                                   data-watermark="请输入权利类型/业务类型/权利人">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="td_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table id="td-grid-table" width="800px"></table>
                            <div id="td-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--关联房屋model-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="titlebz">关联房屋</h4>
                <button type="button" id="proHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td><input type="text" style="width: 265px" class="SSinput watermarkText"
                                                   id="glfw_search_qlr" data-watermark="请输入坐落"></td>
                                        <td class="Search"><a href="#" id="glfw_search" onclick="glfwSsearch()"> 搜索<i
                                                class="ace-icon fa fa-search bigger-130"></i></a></td>
                                        <td class="Search"><a href="#" id="logSearchBtn" onclick="goGlfw()"> 关联<i
                                                class="ace-icon fa fa-exchange bigger-130"></i> </a></td>
                                        <td class="Search"><a href="#" id="addfw" onclick="addfw()"> 新增<i
                                                class="ace-icon fa fa-file-o bigger-130"></i> </a></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <table id="glfw-grid-table"></table>
                        <div id="glfw-grid-pager"></div>
                    </div>
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="tabbable">
                                <div class="simpleSearch">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="text" style="width: 356px" class="SSinput watermarkText"
                                                       id="yglfw_search_qlr" data-watermark="请输入坐落"></td>
                                            <td class="Search"><a href="#" id="yglfw_search">搜索<i
                                                    class="ace-icon fa fa-search bigger-130"></i></a></td>
                                            <td class="Search"><a href="#" id="ylogSearchBtn" onclick="delGlfw()">删除关联<i
                                                    class="ace-icon fa fa-trash-o bigger-130"></i> </a></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <table id="yglfw-grid-table"></table>
                            <div id="yglfw-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page-content" id="editfw" style="display: none;" style="width:1173px ;height:495px "
                 align="center">
                <iframe id='iframepage' src="" frameborder="no" border="0" marginwidth="0" marginheight="0"
                        scrolling="no" allowtransparency="yes" style="width: 1000px;height:500px"></iframe>
            </div>
        <#--</div>-->
        </div>
    </div>
</div>
<#--关联土地model-->
<div class="Pop-upBox moveModel" style="display: none;" id="tdgjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="titletd">关联土地</h4>
                <button type="button" id="tdproHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="gltd">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td><input type="text" style="width: 265px" class="SSinput watermarkText"
                                                   id="gltd_search_qlr" data-watermark="请输入坐落"></td>
                                        <td class="Search"><a href="#" id="gltd_search" onclick="gltdSsearch()"> 搜索<i
                                                class="ace-icon fa fa-search bigger-130"></i></a></td>
                                        <td class="Search"><a href="#" id="tdlogSearchBtn" onclick="goGltd()"> 关联<i
                                                class="ace-icon fa fa-exchange bigger-130"></i> </a></td>
                                        <td class="Search"><a href="#" id="addtd" onclick="addtd()"> 新增<i
                                                class="ace-icon fa fa-file-o bigger-130"></i> </a></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <table id="gltd-grid-table"></table>
                        <div id="gltd-grid-pager"></div>
                    </div>
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="tabbable">
                                <div class="simpleSearch">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="text" style="width: 356px" class="SSinput watermarkText"
                                                       id="ygltd_search_qlr" data-watermark="请输入坐落"></td>
                                            <td class="Search"><a href="#" id="ygltd_search">搜索<i
                                                    class="ace-icon fa fa-search bigger-130"></i></a></td>
                                            <td class="Search"><a href="#" id="tdylogSearchBtn" onclick="delGltd()">删除关联<i
                                                    class="ace-icon fa fa-trash-o bigger-130"></i> </a></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <table id="ygltd-grid-table"></table>
                            <div id="ygltd-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page-content" id="edittd" style="display: none;" style="width:1173px ;height:495px "
                 align="center">
                <iframe id='tdiframepage' src="" frameborder="no" border="0" marginwidth="0" marginheight="0"
                        scrolling="no" allowtransparency="yes" style="width: 1000px;height:500px"></iframe>
            </div>
        <#--</div>-->
        </div>
    </div>
</div>

<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<form id="form" hidden="hidden">
    <input type="hidden" id="proid" name="proid" value="${proid!}">
    <input type="hidden" id="fwid" name="fwid" value="${fwid!}">
    <input type="hidden" id="tdid" name="tdid" value="${tdid!}">
    <input type="hidden" id="bdcid" name="bdcid">
    <input type="hidden" id="qlid" name="qlid"/>
    <input type="hidden" id="firstqlid" name="firstqlid"/>
    <input type="hidden" id="secondqlid" name="secondqlid"/>
    <input type="hidden" id="cpt"/>
    <input type="hidden" id='cptTemp'>
    <input type="hidden" id="firstqllx" name="firstqllx">
    <input type="hidden" id="secondqllx" name="secondqllx">
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
