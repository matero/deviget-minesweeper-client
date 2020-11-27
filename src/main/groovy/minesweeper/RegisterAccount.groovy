package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.CommandOutcome

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

class RegisterAccount extends MinesweeperCommand {

    RegisterAccount() {
        super(name: 'register',
                description: 'Registers an Account in minesweeper game.',
                options: [
                        [name       : 'email',
                         description: 'email to identify the account.',
                         required   : 'email_address'],
                        [name       : 'name',
                         description: 'name of the person using the account, if not defined the email is used.',
                         required   : 'nickname'],
                        [name       : 'password',
                         description: 'password of the account.',
                         required   : 'password_text']])
    }

    @Override
    CommandOutcome run(final Cli cli, final WebTarget webTarget) {
        final String email = cli.optionString('email')
        if (!email || email.empty || email.blank)
            return preconditionNotAccomplished('email is required!')

        final String password = cli.optionString('password')
        if (!password || password.empty || password.blank)
            return preconditionNotAccomplished('password is required!')

        def name = cli.optionString('name') ?: email

        def account = [email: email, name: name, password: password]

        final Response response = webTarget.path('/register').request("application/json").post(Entity.json(account))

        final Map json = response.readEntity(Map)
        if (response.status == 201) {
            println "Your account has been created '${json.name}', remember your email is '${json.email}'."
            println "Currectly you can interact with minesweeper using JWT token: ${json.token}"
            return CommandOutcome.succeeded()
        } else {
            println "Could not create the account, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
