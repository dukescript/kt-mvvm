### Android

The [sample project](https://github.com/dukescript/kt-mvvm-demo) comes
with a classical Android Gradle `installDebug` task to package your application
as an Android `.apk` file and deploy it to your device or simulator:
```bash
$ ANDROID_HOME=/android-sdk/ ./gradlew installDebug
$ ls app/build/outputs/apk/
app-debug.apk
```
![Kt-Mvvm-Demo on Android](android.png)

