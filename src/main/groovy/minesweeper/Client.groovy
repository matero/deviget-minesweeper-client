package minesweeper

import io.bootique.BQCoreModule
import io.bootique.BaseModule
import io.bootique.Bootique
import io.bootique.di.Binder

final class Client extends BaseModule {

    static void main(final String[] args) {
        Bootique.app(args)
                .autoLoadModules()
                .module(Client.class)
                .args("--config=classpath:config.yml")
                .exec()
                .exit()
    }

    @Override void configure(final Binder binder) {
        BQCoreModule.extend(binder)
                .addCommand(HelloCommand)
                .addCommand(RegisterAccount)
    }
}
