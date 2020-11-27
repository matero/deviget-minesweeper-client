package minesweeper

import io.bootique.command.CommandOutcome
import io.bootique.junit5.BQTest
import io.bootique.junit5.BQTestFactory
import io.bootique.junit5.BQTestTool
import org.junit.jupiter.api.Test

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static org.junit.jupiter.api.Assertions.assertEquals

@BQTest
class RegisterAccount_tests {
    @BQTestTool
    private final BQTestFactory testFactory = new BQTestFactory()

    @Test
    void "should fail when no email is provided"() {
        //given
        final runtime = testFactory.app('--register')
                .autoLoadModules()
                .module(Client)
                .args("--config=classpath:test-config.yml")
                .createRuntime()

        final CommandOutcome outcome = runtime.run()

        assertEquals(MinesweeperCommand.PRECONDITION_NOT_ACCOMPLISHED, outcome.exitCode)
        assertEquals('email is required!', outcome.message)
    }

    @Test
    void "should fail when no password is provided"() {
        //given
        final runtime = testFactory.app('--register', '--email=test@email.com')
                .autoLoadModules()
                .module(Client)
                .args("--config=classpath:test-config.yml")
                .createRuntime()


        final CommandOutcome outcome = runtime.run()

        assertEquals(MinesweeperCommand.PRECONDITION_NOT_ACCOMPLISHED, outcome.exitCode)
        assertEquals('password is required!', outcome.message)
    }

    @Test
    void "when name is not defined then the email is used as name"() {
        //given
        final runtime = testFactory.app('--register', '--email=test@email.com', '-ppassword')
                .autoLoadModules()
                .module(Client)
                .args("--config=classpath:test-config.yml")
                .createRuntime()
        //and
        MinesweeperServer.MOCK.resetAll()
        MinesweeperServer.MOCK.stubFor(post(urlPathEqualTo("/register"))
                .withRequestBody(equalToJson('{"email":"test@email.com","name":"test@email.com","password":"password"}'))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody('{"email":"test@email.com", "name":"test@email.com", "token":"jwt-token"}')))
        final CommandOutcome outcome = runtime.run()

        assertEquals(0, outcome.exitCode)
    }

    @Test
    void "when name is defined then the complete account definition is passed"() {
        //given
        final runtime = testFactory.app('--register', '--email=test@email.com', '-npepe', '-ppassword')
                .autoLoadModules()
                .module(Client)
                .args("--config=classpath:test-config.yml")
                .createRuntime()
        //and
        MinesweeperServer.MOCK.resetAll()
        MinesweeperServer.MOCK.stubFor(post(urlPathEqualTo("/register"))
                .withRequestBody(equalToJson('{"email":"test@email.com","name":"pepe","password":"password"}'))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody('{"email":"test@email.com", "name":"pepe", "token":"jwt-token"}')))
        final CommandOutcome outcome = runtime.run()

        assertEquals(0, outcome.exitCode)
    }
}
