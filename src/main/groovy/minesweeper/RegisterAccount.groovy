package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.CommandOutcome

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

class RegisterAccount extends MinesweeperCommand {

    protected RegisterAccount() {
        super(
                name: 'register',
                description: 'Registers an Account in minesweeper game.',
                options: [
                        [name: 'email', description: 'email to identify the account.'],
                        [name: 'name', description: 'name of the person using the account, if not defined the email is used.'],
                        [name: 'password', description: 'password of the account.']
                ]
        )
    }

    @Override
    CommandOutcome run(final Cli cli, final WebTarget webTarget) {
        def email = cli.optionString('email')
        def name = cli.optionString('name') ?: email
        def password = cli.optionString('password')

        final Response response = webTarget
                .path('/register')
                .request("application/json")
                .post(Entity.json([email: email, name: name, password: password]))

        final Map json = response.readEntity(Map)
        if (json.errors) {
            return CommandOutcome.failed(response.status, json.errors as String)
        } else {
            println "assigned JWT token: ${json.token}"
            return CommandOutcome.succeeded()
        }
    }
}
