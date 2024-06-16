import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;

public class MenuBar extends JMenuBar {
    public static final int DEFAULT_HEIGHT = 20;

    public MenuBar(CampoMinado campoMinado) {
        setPreferredSize(new Dimension(getPreferredSize().width, DEFAULT_HEIGHT));

        JMenu gameMenu = createMenu("Jogo");
        add(gameMenu);

        JMenu difficultyMenu = new NewGameMenu(campoMinado);
        gameMenu.add(difficultyMenu);

        JMenuItem restartItem = createMenuItem("Reiniciar", campoMinado);
        gameMenu.add(restartItem);
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        return menu;
    }

    private JMenuItem createMenuItem(String title, CampoMinado campoMinado) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(e -> campoMinado.startGame());
        return menuItem;
    }
}