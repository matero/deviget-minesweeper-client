package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.CommandOutcome

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson
import static java.lang.Integer.valueOf

final class RevealCell extends MinesweeperCommand {

    RevealCell() {
        super(name: 'reveal',
                description: 'Reveals a cell in a Game.',
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

    @Override
    protected CommandOutcome execute() {
        final int gameId = getIntOption("game")
        final int row = getIntOption("row")
        final int column = getIntOption("column")

        final Response response = to("/games/${gameId}/reveal/${row}/${column}")
                .request(APPLICATION_JSON)
                .header(AUTHORIZATION, bearer)
                .put(EMPTY)

        if (response.status == 200) {
            final Map game = response.readEntity(Map)
            println "Cell has been revealed, actual status game:\n${show(game)}"
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not create the game, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
