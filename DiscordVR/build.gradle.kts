plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.aliucord.plugin)
}

aliucord {
    // Set this to true so GitHub Actions publishes the compiled build output
    deploy.set(true)
}
