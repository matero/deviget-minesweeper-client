package io.bootique.jersey.client.demo;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;

public class Application implements Module {

    public static void main(final String[] args) {
        Bootique
                .app(args)
                .autoLoadModules()
                .module(Application.class)
                .args("--hello", "--config=classpath:bootique.yml")
                .exec()
                .exit();
    }

    @Override
    public void configure(final Binder binder) {
        BQCoreModule.extend(binder).addCommand(HelloCommand.class);
    }
}
