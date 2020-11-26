package minesweeper

import io.bootique.cli.Cli
import io.bootique.command.Command
import io.bootique.command.CommandOutcome
import io.bootique.jersey.client.HttpTargets

import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.core.Response

final class HelloCommand implements Command
{
  @Inject private Provider<HttpTargets> targets

  @Override CommandOutcome run(final Cli cli)
  {
    Response response = targets.get()
                               .newTarget("google")
                               .request()
                               .get()
    System.out.println(response.readEntity(String.class))

    return CommandOutcome.succeeded()
  }
}
