plugins {
  // Apply the application plugin to add support for building a CLI application in Java.
  application

  // cf. https://checkstyle.sourceforge.io/
  checkstyle
  // cf. https://spotbugs.github.io/
  id("com.github.spotbugs") version "6.0.11"
  // cf. https://github.com/diffplug/spotless/
  id("com.diffplug.spotless") version "6.25.0"

  id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  // Use JUnit Jupiter for testing.
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

application {
  // Define the main class for the application.
  mainClass.set("view.Main")
}

tasks.named<JavaExec>("run") {
  standardInput = System.`in`
  enableAssertions = true
}

tasks.named<Test>("test") {
  // Use JUnit Platform for unit tests.
  useJUnitPlatform()
}


javafx {
//  version = "21.0.3"
  // modules(
  // "javafx.base",
  // "javafx.swing",
  // "javafx.graphics",
  // "javafx.controls",
  // "javafx.fxml",
  // "javafx.media",
  // "javafx.web")
}

checkstyle {
  toolVersion = "10.15.0"
  maxWarnings = 0
}




