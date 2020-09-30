package tv.mongotheelder.tnr.receivers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.wireless.AbstractWirelessRedstoneItem;
import tv.mongotheelder.tnr.wireless.AbstractWirelessRedstoneTile;

import javax.annotation.Nullable;
import java.util.List;

public class WirelessRedstoneReceiverItem extends AbstractWirelessRedstoneItem {
    private static final Logger LOGGER = LogManager.getLogger();

    public WirelessRedstoneReceiverItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public boolean isValidSourceBlock(World world, BlockPos pos) {
        return WirelessRedstoneReceiverTile.isValidSourceBlock(world, pos);
    }
}
