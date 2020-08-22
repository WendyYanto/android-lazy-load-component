# Android Lazy Load Component
A component view to help lazy load any view component (using layout resource ID). It inflate the layout when it is shown in the viewport seen by user. Thus, it helps to reduce layout startup time by inflating layout view lazily.

Disclaimer: For better visual purpose, this demo uses 1 second of delay and color to help you to understand how this lazy load component works

![Demo](https://github.com/WendyYanto/android-lazy-load-component/blob/master/assets/demo.gif)

## Implementation Steps

1. This library has already been included in jitpack.io. In order to use it, you should add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2.  Add the dependency
```
dependencies {
  implementation 'com.github.WendyYanto:android-lazy-load-component:v1.1.0'
}
```
3. Include LazyLoadComponent in your XML layout (best recommended to use in `ScrollView`)
```xml
<dev.wendyyanto.library.LazyLoadComponent
    android:layout_width="match_parent"
    android:layout_height="10dp" />
```
4. Add the required attribute at LazyLoadComponent:
- `app:layout_res`: The layout that is intended to be loaded lazily at runtime
- `app:parent_layout_id`: The root layout's ID (it is used for LazyLoadComponent to decide when to load at runtime if only it's already on the viewport or beyond)

```xml
<dev.wendyyanto.library.LazyLoadComponent
    android:layout_width="match_parent"
    android:layout_height="10dp"
    app:layout_res="@layout/layout_you_want_to_load_lazyily"
    app:parent_layout_id="@id/root_layout_id" />
```
