var prev_play="";
var prev_query="";
var page_number=0;
var NUMBER_OF_RESULTS_ON_PAGE = 10;
var global_playlist=new Array();
var curr_playing_song = -1;
var curr_volume = 100;
var play_link;

$(document).ready(function() {
	$("#query").focus();
	// bind 'myForm' and provide a simple callback function 
	$('#myForm').ajaxForm({
		dataType: 'json', 
		success: processJson 
	});
	soundManager.url = "swf/"; // directory where SM2 .SWFs live
//	$('.pastSearches').prepend('Your Past Searches: ');

	initPlayerControls();	
	/*	
	$('.pastSearches').hide();
	 */
});

function initPlayerControls(){
	playPrevious();
	playNext();
}

function playPrevious(){
	$("#jplayer_previous").click( function() {
		var val = parseInt(curr_playing_song);
		if (val >0){
			val = val - 1;
			play_song(val);
		}
		$(this).blur();
		return false;
	});
}

function playNext() {
	$("#jplayer_next").click( function() {
		var val = parseInt(curr_playing_song);
		if (val < parseInt(global_playlist.length-1)){
			val = val +1;
			play_song(val);
		}
		$(this).blur();
		return false;
	});
}

function addVideo(data) {

	 try {
		 	$.each(data.feed.entry, function(i, item) {
			 var title = item['title']['$t'];
			 var video = item['id']['$t'];
			 alert(video+title);
	        });
		 }catch (err){
			 
		 }
}
//function to create the result div
function createResultDiv(i, doc){
	var id = doc.id;
	var song_id = doc.id;
	var image = doc.image;
	var title = doc.title1;
	var media_location = doc.location;
	var artist = doc.artist;
	var album = doc.album;
	var year = doc.year;
	var random = Math.floor(Math.random()*100);
	id = id+random;
	var playlist_id_title = ' id="'+id+'" title="location:'+song_id+', title:'+title+'" ';
	var play = '<img height="25px" width="25px" id="'+id+'_play" title="'+ title +'"  class="play_link" src="images/arrow.png"/>';
	//generating output now
	var output = '<div class="result">'; 
	output += '<img class="play_link" src="images/play_button.png" height = "25px" width = "25px" '+playlist_id_title+'/><b>'+ title  +'</b>';
	output += '<div class="social">Share: <a class="facebook" target="_blank" href="http://www.facebook.com/sharer.php?u=http://bajega.com/search.html?id='+song_id +'"><img border="0" width="15px" title= "Share on facebook" src="images/facebook.png" /></a>'; 
	output += '<a class="twitter" target="_blank" href="http://twitter.com/home?status=This+is+a+great+song%20http://bajega.com/search.html?id='+song_id+'"><img border="0" width="15px" title= "Post on twitter" src="images/twitter.png" /></a>';
	output += '<a class="hyperlink" target="_blank" href="http://bajega.com/search.html?id='+song_id+'"><img border="0" width="24px" height="16px" title= "Right click to copy link to song" src="images/hyperlink.png" /></a></div>';
	output += '<div class="album">Album: ' /*<a href="#" id="'+ replaceSpecial(album) +"_"+random+'">'*/+album/*'</a>'*/+'</div>';
	output += '<div class="artist">Artist(s): '/*<a href="#"  id="'+replaceSpecial(artist)+"_"+random+'">'*/+artist/*'</a>*/+'</div>';
	output += '<div class="year">Year: '/*<a href="#"  id="'+replaceSpecial(year) +"_"+random+'">'*/+year/*'</a>*/+'<div>';
//	output += '<div class="art"><img src="images/album.png" height = "25px" width = "25px" /></div>';
	output += '</div>';

//	eventAssigner(replaceSpecial(album)+"_"+random, random, "album");
//	eventAssigner(replaceSpecial(artist)+"_"+random, random, "artist");
//	eventAssigner(replaceSpecial(year)+"_"+random, random, "year");
//	eventAssigner(replaceSpecial(title)+"_"+random, random, "title");

	//playing call
	$("'#"+id+"'").live('click', function(first_event){
		var in_array = first_event.target.title;
		var index =  global_playlist.push(in_array) - 1;
		$('.playlist').show();
		$('.playlist').append('<li class="playlist_item"><a href="#" id="'+ index +'" title="'/*+ first_event.target.title*/  +'">'+title+'</a></li>').unbind
		('click').bind('click', function(second_event){
			play_song( second_event.target.id );
		});
		if (index==0){			
			play_song(index);
		}
	}).unbind('click');
//	$.getJSON('http://gdata.youtube.com/feeds/api/videos?q='+title+'+'+album+'&alt=json-in-script&callback=addVideo&max-results=3&v=2');
	return output;	
}

function play_song(index){	
	$('#'+curr_playing_song).css('color','#666');
	curr_playing_song = index;
	var value = global_playlist[index];
	var title_link = value.split(",")[1].split(":")[1];
	var play_id = value.split(",")[0].split(":")[1];
	$.getJSON("play?id="+play_id, function(newData){
		play_link=newData.location;
		mySoundObject = initSoundManager('../'+play_link, index+'_play');
		$('a.jp-pause').show();
		$('#'+index).css('color','#0d88c1');
		/*	$('.nowPlaying').empty().append('Now Playing : '+title_link);*/
		$('#jplayer_stop').unbind('click').bind('click', function(){
			mySoundObject.stop();
			$('a.jp-pause').hide();
			$('a.jp-play').show();

		});
		$('#jplayer_play').unbind('click').bind('click', function(){
			mySoundObject.play();
			$('a.jp-pause').show();
			$('a.jp-play').hide();
		});

		$('#jplayer_pause').unbind('click').bind('click', function(){
			mySoundObject.pause();
			$('a.jp-pause').hide();
			$('a.jp-play').show();
		});
	}); 

}

//soundmanager initialization
function initSoundManager(mp3Url, mp3Id){
	var now;
	var total;
	var progress_bar_width = $('.jp-progress').css('width').split('px')[0];
	var volume_bar_width = $('.jp-volume-bar').css('width').split('px')[0];
	$('.jp-play-bar').hide();
	$('.jp-volume-bar').show();
	$('.jp-total-time').text('');
	$('.jp-volume-bar-value').css('width', curr_volume*volume_bar_width/100).show();

	if (prev_play)
		soundManager.destroySound(prev_play);
	soundManager.useHTML5Audio = true;
	soundManager.useFlashBlock = false;
	soundManager.debugMode = false;
	var mySound = soundManager.createSound({
		id:  mp3Id,
		url: mp3Url,
		onfinish: function() {
		var index = parseInt(mp3Id.split("_")[0]);
		index =index+1;
		if (mp3Id.split("_")[1]=="play"){
			play_song(index);
		}
	},
	onload: function() {
		var value = Math.floor(this.duration/1000);
		var min =  Math.floor(value/60);
		var seconds = value%60;
		$('.jp-total-time').text(min+":"+(seconds<10?'0'+seconds:seconds));
		total = value;
	}
	// onload: [ event handler function object ],
	// other options here..
	});
	prev_play = mp3Id;
	mySound.options.whileplaying = function() {
		var value = Math.floor(this.position/1000);
		var min =  Math.floor(value/60);
		var seconds = value%60;
		$('#jplayer_play_time').text(min+":"+(seconds<10?'0'+seconds:seconds));
		now = value;
		if (!this.loaded){
			$('.jp-total-time').text('loading..');
		}
		if (total!=null){
			$('.jp-play-bar').css('width', progress_bar_width*now/total);
			$('.jp-play-bar').show();
		}
	}
	$('.jp-progress').show();
	$('.jp-progress').click(function(event){
		if (total!=null){
			var seek = event.pageX-$('.jp-progress').offset().left;
			mySound.setPosition(1000*seek*total/progress_bar_width);
		}
	});
	$('.jp-volume-max').click(function(event){
		curr_volume = curr_volume+10;
		mySound.setVolume(curr_volume);
		$('.jp-volume-bar-value').css('width', curr_volume*volume_bar_width/100).show();
	});
	$('.jp-volume-min').click(function(event){
		curr_volume = curr_volume - 10;
		mySound.setVolume(curr_volume);
		$('.jp-volume-bar-value').css('width', curr_volume*volume_bar_width/100).show();
	});
	$('.jp-volume-bar').click(function(event){
		var seek = event.pageX-$('.jp-volume-bar').offset().left;
		mySound.setVolume(seek*100/volume_bar_width);
		curr_volume = seek*100/volume_bar_width;
		$('.jp-volume-bar-value').css('width', seek).show();
	});
	mySound.play();
	mySound.setVolume(curr_volume);
	return mySound;
} 

//function to process the json received from backend
function processJson(data) {	
	$('.results').empty();
	curr_query = data.responseHeader.params.q;
	//Code for adding the data to "Your Past Searches" div
	curr_query = curr_query.replace(/ /g,"+").replace(/\++/g,"+");
/*	if (curr_query!=prev_query){
		$('.pastSearches').show();
		$('.pastSearches').append('<a class="pastSearch" id="'+ curr_query +'"href="#" >'+ curr_query +'</a>'+'  ' ).unbind('click').bind('click',function(event){
			$.getJSON('search?q='+event.target.id, function(newData){
				$('#query').val(event.target.id);
				processJson(newData);
			}); 
		});	
	}
*/	
	//looping over all the response data from json
	$.each(data.response.docs, function(i,doc){
		$('.results').append(createResultDiv(i, doc));//create the result divs now
	})
	prev_query = curr_query;
	paginate(data);
}

//function to paginate
function paginate(data){	
	var total_found = data.response.numFound;
	var start_row_this_click = data.response.start;
	var remaining = total_found - start_row_this_click- NUMBER_OF_RESULTS_ON_PAGE ;
	var my_query = data.responseHeader.params.q;
	$('.pagination').empty();
	if (total_found<=NUMBER_OF_RESULTS_ON_PAGE){
		return;
	}
	if (remaining >0) {
		$('.pagination').append('<div><a href="#" class="prevPage">Previous Page</a></div>');
		createPreviousResults(data.response.start, my_query);
		if (!start_row_this_click){
			$('.pagination .prevPage').hide();
		}
		$('.pagination').append('<div><a href="#" class="nextPage">Next Page</a></div>');
		createNextResults(data.response.start, my_query);
	} else {
		if (start_row_this_click<=total_found){
			$('.pagination').append('<div><a href="#" class="prevPage">Previous Page</a></div>');
			createPreviousResults(data.response.start);
			$('.pagination .prevPage').show();	
		}
		$('.pagination .nextPage').hide();
	}
}

//create more results link
function createPreviousResults(prev_start, query){
	$('.prevPage').unbind('click');	
	$('.prevPage').bind('click',function(event){
		$.getJSON('search?q='+query+'&start='+(prev_start-NUMBER_OF_RESULTS_ON_PAGE),null, processJson(newData));
	});	
}

//create more results link
function createNextResults(prev_start, query){
	$('.nextPage').unbind('click');	
	$('.nextPage').bind('click',function(event){
		$.getJSON('search?q='+query+'&start='+(prev_start+NUMBER_OF_RESULTS_ON_PAGE), function(newData){
			processJson(newData);
		}); 
	});	
}

function eventAssigner(id, random, type){
	if (id=="_"+random)
		return;
	$("'#"+id+"'").unbind('click').live('click',function(event){
		id = id.replace("_"+random,"").replace(/_/g,"+").replace(/(\++)/g,"+");
		$.getJSON('search?q='+type+':'+id, function(newData){
			$('#query').val(type+":"+id);
			processJson(newData);
		});
	});
}

function replaceSpecial(str){
	//str = str.trim();
	str = str.replace(/([\^\$\\\.\+\*\?\{\}\(\)\|\[\]])/g,"_").replace(/@/g,"_").replace(/ /g,"_").replace(/,/g,"_").replace(/&/g,"_").replace(/-/g,"_");
	return str;
}