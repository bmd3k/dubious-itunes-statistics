function drawGraph(canvas, values) {       
    // reset the canvas
    canvas.width = canvas.width;
    var ctx = null;
    if (canvas.getContext){  
        ctx = canvas.getContext('2d'); 
    }    
    
    // determine the area surrounding the graph
    var graphXMax = canvas.width*9/10;
    var graphYMax = canvas.height*9/10;
    var graphXMin = (canvas.width - graphXMax);
    var graphYMin = (canvas.height - graphYMax);
        
    var minValue = 0;
    // determine the maximum value.  it has to be at least 1.
    var maxValue = 1;
    for(i=0;i<values.length;i++)
    {
        if(values[i] > maxValue)
        {
            maxValue = values[i];
        }
    }
                
    // collect the location of elements in the graph
    var dataPoints = [];
    for(i=0;i<values.length;i++)
    {
        var x = graphXMin + (graphXMax/(values.length+1)*(i));
        var y = graphYMax - (graphYMax/(maxValue+2)*values[i]);
               
        dataPoints[i] = {x: x, y: y};
    }

    // specify the skew that gives the 3D effect
    var xSkew = 8;
    var ySkew = -6;
    
    drawGraphingArea(ctx, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew);
    drawData(ctx, dataPoints, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew);
}

function drawGraphingArea(ctx, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew)
{
    // colour between the the y=0 and x=0 axes and their skews
    // the points are out of order but it gives a cool effect
    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMin);
    ctx.lineTo(graphXMin + xSkew, graphYMin + ySkew);
    ctx.lineTo(graphXMin, graphYMax);
    ctx.lineTo(graphXMin + xSkew, graphYMax + ySkew);
    ctx.fillStyle = '#A0A0A0';
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMax);
    ctx.lineTo(graphXMin + xSkew, graphYMax + ySkew);
    ctx.lineTo(graphXMax, graphYMax);
    ctx.lineTo(graphXMax + xSkew, graphYMax + ySkew);
    ctx.fillStyle = '#A0A0A0';
    ctx.fill();
    
    // draw x=0 and y=0 grid lines
    ctx.fillStyle = '#000000';
    ctx.fillRect(graphXMin-2, graphYMin, 4, graphYMax-graphYMin+2);
    ctx.fillRect(graphXMin, graphYMax-2, graphXMax-graphXMin, 4);
    ctx.fillRect(graphXMin+xSkew-2, graphYMin+ySkew, 2, graphYMax-graphYMin+1);
    ctx.fillRect(graphXMin+xSkew, graphYMax+ySkew-1, graphXMax-graphXMin, 2);
    
    // draw the connectors between the x=0 and y=0 grid lines and their skews
    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMin);
    ctx.lineTo(graphXMin + xSkew, graphYMin + ySkew);
    ctx.stroke();
    
    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMax);
    ctx.lineTo(graphXMin + xSkew, graphYMax + ySkew);
    ctx.stroke();
    
    ctx.beginPath();
    ctx.lineTo(graphXMax, graphYMax);
    ctx.lineTo(graphXMax + xSkew, graphYMax + ySkew);
    ctx.stroke();
}

function drawData(ctx, dataPoints, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew)
{
    // iterate over the data points to draw the outline of the back of the graph
    ctx.beginPath();
    ctx.moveTo(graphXMin + xSkew, graphYMax + ySkew);
    for(i=0;i<dataPoints.length;i++)
    {
        ctx.lineTo(dataPoints[i].x + xSkew, dataPoints[i].y + ySkew);
    }
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, graphYMax + ySkew);
    ctx.fillStyle = '#000000';
    ctx.stroke();
    
    // iterate over the data points to draw the top of the graph area
    ctx.fillStyle = '#F6EE20';
    for(i=0;i<dataPoints.length-1;i++)
    {
        ctx.beginPath();
        ctx.moveTo(dataPoints[i].x, dataPoints[i].y);
        ctx.lineTo(dataPoints[i+1].x, dataPoints[i+1].y);
        ctx.lineTo(dataPoints[i+1].x + xSkew, dataPoints[i+1].y + ySkew);
        ctx.lineTo(dataPoints[i].x + xSkew, dataPoints[i].y + ySkew);
        ctx.fill();
    }
    
    // draw the side of the graph area
    ctx.beginPath();
    ctx.moveTo(dataPoints[dataPoints.length-1].x, graphYMax);
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, graphYMax + ySkew);
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, dataPoints[dataPoints.length-1].y + ySkew);
    ctx.lineTo(dataPoints[dataPoints.length-1].x, dataPoints[dataPoints.length-1].y);
    ctx.closePath();
    ctx.fill();
    
    // iterate over the data points to draw connecting lines between front and back graph
    ctx.fillStyle = '#000000';
    for(i=0;i<dataPoints.length;i++)
    {
        ctx.beginPath();
        ctx.moveTo(dataPoints[i].x, dataPoints[i].y);
        ctx.lineTo(dataPoints[i].x + xSkew, dataPoints[i].y + ySkew);
        ctx.stroke();
    }
    // there is a final connecting line at the end of the graph
    ctx.beginPath();
    ctx.moveTo(dataPoints[dataPoints.length-1].x, graphYMax);
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, graphYMax + ySkew);
    ctx.stroke();
    
    // iterate over the data points to draw the front of the graph area and its outline
    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMax);
    for(i=0;i<dataPoints.length;i++)
    {
        ctx.lineTo(dataPoints[i].x, dataPoints[i].y);
    }
    ctx.lineTo(dataPoints[dataPoints.length-1].x, graphYMax);
    ctx.closePath();
    ctx.fillStyle = '#5CB6DC';
    ctx.fill();
    ctx.fillStyle = '#000000';
    ctx.stroke();
}