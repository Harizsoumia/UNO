public class PlayerConfig {
    private int humanPlayers;
    private int bots;
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;

    public PlayerConfig() {
        this.humanPlayers = 0;
        this.bots = 0;
    }

    public int getHumanPlayers() {
        return humanPlayers;
    }

    public int getBots() {
        return bots;
    }

    public int getTotalPlayers() {
        return humanPlayers + bots;
    }

    public void incrementHumanPlayers() {
        if (getTotalPlayers() < MAX_PLAYERS) {
            this.humanPlayers++;
        }
    }

    public void decrementHumanPlayers() {
        if (humanPlayers > 0) {
            this.humanPlayers--;
        }
    }

    public void incrementBots() {
        if (getTotalPlayers() < MAX_PLAYERS) {
            this.bots++;
        }
    }

    public void decrementBots() {
        if (bots > 0) {
            this.bots--;
        }
    }
}
