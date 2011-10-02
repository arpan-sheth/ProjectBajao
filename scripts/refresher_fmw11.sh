#!/usr/bin
file="songs_album".`date +"%Y%M%d%H%m%S"`
cp songs_album $file 
cp new_albums "new_albums".`date +"%Y%M%d%H%m%S"`
mv songs_link "songs_link".`date +"%Y%M%d%H%m%S"`
mv mp3links "mp3links".`date +"%Y%M%d%H%m%S"`

curl "www.fmw11.net"|grep -A2 "Latest Added Movies" |sed 's/\<li\>\<a href=//g'|sed 's/\<\/li\>//g'|sed 's/\<small\>//g'|sed 's/<\/small\>/ /g'|sed 's/\<\/a\>//g'|sed 's/Added:/\
/g'|sed 's/\s+//g'|grep -v \<h2\>|cut -d ":" -f2|cut -d ">" -f1|grep www|awk '{print "http:" $0}'>songs_album

cat songs_album $file|sort|uniq -u>new_albums

cat new_albums|awk '{print system ("curl \"" $0 "\"|grep \"http://www.fmw11.net/browser/download\"|cut -d = -f 4|sed s/\">\"//g|sed /^$/d>>songs_link")}'

cat songs_link|sed s/\"//g|awk '{print system("curl \"" $0 "\"|grep \"mp3\"|cut -d \\\" -f2>>mp3links")}'

cp mp3links /mp3
#curl "http://projectbajao.homeunix.com/test/udb"
#add here: Add to the database and then to solr

