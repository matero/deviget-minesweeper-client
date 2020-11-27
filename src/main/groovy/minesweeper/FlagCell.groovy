package minesweeper

final class FlagCell extends CellCommand {
    private final static char FLAG = '?'

    FlagCell() {
        super('flag', 'Flags a cell in a Game.')
    }

    @Override
    protected String getAction() { 'flag' }

    @Override
    protected String getSuccessMessage(final int row, final int column, final Map game) {
        switch (game.status) {
            case "PLAYING":
                String[] board = game.board as String[]
                if (board[row].charAt(column) == FLAG)
                    return "Cell ($row, $column) has been flagged"
                else
                    return "Nothing done, cell ($row, $column) was revealed"
            default:
                throw new IllegalStateException("unexpected game status: '${game.status}'")
        }
    }
}
