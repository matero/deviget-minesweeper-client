package minesweeper


import io.bootique.command.CommandOutcome

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

abstract class CellCommand extends MinesweeperCommand {
    protected static final Entity<String> EMPTY = asJson("")

    CellCommand(String name, String description) {
        super(name: name,
                description: description,
                options: [
                        [name       : 'game',
                         description: 'Id of the game where the cell must be revealed.',
                         required   : 'integer_number'],
                        [name       : 'row',
                         description: 'Row of the cell to reveal (0 based)',
                         required   : 'integer_number'],
                        [name       : 'column',
                         description: 'Column of the cell to reveal (0 based)',
                         required   : 'integer_number'],
                        [name       : 'token',
                         description: 'JWT token of an active session.',
                         required   : 'jwt_token']])
    }

    protected abstract String getAction();

    protected abstract String getSuccessMessage(int row, int column, Map game);

    @Override
    protected CommandOutcome execute() {
        final int gameId = getIntOption("game")
        final int row = getIntOption("row")
        final int column = getIntOption("column")

        final Response response = to("/games/${gameId}/${action}/${row}/${column}")
                .request(APPLICATION_JSON)
                .header(AUTHORIZATION, bearer)
                .put(EMPTY)

        if (response.status == 200) {
            final Map game = response.readEntity(Map)
            println "${getSuccessMessage(row, column, game)}.\n\n${show(game)}"
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not create the game, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
