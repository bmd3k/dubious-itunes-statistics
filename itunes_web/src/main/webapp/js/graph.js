function Graph(canvas)
{
    // TODO: Consider using the KineticJs library to have multi-layered canvas
    // canvas elements
    this.canvas = canvas;
    // attach a reference to this graph object through the canvas
    canvas._dubious_graph = this;
    this.ctx = null;
    if (this.canvas.getContext){  
        this.ctx = this.canvas.getContext('2d'); 
    }

    // methods
    this.draw = draw;
    this.redraw = redraw;
    
    // initialize other data
    this.values = null;
    this.dataPoints = null;
    
    // event handling
    canvas.addEventListener('mousemove', eventMouseMove, true);
}

function redraw() {
    this.draw(this.values);
}

function draw(values) {       
    this.values = values;
    
    // reset the canvases
    this.canvas.width = this.canvas.width;
    
    // determine the graph area
    var graphXMax = this.canvas.width*9/10;
    var graphYMax = this.canvas.height*9/10;
    var graphXMin = (this.canvas.width - graphXMax);
    var graphYMin = (this.canvas.height - graphYMax);
        
    var minValue = 0;
    // determine the maximum value.  it has to be at least 1.
    var maxValue = 1;
    for(var i=0;i<values.length;i++)
    {
        if(values[i] > maxValue)
        {
            maxValue = values[i];
        }
    }
                
    // collect the location of elements in the graph
    this.dataPoints = [];
    for(var i=0;i<values.length;i++)
    {
        var x = graphXMin + (graphXMax/(values.length+1)*(i));
        var y = graphYMax - (graphYMax/(maxValue+2)*values[i]);
               
        this.dataPoints[i] = {x: x, y: y};
    }

    // specify the skew that gives the 3D effect
    var xSkew = 8;
    var ySkew = -6;
    
    drawGraphingArea(this.ctx, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew);
    drawData(this.ctx, this.dataPoints, graphXMin, graphYMin, graphXMax, graphYMax, xSkew, ySkew);
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
    for(var i=0;i<dataPoints.length;i++)
    {
        ctx.lineTo(dataPoints[i].x + xSkew, dataPoints[i].y + ySkew);
    }
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, graphYMax + ySkew);
    ctx.fillStyle = '#000000';
    ctx.stroke();
    
    // iterate over the data points to draw the top of the graph area
    ctx.fillStyle = '#F6EE20';
    for(var i=0;i<dataPoints.length-1;i++)
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
    for(var i=0;i<dataPoints.length;i++)
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
    for(var i=0;i<dataPoints.length;i++)
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

function eventMouseMove(event)
{
    var thisGraph = event.srcElement._dubious_graph
    var dataPoints = thisGraph.dataPoints;
    var values = thisGraph.values;
    var ctx = thisGraph.ctx;
    
    if(dataPoints == null)
    {
        // there is no data in the graph
        return;
    }
    
    var radius = 10;
    
    // determine if the mouse is near to any of the data points
    for(var i=0; i<dataPoints.length;i++)
    {
        if(event.offsetX - dataPoints[i].x < radius && dataPoints[i].x - event.offsetX < radius
           && event.offsetY - dataPoints[i].y < radius && dataPoints[i].y - event.offsetY < radius)
        {
            thisGraph.redraw();
            // draw a node around this data point
            ctx.beginPath();
            ctx.arc(dataPoints[i].x, dataPoints[i].y, 4, 0, Math.PI*2, false);
            ctx.fill();
            drawValueBox(ctx, dataPoints[i].x, dataPoints[i].y, values[i]);
        }
    }
}

function drawValueBox(ctx, x, y, value)
{
    var text = "Value: " + value;
    var textMetrics = ctx.measureText(text);
    
    // draw a box with a border and output a value into it
    var boxXMin = x - textMetrics.width/2 - 5;
    var boxXMax = x + textMetrics.width/2 + 5;
    // we have to make some assumptions about the height of the text
    var boxYMin = y - 32;
    var boxYMax = y - 15;
    ctx.beginPath();
    ctx.moveTo(boxXMin, boxYMin);
    ctx.lineTo(boxXMin, boxYMax);
    ctx.lineTo(boxXMax, boxYMax);
    ctx.lineTo(boxXMax, boxYMin);
    ctx.closePath();
    ctx.fillStyle = "#DDDDDD";
    ctx.fill();
    ctx.fillStyle = "#222222";
    ctx.stroke();
    
    // the size of the box, depends on the size of the text
    
    // output the value of this node, just above the node
    ctx.fillStyle = "#000000";
    ctx.fillText("Value: " + value, boxXMin + 5, boxYMax - 5);
}