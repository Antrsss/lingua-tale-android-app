# Fix Gradle warnings and errors in `app/build.gradle.kts`

The goal is to resolve all compilation errors and lint warnings in `app/build.gradle.kts`. This includes fixing unresolved references to version catalog plugins, updating dependencies to their latest stable versions, and migrating hardcoded versions to the version catalog.

## User Review Required

> [!IMPORTANT]
> - I will be updating `compileSdk` and `targetSdk` to 35 (Android 15), as 36 appears to be a preview version and the current `release(36)` block is syntactically incorrect for standard Android projects.
> - I will be updating Kotlin to 2.1.0 and Hilt to 2.52 (or latest stable) to ensure compatibility.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///home/zgdasha/AndroidStudioProjects/LinguaTale/gradle/libs.versions.toml)
- Add missing versions: `navigation`, `lifecycle`, `hilt`, `ksp`, `aws`.
- Add missing libraries: `androidx-navigation-compose`, `androidx-lifecycle-viewmodel-compose`, `androidx-lifecycle-runtime-compose`, `hilt-android`, `hilt-compiler`, `androidx-hilt-navigation-compose`, `aws-cognito-identity-provider`.
- Add missing plugins: `kotlin-android`, `hilt`, `ksp`.
- Update existing versions to latest stable.

#### [MODIFY] [build.gradle.kts](file:///home/zgdasha/AndroidStudioProjects/LinguaTale/build.gradle.kts)
- Add missing plugin aliases in the root `plugins` block.

#### [MODIFY] [app/build.gradle.kts](file:///home/zgdasha/AndroidStudioProjects/LinguaTale/app/build.gradle.kts)
- Fix `compileSdk` and `targetSdk` configuration.
- Migrate all hardcoded dependencies to use `libs` (version catalog).
- Remove duplicate Compose BOM declaration.
- Ensure all plugins are correctly aliased from `libs`.

## Verification Plan

### Automated Tests
- Run `./gradlew help` to verify that the build scripts compile correctly.
- Run `gradle_sync` to ensure the IDE recognizes the changes.

### Manual Verification
- Verify that `analyze_file` on `app/build.gradle.kts` no longer reports errors.
