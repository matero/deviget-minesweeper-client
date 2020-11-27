package minesweeper

import io.bootique.command.CommandOutcome

import javax.ws.rs.core.Response

final class Login extends MinesweeperCommand {

    Login() {
        super(name: 'login',
                description: 'Authenticates a registered user.',
                options: [
                        [name       : 'email',
                         description: 'email to identify the account.',
                         required   : 'email_address'],
                        [name       : 'password',
                         description: 'password of the account.',
                         required   : 'password_text']])
    }

    @Override
    protected CommandOutcome execute() {
        def credentials = [email: getOption('email'), password: getOption('password')]
        final Response response = to('/login').request(APPLICATION_JSON).post(asJson(credentials))

        if (response.status == 200) {
            final Map account = response.readEntity(Map)
            println "You login was successful '${account.name}', remember your email is '${account.email}'."
            println "Currently you can interact with minesweeper using JWT token: ${account.token}"
            return CommandOutcome.succeeded()
        } else {
            final Map json = response.readEntity(Map)
            println "Could not login into minesweeper, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
