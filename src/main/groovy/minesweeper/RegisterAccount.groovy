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
    protected CommandOutcome execute() {
        final String email = getOption('email')
        final String password = getOption('password')
        def name = optionOrElse('name', email)

        def account = [email: email, name: name, password: password]

        final Response response = to('/register').request(APPLICATION_JSON).post(asJson(account))

        final Map json = response.readEntity(Map)
        if (response.status == 201) {
            println "Your account has been created '${json.name}', remember your email is '${json.email}'."
            println "Currently you can interact with minesweeper using JWT token: ${json.token}"
            return CommandOutcome.succeeded()
        } else {
            println "Could not create the account, see the error message"
            return CommandOutcome.failed(response.status, json.errors as String)
        }
    }
}
