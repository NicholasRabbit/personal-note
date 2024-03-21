@ECHO OFF

git status

git add --all

set /p message=Enter message for Command 1: 

git commit -m message

git push origin-github master

git push origin  master


PAUSE
