package minesweeper

import groovy.transform.PackageScope
import io.bootique.cli.Cli
import io.bootique.command.Command
import io.bootique.command.CommandOutcome
import io.bootique.command.CommandWithMetadata
import io.bootique.jersey.client.HttpTargets
import io.bootique.meta.application.CommandMetadata
import io.bootique.meta.application.OptionMetadata

import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.client.WebTarget

abstract class MinesweeperCommand extends CommandWithMetadata {
    @PackageScope static final int PRECONDITION_NOT_ACCOMPLISHED = 255

    @Inject private Provider<HttpTargets> targets

    protected MinesweeperCommand(final Map metadata) {
        super(describedWith(metadata))
    }

    private static CommandMetadata.Builder describedWith(final Map metadata) {
        CommandMetadata.Builder commandMetadataBuilder
        if (metadata.name)
            commandMetadataBuilder = CommandMetadata.builder(metadata.name as String)
        else if (metadata.type)
            commandMetadataBuilder = CommandMetadata.builder(metadata.type as Class<? extends Command>)
        else
            throw new IllegalStateException("you need to define command name or type!")

        if (metadata.shortName)
            commandMetadataBuilder.shortName(metadata.shortName as char)

        if (metadata.description)
            commandMetadataBuilder.description(metadata.description as String)

        if (metadata.hidden)
            commandMetadataBuilder.hidden()

        if (metadata.options)
            metadata.options.each {
                if (it instanceof Map) {
                    if (!it.name)
                        throw new IllegalStateException("command options must have a name")
                    final OptionMetadata.Builder optionMetadataBuilder = OptionMetadata.builder(it.name as String)

                    if (it.shortName)
                        optionMetadataBuilder.shortName(it.shortName as String)

                    if (it.description)
                        optionMetadataBuilder.description(it.description as String)

                    if (it.required)
                        optionMetadataBuilder.valueRequired(it.required as String)

                    commandMetadataBuilder.addOption(optionMetadataBuilder)
                } else if (it instanceof OptionMetadata) {
                    commandMetadataBuilder.addOption(it)
                } else if (it instanceof OptionMetadata.Builder) {
                    commandMetadataBuilder.addOption(it)
                } else {
                    throw new IllegalStateException("command option type unrecognized: '${it.class.canonicalName}'")
                }
            }
        return commandMetadataBuilder
    }

    @Override
    CommandOutcome run(final Cli cli) {
        boolean useLocalServer = cli.hasOption("local")
        def httpTargets = targets.get()
        final WebTarget webTarget= httpTargets.newTarget(useLocalServer? "local" : "heroku")
        return run(cli, webTarget)
    }

    abstract CommandOutcome run(Cli cli, WebTarget webTarget)

    protected static CommandOutcome preconditionNotAccomplished(String message) {
        CommandOutcome.failed(PRECONDITION_NOT_ACCOMPLISHED, message)
    }
}
