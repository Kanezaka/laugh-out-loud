plugins {
	java
	kotlin("jvm") version "1.9.0"
}

repositories {
	mavenLocal()
	maven {
		url = uri("https://repo.runelite.net")
		content {
			includeGroupByRegex("net\\.runelite.*")
		}
	}
	mavenCentral()
}

val runeLiteVersion = "latest.release"

dependencies {
	compileOnly("net.runelite:client:$runeLiteVersion")

	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	testImplementation("junit:junit:4.13.1")
	testImplementation("net.runelite:client:$runeLiteVersion")
	testImplementation("net.runelite:jshell:$runeLiteVersion")

	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.apache.logging.log4j:log4j-api-kotlin:1.5.0")
	implementation("org.apache.logging.log4j:log4j-core:2.20.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

group = "com.kanezaka"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.release.set(11)
}

tasks.register<Jar>("shadowJar") {
	dependsOn(configurations.testRuntimeClasspath)

	manifest {
		attributes(
			"Main-Class" to "com.example.ExamplePluginTest",
			"Multi-Release" to true
		)
	}

	duplicatesStrategy = DuplicatesStrategy.EXCLUDE

	from(sourceSets.main.get().output)
	from(sourceSets.test.get().output)
	from({
		configurations.testRuntimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
	})

	exclude("META-INF/INDEX.LIST")
	exclude("META-INF/*.SF")
	exclude("META-INF/*.DSA")
	exclude("META-INF/*.RSA")
	exclude("**/module-info.class")

	group = BasePlugin.BUILD_GROUP
	archiveClassifier.set("shadow")
	archiveFileName.set("${rootProject.name}-${project.version}-all.jar")
}

kotlin {
	jvmToolchain(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions {
		freeCompilerArgs.add("-Xjvm-default=all")
	}
}
