# koin-embedded-publish

Pre-relocated [Koin](https://github.com/InsertKoinIO/koin) artifacts where 
the `org.koin.*` packages have been renamed to `embedded.koin.*` to avoid 
classpath conflicts when including Koin in published SDKs.

## Origin

This is a build of [Koin 4.1.1](https://github.com/InsertKoinIO/koin/releases/tag/4.1.1) 
with the relocation pattern from [koin-embedded](https://github.com/InsertKoinIO/koin-embedded).

## Modifications

- Renamed all `org.koin.*` packages to `embedded.koin.*`
- Restructured modules: `:core:koin-core` → `:embedded:embedded-koin-core` (and similar for other modules)
- Pinned `androidx.activity:1.9.3` (originally 1.10.1) and `androidx.fragment:1.8.8` (originally 1.8.9) for compatibility
- Pruned modules not needed for embedded use (compose, ktor, fu, etc.)

## Usage via JitPack

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.celikrecep.koin-embedded-publish:embedded-koin-core:4.1.1")
    implementation("com.github.celikrecep.koin-embedded-publish:embedded-koin-android:4.1.1")
}
```

## License

Apache License 2.0 — see [LICENSE](LICENSE).

This work is derived from [Koin](https://github.com/InsertKoinIO/koin) 
by Arnaud Giuliani and Koin contributors, also under Apache License 2.0.
