
function sub() {
    $('#sub').highcharts({
        chart: {
            type: 'scatter',
            zoomType: 'xy'
        },
        title: {
            text: 'Height Versus Weight of 507 Individuals by Gender'
        },
        subtitle: {
            text: 'Source: Heinz  2003'
        },
        xAxis: {
            title: {
                enabled: true,
                text: 'Height (cm)'
            },
            startOnTick: true,
            endOnTick: true,
            showLastLabel: true
        },
        yAxis: {
            title: {
                text: 'Weight (kg)'
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
                            lineColor: 'rgb(100,100,100)'
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
                    pointFormat: '{point.x} cm, {point.y} kg'
                }
            }
        },
        series: [{
            name: 'Random Attack',
            color: 'rgba(223, 83, 83, .5)',
            data: [[0.00,1.00],[0.04,0.95],[0.08,0.90],[0.12,0.80],[0.16,0.70],[0.20,0.60],[0.24,0.55],[0.28,0.53],[0.32,0.50],[0.36,0.48],[0.40,0.46],[0.44,0.41],[0.48,0.37],[0.52,0.35],[0.56,0.30],[0.60,0.25],[0.64,0.20],[0.68,0.15],[0.72,0.10],[0.76,0.05],[0.8,0.01],[0.84,0.01],[0.87,0.01],[0.90,0.0],[0.95,0.0]]

        }, {
            name: 'Intended Attack',
            color: 'rgba(119, 152, 191, .5)',
            data: [[0.00,1.00],[0.04,0.95],[0.08,0.90],[0.12,0.70],[0.16,0.55],[0.20,0.30],[0.24,0.10],[0.28,0.01],[0.32,0.01],[0.36,0.0],[0.40,0.0],[0.44,0.0],[0.48,0.0],[0.52,0.0],[0.56,0.0]]
        }]
    });
}
