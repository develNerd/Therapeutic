object Versions {
    const val koin = "3.2.0"
    const val kermit = "1.1.3"
}

object Deps {

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
    }

    object Kermit {
        const val kermitMain = "co.touchlab:kermit:${Versions.kermit}"
    }

    object Firebase {
        const val auth =     "dev.gitlive:firebase-auth:1.6.1"
        const val fireStore = "dev.gitlive:firebase-firestore:1.6.1"
    }

    object RealDatabase {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt"
        const val realBase = "io.realm.kotlin:library-base:1.0.1"
    }

}