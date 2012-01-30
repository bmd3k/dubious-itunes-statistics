<!DOCTYPE html>
<html>
  <head>
    <!--  https://developer.mozilla.org/en/Canvas_tutorial -->
    <title>Brian's iTunes Statistics</title>
    
    <link type="text/css" href="../css/jquery.ui/smoothness/jquery-ui-1.8.17.custom.css" rel="Stylesheet" />
    <link type="text/css" href="../css/jquery.dynatree/skin-custom/ui.dynatree.css" rel="Stylesheet" />
      
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
                if(node.data.isFolder == false && node.data.artistName && node.data.albumName && node.data.songName)
                {
                    drawGraph(node.data.artistName, node.data.albumName, node.data.songName);
                } else if (node.isFolder = false)
                {
                    alert('Error retrieving artist information');
                }
            },
            children: loadSongTreeAlbums(),
            onLazyRead: function(node) {
                loadSongTreeSongs(node);
            }
        });
    });
    
    function loadSongTreeAlbums()
    {
        var returnData;
        
        $.ajax({
            url: 'getSongTreeAlbums.do',
            dataType: 'json',
            async: false})
            .fail(function(jqXHR, textStatus) {
                alert('Request Failed: ' + textStatus);
            }).done(function(albums) {
                for(var i=0; i<albums.length; i++)
                {
                    var album = albums[i];
                    album.title = album.artistName + ' - ' + album.albumName;
                    album.isFolder = true;
                    album.isLazy = true;
                }
                
                returnData = albums;
            }).always(function() {
            });
        
        return returnData;
    }
    
    function loadSongTreeSongs(node)
    {
        $.ajax({
            url: 'getSongTreeSongs.do',
            data: {'artistName': node.data.artistName, 'albumName': node.data.albumName},
            dataType: 'json'})
            .done(function(songs) {
                for(var i=0; i<songs.length; i++)
                {
                    var song = songs[i];
                    song.title = song.artistName + ' - ' + song.songName;
                }
                node.addChild(songs);
            })
            .always(function() {
                node.setLazyNodeStatus(DTNodeStatus_Ok);
            });
    }
    
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
      body {
          background-color: #FCFCFD;
      }
    
      #songTree {
        position: absolute;
        left: 5px;
        top: 5px;
        width: 350px;
        height: 460px;
        border: 2px solid black;
        background-color: #FAFAFD;
      }
      
      #graphAndSongForm {
        position: absolute;
        left: 355px;
        top: 5px;
        width: 650px;
        height: 460px;
        border: 2px solid black;
        background-color: #FAFAFD;
      }
      
      #graph { 
        position: absolute;
        left: 20px;
        top: 20px;
        width: 610px;
        height: 300px;
        border: 2px solid black; 
      }
      
      #songForm {
        position: absolute;
        left: 20px;
        top: 325px;
        width: 600px;
        height: 105px;
        padding: 5px;
        border: 2px solid black;
      }
    </style>  
  </head>
  <body>
    
    <nav id="songTree">
    </nav>
    
    <section id="graphAndSongForm">
      <canvas id="graph" width="610" height="300">
      This has only ever been tested on Google Chrome 16.0.912.75.
      </canvas>
    
      <div id="songForm">
        <div class="songFormField">Artist Name: <input id="artistName" type="text" size="30"/></div>
        <div class="songFormField">Album Name: <input id="albumName" type="text" size="30"/></div>
        <div class="songFormField">Song Name: <input id="songName" type="text" size="30"/></div>
        <div class="songFormField"><input id="songSubmit" type="submit" value="Get Statistics"/></div>
      </div>
    </section>
  </body>
</html>