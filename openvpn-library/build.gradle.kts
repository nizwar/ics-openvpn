import com.android.build.gradle.api.LibraryVariant

/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    buildFeatures {
        aidl = true
        buildConfig = true
    }
    namespace = "de.blinkt.openvpn"
    compileSdk = 35

    // Also update runcoverity.sh
    ndkVersion = "28.0.13004108"

    defaultConfig {
        minSdk = 21
    }

    // Conditionally enable external native build only if not in JitPack
    if (System.getenv("JITPACK") != "true") {
        externalNativeBuild {
            cmake {
                path = File("${projectDir}/../main/src/main/cpp/CMakeLists.txt")
            }
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("../main/src/main/java", "../main/src/skeleton/java")
            aidl.srcDirs("../main/src/main/aidl")
            assets.srcDirs("../main/src/main/assets", "../main/build/ovpnassets")
            res.srcDirs("../main/src/main/res", "../main/src/skeleton/res")
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }

    lint {
        enable += setOf("BackButton", "EasterEgg", "StopShip", "IconExpectedSize", "GradleDynamicVersion", "NewerVersionAvailable")
        checkOnly += setOf("ImpliedQuantity", "MissingQuantity")
        disable += setOf("MissingTranslation", "UnsafeNativeCodeLocation")
    }

    flavorDimensions += listOf("ovpnimpl")

    productFlavors {
        create("ovpn23")
        {
            dimension = "ovpnimpl"
            buildConfigField("boolean", "openvpn3", "true")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

// Skip SWIG generation in JitPack environment
if (System.getenv("JITPACK") != "true") {
    var swigcmd = "swig"
    // Workaround for macOS(arm64) and macOS(intel) since it otherwise does not find swig and
    // I cannot get the Exec task to respect the PATH environment :(
    if (file("/opt/homebrew/bin/swig").exists())
        swigcmd = "/opt/homebrew/bin/swig"
    else if (file("/usr/local/bin/swig").exists())
        swigcmd = "/usr/local/bin/swig"

    fun registerGenTask(variantName: String, variantDirName: String): File {
        val baseDir = File(buildDir, "generated/source/ovpn3swig/${variantDirName}")
        val genDir = File(baseDir, "net/openvpn/ovpn3")

        tasks.register<Exec>("generateOpenVPN3Swig${variantName}")
        {
            doFirst {
                mkdir(genDir)
            }
            commandLine(listOf(swigcmd, "-outdir", genDir, "-outcurrentdir", "-c++", "-java", "-package", "net.openvpn.ovpn3",
                    "-I../main/src/main/cpp/openvpn3/client", "-I../main/src/main/cpp/openvpn3/",
                    "-DOPENVPN_PLATFORM_ANDROID",
                    "-o", "${genDir}/ovpncli_wrap.cxx", "-oh", "${genDir}/ovpncli_wrap.h",
                    "../main/src/main/cpp/openvpn3/client/ovpncli.i"))
            inputs.files( "../main/src/main/cpp/openvpn3/client/ovpncli.i")
            outputs.dir( genDir)
        }
        return baseDir
    }

    android.libraryVariants.all(object : Action<LibraryVariant> {
        override fun execute(variant: LibraryVariant) {
            val sourceDir = registerGenTask(variant.name, variant.baseName.replace("-", "/"))
            val task = tasks.named("generateOpenVPN3Swig${variant.name}").get()

            variant.registerJavaGeneratingTask(task, sourceDir)
        }
    })
}

dependencies {
    // https://maven.google.com/web/index.html
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.core:core-ktx:1.15.0")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
    testImplementation("org.mockito:mockito-core:4.8.1")
    testImplementation("org.robolectric:robolectric:4.10.2")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("ovpn23") {
                groupId = "com.github.nizwar"
                artifactId = "ics-openvpn"
                version = findProperty("VERSION_NAME")?.toString() ?: "1.0.0"
                
                // Use the ovpn23 AAR artifact
                afterEvaluate {
                    from(components["ovpn23Release"])
                }
                
                pom {
                    name.set("ICS OpenVPN Library (OpenVPN 3)")
                    description.set("OpenVPN for Android as a library (AAR) - OpenVPN 3 variant")
                    url.set("https://github.com/nizwar/ics-openvpn")
                    
                    licenses {
                        license {
                            name.set("GNU General Public License v2.0")
                            url.set("https://www.gnu.org/licenses/gpl-2.0.html")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("nizwar")
                            name.set("Nizwar")
                            email.set("nizwar@example.com")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/nizwar/ics-openvpn.git")
                        developerConnection.set("scm:git:ssh://github.com:nizwar/ics-openvpn.git")
                        url.set("https://github.com/nizwar/ics-openvpn/tree/main")
                    }
                }
            }
        }
    }
}

// Fix task dependency issue for JitPack
afterEvaluate {
    tasks.named("publishOvpn23PublicationToMavenLocal") {
        dependsOn("bundleOvpn23ReleaseAar")
    }
}
