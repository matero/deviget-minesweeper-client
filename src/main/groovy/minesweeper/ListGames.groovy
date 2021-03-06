package minesweeper
import io.bootique.command.CommandOutcome

import javax.ws.rs.core.Response

final class ListGames extends MinesweeperCommand {

    ListGames() {
        super(name: 'games',
                description: 'List the account games.',
                options: [
                        [name       : 'token',
                         description: 'JWT token of an active session for the account owner of the games to list.',
                         required   : 'jwt_token']])
    }

    @Override
    protected CommandOutcome execute() {
        final Response response = to('/games').request(APPLICATION_JSON).header(AUTHORIZATION, bearer).get()

        if (response.status == 200) {
            final List<Map> games = response.readEntity(List)
            switch (games.size()) {
                case 0:
                    println "Currently you have no game created."
                    break
                case 1:
                    println "Currently you have 1 game created.:\n ${show(games[0])}"
                    break
                default:
                    println "Currently you have ${games.size()} games created.\n"
                    games.eachWithIndex { def game, int i -> println "${i + 1}) ${show(game)}\n" }
            }
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not refresh the JWT token, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
