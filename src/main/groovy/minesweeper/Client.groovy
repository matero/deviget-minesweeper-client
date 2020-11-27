package minesweeper

import io.bootique.BQCoreModule
import io.bootique.BaseModule
import io.bootique.Bootique
import io.bootique.di.Binder
import io.bootique.meta.application.OptionMetadata

final class Client extends BaseModule {

    static void main(final String[] args) {
        Bootique.app(args)
                .autoLoadModules()
                .module(Client)
                .args("--config=classpath:config.yml")
                .exec()
                .exit()
    }

    @Override void configure(final Binder binder) {
        BQCoreModule.extend(binder)
                .addCommand(Login)
                .addCommand(RegisterAccount)
                .addOption(OptionMetadata.builder("local")
                        .description("Instructs the server to use a local server instead the heroku one.")
                        .build())
    }
}
