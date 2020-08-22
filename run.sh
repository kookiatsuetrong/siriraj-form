~/Downloads/apache-maven/bin/mvn package
# java -jar target/main-1.jar
rm -r release
mkdir release
cd release
jar -xf ../target/main-1.jar
cd ..
java -cp release org.springframework.boot.loader.JarLauncher
