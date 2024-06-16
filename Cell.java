import java.awt.Dimension;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.HashMap;

public class Cell extends JButton {
    private boolean isBomb;
    private static final Icon bombLoseIcon = getImageIconConfigured("/images/Bombs/BombLoss.png");
    private static final Icon bombWinIcon = getImageIconConfigured("/images/Bombs/BombWin.png");
    private static final Icon emptyIcon = getImageIconConfigured("/images/Vazio.png");
    private static final Icon flagIcon = getImageIconConfigured("/images/Flag.png");
    private static final HashMap<Number, Icon> numberIcons = new HashMap<>();

    static {
        for (int num = 0; num < 8; num++) {
            numberIcons.put(num, getImageIconConfigured("/images/Numbers/" + num + ".png"));
        }
    }

    public Cell(boolean isBomb) {
        setBomb(isBomb);

        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setSize(new Dimension(CampoMinado.cellSize, CampoMinado.cellSize));
    }

    // Corrigir
    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
        resetCell();
    }

    public void resetCell() {
        setText("");
        setIcon(emptyIcon);
    }

    public void displayEmpty() {
        setIcon(emptyIcon);
    }

    public void displayFlag() {
        if (!isRevealed()) {
            setIcon(flagIcon);
        }
    }

    public void displayBomb(CampoMinado.GameStatus status) {
        if (isBomb) {
            setIcon(status == CampoMinado.GameStatus.LOSE ? bombLoseIcon : bombWinIcon);
        }
    }

    public void displayNumber(int number) {
        if (!isRevealed() && !isBomb) {
            int num = number > 0 && number < 8 ? number : 0;
            setIcon(numberIcons.get(num));
        }
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isRevealed() {
        return getIcon() != emptyIcon;
    }

    public boolean isFlagged() {
        return getIcon() == flagIcon;
    }

    public boolean isNumber() {
        return isRevealed() && !isFlagged() && getIcon() != bombLoseIcon && getIcon() != bombWinIcon;
    }

    private static ImageIcon getImageIconConfigured(String path) {
        ImageIcon icon = new ImageIcon(Cell.class.getResource(path));
        return new ImageIcon(
                icon.getImage().getScaledInstance(CampoMinado.cellSize, CampoMinado.cellSize, Image.SCALE_SMOOTH));
    }
}