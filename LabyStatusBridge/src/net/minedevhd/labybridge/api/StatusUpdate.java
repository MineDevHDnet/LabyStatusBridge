package net.minedevhd.labybridge.api;

public final class StatusUpdate {

    private final String gameMode;
    private final boolean discordEnabled;
    private final String discordMode;
    private final String discordText;
    private final long discordOffsetSeconds;

    public StatusUpdate(String gameMode, boolean discordEnabled, String discordMode, String discordText, long discordOffsetSeconds) {
        this.gameMode = gameMode;
        this.discordEnabled = discordEnabled;
        this.discordMode = discordMode;
        this.discordText = discordText;
        this.discordOffsetSeconds = discordOffsetSeconds;
    }

    public String getGameMode() {
        return gameMode;
    }

    public boolean isDiscordEnabled() {
        return discordEnabled;
    }

    public String getDiscordMode() {
        return discordMode;
    }

    public String getDiscordText() {
        return discordText;
    }

    public long getDiscordOffsetSeconds() {
        return discordOffsetSeconds;
    }

    public static StatusUpdate simple(String gameMode, String discordText) {
        return new StatusUpdate(gameMode, true, "simple", discordText, 0L);
    }

    public static StatusUpdate withStart(String gameMode, String discordText) {
        return new StatusUpdate(gameMode, true, "with_start", discordText, 0L);
    }

    public static StatusUpdate disabled(String gameMode) {
        return new StatusUpdate(gameMode, false, "reset", "", 0L);
    }
}