echo " - Setting up members data"
sqlite3 effectiveness.db ".read sql/member.sql"
echo " - SELECT * FROM 'member':"
sqlite3 effectiveness.db "select * from 'member';"

echo " - Setting up channel data"
sqlite3 effectiveness.db ".read sql/channel.sql"
echo " - SELECT * FROM 'channel':"
sqlite3 effectiveness.db "select * from 'channel';"

echo " - Setting up message data"
sqlite3 effectiveness.db ".read sql/message.sql"
echo " - SELECT * FROM 'message':"
sqlite3 effectiveness.db "select * from 'message';"
