call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Auxiliary\Build\vcvars64.bat"
%JAVA_HOME%\bin\native-image ^
  -jar target\fix-default-branch.jar ^
  --verbose ^
  --no-fallback ^
  --no-server ^
  -H:Name=fix-default-branch ^
  -H:+ReportExceptionStackTraces ^
  --initialize-at-build-time ^
  --report-unsupported-elements-at-runtime ^
  --allow-incomplete-classpath ^
  --enable-https ^
  -J-Dclojure.spec.skip-macros=true ^
  -J-Dclojure.compiler.direct-linking=true
