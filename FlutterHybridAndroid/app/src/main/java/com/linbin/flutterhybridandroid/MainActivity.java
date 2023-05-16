package com.linbin.flutterhybridandroid;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MainActivity extends AppCompatActivity {

    public FlutterEngine flutterEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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


        findViewById(R.id.Shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 //               // 每次都构建新 FlutterEngine, 慢
//                startActivity(
//                      FlutterActivity.createDefaultIntent(MainActivity.this)
//                );
//                // 每次都构建新 FlutterEngine, 慢
//                startActivity(
//                    FlutterActivity
//                        .withNewEngine()
//                        .initialRoute("/my_route")
//                        .build(MainActivity.this)
//                );
                // 切记，Dart 代码会在你预热 FlutterEngine 时就开始执行，
                // 并且在你的 FlutterActivity 或 FlutterFragment 销毁后继续运行。
                // 要停止代码运行和清理相关资源，可以从 FlutterEngineCache 中获取你的 FlutterEngine，
                // 然后使用 FlutterEngine.destroy() 来销毁 FlutterEngine。
                startActivity(
                    FlutterActivity
                        .withCachedEngine("my_engine_id")
                        .build(MainActivity.this)
                );
            }
        });

        findViewById(R.id.Community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                    FlutterActivity
                        .withCachedEngine("my_engine_id")
                        .build(MainActivity.this)
                );
            }
        });

        findViewById(R.id.Me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                    FlutterActivity
                        .withNewEngine()
                        .initialRoute("/my_route")
                        .build(MainActivity.this)
                );
            }
        });
    }
}