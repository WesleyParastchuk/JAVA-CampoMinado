import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class CampoMinado extends JFrame {
    static enum DifficultyLevel {
        EASY, MEDIUM, HARD
    };

    static enum GameStatus {
        INGAME, WIN, LOSE, IDLE
    };

    final static public int cellSize = 50;
    private DifficultyConfig config;
    private DifficultyLevel difficulty = DifficultyLevel.MEDIUM;
    private MenuBar menu;
    private InfoBar info;
    private CellsCamp cellsCamp;
    private GameStatus gameStatus = GameStatus.IDLE;

    public static void main(String[] args) {
        CampoMinado campoMinado = new CampoMinado();
        campoMinado.initComponents();
    }

    public void initComponents() {
        setDifficulty(DifficultyLevel.MEDIUM);
        config = DifficultyConfigFactory.createConfig(difficulty);

        menu = new MenuBar(this);
        info = new InfoBar(config.getBombs(), this);
        cellsCamp = new CellsCamp(config, this, info);

        setTitle("Campo Minado");
        resetSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());
        setJMenuBar(menu);
        add(info, BorderLayout.NORTH);
        add(cellsCamp, BorderLayout.CENTER);

        setVisible(true);
    }

    public void startGame(DifficultyLevel difficulty) {
        if (this.difficulty != difficulty) {
            setDifficulty(difficulty);
            this.config = DifficultyConfigFactory.createConfig(difficulty);
            resetSize();
        }
        startGame();
    }

    public void startGame() {
        info.start(config.getBombs());
        cellsCamp.setCells(config, info);
    }

    public void endGame(GameStatus gameStatus) {
        setGameStatus(gameStatus);
        cellsCamp.freezeCells();
        cellsCamp.displayBombs(gameStatus);
        info.endGame(gameStatus);
    }

    private void resetSize() {
        Dimension menuDimensions = menu.getPreferredSize();
        setSize(menuDimensions.width + cellSize * config.getColumns(),
                menuDimensions.height + cellSize * config.getRows() + InfoBar.DEFAULT_HEIGHT);
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
}