plugins {
    alias(libs.plugins.cashsense.android.library)
    alias(libs.plugins.cashsense.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.resodostudios.cashsense.core.network"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.kotlinx.serialization.json)
}
