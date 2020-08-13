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
})

function changeCkxxMl(proid, bdcdyh) {
    var url = analysisUrl + "/jyZdCx?proid=" + proid + "&bdcdyh=" + bdcdyh
    $("#iframe").attr("src", url);
}