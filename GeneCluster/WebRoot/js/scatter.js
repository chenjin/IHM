
function scatter(datajson) {
    $('#scattercontainer').highcharts({
        chart: {
            type: 'scatter',
            zoomType: 'xy'
        },
        title: {
            text: 'Coreness of Graph'
        },
        xAxis: {
            title: {
                enabled: true,
                text: 'Coreness'
            },
            startOnTick: true,
            endOnTick: true,
            showLastLabel: true
        },
        yAxis: {
            title: {
                text: 'Node'
            }
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            x: 100,
            y: 70,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',
            borderWidth: 1
        },
        plotOptions: {
            scatter: {
                marker: {
                    radius: 5,
                    states: {
                        hover: {
                            enabled: true,
                            //lineColor: 'rgb(100,100,100)'
                            lineColor:'rgb(22,0,255)'
                            	
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: 'Node:{point.y} , Coreness:{point.x} , '
                }
            }
        },
        series: [{
            name: 'Ccoreness of node',
            //color: 'rgba(223, 83, 83, .5)',
            color:'rgba(79,255,0,1)',
            data: datajson

        }]
    });
}
    