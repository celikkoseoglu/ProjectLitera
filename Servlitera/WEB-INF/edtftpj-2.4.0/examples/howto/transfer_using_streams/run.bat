@call ..\env.bat
IF NOT ERRORLEVEL 1 (
  "%JAVA_HOME%\bin\javac" -classpath %CP% TransferUsingStreams.java
  "%JAVA_HOME%\bin\java" -cp %CP% TransferUsingStreams %1 %2 %3
)