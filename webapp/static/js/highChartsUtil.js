/**
 * 创建chart
 * @param cat
 * @param title
 * @param xAxis
 * @param yAxis
 * @param plotOptions
 * @param tooltip
 * @param series
 * @return {Highcharts.Chart}
 */
function buildChart(cat,title,xAxis,yAxis,plotOptions,tooltip,series){
    var chart = new Highcharts.Chart({
        chart:cat,
        title: title,
        xAxis: xAxis,
        yAxis:yAxis,
        plotOptions:plotOptions,
        tooltip: tooltip,
        series:series,
        exporting: {
            enabled: false
        }
    });
    return chart;
}
/**
 * 创建柱状图
 * @param renderToId
 * @param data
 * @param y_title
 * @param title
 * @param x_axis
 * @param y_axis
 * @return {Highcharts.Chart}
 */
function buildColumnChart(renderToId,data,y_title,title,x_axis,y_axis){
    var colors = Highcharts.getOptions().colors,
        categories= x_axis,
        name=y_title;

    function setChart(name, categories, data, color) {

        chart.xAxis[0].setCategories(categories);
        if(categories!=null && categories.length>6){
            chart.xAxis[0].options.labels.rotation = -45;
            chart.xAxis[0].options.labels.color = "#666767";
            chart.xAxis[0].options.labels.fontWeight = "lighter";
            chart.redraw();
        }
        chart.series[0].remove();
        chart.addSeries({
            name:name,
            data:data,
            color:color || 'black'
        });
    }

    var cat = {
        renderTo:renderToId,
        type:'column'
    };
    var title = {
        text:title
    };
    var xAxis = {
        categories:x_axis
    };
    if(data.length>6)
        xAxis = {
            categories:x_axis,
            labels:{
                rotation:-45 ,
                style: {color:"#666767",fontWeight:"lighter"}
            }
        };
    var yAxis = {
        title: {
            text: '单位:'+y_axis
        }
    } ;
    var plotOptions = {
        column:{
            cursor:'pointer',
            point:{
                events:{
                    click:function () {
                        var drilldown = this.drilldown;

                        if (drilldown) { // drill down
                            setChart(drilldown.name, drilldown.categories, drilldown.data, drilldown.color);
                        } else { // restore
                            setChart(name, categories, data);
                        }
                    }
                }
            },

            pointWidth:100,
            dataLabels:{
                enabled:true,
                color:colors[0],

                style:{
                    fontWeight:'bold'
                },
                formatter:function () {
                    return this.y;
                }
            }
        }
    };
    var tooltip = {
        formatter:function () {
            var point = this.point,
                s = this.x + ':<b>' + this.y +y_axis+ '</b><br/>';
            if (point.drilldown) {
                s += '点击 ' + point.category + ' 查看详情';
            }
            return s;
        }
    };
    var series = [
            {
                name:name,
                data:data,
                color:'black'
            }
        ];
    chart = buildChart(cat, title, xAxis, yAxis, plotOptions, tooltip, series);
    return chart;
}
/**
 * 创建饼状图
 * @param renderToId
 * @param data
 * @param y_title
 * @param title
 * @param x_axis
 * @param y_axis
 * @return {*}
 */
function buildPieChart(renderToId,data,y_title,title,x_axis,y_axis){
    var colors = Highcharts.getOptions().colors,
        categories= x_axis,
        name=y_title;

    var browserData = [];
    var versionsData = [];
    for (var i = 0; i < data.length; i++) {

        // add browser data
        browserData.push({
            name: categories[i],
            y: data[i].y,
            color: data[i].color
        });

        // add version data
        for (var j = 0; j < data[i].drilldown.data.length; j++) {
            var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5 ;
            versionsData.push({
                name: data[i].drilldown.categories[j],
                y: data[i].drilldown.data[j],
                color: Highcharts.Color(data[i].color).brighten(brightness).get()
            });
        }
    }

    var cat = {
        renderTo:renderToId,
        type:'pie'
    };
    var title = {
        text:title
    };
    var xAxis = {
        categories:x_axis
    };
    var yAxis = {
        title: {
            text: '单位:'+y_axis
        }
    } ;
    var plotOptions = {
        pie: {
            shadow: false
        }
    };
    var tooltip = {
        formatter:function () {
            var point = this.point,
                s = this.point.name + ':<b>' + this.y +y_axis+ '</b><br/>';
            return s;
        }
    };
    var series = [{
        name: 'Browsers',
        data: browserData,
        size: '60%',
        dataLabels: {
            formatter: function() {
                return this.y > 0 ? '<b>'+ this.point.name +'</b> ': null;
            },
            color: 'white',
            distance: -30
        }
    }, {
        name: 'Versions',
        data: versionsData,
        innerSize: '60%',
        dataLabels: {
            formatter: function() {
                // display only if larger than 1
                return this.y > 0 ? '<b>'+ this.point.name +':</b> '+ this.y+y_axis : null;
            }
        }
    }];
    chart = buildChart(cat, title, xAxis, yAxis, plotOptions, tooltip, series);
    return chart;
}

function expExcel(hideDivID,expUrl){
    window.location.href=expUrl;
    showProgressBar();
}