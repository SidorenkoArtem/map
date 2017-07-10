@echo off

rem JRE_HOME ссылается на папку, в которую установлена JRE. Например: C:\Program Files\Java\jre7
set JRE_HOME=

rem M2_HOME ссылается на папку, в которую установлен apache-maven. Например: c:\apache-maven-3.2.5
set M2_HOME=

rem M2 для удобства, править не надо
set M2=%M2_HOME%\bin

rem В переменную Path добавить %M2%, править не надо
Path=%Path%;%M2%

rem Если не получается - изменить переменные среды вручную (Мой компьютер->Дополнительно->Переменные среды)

rem Создаём War-файл
c:\java\apache-maven-3.2.5\bin\mvn package 
pause Building successful!