<!DOCTYPE html>
<html>
  <head id="head">
    <!--  https://developer.mozilla.org/en/Canvas_tutorial -->
    <title>Brian's iTunes Statistics</title>
    
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js">
    </script>
    
    <script type="text/javascript">  
    $(document).ready(function(){
        draw();
    });
    
    function getGraphValues() {
        var values = [0,0,0,0,0,0,0,0,0,0];
        
        $.ajax({
            url: 'getSongStatistics.do',
            dataType: 'json',
            async: false})
            .fail(function() {
                // alert('fail!');
            }).done(function(data) {
                // alert('done!');
                values = data;
            }).always(function() {
                // alert('always!');  
            });
        
        return values;
    }
    
    function draw() {  
        var canvas = document.getElementById('graph');  
        if (canvas.getContext){  
            var ctx = canvas.getContext('2d');  
        }   
            
        var graphXMax = canvas.width*9/10;
        var graphYMax = canvas.height*9/10;
        var graphXMin = (canvas.width - graphXMax);
        var graphYMin = (canvas.height - graphYMax);
            
        var minValue = 0;
        var maxValue = 15;
        var values = getGraphValues();
            
        // build x=0 and y=0 grid lines
        ctx.fillRect(graphXMin-2, graphYMin, 4, graphYMax-graphYMin+2);
        ctx.fillRect(graphXMin, graphYMax-2, graphXMax-graphXMin, 4);
            
        // build the graph
        var lastX = null;
        var lastY = null;
        for(i=0;i<values.length;i++)
        {
            var x = graphXMin + (graphXMax/values.length*(i));
            var y = graphYMax - (graphYMax/maxValue*values[i]);
                   
            drawPoint(ctx,x,y);
            if(lastX != null && lastY != null)
            {
                drawLine(ctx,x,y,lastX,lastY);
            }
                   
            var lastX = x;
            var lastY = y;
        }
    }  
      
    function drawPoint(ctx, x, y)
    {
        ctx.beginPath();  
        ctx.arc(x,y,6,0,Math.PI*2,true);
        ctx.closePath();
        ctx.fill();  
    }
        
    function drawLine(ctx, firstX, firstY, lastX, lastY)
    {
        ctx.beginPath();
        ctx.moveTo(firstX, firstY);
        ctx.lineTo(lastX, lastY);
        ctx.closePath();
        ctx.stroke();
    }
    </script>  
    <style type="text/css">  
      canvas { border: 1px solid black; }  
    </style>  
  </head>
  <body>
    <canvas id="graph" width="600" height="400">
    Your browser does not support this.
    </canvas>
  </body>
</html>