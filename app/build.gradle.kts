import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

// Load local.properties (for local dev) and gradle.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val gradleProperties = Properties()
val gradlePropertiesFile = rootProject.file("gradle.properties")
if (gradlePropertiesFile.exists()) {
    gradleProperties.load(gradlePropertiesFile.inputStream())
}

// Helper function to get property from either file
fun getProperty(key: String, default: String = ""): String {
    return localProperties.getProperty(key) ?: gradleProperties.getProperty(key) ?: default
}

android {
    namespace = "com.indiancalorietracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.indiancalorietracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        
        // Build config values from gradle.properties or local.properties
        buildConfigField("String", "AUTH0_CLIENT_ID", "\"${getProperty("AUTH0_CLIENT_ID", "")}\"")
        buildConfigField("String", "AUTH0_DOMAIN", "\"${getProperty("AUTH0_DOMAIN", "")}\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"${getProperty("GEMINI_API_KEY", "")}\"")
        
        // Auth0 manifest placeholders
        manifestPlaceholders["auth0Domain"] = getProperty("AUTH0_DOMAIN", "your-tenant.auth0.com")
        manifestPlaceholders["auth0Scheme"] = "com.indiancalorietracker"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "AUTH0_CLIENT_ID", "\"${getProperty("AUTH0_CLIENT_ID", "")}\"")
            buildConfigField("String", "AUTH0_DOMAIN", "\"${getProperty("AUTH0_DOMAIN", "")}\"")
            buildConfigField("String", "GEMINI_API_KEY", "\"${getProperty("GEMINI_API_KEY", "")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-android-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Auth0 Authentication (using 2.11.0 for public constructor)
    implementation("com.auth0.android:auth0:2.11.0")
    
    // Google Generative AI for AI Assistant
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    
    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
