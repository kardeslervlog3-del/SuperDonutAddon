plugins {
    alias(libs.plugins.fabric.loom)
}

base {
    archivesName.set(properties["archives_base_name"] as String)
    version = libs.versions.mod.get()
    group = properties["maven_group"] as String
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.meteordev.org/releases") }
    maven { url = uri("https://maven.meteordev.org/snapshots") }
}

dependencies {
    // Minecraft & Fabric
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)

    // Meteor Client (snapshot)
    modImplementation(libs.meteor.client)
}

tasks {
    processResources {
        val versionMap = mapOf(
            "version" to project.version,
            "mc_version" to libs.versions.minecraft.get()
        )

        inputs.properties(versionMap)

        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(versionMap)
        }
    }
}
