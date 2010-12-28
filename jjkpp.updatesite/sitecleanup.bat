cd \dev\jjkpp\jjkpp.updatesite
mkdir plugins2
copy plugins\*weaving* plugins2
copy plugins\*aspect* plugins2
copy plugins\pingpong.* plugins2
rmdir /Q /S plugins
ren plugins2 plugins
