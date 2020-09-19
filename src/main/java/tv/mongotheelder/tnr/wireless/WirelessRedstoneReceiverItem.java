package tv.mongotheelder.tnr.wireless;

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

import javax.annotation.Nullable;
import java.util.List;

public class WirelessRedstoneReceiverItem extends BlockItem {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos targetPos;

    public WirelessRedstoneReceiverItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getPlayer().isCrouching()) {
            if (WirelessRedstoneReceiverTile.isValidSourceBlock(context.getWorld(), context.getPos())) {
                targetPos = context.getPos();
            }
            return ActionResultType.SUCCESS;
        } else {
            ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
            return actionresulttype != ActionResultType.SUCCESS && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
        }
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        if (targetPos == null || !WirelessRedstoneReceiverTile.isValidSourceBlock(context.getWorld(), targetPos))
            return ActionResultType.FAIL;
        return super.tryPlace(context);
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        MinecraftServer minecraftserver = worldIn.getServer();
        if (minecraftserver != null && player != null) {
            //CompoundNBT nbt = stack.getOrCreateTag();
            //BlockPos blockPos = nbt.contains(JunkDrawer.WIRELESS_REDSTONE_RECEIVER_TAG) ? NBTUtil.readBlockPos(nbt.getCompound(JunkDrawer.WIRELESS_REDSTONE_RECEIVER_TAG)) : new BlockPos(0, 0, 0);
            WirelessRedstoneReceiverTile tileEntity = (WirelessRedstoneReceiverTile) worldIn.getTileEntity(pos);
            if (tileEntity != null) {
                tileEntity.setSourcePos(targetPos);
            } else {
                LOGGER.error(String.format("Could not locate the tile entity for block at (%d %d %d)", pos.getX(), pos.getY(), pos.getZ()));
            }
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (targetPos != null && WirelessRedstoneReceiverTile.isValidSourceBlock(worldIn, targetPos)) {
            String blockType = worldIn.getBlockState(targetPos).getBlock().getTranslationKey();
            TranslationTextComponent blockName = new TranslationTextComponent(blockType);
            String newTip = String.format("Linked to %s at %d %d %d", blockName.getUnformattedComponentText(), targetPos.getX(), targetPos.getY(), targetPos.getZ());
            tooltip.add(new StringTextComponent(newTip).applyTextStyle(TextFormatting.GREEN));
        } else {
            tooltip.add(new StringTextComponent("Not linked").applyTextStyle(TextFormatting.RED));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
