import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellsCamp extends JPanel {
    private int lastRows = 0;
    private int lastColumns = 0;
    private boolean[][] bombs;
    private CampoMinado campoMinado;
    private boolean freezeCells = false;

    CellsCamp(DifficultyConfig config, CampoMinado campoMinado, InfoBar infoBar) {
        setCells(config, infoBar);
        this.campoMinado = campoMinado;
    }

    public void setCells(DifficultyConfig config, InfoBar infoBar) {
        int rows = config.getRows();
        int columns = config.getColumns();
        int quantBombs = config.getBombs();
        freezeCells = false;

        if (this.lastRows == rows && this.lastColumns == columns) {
            setRandomBombs(quantBombs);
            setExistentCells();
        } else {
            this.lastRows = rows;
            this.lastColumns = columns;
            setRandomBombs(quantBombs);
            setNewSize();
            setNewCells(infoBar);
        }
    }

    private void setRandomBombs(int quantBombs) {
        this.bombs = new boolean[this.lastRows][this.lastColumns];
        for (int i = 0; i < quantBombs; i++) {
            int randomRow = (int) (Math.random() * this.lastRows);
            int randomColumn = (int) (Math.random() * this.lastColumns);

            if (this.bombs[randomRow][randomColumn]) {
                i--;
            } else {
                this.bombs[randomRow][randomColumn] = true;
            }
        }
    }

    private void setExistentCells() {
        for (int i = 0; i < this.lastRows; i++) {
            for (int j = 0; j < this.lastColumns; j++) {
                ((Cell) getComponent(i * this.lastColumns + j)).setBomb(this.bombs[i][j]);
            }
        }
    }

    private void setNewCells(InfoBar infoBar) {
        clean();
        for (int i = 0; i < this.lastRows; i++) {
            for (int j = 0; j < this.lastColumns; j++) {
                final int finalI = i;
                final int finalJ = j;
                Cell cell = new Cell(this.bombs[i][j]);
                cell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (freezeCells || cell.isRevealed() || cell.isFlagged()) {
                            return;
                        }

                        if (cell.isBomb()) {
                            endGame(CampoMinado.GameStatus.LOSE);
                        } else {
                            showSpacesAround(finalI, finalJ);
                            verifyWin();
                        }
                    }
                });
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (freezeCells) {
                            return;
                        }

                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (!cell.isRevealed() && infoBar.getRemainingBombs() > 0) {
                                cell.displayFlag();
                                infoBar.reduceBombs();
                            } else if (cell.isFlagged()) {
                                cell.displayEmpty();
                                infoBar.increaseBombs();
                            }
                        }
                    }
                });
                add(cell);
            }
        }
    }

    private void verifyWin() {
        for (Component component : getComponents()) {
            Cell cell = (Cell) component;
            if (!cell.isRevealed() && !cell.isBomb()) {
                return;
            }
        }
        endGame(CampoMinado.GameStatus.WIN);
    }

    public void setNewSize() {
        setLayout(new GridLayout(this.lastRows, this.lastColumns));
        setPreferredSize(new Dimension(this.lastColumns * CampoMinado.cellSize, this.lastRows * CampoMinado.cellSize));
    }

    public void showSpacesAround(int row, int column) {
        if (!cellIsValid(row, column)) {
            return;
        }

        Cell cellClicked = getCell(row, column);
        int clickedBombsAround = getBombsAround(row, column);

        if (clickedBombsAround > 0) {
            cellClicked.displayNumber(clickedBombsAround);
        } else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newColumn = column + j;

                    if (cellIsValid(newRow, newColumn)) {
                        Cell cell = getCell(newRow, newColumn);

                        if (!cell.isBomb() && !cell.isRevealed()) {
                            int bombsAround = getBombsAround(newRow, newColumn);
                            cell.displayNumber(bombsAround);

                            if (bombsAround == 0) {
                                showSpacesAround(newRow, newColumn);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getBombsAround(int row, int column) {
        if (!cellIsValid(row, column)) {
            return 0;
        }

        int bombCount = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newColumn = column + j;
                if (cellIsValid(newRow, newColumn)
                        && bombs[newRow][newColumn]) {
                    bombCount++;
                }
            }
        }
        return bombCount;
    }

    public void showNumber(int row, int column) {
        if (!cellIsValid(row, column)) {
            return;
        }
        getCell(row, column).displayNumber(getBombsAround(row, column));
    }

    public void displayBombs(CampoMinado.GameStatus status) {
        if (CampoMinado.GameStatus.LOSE == status || CampoMinado.GameStatus.WIN == status) {
            for (Component component : getComponents()) {
                ((Cell) component).displayBomb(status);
            }
        }
    }

    public Cell getCell(int row, int column) {
        if (!cellIsValid(row, column)) {
            throw new IllegalArgumentException("Invalid cell coordinates");
        }
        return (Cell) getComponent(row * this.lastColumns + column);
    }

    public void clean() {
        removeAll();
    }

    public void endGame(CampoMinado.GameStatus status) {
        if (CampoMinado.GameStatus.LOSE == status || CampoMinado.GameStatus.WIN == status) {
            campoMinado.endGame(status);
        }
    }

    public boolean cellIsValid(int row, int column) {
        return row >= 0 && row < this.lastRows && column >= 0 && column < this.lastColumns;
    }

    public void freezeCells() {
        freezeCells = true;
    }

}
