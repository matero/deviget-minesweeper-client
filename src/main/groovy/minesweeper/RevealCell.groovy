package minesweeper

final class RevealCell extends CellCommand {

    RevealCell() {
        super('reveal', 'Reveals a cell in a Game.')
    }

    @Override
    protected String getAction() { 'reveal' }

    @Override
    protected String getSuccessMessage(final int row, final int column, final Map game) {
        switch (game.status) {
            case "PLAYING":
                return "Cell ($row, $column) has been revealed"
            case "WON":
                return "Cell ($row, $column) has been revealed, game WON"
            case "LOOSE":
                return "Cell ($row, $column) has been revealed, there was a mine on it, game over"
            default:
                throw new IllegalStateException("unexpected game status: '${game.status}'")
        }
    }
}
