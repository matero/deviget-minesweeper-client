package minesweeper


import groovy.util.logging.Slf4j
import io.bootique.cli.Cli
import io.bootique.command.Command
import io.bootique.command.CommandOutcome
import io.bootique.jersey.client.HttpTargets

import javax.inject.Inject
import javax.inject.Provider

@Slf4j
final class HelloCommand implements Command {
    @Inject
    private Provider<HttpTargets> targets

    @Override
    CommandOutcome run(final Cli cli) {
        def response = targets.get().newTarget("google").request().get()
        if (log.debugEnabled)
            log.debug("Login response: {}", response.readEntity(String.class))

        return CommandOutcome.succeeded()
    }
}
