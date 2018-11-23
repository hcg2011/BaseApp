package com.chungo.basemore.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * 在子线程中完成其他初始化
 */
public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {
        //初始化日志
        //Logger.init(getPackageName()).hideThreadInfo();

        //初始化错误收集
        //        CrashHandler.init(new CrashHandler(getApplicationContext()));
        initBugly();

        //初始化内存泄漏检测
        //LeakCanary.install(App.getInstance());

        //初始化过度绘制检测
        //BlockCanary.install(getApplicationContext(), new AppBlockCanaryContext()).start();

    }

    private void initBugly() {
        //        Context context = getApplicationContext();
        //        String packageName = context.getPackageName();
        //        String processName = SystemUtil.getProcessName(android.os.Process.myPid());
        //        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        //        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //        CrashReport.initCrashReport(context, Constants.BUGLY_ID, isDebug, strategy);
    }
}
