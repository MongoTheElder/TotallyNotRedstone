package tv.mongotheelder.tnr.misc;

import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;

public enum SolidColors {
    WHITE("solid_colors/white", "solid_colors/gray", Tags.Items.GLASS_PANES_WHITE),
    RED("solid_colors/red", "solid_colors/dark_red", Tags.Items.GLASS_PANES_RED),
    BLUE("solid_colors/blue", "solid_colors/dark_blue", Tags.Items.GLASS_PANES_BLUE),
    GREEN("solid_colors/green", "solid_colors/dark_green", Tags.Items.GLASS_PANES_GREEN),
    MAGENTA("solid_colors/magenta", "solid_colors/dark_magenta", Tags.Items.GLASS_PANES_MAGENTA),
    PINK("solid_colors/pink", "solid_colors/dark_pink", Tags.Items.GLASS_PANES_PINK),
    CYAN("solid_colors/cyan", "solid_colors/dark_cyan", Tags.Items.GLASS_PANES_CYAN),
    YELLOW("solid_colors/yellow", "solid_colors/dark_yellow", Tags.Items.GLASS_PANES_YELLOW),
    ORANGE("solid_colors/orange", "solid_colors/dark_orange", Tags.Items.GLASS_PANES_ORANGE),
    PURPLE("solid_colors/purple", "solid_colors/dark_purple", Tags.Items.GLASS_PANES_PURPLE);

    String primary;
    String secondary;
    Tag<Item> pane;

    SolidColors(String primary, String secondary, Tag<Item> pane) {
        this.primary = primary;
        this.secondary = secondary;
        this.pane = pane;
    }

    public String getPrimary() { return primary; }
    public String getSecondary() { return secondary; }
    public Tag<Item> getPane() { return pane; }

    public SolidColors getByPrimaryColor(String color) {
        for(SolidColors c: SolidColors.values()) {
            if (c.primary.equals(color)) return c;
        }
        return WHITE;
    }
}

