import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InfoBar extends JPanel {
    public static final int DEFAULT_HEIGHT = 50;
    private static final Icon HAPPY_EMOJI = getImageIconConfigured("/images/Happy.png");
    private static final Icon SAD_EMOJI = getImageIconConfigured("/images/Sad.png");
    private static final Icon NEUTRAL_EMOJI = getImageIconConfigured("/images/Neutral.png");

    private JLabel bombsLabel;
    private JButton emojiButton;
    private JLabel timeLabel;
    private Timer timer;
    private int currentTime = 0;
    private int remainingBombs;

    public InfoBar(int numberOfBombs, CampoMinado campoMinado) {
        setupPanel();
        setupComponents(campoMinado);
        start(numberOfBombs);
    }

    private void setupPanel() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(0, 10, 0, 10));
    }

    private void setupComponents(CampoMinado campoMinado) {
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Arial", Font.BOLD, 20);

        bombsLabel = new JLabel();
        bombsLabel.setFont(font);
        gbc.anchor = GridBagConstraints.WEST;
        add(bombsLabel, gbc);

        emojiButton = new JButton();
        emojiButton.setIcon(NEUTRAL_EMOJI);
        emojiButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        emojiButton.addActionListener(e -> campoMinado.startGame());
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        add(emojiButton, gbc);

        timeLabel = new JLabel();
        timeLabel.setFont(font);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        add(timeLabel, gbc);
    }

    public void start(int numberOfBombs) {
        resetTimer();
        this.remainingBombs = numberOfBombs;
        setBombsNumber(this.remainingBombs);
        setEmoji(NEUTRAL_EMOJI);
    }

    public void endGame(CampoMinado.GameStatus status) {
        stopTimer();
        setEmoji(status == CampoMinado.GameStatus.WIN ? HAPPY_EMOJI : SAD_EMOJI);
    }

    private void resetTimer() {
        if (timer != null) {
            stopTimer();
        }
        currentTime = 0;
        setTime(currentTime);
        initTimer();
    }

    private void initTimer() {
        timer = new Timer(1000, (e) -> increaseTime());
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
    }

    private void increaseTime() {
        setTime(++currentTime);
    }

    private void setTime(int seconds) {
        timeLabel.setText(String.format("%03d", seconds));
    }

    public void reduceBombs() {
        if (remainingBombs > 0) {
            remainingBombs--;
            setBombsNumber(remainingBombs);
        }
    }

    public void increaseBombs() {
        remainingBombs++;
        setBombsNumber(remainingBombs);
    }

    private void setBombsNumber(int numberOfBombs) {
        bombsLabel.setText(String.format("%03d", numberOfBombs));
    }

    public int getRemainingBombs() {
        return remainingBombs;
    }

    private void setEmoji(Icon emoji) {
        emojiButton.setIcon(emoji);
    }

    private static Icon getImageIconConfigured(String path) {
        return new ImageIcon(new ImageIcon(InfoBar.class.getResource(path)).getImage()
                .getScaledInstance(DEFAULT_HEIGHT - 5, DEFAULT_HEIGHT - 5, Image.SCALE_SMOOTH));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, DEFAULT_HEIGHT);
    }
}