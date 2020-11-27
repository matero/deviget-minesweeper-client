package minesweeper

import com.github.tomakehurst.wiremock.WireMockServer

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

enum MinesweeperServer {
    MOCK

    final int port = 8765

    @Delegate
    private final WireMockServer server

    MinesweeperServer() {
        this.server = new WireMockServer(wireMockConfig().port(port))
        this.start()
        addShutdownHook {
            server.shutdownServer()
        }
    }

}
