# typedconfig
typedconfig


./gradlew :typedconfig-sample-2level-testapp:build

debug:
./gradlew -Dorg.gradle.debug=true :typedconfig-sample-2level-testapp:build


if field doesn't have rule:
1. config file has setup. use it
2. config file doesn't have setup, don't set value
