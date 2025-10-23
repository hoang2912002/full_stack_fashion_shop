plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("io.freefair.lombok") version "9.0.0"
}

group = "vn.clothing"
version = "0.0.1-SNAPSHOT"
description = "Fashion shop"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

// configurations {
// 	compileOnly {
// 		extendsFrom(configurations.annotationProcessor.get())
// 	}
// }

repositories {
	mavenCentral()
}
extra["snippetsDir"] = file("build/generated-snippets")
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// implementation("org.springframework.session:spring-session-jdbc")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	// compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	// annotationProcessor("org.projectlombok:lombok")
	implementation("com.turkraft.springfilter:jpa:3.1.7")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.withType<Test> {
    jvmArgs("--add-opens=java.base/java.time=ALL-UNNAMED")
}
tasks.withType<JavaExec> {
    jvmArgs("--add-opens=java.base/java.time=ALL-UNNAMED")
}