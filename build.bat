@echo off

rem JRE_HOME ��������� �� �����, � ������� ����������� JRE. ��������: C:\Program Files\Java\jre7
set JRE_HOME=

rem M2_HOME ��������� �� �����, � ������� ���������� apache-maven. ��������: c:\apache-maven-3.2.5
set M2_HOME=

rem M2 ��� ��������, ������� �� ����
set M2=%M2_HOME%\bin

rem � ���������� Path �������� %M2%, ������� �� ����
Path=%Path%;%M2%

rem ���� �� ���������� - �������� ���������� ����� ������� (��� ���������->�������������->���������� �����)

rem ������ War-����
c:\java\apache-maven-3.2.5\bin\mvn package 
pause Building successful!