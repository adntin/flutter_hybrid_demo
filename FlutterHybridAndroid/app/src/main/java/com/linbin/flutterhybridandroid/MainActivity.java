package com.linbin.flutterhybridandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.RenderMode;
import io.flutter.embedding.android.TransparencyMode;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends Application {
//public class MainActivity extends FragmentActivity {

    // ------ FlutterEngine ------
    // 声明一个局部变量来引用FlutterEngine，以便以后可以转发对它的调用。
    public FlutterEngine flutterEngine;

    // ------ FlutterFragment ------
    // 定义一个标签字符串来表示这个Activity的FragmentManager中的FlutterFragment。这个值可以是你想要的任何值。
    private static final String TAG_FLUTTER_FRAGMENT = "flutter_fragment";
    // 声明一个局部变量来引用FlutterFragment，以便以后可以转发对它的调用。
    private FlutterFragment flutterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("MainActivity", "onCreate");

        // 加载布局页面 "/app/src/main/res/layout/activity_main.xml"
        setContentView(R.layout.activity_main);

        // ------ FlutterEngine ------
        // 初始化Flutter引擎
        flutterEngine = new FlutterEngine(this);
        // 初始化路由
        flutterEngine.getNavigationChannel().setInitialRoute("your/route/here");
        // 开始执行Dart代码, 预热Flutter引擎
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        // 缓存FlutterEngine以供FlutterActivity或FlutterFragment使用。
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);

        // ------ FlutterFragment ------
        // 获取对Activity的FragmentManager的引用来添加一个新的FlutterFragment，或者找到一个现有的。
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 尝试找到一个现有的FlutterFragment，以防这不是第一次运行onCreate()。
        flutterFragment = (FlutterFragment) fragmentManager
                .findFragmentByTag(TAG_FLUTTER_FRAGMENT);


        // ------ FlutterActivity ------
        findViewById(R.id.Shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ------ FlutterActivity ------
                //               // 每次都构建新 FlutterEngine, 慢!!!
//                startActivity(
//                      FlutterActivity.createDefaultIntent(MainActivity.this)
//                );
                // 切记，Dart 代码会在你预热 FlutterEngine 时就开始执行，
                // 并且在你的 FlutterActivity 或 FlutterFragment 销毁后继续运行。
                // 要停止代码运行和清理相关资源，可以从 FlutterEngineCache 中获取你的 FlutterEngine，
                // 然后使用 FlutterEngine.destroy() 来销毁 FlutterEngine。
                // 使用缓存的FlutterEngine创建FlutterActivity。快!!!
                startActivity(
                        FlutterActivity
                                .withCachedEngine("my_engine_id")
                                .build(MainActivity.this)
                );
                // ------ FlutterFragment ------
//                fragmentManager.beginTransaction().remove(flutterFragment).commit();
            }
        });

        // ------ FlutterActivity ------
        findViewById(R.id.Community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ------ FlutterActivity ------
                // 使用新的FlutterEngine创建FlutterActivity。慢!!!
                startActivity(
                        FlutterActivity
                                .withNewEngine()
                                .initialRoute("/my_route")
                                .build(MainActivity.this)
                );
                // ------ FlutterFragment ------
//                fragmentManager.beginTransaction().remove(flutterFragment).commit();
            }
        });

        // ------ FlutterFragment ------
        findViewById(R.id.Me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建并附加一个FlutterFragment(如果不存在)。
                if (flutterFragment == null) {
//                    // 创建默认, 慢!!!
//                    flutterFragment = FlutterFragment.createDefault();
//                    // 指定 Flutter 运行的初始路由
//                    flutterFragment = FlutterFragment.withNewEngine()
//                        .initialRoute("myInitialRoute/")
//                        .build();
//                    // 指定 Flutter 运行的入口
//                    flutterFragment = FlutterFragment.withNewEngine()
//                        .dartEntrypoint("mySpecialEntrypoint")
//                        .build();
                    // 使用缓存的FlutterEngine创建FlutterFragment。快!!!
                    // 默认配置的 SurfaceView 在性能上明显好于 TextureView
                    // https://flutter.cn/docs/development/add-to-app/android/add-flutter-fragment#control-flutterfragments-render-mode
                    flutterFragment = FlutterFragment.withCachedEngine("my_engine_id")
                            .renderMode(RenderMode.texture) // 控制 FlutterFragment 的渲染模式
                            .transparencyMode(TransparencyMode.transparent) // 展示透明的 FlutterFragment
                            .shouldAttachEngineToActivity(false) // FlutterFragment 与其 Activity 之间的关系
                            .build();
                }
                if (flutterFragment.isAdded()) {
                    Log.i("MainActivity", "flutterFragment added.");
                    return;
                }
                // 挂载 FlutterFragment 到 FrameLayout 容器中
                fragmentManager
                        .beginTransaction()
                        .add(
                                R.id.someContainer,
                                flutterFragment,
                                TAG_FLUTTER_FRAGMENT
                        )
                        .commit();
            }
        });
    }
}