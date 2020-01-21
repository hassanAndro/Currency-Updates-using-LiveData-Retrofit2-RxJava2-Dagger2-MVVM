package test.revolut.revolut;

import android.app.Application;
import android.content.Context;

import test.revolut.revolut.di.AppComponent;
import test.revolut.revolut.di.AppModule;
import test.revolut.revolut.di.DaggerAppComponent;
import test.revolut.revolut.di.UtilsModule;

public class MyApplication extends Application {
    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).utilsModule(new UtilsModule()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}