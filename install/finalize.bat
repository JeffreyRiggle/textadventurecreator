cd ..
set "mvndir=%cd%\Apache\Maven\bin"
set "ffmpegdir=%cd%\ffmpeg\bin"
setx path "%path%;%mvndir%;%ffmpegdir%"
setx /M path "%path%;%mvndir%;%ffmpegdir%"
if "%JAVA_HOME%"=="" (
	echo Java Home not set!
) else (
	echo Found Java Home.
)
cd %mvndir%
mklink "mvnlnk.lnk" "%mvndir%\mvn.cmd"
