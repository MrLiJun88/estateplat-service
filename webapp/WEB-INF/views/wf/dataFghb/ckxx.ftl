<@com.html title="不动产登记业务管理系统" import="bootstrap,public">
    <script type="text/javascript">
        var bdcdjUrl = "${bdcdjUrl!}";
        var analysisUrl = "${analysisUrl!}";
    </script>
    <script type="text/javascript" src="${bdcdjUrl!}/static/js/bdcFghb/ckxx.js"></script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/bdcPic.css"/>
    <div class="container-fluid sjpp_content">
        <div class="row-fluid row1">
            <div class="span2 left">
                <h2>目录</h2>
                <ul>
                    <#list bdcPicQoList! as bdcPicQo>
                        <li onclick="changeCkxxMl('${bdcPicQo.proid!}','${bdcPicQo.bdcdyh!}')"
                            id="ml-${bdcPicQo_index}">
                            <img src="../static/img/wenjianjia.png"/>
                            <i class="icon-folder-open icon-white"></i>
                            <span>${bdcPicQo.bdcqzh!}</span>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="span10 right">
                <a href="#" class="showMore" id="showMore"><i class="icon-chevron-right"></i></a>
                <iframe src="" style="width: 100%;height: 99%;border: 0;" id="iframe" ></iframe>
            </div>
        </div>
    </div>
</@com.html>
