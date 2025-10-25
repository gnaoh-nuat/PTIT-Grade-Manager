
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id("com.google.gms.google-services")

    // THÊM MỚI: Plugin cho Navigation Safe Args (truyền dữ liệu an toàn)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.ptit_grade_manager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ptit_grade_manager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }



    dependencies {

        // Core & UI
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        // Testing
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // Firebase (BOM - Bill of Materials)
        implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
        implementation("com.google.firebase:firebase-firestore")
        implementation("com.google.firebase:firebase-auth")

        // --- THƯ VIỆN THÊM MỚI ---

        // Firebase Storage (Cho upload ảnh)
        implementation("com.google.firebase:firebase-storage")

        // Navigation Component (Quản lý Fragment và Bottom Nav)
        val nav_version = "2.7.7"
        implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
        implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

        // ViewModel & LiveData (Cho MVVM)
        val lifecycle_version = "2.8.3"
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

        // Glide (Tải và hiển thị ảnh)
        implementation("com.github.bumptech.glide:glide:4.16.0")

        // Circle ImageView (Hiển thị avatar tròn)
        implementation("de.hdodenhof:circleimageview:3.1.0")

        // -------------------------
    }
}