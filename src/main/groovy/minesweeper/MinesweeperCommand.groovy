package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.Command
import io.bootique.command.CommandOutcome
import io.bootique.command.CommandWithMetadata
import io.bootique.jersey.client.HttpTargets
import io.bootique.meta.application.CommandMetadata
import io.bootique.meta.application.OptionMetadata

import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

abstract class MinesweeperCommand extends CommandWithMetadata {
    protected static final int PRECONDITION_NOT_ACCOMPLISHED = 255

    protected static final Entity<Object> NOTHING = Entity.json(null)
    protected static final Entity<String> EMPTY = asJson("")

    protected static final String APPLICATION_JSON = "application/json"
    protected static final String AUTHORIZATION = "Authorization"

    @Inject
    private Provider<HttpTargets> targets

    private WebTarget webTarget
    private Cli cli

    protected MinesweeperCommand(final Map metadata) { super(describedWith(metadata)) }

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

        webTarget = httpTargets.newTarget(useLocalServer ? "local" : "heroku")
        this.cli = cli

        try {
            return execute()
        } catch (IllegalStateException | IllegalArgumentException e) {
            return CommandOutcome.failed(PRECONDITION_NOT_ACCOMPLISHED, e.message)
        }
    }

    /**
     * Executes the command after the webTarget has been defined and the cli is registered.
     * @return the outcome of executing the command.
     */
    protected abstract CommandOutcome execute()

    protected WebTarget to(final String path) { webTarget.path(path) }

    /**
     * Checks if an option is defined.
     *
     * @param name name of the option to check.
     *
     * @return @{code false} if the cli doesn't have the option, or its value is @{code null}, @{code empty} or
     * @{code blank}, @{code true} in any other case.
     */
    protected boolean has(final String name) {
        final String value = cli.optionString(name)
        return value && !value.empty && !value.blank
    }

    /**
     * Reads the value of an option
     *
     * @param name name of the option to read.
     *
     * @return the value of the option or @{code null} if option is undefined.
     */
    protected String option(final String name) { optionOrElse(name, null) }

    /**
     * Reads the value of an option, if not defined returns the provided default value.
     *
     * @param name name of the option to read.
     * @param defaultValue result when option is undefined.
     *
     * @return the value of the option or the @{code defaultValue} if option is undefined.
     */
    protected String optionOrElse(final String name, final String defaultValue) {
        final String value = cli.optionString(name)
        return value ? value.trim() : defaultValue
    }

    /**
     * Reads the value of a required option.
     *
     * @param name name of the option to read.
     *
     * @return the value of the option.
     *
     * @throws IllegalStateException if the option is not defined.
     * @throws IllegalArgumentException if the option is null, empty or blank.
     */
    protected String getOption(final String name) {
        if (!cli.hasOption(name))
            throw new IllegalStateException("$name is required!")
        final String value = cli.optionString(name)
        if (!value || value.empty || value.blank)
            throw new IllegalArgumentException("$name is required!")
        return value.trim()
    }


    /**
     * Reads the value of a required int option.
     *
     * @param name name of the option to read.
     *
     * @return the value of the option.
     *
     * @throws IllegalStateException if the option is not defined.
     * @throws IllegalArgumentException if the option is null, empty or blank.
     * @throws NumberFormatException if the option value doesn't represent an integer.
     */
    protected int getIntOption(final String name) { Integer.parseInt(getOption(name)) }

    protected String getBearer() { "Bearer " + getOption("token") }

    protected static String show(final Object definition) { prettyPrint(toJson(definition)) }

    protected static <T> Entity<T> asJson(final T entity) { Entity.json(entity) }
}
