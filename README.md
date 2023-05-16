# 说明

1. 创建 Flutter Module 工程

```sh
flutter create -t module shop
```

2. 创建 Android 工程

```groovy

// 1. 创建 Empty Activity 空工程

// 2. 配置 '/app/build.gradle' 文件

android {
    defaultConfig {
        minSdk 23 // 大于16版本
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation project(':flutter')
}

// 3. 配置 '/settings.gradle' 文件

setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'shop/.android/include_flutter.groovy'
))
include ':shop'
project(':shop').projectDir = new File('../shop')

// 如果配置完成, 重新编译报错
// Caused by: org.gradle.api.internal.plugins.PluginApplicationException: Failed to apply plugin class 'FlutterPlugin'.

// 1. 修改 '/settings.gradle' 配置

dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

// 2. 增加 '/build.gradle' 配置

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

```

3. 创建 iOS 工程
