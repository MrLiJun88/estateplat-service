var _menuJsonObj;
var proid;
var readOnly;

/*var getArgs=(function(){
    var sc=$('#tab');
    var paramsArr=sc.attr("data").split('&');
    var args={},argsStr=[],param,t,name,value;
    for(var ii=0,len=paramsArr.length;ii<len;ii++){
        param=paramsArr[ii].split('=');
        name=param[0],value=param[1];
        if(typeof args[name]=="undefined"){ //参数尚不存在
            args[name]=value;
        }else if(typeof args[name]=="string"){ //参数已经存在则保存为数组
            args[name]=[args[name]]
            args[name].push(value);
        }else{  //已经是数组的
            args[name].push(value);
        }
    }
    return function(){return args;} //以json格式返回获取的所有参数
})();*/
function findMenuObj(menuObj, menuId) {
    for (var i = 0; i < menuObj.length; i++) {
        if (menuObj[i].resourceId == menuId) {
            return  menuObj[i];
        } /*else {
            var menu = findMenuObj(menuObj.children[i], menuId);
            if (menu != null)
                return menu;
        }*/
    }
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function setFrameHeight() {
    var offset = 0;
    var userAgent = navigator.userAgent.toLowerCase();
    if (/firefox/.test(userAgent))
        offset = 3;
    else if (/msie/.test(userAgent))
        offset = 3;
    else if(/chrome/.test(userAgent))
        offset = 2;

    var topOffset=60;
    var mainheight = $(window).height() - topOffset - offset;
    $('#mainFrame').height(mainheight);
    $('.tab-pane').height(mainheight);
}
/*入口*/
$(document).ready(function () {
    setFrameHeight();
    $(window).resize(function () {
        setFrameHeight();
    });

    var url=$('#tab').attr("url");
    if(url==''){
       return;
    }
    proid = getQueryString("proid");
    readOnly=getQueryString("readOnly");
    $.getJSON(url, function (result) {
        _menuJsonObj = result;
        /*初始化第一个*/
        initTab(0);
    });
   /* //一级菜单
    $.get("${bdcdjUrl}/bdcConfig/getResourceByDjlx", function (result) {
        _menuJsonObj = result;
        var rootMenuChilds = _menuJsonObj.children;
        if (rootMenuChilds.length > 1) {
            for (var i = 0; i < rootMenuChilds.length; i++) {
                var menu = rootMenuChilds[i];
                var tabStr = '<li ><a href="#" data-toggle="tab" onclick="initTab(\'' + menu.id + '\')" style="border-top-left-radius:5px;border-top-right-radius:5px;border-bottom-right-radius:0px;border-bottom-left-radius:0px;">' +
                    '<i class="bigger-110 ' + menu.cls + '"></i> ' + menu.text +
                    '</a></li>';
                $(tabStr).appendTo('#menuTab');
            }
        }
        //初始化第一个tab
        if (_menuJsonObj.children.length > 0) {
            initTab(_menuJsonObj.children[menuIndex].id);
            $('#menuTab li:eq(' + menuIndex + ')').addClass("active");
        }

        var taskMenuIndex = findTaskListMenuIndex(_menuJsonObj);
        $('.tasks').each(function(){
            var hrefUrl = $(this).attr('href');
            $(this).attr('href',hrefUrl+taskMenuIndex);
        });

    });
    $.getJSON(platform_url+"/portal!count.action",function(result){
        if(result!=null){
            $('#task').text(result.task);
            $('#taskCount').text(result.task);
            $('#taskOverCount').text(result.taskover);
            $('#taskCqCount').text(result.taskcq);
        }
    });*/

});

function initTab(menuId) {
    $(".nav-tabs").html('');
    var secondMenu;
    //循环给路径添加到标签上
    for (var i = 0; i < _menuJsonObj.length; i++) {
        /*if (_menuJsonObj[i].id == menuId) {*/
            addLinkBar(_menuJsonObj[i].resourceId);
       /* }*/
    }
    if(_menuJsonObj.length==0){
        return;
    }
    $('#myTab li:eq(' + menuId + ')').addClass("active");
    //打开第一个tab页
    dealUpdatePropert(readOnly,_menuJsonObj[menuId]);
    var hasParam=parse_url(_menuJsonObj[menuId].resourceUrl);
    var showtoolbar='';
    if(readOnly){
        showtoolbar="&__showtoolbar__="+false;
    }
    if(hasParam){
        openMenuResource(_menuJsonObj[menuId].resourceUrl+"?proid="+proid+showtoolbar, "");
    }else{
        openMenuResource(_menuJsonObj[menuId].resourceUrl+"&proid="+proid+showtoolbar, "");
    }

  /*  if (secondMenu.children.length > 0) {
        displaySidebar(true);
        for (var i = 0; i < secondMenu.children.length; i++) {
            if(i==0&&secondMenu.children[i].children.length==0)
                addMenu(secondMenu.children[i],true);
            else
                addMenu(secondMenu.children[i],false);
        }
    }else{
        displaySidebar(false);
    }*/
}

//根据readOnly属性处理url
function dealUpdatePropert(readOnly,menuObj){
   if(readOnly){
     var url1=  menuObj['resourceUrl'].replace("?updateZs=yes","");
     var url2=  url1.replace("?updateQllx=yes","");
       menuObj['resourceUrl']=url2;
   }
}

/*function MakeMenuActive(menuObj) {
    try {
        if (menuObj.parent()[0].tagName != "DIV") {
            if (menuObj.parent()[0].tagName == "LI") {
                $(menuObj.parent()[0]).addClass("active open");
            }
            MakeMenuActive(menuObj.parent());
        }
    } catch (e) {
    }
}*/

/*function MakeMenuNoActive() {
    $('#sidebar li').each(function () {
        $(this).removeClass('active');
    });
}*/

/*function addMenu(menuObj,isActive) {
    if (!menuObj.cls)
        menuObj.cls = 'fa fa-angle-double-right';

    var menuHtml = "<li menuid='" + menuObj.id + "'>" +
        '<a href="#" class="dropdown-toggle" link="' + menuObj.link + '" model="' + menuObj.model + '" onclick="MenuClickEvent(this)" >' +
        '<i class="' + menuObj.cls + '"/>' +
        '<span class="menu-text"> ' + menuObj.text + '</span>';
    if (menuObj.children.length > 0)
        menuHtml = menuHtml + '<b class="arrow fa fa-angle-down"></b>';
    menuHtml = menuHtml + '</a>';
    if (menuObj.children.length > 0) {
        if (menuObj.expanded)
            menuHtml = menuHtml + '<ul class="submenu" style="display:block"/>';
        else
            menuHtml = menuHtml + '<ul class="submenu"/>';
    }
    menuHtml = menuHtml + '</li>';

    var pMenu = $('#sidebar li[menuid="' + menuObj.pid + '"]');
    if (pMenu.length > 0) {
        var submenu = $(pMenu[0]).find("ul");
        $(submenu[0]).append($(menuHtml));
    } else {
        $('#sidebar ul:first').append($(menuHtml));
    }
    for (var i = 0; i < menuObj.children.length; i++) {
        addMenu(menuObj.children[i],false);
    }

    if (menuObj.text === menuText || (isActive!=null&&isActive)) {
        $("#breadcrumbs .breadcrumb").html('');
        addLinkBar(menuObj.id);   //导航栏
        MakeMenuNoActive();
        $("#sidebar li[menuid='" + menuObj.id + "']").addClass("active");
        MakeMenuActive($("#sidebar li[menuid='" + menuObj.id + "']"));
        openMenuResource(menuObj.link, menuObj.model);
    }
}*/
//点击事件
function MenuClickEvent(aobj) {
    openMenuResource($(aobj).attr("link"), "");
   /* var menuId = $(aobj).parent().attr('menuid');
    if (menuId) {
        if ($(aobj).parent().children("ul").children("li").length == 0) {
            $("#breadcrumbs .breadcrumb").html('');
            addLinkBar(menuId);   //导航栏
            MakeMenuNoActive();
            $("#sidebar li[menuid='" + menuId + "']").addClass("active");
            MakeMenuActive($("#sidebar li[menuid='" + menuId + "']"));
            openMenuResource($(aobj).attr('link'), $(this).attr('model'));
        }
    }*/
}

//判断是否有参数
function parse_url(_url){ //定义函数
    var pattern = /(\w+)=(\w+)/ig;//定义正则表达式
    var parames = new Array();//定义数组
    _url.replace(pattern, function(a, b, c){
        parames.push(b);
    });
      if(parames.length==0){
          return true;
      }else{
          return false;
      }
}

function openMenuResource(linkstr, model) {
    if (linkstr != null) {
        $('#mainFrame').attr("src", linkstr);
       /* $.get("menu/open?link=" + linkstr, function (result) {
            var resourceUrl = buildLinkUrl(result.resourceUrl);
            if (!model || model == 0)
                $('#mainFrame').attr("src", resourceUrl);
            else if (model == 2)
                window.document.location = resourceUrl;
            else if (model == 3)
                $('#mainFrame').load(resourceUrl);

        });*/
    }
}

/*function buildLinkUrl(resourceUrl) {
    if (resourceUrl!=null&&resourceUrl.substr(0, 1) != "/" && resourceUrl.substr(0, 1) != "h") {
        return platform_url + '/' + resourceUrl;
    } else {
        return  resourceUrl;
    }
}*/

/**
 * 导航条
 */
function addLinkBar(menuId) {
    var menuObj = findMenuObj(_menuJsonObj, menuId);
    if (!menuObj) return;
    dealUpdatePropert(readOnly,menuObj);
    var showtoolbar='';
    if(readOnly){
        showtoolbar="&__showtoolbar__="+false;
    }
    var hasParam=parse_url(menuObj.resourceUrl);
    var btnHtml = "<li menuid='" + menuObj.resourceId + "'>";
    if (menuObj.resourceUrl && menuObj.resourceUrl!='' && menuObj.resourceUrl != null){
        if(hasParam){
            btnHtml = btnHtml + '<a href="#" data-toggle="tab" onclick="MenuClickEvent(this)" link="' + menuObj.resourceUrl +"?proid="+proid +showtoolbar+'">';
        }else{
            btnHtml = btnHtml + '<a href="#" data-toggle="tab" onclick="MenuClickEvent(this)" link="' + menuObj.resourceUrl +"&proid="+proid +showtoolbar+'">';
        }
        if (menuObj.resourceImg && menuObj.resourceImg!='' && menuObj.resourceImg != null){
            btnHtml = btnHtml + '<i class="' + menuObj.resourceImg + '"></i>';
        }
        btnHtml = btnHtml +'&nbsp;'+ menuObj.resourceName + '</a>';
    }else{
        btnHtml = btnHtml + menuObj.resourceName;
    }
    btnHtml = btnHtml + '</li>';
    $("#myTab").append(btnHtml);
   /* var firstLiObj = $("#breadcrumbs .breadcrumb li:first");
    if (firstLiObj.length == 0)
        $("#breadcrumbs .breadcrumb").append($(btnHtml));
    else
        $(btnHtml).insertBefore(firstLiObj);

    if (menuObj.pid) {
        addLinkBar(menuObj.pid);
    }*/
}


/*
function displaySidebar(display){
    var sidebar = $('#sidebar');
    var mainContent = $('.main-content')[0];
    var breadcrumbs = $('#breadcrumbs');
    if(display){
        $(mainContent).removeClass('no-margin');
        breadcrumbs.show();
        sidebar.show();
        setFrameHeight();
    }else{
        breadcrumbs.hide();
        sidebar.hide();
        setFrameHeight();
        $(mainContent).addClass('no-margin');
    }

}

function findTaskListMenuIndex(rootMenus){
    for(var i=0;i<rootMenus.children.length;i++){
        if(findTaskListInMenu(rootMenus.children[i]))
           return i;
    }
    return 0;
}

function findTaskListInMenu(menus){
    for(var i=0;i<menus.children.length;i++){
        if('待办任务'===menus.children[i].text)
            return true;
        else if(menus.children[i].children.length>0){
            findTaskListInMenu(menus.children[i].children);
        }
    }
    return false;
}
*/
/*
function displaySidebar(display){
    var sidebar = $('#sidebar');
    var mainContent = $('.main-content')[0];
    var breadcrumbs = $('#breadcrumbs');
    if(display){
        $(mainContent).removeClass('no-margin');
        breadcrumbs.show();
        sidebar.show();
        setFrameHeight();
    }else{
        breadcrumbs.hide();
        sidebar.hide();
        setFrameHeight();
        $(mainContent).addClass('no-margin');
    }

}

function findTaskListMenuIndex(rootMenus){
    for(var i=0;i<rootMenus.children.length;i++){
        if(findTaskListInMenu(rootMenus.children[i]))
           return i;
    }
    return 0;
}

function findTaskListInMenu(menus){
    for(var i=0;i<menus.children.length;i++){
        if('待办任务'===menus.children[i].text)
            return true;
        else if(menus.children[i].children.length>0){
            findTaskListInMenu(menus.children[i].children);
        }
    }
    return false;
}
*/



