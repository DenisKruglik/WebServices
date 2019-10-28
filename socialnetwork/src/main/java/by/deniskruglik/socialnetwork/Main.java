package by.deniskruglik.socialnetwork;

import by.deniskruglik.socialnetwork.di.BaseModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BaseModule());
        App app = injector.getInstance(App.class);
        app.run();
    }
}
