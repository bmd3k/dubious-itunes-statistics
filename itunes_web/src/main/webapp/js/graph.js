function Graph(canvas)
{
    // methods
    this.draw = draw;
    this.redraw = redraw;
    this.drawGraphingArea = drawGraphingArea;
    this.drawData = drawData;
    this.animateDraw = animateDraw;
    this.drawContextFromEvent = drawContextFromEvent;
    this.drawContextBox = drawContextBox;
    
    // canvas elements
    this.canvas = canvas;
    this.ctx = this.canvas.getContext('2d');
    this.values = null;
    this.dataPoints = null;
    
    // colour constants
    this.COLOUR_AXIS_LINE = '#000000';
    this.COLOUR_AXIS_FILL = '#A0A0A0';
    this.COLOUR_GRAPH_LINE = '#000000';
    this.COLOUR_GRAPH_TOP_AREA = '#AAAACC';
    this.COLOUR_GRAPH_FRONT_AREA = '#6774B0'; 
    this.COLOUR_CONTEXT_DOT = '#000000';
    this.COLOUR_CONTEXT_AREA = '#AAAAAA';
    this.COLOUR_CONTEXT_LINE = '#111111';
    this.COLOUR_CONTEXT_TEXT = '#111111';
    
    // event handling
    var thisGraph = this;
    canvas.addEventListener(
            'mousemove', 
            function(event) 
            {
                thisGraph.drawContextFromEvent(event);
            }, 
            true);
}

window.requestAnimFrame = (function(callback){
    return window.requestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    window.mozRequestAnimationFrame ||
    window.oRequestAnimationFrame ||
    window.msRequestAnimationFrame ||
    function(callback){
        window.setTimeout(callback, 1000 / 60);
    };
})();

//
// graph-specific methods
//
function redraw() {
    this.draw(this.values);
}

function draw(values) {  
    var oldDataPoints = this.dataPoints;
    var oldValues = this.values;
    this.values = values;
    
    var graphDimensions = getGraphDimensions(this.canvas);
        
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
        var x = graphDimensions.graphXMin + (graphDimensions.graphXMax/(values.length+1)*(i));
        var y = graphDimensions.graphYMax - (graphDimensions.graphYMax/(maxValue+2)*values[i]);
               
        this.dataPoints[i] = {x: x, y: y};
    }
    
    if(oldValues == values || oldValues == null)
    {
        // reset the canvases
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        // same set of values as before, nothing to animate
        this.drawGraphingArea(this.ctx, graphDimensions);
        this.drawData(this.ctx, this.dataPoints, graphDimensions);
    } else
    {
        if(oldValues.length != values.length)
        {
            alert('handle this case');
        }
        this.animateDraw(1, this.canvas, this.ctx, values, oldDataPoints, this.dataPoints);
    }
}

function animateDraw(currentFrame, canvas, ctx, values, oldDataPoints, dataPoints)
{
    var frames = 30;
    var graphDimensions = getGraphDimensions(canvas);
    
    // determine the points to draw
    var scaledDataPoints = [];
    for(var i=0;i<dataPoints.length;i++)
    {
        scaledDataPoints[i] =
            {x: dataPoints[i].x,
             y: oldDataPoints[i].y - ((oldDataPoints[i].y - dataPoints[i].y) * currentFrame / frames)}
    }

    // reset the canvases
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    // draw values
    this.drawGraphingArea(ctx, graphDimensions);
    this.drawData(ctx, scaledDataPoints, graphDimensions);
    
    if(currentFrame < frames)
    {
        // continue animation
        var thisGraph = this;
        requestAnimFrame(
                function() 
                { 
                    thisGraph.animateDraw(currentFrame+1, canvas, ctx, values, oldDataPoints, dataPoints) 
                });
    }
}

function getGraphDimensions(canvas)
{
    var graphDimensions = new Object();
    
    // graph area
    graphDimensions.graphXMax = canvas.width * 9/10;
    graphDimensions.graphYMax = canvas.height * 9/10;
    graphDimensions.graphXMin = canvas.width - graphDimensions.graphXMax;
    graphDimensions.graphYMin = canvas.height - graphDimensions.graphYMax;
    
    // skew parameters give the 3D affect
    graphDimensions.xSkew = 8;
    graphDimensions.ySkew = -6;
    
    return graphDimensions;
}

function drawGraphingArea(ctx, graphDimensions)
{
    var graphXMin = graphDimensions.graphXMin;
    var graphXMax = graphDimensions.graphXMax;
    var graphYMin = graphDimensions.graphYMin;
    var graphYMax = graphDimensions.graphYMax;
    var xSkew = graphDimensions.xSkew;
    var ySkew = graphDimensions.ySkew;
    
    // colour between the the y=0 and x=0 axes and their skews
    // the points are out of order but it gives a cool effect
    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMin);
    ctx.lineTo(graphXMin + xSkew, graphYMin + ySkew);
    ctx.lineTo(graphXMin, graphYMax);
    ctx.lineTo(graphXMin + xSkew, graphYMax + ySkew);
    ctx.fillStyle = this.COLOUR_AXIS_FILL;
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(graphXMin, graphYMax);
    ctx.lineTo(graphXMin + xSkew, graphYMax + ySkew);
    ctx.lineTo(graphXMax, graphYMax);
    ctx.lineTo(graphXMax + xSkew, graphYMax + ySkew);
    ctx.fillStyle = this.COLOUR_AXIS_FILL;
    ctx.fill();
    
    // draw x=0 and y=0 grid lines
    ctx.fillStyle = this.COLOUR_AXIS_LINE;
    ctx.strokeStyle = this.COLOUR_AXIS_LINE;
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

function drawData(ctx, dataPoints, graphDimensions)
{
    var graphXMin = graphDimensions.graphXMin;
    var graphXMax = graphDimensions.graphXMax;
    var graphYMin = graphDimensions.graphYMin;
    var graphYMax = graphDimensions.graphYMax;
    var xSkew = graphDimensions.xSkew;
    var ySkew = graphDimensions.ySkew;
    
    // iterate over the data points to draw the outline of the back of the graph
    ctx.beginPath();
    ctx.moveTo(graphXMin + xSkew, graphYMax + ySkew);
    for(var i=0;i<dataPoints.length;i++)
    {
        ctx.lineTo(dataPoints[i].x + xSkew, dataPoints[i].y + ySkew);
    }
    ctx.lineTo(dataPoints[dataPoints.length-1].x + xSkew, graphYMax + ySkew);
    ctx.strokeStyle = this.COLOUR_GRAPH_LINE;
    ctx.stroke();
    
    // iterate over the data points to draw the top of the graph area
    ctx.fillStyle = this.COLOUR_GRAPH_TOP_AREA;
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
    ctx.strokeStyle = this.COLOUR_GRAPH_LINE;
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
    ctx.fillStyle = this.COLOUR_GRAPH_FRONT_AREA;
    ctx.fill();
    ctx.strokeStyle = this.COLOUR_GRAPH_LINE;
    ctx.stroke();
}

function drawContextFromEvent(event)
{   
    if(this.dataPoints == null)
    {
        // there is no data in the graph
        return;
    }
    
    var radius = 10;
    
    // determine if the mouse is near to any of the data points
    for(var i=0; i<this.dataPoints.length;i++)
    {
        if(event.offsetX - this.dataPoints[i].x < radius && this.dataPoints[i].x - event.offsetX < radius
           && event.offsetY - this.dataPoints[i].y < radius && this.dataPoints[i].y - event.offsetY < radius)
        {
            this.redraw();
            // draw a node around this data point
            this.ctx.beginPath();
            this.ctx.arc(this.dataPoints[i].x, this.dataPoints[i].y, 4, 0, Math.PI*2, false);
            this.ctx.fillStyle = this.COLOUR_CONTEXT_DOT;
            this.ctx.fill();
            // draw context box
            this.drawContextBox(this.ctx, this.dataPoints[i].x, this.dataPoints[i].y, this.values[i]);
        }
    }
}

function drawContextBox(ctx, x, y, value)
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
    ctx.fillStyle = graph.COLOUR_CONTEXT_AREA;
    ctx.strokeStyle = graph.COLOUR_CONTEXT_LINE;
    ctx.fill();
    ctx.stroke();
    
    // the size of the box, depends on the size of the text
    
    // output the value of this node, just above the node
    ctx.fillStyle = graph.COLOUR_CONTEXT_TEXT;
    ctx.fillText("Value: " + value, boxXMin + 5, boxYMax - 5);
}