//引用下面这段js，防止调用$.browser时浏览器报错
jQuery.browser={};
(function(){
    jQuery.browser.msie=false;
    jQuery.browser.version=0;
    if(navigator.userAgent.match(/MSIE ([0-9]+)./)){
        jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;
    }
})();