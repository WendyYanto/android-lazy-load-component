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
  implementation 'com.github.WendyYanto:android-lazy-load-component:v1.3.0'
}
```
3. Include LazyLoadComponent in your XML layout (best recommended to use in `ScrollView`)
```xml
<dev.wendyyanto.library.LazyLoadComponent
    android:layout_width="match_parent"
    android:layout_height="10dp" />
```
4. Add the attribute needed at LazyLoadComponent:
- `app:layout_res` (required:int) : The layout that is intended to be loaded lazily at runtime
- `app:parent_layout_id` (required:int) : The root layout's ID (it is used for LazyLoadComponent to decide when to load at runtime if only it's already on the viewport or beyond)
- `app:image_placeholder` (not-required:int) : Image resource as placeholder of lazy load component (first priority to load if exist)
- `app:text_placeholder` (not-required:string) : Text placeholder of the lazy load component (second priority to load if exist)
- `app:reset_padding_on_load` (not-required:boolean) : Flag for lazy load component to reset padding into zero

```xml
<dev.wendyyanto.library.LazyLoadComponent
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp"
    app:layout_res="@layout/layout_you_want_to_load_lazyily"
    app:parent_layout_id="@id/root_layout_id"
    app:text_placeholder="Please wait ... "
    app:reset_padding_on_load="true"
/>
```
