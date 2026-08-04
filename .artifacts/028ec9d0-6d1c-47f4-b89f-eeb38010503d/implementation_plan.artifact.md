# Fix Gradle Wrapper Checksum and Plugin Configuration

The goal is to resolve the Gradle distribution checksum mismatch and ensure all plugins are correctly applied in the `app` module.

## Proposed Changes

### Gradle Wrapper

#### [MODIFY] [gradle-wrapper.properties](file:///home/zgdasha/AndroidStudioProjects/LinguaTale/gradle/wrapper/gradle-wrapper.properties)
- Update `distributionSha256Sum` to `9c0f7faeeb306cb14e4279a3e084ca6b596894089a0638e68a07c945a32c9e14` for Gradle 9.6.1.

### App Module Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///home/zgdasha/AndroidStudioProjects/LinguaTale/app/build.gradle.kts)
- Add `alias(libs.plugins.kotlin.app)` to the `plugins` block to ensure the Kotlin Android plugin is applied.

## Verification Plan

### Automated Tests
- Run `gradle_sync` to verify the new Gradle distribution is accepted and the project syncs successfully.
- Run `./gradlew help` to verify build script compilation.
