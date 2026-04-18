import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  application
  alias(libs.plugins.lombok)
  alias(libs.plugins.shadow)
}

repositories { mavenCentral() }

dependencies {
  implementation(libs.slf4j.api)
  implementation(libs.slf4j.simple)
  implementation(libs.jackson.databind)
  implementation(libs.fabric.chaincode.shim)

  testImplementation(libs.assertj.core)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.mockito.core)

  testRuntimeOnly(libs.junit.platform.launcher)
}

application { mainClass.set("org.hyperledger.fabric.contract.ContractRouter") }

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
  }
}

tasks.named<ShadowJar>("shadowJar") {
  archiveBaseName = "chaincode"
  archiveClassifier = ""
  archiveVersion = ""

  mergeServiceFiles()
  duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
