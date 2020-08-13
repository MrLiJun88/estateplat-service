<script type="text/javascript">
    //选择不动产单元号的赋值操作
    function dyhSel(dyh,id,bdclx,xmmc){
        var zdzhh;
        if(dyh && dyh!='undefined'){
            zdzhh=dyh.substr(0,19);
            $("#bdcdyh").val(dyh);
            $("#zdzhh").val(zdzhh);
        }else{
            $("#bdcdyh").val("");
            $("#zdzhh").val("");
        }
        if(id && id!='undefined'){
            if(id!=$("#djId").val()){
                clearCheckInfo();
            }
            $("#djId").val(id);
        }else{
            if($("#djId").val()!=""){
                clearCheckInfo();
            }
            $("#djId").val("");
        }
        if(xmmc && xmmc!='undefined'){
            $("#xmmc").val(xmmc);
        }else{
            $("#xmmc").val("");
        }
        if(bdclx && bdclx!='undefined'){
            $("#bdclx").val(bdclx);
            getQllx(bdclx,$("#djlx_text").val(),"csdjQllx");
        }else{
            $("#bdclx").val("");
        }
    }

    function dyhTableInit(){
        var grid_selector = "#dyh-grid-table";
        var pager_selector = "#dyh-grid-pager";
        //resize to fit page size
       /* $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
        });*/
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            //url:"${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson",
            datatype:"local",
            height:'275px',
            jsonReader:{id:'ID'},
            colNames:['序列',"不动产单元号",'权利人','地籍号', '坐落','不动产类型'],
            colModel:[
                {name:'XL',index:'', width:'8%',sortable:false,formatter:function(cellvalue, options, rowObject){
                    return '<span style="font-family: cursive;"> '+rowObject.ROWNUM_+'. </span><input type="radio" name="dyhXl" onclick="dyhSel(\''+rowObject.BDCDYH+'\',\''+rowObject.ID+'\',\''+rowObject.BDCLX+'\',\''+rowObject.QLR+'\')"/>'
                }
                },
                {name:'BDCDYH',index:'BDCDYH',width:'23%',sortable:false,formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                    return value;
                }},
                {name:'QLR',index:'QLR', width:'23%',sortable:false},
                {name:'DJH',index:'DJH', width:'23%',sortable:false},
                {name:'TDZL',index:'TDZL', width:'23%',sortable:false},
                {name:'BDCLX',index:'BDCLX', width:'18%',sortable:false,hidden:true}
            ],
            viewrecords:true,
            rowNum:7,/*
            rowList:[10, 20, 30],*/
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,/*
            multiboxonly:true,
            multiselect:true,*/
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if($(grid_selector).jqGrid("getRowData").length==7){
                    $(grid_selector).jqGrid("setGridHeight","auto");
                }else{
                    $(grid_selector).jqGrid("setGridHeight","275px");
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true

        });
     }
</script>
<table id="dyh-grid-table"></table>
<div id="dyh-grid-pager"></div>
