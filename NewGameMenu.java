import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class NewGameMenu extends JMenu {
    NewGameMenu(CampoMinado campoMinado) {
        super("Novo Jogo");

        add(createMenuItem("Fácil", CampoMinado.DifficultyLevel.EASY, campoMinado));
        add(createMenuItem("Médio", CampoMinado.DifficultyLevel.MEDIUM, campoMinado));
        add(createMenuItem("Difícil", CampoMinado.DifficultyLevel.HARD, campoMinado));
    }

    private JMenuItem createMenuItem(String title, CampoMinado.DifficultyLevel difficulty, CampoMinado campoMinado) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(e -> campoMinado.startGame(difficulty));
        return menuItem;
    }
}