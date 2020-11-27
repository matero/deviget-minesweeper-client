package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.CommandOutcome
import org.glassfish.grizzly.http.HttpHeader

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response
import java.net.http.HttpHeaders

final class RefreshToken extends MinesweeperCommand {

    RefreshToken() {
        super(name: 'refresh',
                description: 'Refreshes an account token.',
                options: [
                        [name       : 'token',
                         description: 'JWT token of an active session, that must be refreshed.',
                         required   : 'jwt_token']])
    }

    @Override
    protected CommandOutcome execute() {
        final Response response = to('/refresh').request(APPLICATION_JSON).header(AUTHORIZATION, bearer).post(NOTHING)

        final Map json = response.readEntity(Map)
        if (response.status == 200) {
            println "Your token has been refreshed."
            println "Currently you can interact with minesweeper using JWT token: ${json.token}"
            return CommandOutcome.succeeded()
        } else {
            println "Could not refresh the JWT token, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
