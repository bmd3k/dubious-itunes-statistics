<!DOCTYPE html>
<html>
  <head id="head">
    <!--  https://developer.mozilla.org/en/Canvas_tutorial -->
    <title>Brian's iTunes Statistics</title>
    
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
    <script src="../js/graph.js"></script>
    
    <script type="text/javascript">  
    $(document).ready(function(){
        drawSongGraph([0,0,0,0,0,0,0,0,0,0]);
    });
    
    function drawGraphFromForm() {        
        var artistName = $('#artistName').val();
        var albumName = $('#albumName').val();
        var songName = $('#songName').val();
        
        $.ajax({
            url: 'getSongHistory.do',
            data: {'artistName': artistName, 'albumName': albumName, 'songName': songName},
            dataType: 'json',
            async: false})
            .fail(function(jqXHR, textStatus) {
                alert('Request Failed: ' + textStatus);
            }).done(function(data) {
                // alert('done!');
                drawSongGraph(data);
            }).always(function() {
                // alert('always!');  
            });
    }
    
    function drawSongGraph(values) {  
        drawGraph(document.getElementById('graph'), values);
    }       
    </script>  

    <style type="text/css">  
      canvas { border: 1px solid black; }  
    </style>  
  </head>
  <body>
    <canvas id="graph" width="600" height="400">
    This has only ever been tested on Google Chrome 16.0.912.75.
    </canvas>
    
    <form id="songForm" action="javascript:drawGraphFromForm();">
      Artist Name: <input id="artistName" type="text" size="30"/><br>
      Album Name: <input id="albumName" type="text" size="30"/><br>
      Song Name: <input id="songName" type="text" size="30"/><br>
      <input id="songSubmit" type="submit" value="Click Me"/>
    </form>
  </body>
</html>