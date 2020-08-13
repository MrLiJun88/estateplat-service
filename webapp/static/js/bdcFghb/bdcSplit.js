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
    $("#splitTab").click(function () {
        removewAllActive();
        $("#splitModel").addClass("active");
        initSplit();
    })
})

function changeMl(proid, bdcqzh, bdclx, bdcdyh) {
    ml_proid = proid;
    ml_bdcqzh = bdcqzh;
    ml_bdclx = bdclx;
    ml_bdcdyh = bdcdyh;
    djsjbdcdy_bdcdyh = "";
    djsjbdcdy_id = "";
    td_bdcqzh = "";
    td_id = "";
    $("#djsj-grid-table").jqGrid("clearGridData");
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

function initSplit() {
    $.ajax({
        url: bdcdjUrl + "/bdcFghb/getFgDetailInfo",
        type: 'POST',
        dataType: 'json',
        data: {splitDatas: JSON.stringify($splitData)},
        success: function (data) {
            if(data != null && data != undefined){
                organizeFgDetail(data);
            }
        },
        error: function (data) {
            infoMsg("获取拆分信息失败!");
        }
    });
}

function organizeFgDetail(data) {
    $("#splitModel").empty();
    var dom = '<form class="UItable" id="splitForm" method="post" style="margin: auto;margin-top:50px; width: 800px">';
    dom += '<table cellpadding="0" cellspacing="0"class="splitTable">';

    if(data.cq != null && data.cq != undefined){
        dom += '<tr>';
        dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>产权信息</strong></td>';
        dom += '</tr>';

        var bdcqzh = '';
        if(data.cq.bdcqzh != null && data.cq.bdcqzh != undefined){
            bdcqzh = data.cq.bdcqzh;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>产权证号：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + bdcqzh + '</strong></td>';
        dom += '</tr>';

        if(data.cq.fwxxlist != null && data.cq.fwxxlist != undefined && data.cq.fwxxlist.length > 0){
            for (var i = 0; i < data.cq.fwxxlist.length; i++) {
                var zl = '';
                if(data.cq.fwxxlist[i].zl != null && data.cq.fwxxlist[i].zl != undefined){
                    zl = data.cq.fwxxlist[i].zl;
                }
                var bdcdyh = '';
                if(data.cq.fwxxlist[i].bdcdyh != null && data.cq.fwxxlist[i].bdcdyh != undefined){
                    bdcdyh = data.cq.fwxxlist[i].bdcdyh;
                }
                dom += '<tr>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>坐落：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zl + '</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>不动产单元号：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + bdcdyh + '</strong></td>';
                dom += '</tr>';
            }
        }
    }

    if(data.dy != null && data.dy != undefined){
        if(data.dy.dyxxs != null && data.dy.dyxxs != undefined && data.dy.dyxxs.length > 0){
            dom += '<tr>';
            dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>抵押信息</strong></td>';
            dom += '</tr>';

            for (var i = 0; i < data.dy.dyxxs.length; i++) {
                var dyzm = '';
                if(data.dy.dyxxs[i].dyzm != null && data.dy.dyxxs[i].dyzm != undefined){
                    dyzm = data.dy.dyxxs[i].dyzm;
                }
                dom += '<tr>';
                dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>抵押证明号：</strong></td>';
                dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + dyzm + '</strong></td>';
                dom += '</tr>';
                var dyfs = '';
                if(data.dy.dyxxs[i].dyfs != null && data.dy.dyxxs[i].dyfs != undefined){
                    if(data.dy.dyxxs[i].dyfs == '1'){
                        dyfs = '一般抵押';
                    }else if(data.dy.dyxxs[i].dyfs == '2'){
                        dyfs = '最高额抵押';
                    }
                }
                var bdbzzqs = '';
                if(data.dy.dyxxs[i].bdbzzqs != null && data.dy.dyxxs[i].bdbzzqs != undefined){
                    bdbzzqs = data.dy.dyxxs[i].bdbzzqs;
                }
                dom += '<tr>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>抵押方式：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + dyfs + '</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>抵押金额：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + bdbzzqs + '</strong></td>';
                dom += '</tr>';
                var zwlxksqx = '';
                if(data.dy.dyxxs[i].zwlxksqx != null && data.dy.dyxxs[i].zwlxksqx != undefined){
                    zwlxksqx = data.dy.dyxxs[i].zwlxksqx;
                }
                var zwlxjsqx = '';
                if(data.dy.dyxxs[i].zwlxjsqx != null && data.dy.dyxxs[i].zwlxjsqx != undefined){
                    zwlxjsqx = data.dy.dyxxs[i].zwlxjsqx;
                }
                dom += '<tr>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行开始期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxksqx + '</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行结束期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxjsqx + '</strong></td>';
                dom += '</tr>';
            }
        }
    }

    if(data.cf != null && data.cf != undefined){
        if(data.cf.cfxxs != null && data.cf.cfxxs != undefined && data.cf.cfxxs.length > 0){
            dom += '<tr>';
            dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>查封信息</strong></td>';
            dom += '</tr>';

            for (var i = 0; i < data.cf.cfxxs.length; i++) {
                var cfwh = '';
                if(data.cf.cfxxs[i].cfwh != null && data.cf.cfxxs[i].cfwh != undefined){
                    cfwh = data.cf.cfxxs[i].cfwh;
                }
                dom += '<tr>';
                dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>查封文号：</strong></td>';
                dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + cfwh + '</strong></td>';
                dom += '</tr>';
                var cffw = '';
                if(data.cf.cfxxs[i].cffw != null && data.cf.cfxxs[i].cffw != undefined){
                    cffw = data.cf.cfxxs[i].cffw;
                }
                dom += '<tr>';
                dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>查封范围：</strong></td>';
                dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + cffw + '</strong></td>';
                dom += '</tr>';
                var cfksqx = '';
                if(data.cf.cfxxs[i].cfksqx != null && data.cf.cfxxs[i].cfksqx != undefined){
                    cfksqx = data.cf.cfxxs[i].cfksqx;
                }
                var cfjsqx = '';
                if(data.cf.cfxxs[i].cfjsqx != null && data.cf.cfxxs[i].cfjsqx != undefined){
                    cfjsqx = data.cf.cfxxs[i].cfjsqx;
                }
                dom += '<tr>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>查封开始期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + cfksqx + '</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>查封结束期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + cfjsqx + '</strong></td>';
                dom += '</tr>';
            }
        }
    }

    if(data.yg != null && data.yg != undefined){
        if(data.yg.ygxxs != null && data.yg.ygxxs != undefined && data.yg.ygxxs.length > 0){
            dom += '<tr>';
            dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>预告信息</strong></td>';
            dom += '</tr>';

            for (var i = 0; i < data.yg.ygxxs.length; i++) {
                var ygzm = '';
                if(data.yg.ygxxs[i].ygzm != null && data.yg.ygxxs[i].ygzm != undefined){
                    ygzm = data.yg.ygxxs[i].ygzm;
                }
                dom += '<tr>';
                dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>预告证明号：</strong></td>';
                dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + ygzm + '</strong></td>';
                dom += '</tr>';
                var ygdjzl = '';
                if(data.yg.ygxxs[i].ygdjzl != null && data.yg.ygxxs[i].ygdjzl != undefined){
                    if(data.yg.ygxxs[i].ygdjzl == '1'){
                        ygdjzl = '预售商品房买卖预告登记';
                    }else if(data.yg.ygxxs[i].ygdjzl == '2'){
                        ygdjzl = '其它不动产买卖预告登记';
                    }else if(data.yg.ygxxs[i].ygdjzl == '3'){
                        ygdjzl = '预售商品房抵押权预告登记';
                    }else if(data.yg.ygxxs[i].ygdjzl == '4'){
                        ygdjzl = '其它不动产抵押权预告登记';
                    }
                }
                dom += '<tr>';
                dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>预告登记种类：</strong></td>';
                dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + ygdjzl + '</strong></td>';
                dom += '</tr>';
                var zwlxksqx = '';
                if(data.yg.ygxxs[i].zwlxksqx != null && data.yg.ygxxs[i].zwlxksqx != undefined){
                    zwlxksqx = data.yg.ygxxs[i].zwlxksqx;
                }
                var zwlxjsqx = '';
                if(data.yg.ygxxs[i].zwlxjsqx != null && data.yg.ygxxs[i].zwlxjsqx != undefined){
                    zwlxjsqx = data.yg.ygxxs[i].zwlxjsqx;
                }
                dom += '<tr>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行开始期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxksqx + '</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行结束期限：</strong></td>';
                dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxjsqx + '</strong></td>';
                dom += '</tr>';
            }
        }
    }

    dom += '</table>';
    dom += '<div style="float: right">'
    dom += '<button type="button" class="btn btn-sm btn-primary" id="qrSplit">确认分割</button>';
    dom += '</div>'
    dom += '</form>';
    $("#splitModel").append(dom);

    $("#qrSplit").bind("click",function(){
        qrSplit();
    });
}

function qrSplit() {
    //验证分割产权数量是否与bdcfwfzxx数量相同
    var fzxxidlist = fzxxids.split(",");
    if($splitData.length == fzxxidlist.length){
        //进行房地产权多幢分割
        $.ajax({
            url: bdcdjUrl + "/bdcFghb/split",
            type: 'POST',
            dataType: 'json',
            data: {splitDatas: JSON.stringify($splitData)},
            success: function (data) {
                if(data != null && data.msg != null
                    && data.msg != '' && data.msg != undefined
                    && data.msg == 'success'){
                    infoMsg("拆分成功!");
                }else{
                    infoMsg("拆分失败!");
                }
            },
            error: function (data) {
                infoMsg("拆分失败!");
            }
        });
    }else{
        infoMsg("存在房屋未选择不动产单元！");
    }
}


function infoMsg(message, showid) {
    var boxModel = bootbox.dialog({
        message: '<h5 style="text-align: center">' + message + '</h5>',
        size: 'large',
        onEscape: false,
        backdrop: true,
        closeButton: false
    });
    boxModel.modal('show');
    setTimeout(function () {
        boxModel.modal('hide');
    }, 1000);
}