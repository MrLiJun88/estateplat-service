$(function () {
    //iE8兼容placeholder的方法
    jQuery('[placeholder]').focus(function () {
        var input = jQuery(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
            input.removeClass('placeholder');
        }
    }).blur(function () {
        var input = jQuery(this);
        if (input.val() == '' || input.val() == input.attr('placeholder')) {
            input.addClass('placeholder');
            input.val(input.attr('placeholder'));
        }
    }).blur().parents('form').submit(function () {
        jQuery(this).find('[placeholder]').each(function () {
            var input = jQuery(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
            }
        })
    });
    $('#showMore').click(function () {
        if ($(this).find('i').hasClass('icon-chevron-right')) {
            $(this).find('i').removeClass().addClass('icon-chevron-left');
            $('.left').removeClass('span2').hide();
            $('.right').removeClass('span10').addClass('span12');
        } else {
            $(this).find('i').removeClass().addClass('icon-chevron-right');
            $('.left').addClass('span2').show();
            $('.right').removeClass('span12').addClass('span10');
        }
    })
    $('.left').on('click', 'li', function () {
        $(this).css({'background': '#1d87d1', 'color': '#FFFFFF'})
            .children('img').hide()
            .end().children('i').css('display', 'inline-block')
            .end().siblings().css({'background': '#eaedf1', 'color': '#1d87d1'})
            .children('img').show()
            .end().children('i').css('display', 'none');
    })
    $("#ml-0").click();
    $("#picTab").click(function () {
        removewAllActive();
        $("#picModel").addClass("active");
        initPic();
    })
})

function changeMl(proid, bdcqzh, bdclx, bdcdyh, ppbdcdyhid, ppbdcdyh, fwbm, tdid, tdbdcqzh) {
    jQuery("#djsj-grid-table").jqGrid("clearGridData");
    jQuery("#td-grid-table").jqGrid("clearGridData");
    ml_proid = proid;
    ml_bdcqzh = bdcqzh;
    ml_bdclx = bdclx;
    ml_bdcdyh = bdcdyh;
    djsjbdcdy_bdcdyh = "";
    djsjbdcdy_id = "";
    td_bdcqzh = "";
    td_id = "";
    $("#djsjbdcdy_bdcdyh").val("");
    $("#djsjbdcdy_fwbm").val("");
    $("#td_bdcqzh").val("");
    if (tdid != null && tdid != "") {
        td_id = tdid;
    }
    if (tdbdcqzh != null && tdbdcqzh != "") {
        td_bdcqzh = tdbdcqzh;
    }
    if (ppbdcdyh != null && ppbdcdyh != "") {
        djsjbdcdy_bdcdyh = ppbdcdyh;
        $("#djsjbdcdy_bdcdyh").val(djsjbdcdy_bdcdyh);
    }
    if (ppbdcdyhid != null && ppbdcdyhid != "") {
        djsjbdcdy_id = ppbdcdyhid;
    }
    if (isNotBlank(fwbm) && isBlank(djsjbdcdy_bdcdyh)) {
        $("#djsjbdcdy_fwbm").val(fwbm);
        setTimeout(function () {
            $("#djsjbdcdy_searchBtn").click()
        }, 500)
    }
    if (isNotBlank(fwbm) || isNotBlank(djsjbdcdy_bdcdyh)) {
        setTimeout(function () {
            $("#djsjbdcdy_searchBtn").click()
        }, 500)
    }
}

function removewAllActive() {
    var domArray = $(".tab-pane");
    if (domArray != null && domArray.length > 0) {
        for (var i = 0; i < domArray.length; i++) {
            var dom = domArray[i];
            $(dom).removeClass("active");
        }
    }
}

function initPic() {
    $("#picModel").empty();
    var dom = '<form class="UItable" id="glForm" method="post" style="margin: auto;margin-top:50px; width: 600px">';
    dom += '<table cellpadding="0" cellspacing="0"class="picTable">';
    if (ml_bdclx != "") {
        var bdclx = "";
        if (ml_bdclx == "TDFW") {
            bdclx = "土地房屋";
        }
        if (ml_bdclx == "TD") {
            bdclx = "土地";
        }
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>不动产类型：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + bdclx + '</strong></td>';
        dom += '</tr>';
    }
    if (ml_bdcqzh != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>产权证号：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + ml_bdcqzh + '</strong></td>';
        dom += '</tr>';
    }
    if (djsjbdcdy_bdcdyh != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>不动产单元号：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + djsjbdcdy_bdcdyh + '</strong></td>';
        dom += '</tr>';
    }
    if (djsjbdcdy_qlr != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>产权权利人：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + djsjbdcdy_qlr + '</strong></td>';
        dom += '</tr>';
    }
    if (djsjbdcdy_zl != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>产权坐落：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + djsjbdcdy_zl + '</strong></td>';
        dom += '</tr>';
    }
    if (djsjbdcdy_dytdmj != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>独用土地面积：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + djsjbdcdy_dytdmj + '</strong></td>';
        dom += '</tr>';
    }
    if (djsjbdcdy_fttdmj != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>分摊土地面积：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + djsjbdcdy_fttdmj + '</strong></td>';
        dom += '</tr>';
    }
    if (td_bdcqzh != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>土地证号：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + td_bdcqzh + '</strong></td>';
        dom += '</tr>';
    }

    if (td_qlr != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>土地权利人：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + td_qlr + '</strong></td>';
        dom += '</tr>';
    }

    if (td_zl != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>土地坐落：</strong></td>';
        dom += '<td style="width: 350px" align="center"><strong>' + td_zl + '</strong></td>';
        dom += '</tr>';
    }

    if (djsjbdcdy_bdcdyh != "") {
        dom += '<tr>';
        dom += '<td style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>需重新获取权籍信息：</strong></td>';
        dom += '<td style="width: 350px" align="center"><select id="sfhqqj" name="sfhqqj" style="" class="select">' +
            '<option value="0" selected>否</option>' +
            '<option value="1">是</option>' +
            '</select></td>';
        dom += '</tr>';
    }

    dom += '</table>';
    dom += '<div style="float: right">'
    dom += '<button type="button" class="btn btn-sm btn-primary" id="qrgl" onclick="qrPic()">确认</button>';
    dom += '</div>'
    dom += '</form>';
    $("#picModel").append(dom);
}

function qrPic() {
    var bdcPpgxLog = {};
    if (bdclx != "TDFW") {
        bdcPpgxLog.tdproid = ml_proid;
        bdcPpgxLog.tdbdcdyh = ml_bdcdyh;
    } else {
        bdcPpgxLog.fwproid = ml_proid;
        bdcPpgxLog.fwbdcdyh = ml_bdcdyh;
        bdcPpgxLog.tdproid = td_id;
        bdcPpgxLog.tdbdcdyh = td_bdcqzh;
    }
    bdcPpgxLog.qjid = djsjbdcdy_id;
    bdcPpgxLog.bdcdyh = djsjbdcdy_bdcdyh;
    $.ajax({
        url: bdcdjUrl + "/bdcpic/checkPic",
        type: "POST",
        data: {bdcPpgxLogJson: JSON.stringify(bdcPpgxLog), bdclx: bdclx},
        success: function (data) {
            if (data != null) {
                if (data.checkModel == "alert") {
                    infoMsg(data.checkMsg, 3000);
                } else if (data.checkModel == "comfirm") {
                    showConfirmDialog("提示信息", data.checkMsg, "pic", JSON.stringify(bdcPpgxLog), "", "");
                } else {
                    if (djsjbdcdy_qlr == td_qlr && djsjbdcdy_zl == td_zl) {
                        pic(bdcPpgxLog);
                    } else {
                        showConfirmDialog("提示信息", "权利人和坐落信息不完全一致,是否继续匹配!", "pic", JSON.stringify(bdcPpgxLog), "", "");
                    }
                }
            }
        },
        error: function () {
            infoMsg("验证失败", 3000);
        }
    });
}

function pic(bdcPpgxLog) {
    $("#loadingModal").modal('show');
    $("#loadingModal").modal('show');
    var sfhqqj=$("#sfhqqj").val();
    $.ajax({
        url: bdcdjUrl + "/bdcpic/qrPic",
        type: "POST",
        data: {bdcPpgxLogJson: JSON.stringify(bdcPpgxLog), bdclx: bdclx,sfhqqj:sfhqqj},
        success: function (data) {
            $('#loadingModal').modal('hide');
            if (data.msg == "success") {
                infoMsg("匹配成功", 3000);
            } else {
                infoMsg("匹配失败", 3000);
            }
        },
        error: function () {
            infoMsg("匹配失败", 3000);
        }
    });
}

//显示确认对话框
function showConfirmDialog(title, msg, okMethod, okParm, cancelMethod, cancelParm) {
    var comfirmDia = bootbox.dialog({
        message: "<h3><b>" + msg + "</b></h3>",
        title: title,
        buttons: {
            OK: {
                label: "忽略，继续匹配",
                className: "btn-primary",
                callback: function () {
                    if (okMethod != null && okMethod != "")
                        eval(okMethod + "(" + okParm + ")");
                }
            },
            Cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                    comfirmDia.hide();
                    if (cancelMethod != null && cancelMethod != "")
                        eval(cancelMethod + "(" + cancelParm + ")");
                }
            }
        }
    });
}

