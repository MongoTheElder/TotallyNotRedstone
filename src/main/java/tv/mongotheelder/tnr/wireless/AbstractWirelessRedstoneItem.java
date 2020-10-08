package tv.mongotheelder.tnr.wireless;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractWirelessRedstoneItem extends BlockItem {
    private static final String ITEM_TAG = "TNRWirelessRedstoneItem";
    private static final Logger LOGGER = LogManager.getLogger();

    public AbstractWirelessRedstoneItem(Block blockIn, Item.Properties builder) {
        super(blockIn, builder);
    }

    private void writeTargetPosTag(ItemUseContext context) {
        CompoundNBT tag = context.getItem().getOrCreateChildTag(ITEM_TAG);
        tag.put(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG, NBTUtil.writeBlockPos(context.getPos()));
    }

    private BlockPos readTargetPosTag(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateChildTag(ITEM_TAG);
        if (tag.contains(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG)) {
            CompoundNBT posTag = tag.getCompound(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG);
            return NBTUtil.readBlockPos(posTag);
        }
        return null;
    }

    private void writeFaceTag(ItemUseContext context) {
        CompoundNBT tag = context.getItem().getOrCreateChildTag(ITEM_TAG);
        tag.putString(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG, context.getFace().getName2());
    }

    private Direction readFaceTag(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateChildTag(ITEM_TAG);
        if (tag.contains(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG)) {
            return Direction.byName(tag.getString(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG));
        }
        return null;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getPlayer() == null) {
            LOGGER.error("Attempted to use an item with a null player");
            return ActionResultType.FAIL;
        }
        if (context.getPlayer().isCrouching()) {
            if (isValidSourceBlock(context.getWorld(), context.getPos())) {
                writeTargetPosTag(context);
                writeFaceTag(context);
            }
            return ActionResultType.SUCCESS;
        } else {
            ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
            return actionresulttype != ActionResultType.SUCCESS && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BlockPos targetPos = readTargetPosTag(stack);
        if (targetPos != null && worldIn != null && worldIn.isBlockPresent(targetPos) && isValidSourceBlock(worldIn, targetPos)) {
            String blockName = worldIn.getBlockState(targetPos).getBlock().getTranslatedName().getString();
            ITextComponent formattedString = new TranslationTextComponent(TotallyNotRedstone.LINKED_KEY, blockName, targetPos.getX(), targetPos.getY(), targetPos.getZ()).mergeStyle(TextFormatting.GREEN);
            tooltip.add(formattedString);
        } else {
            ITextComponent tipText = new TranslationTextComponent(TotallyNotRedstone.UNLINKED_KEY).mergeStyle(TextFormatting.RED);
            tooltip.add(tipText);
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        BlockPos targetPos = readTargetPosTag(context.getItem());
        if (targetPos == null || !isValidSourceBlock(context.getWorld(), targetPos))
            return ActionResultType.FAIL;
        return super.tryPlace(context);
    }

    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        MinecraftServer minecraftserver = worldIn.getServer();
        if (minecraftserver != null && player != null) {
            AbstractWirelessRedstoneTile tileEntity = (AbstractWirelessRedstoneTile) worldIn.getTileEntity(pos);
            BlockPos targetPos = readTargetPosTag(stack);
            Direction face = readFaceTag(stack);
            if (tileEntity != null && targetPos != null) {
                tileEntity.setSourcePos(targetPos);
                tileEntity.setFace(face);
                return true;
            } else {
                LOGGER.error(String.format("Could not locate the tile entity for block at (%d %d %d)", pos.getX(), pos.getY(), pos.getZ()));
                return false;
            }
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }

    // Is the target block at pos a valid redstone signal source
    abstract public boolean isValidSourceBlock(World world, BlockPos pos);

}
