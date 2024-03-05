Write-Host "Building Omini project"

$Java17Path = "C:\Program Files\Java\jdk-17.0.4.1"

.\gradlew "-Dorg.gradle.java.home=$Java17Path" bootJar