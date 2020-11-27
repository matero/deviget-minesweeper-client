package minesweeper

import io.bootique.command.CommandOutcome

import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

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
                         description: 'JWT token of an active session.',
                         required   : 'jwt_token']])
    }

    @Override
    protected CommandOutcome execute() {
        if (gameLevelsDefined() > 1)
            throw new IllegalStateException('You can only use one of easy/intermediate/expert/custom options!')

        final String option = getGameDefinition()

        WebTarget target = to("/games/create/$option")
        if ("custom" == option) {
            def custom = getCustomDefinition()
            target = target.queryParam('rows', custom.rows)
                    .queryParam('columns', custom.columns)
                    .queryParam('mines', custom.mines)
        }

        final Response response = target.request(APPLICATION_JSON).header(AUTHORIZATION, bearer).post(NOTHING)

        if (response.status == 201) {
            final Map game = response.readEntity(Map)
            println "Your game has been created.\n"
            show(game)
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not create the game, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }

    private int gameLevelsDefined() {
        int options = has('easy') ? 1 : 0
        if (has('intermediate'))
            options++
        if (has('expert'))
            options++
        if (has('custom'))
            options++
        return options
    }

    private String getGameDefinition() {
        if (has('easy'))
            return 'EASY'
        if (has('intermediate'))
            return 'INTERMEDIATE'
        if (has('expert'))
            return 'EXPERT'
        if (has('custom')) {
            return 'custom'
        }
        throw new IllegalStateException('You must use one of easy/intermediate/expert/custom options!')
    }

    private Map<String, Integer> getCustomDefinition() {
        final String[] spec = getOption('custom').split(',')
        if (spec.length != 3) {
            throw new IllegalStateException("Custom games must be defined using the format <int>,<int>,<int>")
        }
        try {
            return [rows: valueOf(spec[0]), columns: valueOf(spec[1]), mines: valueOf(spec[2])]
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Custom games must be defined using the format <int>,<int>,<int>", e)
        }
    }
}
