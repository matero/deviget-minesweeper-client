package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.CommandOutcome

import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson
import static java.lang.Integer.valueOf

final class CreateGame extends MinesweeperCommand {
    CreateGame() {
        super(name: 'create',
                description: 'Refreshes an account token.',
                options: [
                        [name       : 'easy',
                         description: 'creates a game of EASY level (8 rows, 8 columns, 10 mines).'],
                        [name       : 'intermediate',
                         description: 'creates a game of INTERMEDIATE level (16 rows, 16 columns, 40 mines).'],
                        [name       : 'expert',
                         description: 'creates a game of EXPERT level (16 rows, 30 columns, 99 mines).'],
                        [name       : 'custom',
                         description: 'creates a custom game, with the desired rows,columns,mines',
                         required   : 'rows,columns,mines'],
                        [name       : 'token',
                         description: 'JWT token of an active session, that must be refreshed.',
                         required   : 'jwt_token']])
    }

    @Override
    CommandOutcome run(final Cli cli, final WebTarget webTarget) {
        int options = countOptions(cli)

        if (options > 1)
            return preconditionNotAccomplished('You can only use one of easy/intermediate/expert/custom options!')

        if (options == 0)
            return preconditionNotAccomplished('You must use one of easy/intermediate/expert/custom options!')

        final String option
        try {
            option = getGameDefinition(cli)
        } catch (e) {
            return preconditionNotAccomplished(e.message)
        }

        final String token = cli.optionString('token')
        if (!token || token.empty || token.blank)
            return preconditionNotAccomplished('JWT token is required!')

        WebTarget target = webTarget.path("/games/create/$option")
        if ("custom" == option) {
            def custom = getCustomDefinition(cli)
            target = target.queryParam('rows', custom.rows)
                    .queryParam('columns', custom.columns)
                    .queryParam('mines', custom.mines)
        }

        final Response response = target
                .request(APPLICATION_JSON)
                .header("Authorization", bearer(token))
                .post(NOTHING)

        if (response.status == 201) {
            final Map game = response.readEntity(Map)
            println "Your game has been created.\n"
            println prettyPrint(toJson(game))
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not create the game, see the error message $response"
            return CommandOutcome.failed(response.status, "json.errors" as String)
        }
    }

    private static int countOptions(Cli cli) {
        int options = cli.hasOption('easy') ? 1 : 0
        if (cli.hasOption('intermediate'))
            options++
        if (cli.hasOption('expert'))
            options++
        if (cli.hasOption('custom'))
            options++
        return options
    }

    private static String getGameDefinition(Cli cli) {
        if (cli.hasOption('easy'))
            return 'EASY'
        if (cli.hasOption('intermediate'))
            return 'INTERMEDIATE'
        if (cli.hasOption('expert'))
            return 'EXPERT'
        if (cli.hasOption('custom')) {
            return 'custom'
        }
        throw new IllegalStateException("unrecognized option")
    }

    Map<String, Integer> getCustomDefinition(final Cli cli) {
        final String[] spec = cli.optionString('custom').split(',')
        if (spec.length != 3) {
            throw new IllegalStateException("Custom games must be defined using the format <int>,<int>,<int>")
        }
        try {
            return [rows: valueOf(spec[0]), columns: valueOf(spec[1]), mines: valueOf(spec[2])]
        } catch (NumberFormatException nfe) {
            throw new IllegalStateException("Custom games must be defined using the format <int>,<int>,<int>")
        }
    }
}
