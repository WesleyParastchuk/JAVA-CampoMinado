public abstract class DifficultyConfigFactory {
    public static DifficultyConfig createConfig(CampoMinado.DifficultyLevel difficultyLevel) {
        DifficultyConfig config = new DifficultyConfig();
        switch (difficultyLevel) {
            case EASY:
                config.setBombs(10);
                config.setRows(9);
                config.setColumns(9);
                break;
            case MEDIUM:
                config.setBombs(40);
                config.setRows(16);
                config.setColumns(16);
                break;
            case HARD:
                config.setBombs(99);
                config.setRows(16);
                config.setColumns(30);
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficultyLevel);
        }
        return config;
    }
}