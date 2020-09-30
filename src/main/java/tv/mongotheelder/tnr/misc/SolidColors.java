package tv.mongotheelder.tnr.misc;

public enum SolidColors {
    WHITE("solid_colors/white", "solid_colors/gray"),
    RED("solid_colors/red", "solid_colors/dark_red"),
    BLUE("solid_colors/blue", "solid_colors/dark_blue"),
    GREEN("solid_colors/green", "solid_colors/dark_green"),
    MAGENTA("solid_colors/magenta", "solid_colors/dark_magenta"),
    PINK("solid_colors/pink", "solid_colors/dark_pink"),
    CYAN("solid_colors/cyan", "solid_colors/dark_cyan"),
    YELLOW("solid_colors/yellow", "solid_colors/dark_yellow"),
    ORANGE("solid_colors/orange", "solid_colors/dark_orange"),
    PURPLE("solid_colors/purple", "solid_colors/dark_purple");

    String primary;
    String secondary;

    SolidColors(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public String getPrimary() { return primary; }
    public String getSecondary() { return secondary; }

    public SolidColors getByPrimaryColor(String color) {
        for(SolidColors c: SolidColors.values()) {
            if (c.primary.equals(color)) return c;
        }
        return WHITE;
    }
}

