<!DOCTYPE html>
<html>
  <head id="head">
    <!--  https://developer.mozilla.org/en/Canvas_tutorial -->
    <title>Brian's iTunes Statistics</title>
    <script type="text/javascript">  
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
        var values = [4,5,1,0,0,8,10,4,4,3];
        var valuesSize = 10;
        
        // build x=0 and y=0 grid lines
        ctx.fillRect(graphXMin-2, graphYMin, 4, graphYMax-graphYMin+2);
        ctx.fillRect(graphXMin, graphYMax-2, graphXMax-graphXMin, 4);
        
        // build the graph
        var lastX = null;
        var lastY = null;
        for(i=0;i<valuesSize;i++)
       	{
       		var x = graphXMin + (graphXMax/valuesSize*(i));
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
  <body onload="draw();">
    <canvas id="graph" width="600" height="400">
    Your browser does not support this.
    </canvas>
  </body>
</html>