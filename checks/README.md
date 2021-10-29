## checks ##

Chequeos de checkstyle personalizados para el curso IC-6821

#### Compilar el submódulo independientemente ####

```bash
./gradlew :checks:build 
```

#### Correr chequeos manualmente (sin gradle) ####

```bash
java -cp ~/dev/checkstyle-8.44-all.jar:checks/build/libs/checks.jar com.puppycrawl.tools.checkstyle.Main -c config/checkstyle/checkstyle.xml src/main/java/edu/tec/ic6821/blog/sync/DefaultSyncDataService.java   
```