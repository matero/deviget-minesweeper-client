package minesweeper


import io.bootique.command.CommandOutcome

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

final class PauseGame extends MinesweeperCommand {
    protected static final Entity<String> EMPTY = asJson("")

    PauseGame() {
        super(name: 'pause',
                description: 'Pause a game',
                options: [
                        [name       : 'game',
                         description: 'Id of the game that must be paused.',
                         required   : 'integer_number'],
                        [name       : 'token',
                         description: 'JWT token of an active session.',
                         required   : 'jwt_token']])
    }

    @Override
    protected CommandOutcome execute() {
        final int gameId = getIntOption("game")

        final Response response = to("/games/${gameId}/pause")
                .request(APPLICATION_JSON)
                .header(AUTHORIZATION, bearer)
                .put(EMPTY)

        if (response.status == 200) {
            final Map game = response.readEntity(Map)
            println "Game paused.\n\n${show(game)}"
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
