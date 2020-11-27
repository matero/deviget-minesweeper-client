package minesweeper

final class UnflagCell extends CellCommand {
    private final static char UNREVEALED = '#'

    UnflagCell() {
        super('unflag', 'Unflags a cell in a Game.')
    }

    @Override
    protected String getAction() { 'unflag' }

    @Override
    protected String getSuccessMessage(final int row, final int column, final Map game) {
        if ("PLAYING" != game.status)
            throw new IllegalStateException("unexpected game status: '${game.status}'")

        final String[] board = game.board as String[]
        if (board[row].charAt(column) == UNREVEALED)
            return "Cell ($row, $column) has been unflagged"
        else
            return "Nothing done, cell ($row, $column) was revealed"
    }
}
