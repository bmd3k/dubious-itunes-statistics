<!DOCTYPE html>
<html>
  <head>
    <!--  https://developer.mozilla.org/en/Canvas_tutorial -->
    <title>Brian's iTunes Statistics</title>
    
    <link type="text/css" href="../css/jquery.ui/smoothness/jquery-ui-1.8.17.custom.css" rel="Stylesheet" />
      
    <script src="../js/jquery/jquery-1.7.1.js"></script>
    <script src="../js/jquery.ui/jquery-ui-1.8.17.custom.min.js"></script>
    <script src="../js/jquery.cookie/jquery.cookie.js"></script>
    <script src="../js/jquery.dynatree/jquery.dynatree.js"></script>
    <script src="../js/graph.js"></script>
    <script src="../js/tree.js"></script>
    
    <script type="text/javascript">  
    var graph;
    
    $(document).ready(function(){
        graph = new Graph(document.getElementById('graph'));
        graph.draw([0,0,0,0,0,0,0,0,0,0,0,0,0,0]);
        
        $('#songSubmit').click(function(event) {
            drawGraphFromForm();
            event.preventDefault();
        });
        

        // Attach the dynatree widget to an existing <div id="tree"> element
        // and pass the tree options as an argument to the dynatree() function:
        $("#songTree").dynatree({
            onActivate: function(node) {
                // A DynaTreeNode object is passed to the activation handler
                // Note: we also get this event, if persistence is on, and the page is reloaded.
                if(node.data.isFolder == false && node.data.artist && node.data.album && node.data.title)
                {
                    drawGraph(node.data.artist, node.data.album, node.data.song);
                } else if (node.isFolder = false)
                {
                    alert('Error retrieving artist information');
                }
            },
            children: [ // Pass an array of nodes.
                {title: 'Radiohead - Kid A', isFolder: true,
                    children: [
                        {title: 'Morning Bell', artist:'Radiohead', album:'Kid A', song:'Morning Bell'},
                        {title: 'Optimistic', artist:'Radiohead', album:'Kid A', song:'Optimistic'},
                        {title: 'Idioteque', artist:'Radiohead', album:'Kid A', song:'Idioteque'}
                    ]
                },
                {title: 'Radiohead - OK Computer', isFolder: true,
                    children: [
                        {title: 'Karma Police', artist:'Radiohead', album:'OK Computer', song:'Karma Police'},
                        {title: 'Electioneering', artist:'Radiohead', album:'OK Computer', song:'Electioneering'}
                    ]
                },
                {title: 'Silverchair - Freak Show', isFolder: true,
                    children: [
                        {title: 'Cemetery', artist:'Silverchair', album:'Freak Show', song:'Cemetery'},
                        {title: 'The Door', artist:'Silverchair', album:'Freak Show', song:'The Door'}
                    ]
                }
            ]
        });
    });
    
    function drawGraph(artistName, albumName, songName)
    {        
        $('#artistName').val(artistName);
        $('#albumName').val(albumName);
        $('#songName').val(songName);
        
        $.ajax({
            url: 'getSongHistory.do',
            data: {'artistName': artistName, 'albumName': albumName, 'songName': songName},
            dataType: 'json',
            async: false})
            .fail(function(jqXHR, textStatus) {
                alert('Request Failed: ' + textStatus);
            }).done(function(data) {
                graph.draw(data);
            }).always(function() {
            });
    }
    
    function drawGraphFromForm() {        
        var artistName = $('#artistName').val();
        var albumName = $('#albumName').val();
        var songName = $('#songName').val();
        
        drawGraph(artistName, albumName, songName);
    }
    </script>  

    <style type="text/css">  
      #songTree {
        position: absolute;
        left: 0px;
        top: 0px;
        width: 225px;
        height: 600px;
        border: 1px solid red;
      }
      
      #graphAndSongForm {
        position: absolute;
        left: 230px;
        top: 0px;
        width: 650px;
        height: 600px;
        border: 1px solid blue;
      }
      
      #graph { 
        position: absolute;
        left: 25px;
        top: 25px;
        width: 600px;
        height: 400px;
        border: 1px solid black; 
      }
      
      #songForm {
        position: absolute;
        left: 25px;
        top: 430px;
        width: 590px;
        height: 135px;
        padding: 5px;
        border: 1px solid purple;
      }
    </style>  
  </head>
  <body>
    
    <nav id="songTree">
    </nav>
    
    <section id="graphAndSongForm">
      <canvas id="graph" width="600" height="400">
      This has only ever been tested on Google Chrome 16.0.912.75.
      </canvas>
    
      <div id="songForm">
        <div class="songFormField">Artist Name: <input id="artistName" type="text" size="30" value="Radiohead"/></div>
        <div class="songFormField">Album Name: <input id="albumName" type="text" size="30" value="Kid A"/></div>
        <div class="songFormField">Song Name: <input id="songName" type="text" size="30" value="Morning Bell"/></div>
        <div class="songFormField"><input id="songSubmit" type="submit" value="Click Me"/></div>
      </div>
    </section>
  </body>
</html>