$(document).ready(function(){
	
	function unfoldMenu(pn, cn){
		var p = $('span.flip'), c = $('div.panel'), cc = $('div.panel-current');
		
			p.removeClass('flip-current');
			cc.hide(200, function(){
				$(this).removeAttr('style').removeClass('panel-current');
			})
			if(c.index(cn) != c.index(cc)){
			pn.addClass('flip-current');
			cn.show(200, function(){
				$(this).removeAttr('style').addClass('panel-current');
			});
		}
	}
	
	function menuHandle(){
		$('span.flip').click(function(){
			var pn = $(this), cn = pn.next();
			unfoldMenu(pn, cn);
		});
	}
	
	//设置默认下当前展开
	function menuCurrent(){
		var idx = $('input.menu-code-index').val(), m, pn, cn, p = $('span.flip'), c = $('div.panel'), cc = $('div.panel-current');
		if(/c(\d)+/.test(idx)){ //判断c（十进制）条件
			m = $('a[data-service-index="' + idx + '"]').addClass('current');
			cn = m.parents('div.panel');
			pn = cn.prev();
			unfoldMenu(pn, cn);
		}
	}
	
	menuCurrent();
	menuHandle();
	
});


// 收缩菜单
			$(document).ready(function () {
				$("#arrow").click(function(){
					$("#sidebar").toggleClass('sidebar2');
					$("#sidebar").toggleClass('sidebar');
					$("#menu2Tab").toggleClass('menu2Tab2');
					$("#menu2Tab").toggleClass('menu2Tab');
					$(".leftMenu").toggle();
					$("#main-content").toggleClass('main-content');
					
				});
		});
			
// end  收缩菜单 