<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <style>
        iframe{
            position:relative;
        }
        .page-content {
            padding: 0px 12px 24px;
        }

        body {
            background-color: #FFFFFF;
        }

        .headTab {
            position: fixed;
            top: 0px;
        }

        li.ui-menu-item.active  a, .headTab li.active a {
            background-color: #408fc6 !important;
            color: #fff !important;
        }

        li.ui-menu-item.active  a {
            background-color: #408fc6 !important;
            color: #fff !important;
        }

        .imgClass {
            max-width: 1000px;
            myimg: expression(onload=function(){
            this.style.width=(this.offsetWidth > 500)?"500px":"auto"}
        );
        }
        .fontClass{
            /*text-decoration:none;!important;*/
            color:#333;
            float:left;
            width:160px;
            overflow:hidden;
            text-overflow: ellipsis;
            white-space:nowrap;
        }
        .page-content{
            position: relative;
            margin: 15px 0;
            padding: 39px 19px 14px;
            background-color: #fff;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="static/css/other.css" >
    <link rel="stylesheet" href="static/css/doc.css" >
    <link rel="stylesheet" href="static/css/bootstrap-datetimepicker.min.css" >
    <link href="static/bootstrap/css/bootstrap.css" rel="stylesheet" />
    <script src="static/js/jquery-1.8.1.min.js"></script>
    <script src="static/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">

        <!--   自适应高度    -->
        $(document).ready(function () {
            loadLeftMenu();

        });

        $(window).resize(function() {
            loadLeftMenu();
        });

        function loadLeftMenu(){
            var winHeight=$(window).height();
            var leftMenuHieght=0;
            if (winHeight>0){
                leftMenuHieght1=winHeight-60;
            }
            $(".Adaptive").css({height:leftMenuHieght1  +"px"});
        }
        <!--end   自适应高度    -->

        <!--   滑动目录树    -->
        $(function(){

            function sideSlider(){
                if(!$(".help-side dl").length){
                    return false;
                }

                var $aCur = $(".help-side dl").find(".cur a"),
                        $targetA = $(".help-side dl dd a"),
                        $sideSilder = $(".side-slider"),
                        curT = $aCur.position().top +1;

                $sideSilder.stop(true, true).animate({
                    "top":curT
                });

                $targetA.mouseenter(function(){
                    var posT = $(this).position().top +1;
                    $sideSilder.stop(true, true).animate({
                        "top":posT
                    }, 240);
                }).parents(".help-side").mouseleave(function(_curT){
                            _curT = curT
                            $sideSilder.stop(true, true).animate({
                                "top":_curT
                            });
                        });
            };

            sideSlider();

        });
        <!--end   滑动目录树    -->
        <!--改变颜色-->
        function changeColor(name){
            $("a").each(function(){
                //alert($(this).text());
                if($(this).text() != name){
                    $(this).css("color","#333");
                }else{
                    $(this).css("color","#2828FF");
                }
            })
        }
    </script>
</head>

<body style="overflow:hidden">
<div class="main-container">
    <div class="page-content"  style="padding:0px;">
        <!--  滚动浏览书签  -->
        <div class="bookmarksBar">
            <!-- 目录树  -->
            <div class="Tree ">
                <div class="help-side">
                    <i class="first"></i>
                    <dl class="nav">
                        <dd class="cur">
                            <a href="#mrak01"  onclick="changeColor('不动产登记簿')">不动产登记簿</a>
                        </dd>
                        <dd>
                            <a href="#mrak02"  onclick="changeColor('宗地/宗海基本信息')">宗地/宗海基本信息</a>
                        </dd>
                        <#if havaBdcdy??>
                            <dd><a href="#mrak03"  onclick="changeColor('不动产权利登记目录')">不动产权利登记目录</a></dd>
                            <#if havaBdcdy=="true">
                                <#if list?size!=0 >
                                    <#list list as a>
                                        <#if a.LJZ??>
                                            <#list a.LJZ as b>
                                                <dd><a href="#ljz" class="fontClass" onclick="changeColor('${b.FWMC!}')">${b.FWMC!}</a></dd>
                                            </#list>
                                        </#if>
                                    </#list>
                                </#if>
                            </#if>

                            <#if havaBdcdy=="false">
                                <dd>
                                    <a href="#mrak04" title="${bdcdyhdy!}" class="fontClass"  onclick="changeColor('${bdcdyhdy!}')">${bdcdyhdy!}</a>
                                    <ul>
                                        <#if list?size!=0 >
                                            <#list list as a>
                                                    <li>
                                                        <a href="#${a.qllx!}" title="${a.mc!}" onclick="changeColor('${a.mc!}')">${a.mc!}</a>
                                                    </li>
                                            </#list>
                                        </#if>
                                    </ul>
                               </dd>
                            </#if>
                        </#if>

                        <#--<dd><a href="" class="img">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</a></dd>-->
                    </dl>
                    <i class="last"></i>
                    <div class="side-slider"></div>
                </div>

            </div>
            <!--end 目录树  -->
            <!-- 文章内容  -->
            <div class="Adaptive "  style="overflow: auto; ">
                <div id="mrak01" class="bookContent">
                    <#--<h2 class="bookContentLab">不动产登记簿</h2>-->
                    <section id="bdcdjb">
                        <div class="row-fluid" align="center">
                            <iframe id="bdcdjbModalFrame" name="fwModalFrame" src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}&__showtoolbar__=false" width = "700px" height = "1050px" class="iframeStyle" frameborder="0"  ></iframe>
                        </div>
                    </section>
                </div>
                <div id="mrak02" class="bookContent">
                    <#--<h2 class="bookContentLab">宗地/宗海基本信息</h2>-->
                    <section id="zdzhxx">
                        <div class="row-fluid" align="center">
                            <iframe id="fwqlrModalFrame" name="fwqlrModalFrame" src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdc_zdxx.cpt&op=write&djbid=${djbid!}&__showtoolbar__=false" width = "700px" height = "1200px" class="iframeStyle" frameborder="0"></iframe>
                        </div>
                    </section>
                </div>
                <#if havaBdcdy??>
                <div id="mrak03" class="bookContent">
                    <#--<h2 class="bookContentLab">不动产权利登记目录</h2>-->
                    <section id="qldjCatalog">
                        <div class="row-fluid" align="center">
                            <#--<iframe id="myModalFrame" name="myModalFrame"  src="${bdcdjUrl!}/bdcqldj?djbid=${djbid!}&showPrint=true" width = "800px" height ="700px" class="iframeStyle" frameborder="0"></iframe>-->
                                <iframe id="myModalFrame" name="myModalFrame" src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjml.cpt&op=write&djbid=${djbid!}&__showtoolbar__=false" width = "700px" height = "1200px" class="iframeStyle" frameborder="0"></iframe>
                        </div>
                    </section>


                    <#if havaBdcdy=="true">
                        <#if list?size!=0 >
                            <#list list as a>
                                <#if a.LJZ??>
                                    <#list a.LJZ as b>
                                        <li class="ui-menu-item">
                                            <section id="ljz">
                                                    <#--<h2 class="bookContentLab">${b.FWMC!}</h2>-->
                                                <div class="row-fluid" align="center">
                                                    <iframe id="${b.KEYCODE!}" name="myModalFrame"  src="${b.URL!}${"&djbid="}${djbid!}" width = "70%" height = "600px"class="iframeStyle" frameborder="0"></iframe>
                                                </div>
                                            </section>
                                        </li>
                                    </#list>
                                </#if>
                            </#list>
                        </#if>
                    </#if>
                    <#if havaBdcdy=="false">
                    <div id="mrak04" class="bookContent">
                        <li class="ui-menu-item">
                        <section id="bdcdy">
                            <div class="row-fluid" align="center">
                                <iframe id="${bdcdyhdy!}" name="myModalFrame"  src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdydjxx.cpt&op=form&bdcdyh=${bdcdyhdy!}&djbid=${djbid!}&qt=${qlPageMap.qt!}&dy=${qlPageMap.dy!}&dya=${qlPageMap.dya!}&yg=${qlPageMap.yg!}&yy=${qlPageMap.yy!}&cf=${qlPageMap.cf!}&__showtoolbar__=false" width = "900px" height = "1200px" class="iframeStyle" frameborder="0"></iframe>
                            </div>
                        </section>
                        </li>
                    </div>
                        <#if list?size!=0 >
                            <#list list as a>
                                <li class="ui-menu-item">
                                    <section id="">
                                        <#--<h2 class="bookContentLab">${a.mc!}</h2>-->
                                        <div class="row-fluid" align="center">
                                            <iframe id="${a.qllx!}" name="myModalFrame"  src="${a.tableName!}&djbid=${djbid!}" width = "800px" height = "1300px" class="iframeStyle" frameborder="0"></iframe>
                                        </div>
                                    </section>
                                </li>
                            </#list>
                        </#if>
                    </#if>
                </div>
                </#if>

                <#--<div id="mrak03" class="bookContent">
                    <h2 class="bookContentLab">调查信息</h2>
                    <img src=" ../images/text06.png" alt=""/> </div>
                <div id="mrak04" class="bookContent">
                    <h2 class="bookContentLab">分层分户图</h2>
                    <img src=" ../images/text07.png" alt=""/> </div>-->
            </div>
            <!--end 文章内容  -->
        </div>
        <!--end  滚动浏览书签 -->
    </div>
</div>
</body>
</html>
