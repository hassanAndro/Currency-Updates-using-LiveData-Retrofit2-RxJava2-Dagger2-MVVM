package test.revolut.revolut.di;

import javax.inject.Singleton;

import dagger.Component;
import test.revolut.revolut.Currency.MainActivity;


@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivity mainActivity);

}
