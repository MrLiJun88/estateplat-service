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
    $("#combineTab").click(function () {
        removewAllActive();
        $("#combineModel").addClass("active");
        initCombine();
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

function initCombine() {
    $.ajax({
        url: bdcdjUrl + "/bdcFghb/getHbDetailInfo",
        type: 'POST',
        dataType: 'json',
        data: {proids: proids},
        success: function (data) {
            if(data != null && data != undefined){
                organizeHbDetail(data);
            }
        },
        error: function (data) {
            infoMsg("获取合并信息失败!");
        }
    });
}

function organizeHbDetail(data){
    $("#combineModel").empty();

    var dom = '<form class="UItable" id="combineForm" method="post" style="margin: auto;margin-top:50px; width: 800px">';
    dom += '<table cellpadding="0" cellspacing="0"class="combineTable">';

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

            var bdcdyh = '';
            if(djsjbdcdy_bdcdyh != '' && djsjbdcdy_bdcdyh != null){
                bdcdyh = djsjbdcdy_bdcdyh;
            }
            dom += '<tr>';
            dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>不动产单元号：</strong></td>';
            dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + bdcdyh + '</strong></td>';
            dom += '</tr>';

            var mj = '';
            var zdzhmj = '';
            if(data.cq.mj != null && data.cq.mj != undefined){
                mj = data.cq.mj;
            }
            if(data.cq.zdzhmj != null && data.cq.zdzhmj != undefined){
                zdzhmj = data.cq.zdzhmj;
            }
            dom += '<tr>';
            dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>面积：</strong></td>';
            dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + mj + '</strong></td>';
            dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>土地面积：</strong></td>';
            dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zdzhmj + '</strong></td>';
            dom += '</tr>';
    }

    if(data.dy != null && data.dy != undefined){
        dom += '<tr>';
        dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>抵押信息</strong></td>';
        dom += '</tr>';

        var dyzm = '';
        if(data.dy.dyzm != null && data.dy.dyzm != undefined){
            dyzm = data.dy.dyzm;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>抵押证明号：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + dyzm + '</strong></td>';
        dom += '</tr>';

        var bdbzqse = '';
        if(data.dy.bdbzqse != '' && data.dy.bdbzqse != null){
            bdbzqse = data.dy.bdbzqse;
        }
        var dyfs = '';
        if(data.dy.dyfs != '' && data.dy.dyfs != null){
            if(data.dy.dyfs == '1'){
                dyfs = '一般抵押';
            }else if(data.dy.dyfs == '2'){
                dyfs = '最高额抵押';
            }
        }
        dom += '<tr>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>抵押金额：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + bdbzqse + '</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>抵押方式：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + dyfs + '</strong></td>';
        dom += '</tr>';

        var zwlxksqx = '';
        var zwlxjsqx = '';
        if(data.dy.zwlxksqx != null && data.dy.zwlxksqx != undefined){
            zwlxksqx = data.dy.zwlxksqx;
        }
        if(data.dy.zwlxjsqx != null && data.dy.zwlxjsqx != undefined){
            zwlxjsqx = data.dy.zwlxjsqx;
        }
        dom += '<tr>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行开始期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxksqx + '</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行结束期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxjsqx + '</strong></td>';
        dom += '</tr>';
    }

    if(data.cf != null && data.cf != undefined){
        dom += '<tr>';
        dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>查封信息</strong></td>';
        dom += '</tr>';

        var cfwh = '';
        if(data.cf.cfwh != null && data.cf.cfwh != undefined){
            cfwh = data.cf.cfwh;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>查封文号：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + cfwh + '</strong></td>';
        dom += '</tr>';

        var cffw = '';
        if(data.cf.cffw != null && data.cf.cffw != undefined){
            cffw = data.cf.cffw;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>查封范围：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + cffw + '</strong></td>';
        dom += '</tr>';


        var cfksqx = '';
        var cfjsqx = '';
        if(data.cf.cfksqx != null && data.cf.cfksqx != undefined){
            cfksqx = data.cf.cfksqx;
        }
        if(data.cf.cfjsqx != null && data.cf.cfjsqx != undefined){
            cfjsqx = data.cf.cfjsqx;
        }
        dom += '<tr>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>查封开始期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + cfksqx + '</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>查封结束期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + cfjsqx + '</strong></td>';
        dom += '</tr>';
    }

    if(data.yg != null && data.yg != undefined){
        dom += '<tr>';
        dom += '<td colspan="4" style="width: 800px;border-style:none;" align="center"><strong>预告信息</strong></td>';
        dom += '</tr>';

        var ygzm = '';
        if(data.yg.ygzm != null && data.yg.ygzm != undefined){
            ygzm = data.yg.ygzm;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>预告证明号：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + ygzm + '</strong></td>';
        dom += '</tr>';

        var dyygzm = '';
        if(data.yg.dyygzm != null && data.yg.dyygzm != undefined){
            dyygzm = data.yg.dyygzm;
        }
        dom += '<tr>';
        dom += '<td colspan="2" style="width: 250px" align="center" bgcolor="#EEEEEE"><strong>抵押预告证明号：</strong></td>';
        dom += '<td colspan="2" style="width: 550px" align="center"><strong>' + dyygzm + '</strong></td>';
        dom += '</tr>';


        var zwlxksqx = '';
        var zwlxjsqx = '';
        if(data.yg.zwlxksqx != null && data.yg.zwlxksqx != undefined){
            zwlxksqx = data.yg.zwlxksqx;
        }
        if(data.yg.zwlxjsqx != null && data.yg.zwlxjsqx != undefined){
            zwlxjsqx = data.yg.zwlxjsqx;
        }
        dom += '<tr>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行开始期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxksqx + '</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center" bgcolor="#EEEEEE"><strong>债务履行结束期限：</strong></td>';
        dom += '<td colspan="1" style="width: 200px" align="center"><strong>' + zwlxjsqx + '</strong></td>';
        dom += '</tr>';
    }

    dom += '</table>';
    dom += '<div style="float: right">'
    dom += '<button type="button" class="btn btn-sm btn-primary" id="qrCombine">确认合并</button>';
    dom += '</div>'
    dom += '</form>';
    $("#combineModel").append(dom);

    $("#qrCombine").bind("click",function(){
        qrCombine();
    });
}

function qrCombine() {
    //进行房地产权合并
    $.ajax({
        url: bdcdjUrl + "/bdcFghb/combine",
        type: 'POST',
        dataType: 'json',
        data: {proids: proids,bdcdyh:djsjbdcdy_bdcdyh,qjid:djsjbdcdy_id},
        success: function (data) {
            if(data != null && data.msg != null
                && data.msg != '' && data.msg != undefined
            && data.msg == 'success'){
                infoMsg("合并成功!");
            }else{
                infoMsg("合并失败!");
            }
        },
        error: function (data) {
            infoMsg("合并失败!");
        }
    });
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