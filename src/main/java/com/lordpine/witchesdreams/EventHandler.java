package com.lordpine.witchesdreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onChangeDimension(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer && !event.world.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entity;
            WitchesDreams.LOG.info(player.inventory.getFirstEmptyStack());
            if (player.dimension == com.emoniph.witchery.util.Config.instance().dimensionDreamID) {
                NBTTagList inventoryList = getOverworldInventory(player);
                NBTTagList newInventoryList = (NBTTagList) inventoryList.copy();
                int removedItems = 0;
                for (int i = 0; i < inventoryList.tagCount(); i++) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(inventoryList.getCompoundTagAt(i));
                    // Prevent trying to add more items than fit in an inventory
                    if (player.inventory.getFirstEmptyStack() == -1) break;
                    ItemStack conversion = Config.spiritWorldConversion(stack);
                    if (Config.isAllowedIntoSpiritWorld(stack) && player.inventory.addItemStackToInventory(stack)) {
                        newInventoryList.removeTag(i - removedItems);
                        removedItems++;
                    } else if (conversion != null && player.inventory.addItemStackToInventory(conversion)) {
                        newInventoryList.removeTag(i - removedItems);
                        removedItems++;
                    }
                }
                setOverworldInventory(player, newInventoryList);
            } else {
                NBTTagList inventoryList = getSpiritWorldInventory(player);
                NBTTagList newInventoryList = (NBTTagList) inventoryList.copy();
                int removedItems = 0;
                for (int i = 0; i < inventoryList.tagCount(); i++) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(inventoryList.getCompoundTagAt(i));
                    // Prevent trying to add more items than fit in an inventory
                    if (player.inventory.getFirstEmptyStack() == -1) break;
                    ItemStack conversion = Config.normalWorldConversion(stack);
                    if (Config.isAllowedFromSpiritWorld(stack) && player.inventory.addItemStackToInventory(stack)) {
                        newInventoryList.removeTag(i - removedItems);
                        removedItems++;
                    } else if (conversion != null && player.inventory.addItemStackToInventory(conversion)) {
                        newInventoryList.removeTag(i - removedItems);
                        removedItems++;
                    }
                }
                setSpiritWorldInventory(player, newInventoryList);
            }
        }
    }

    private static NBTTagList getOverworldInventory(EntityPlayer player) {
        return player.getEntityData()
            .getCompoundTag("PlayerPersisted")
            .getCompoundTag("WITCSpiritWorld")
            .getTagList("OverworldInventory", 10);
    }

    private static void setOverworldInventory(EntityPlayer player, NBTTagList inventoryList) {
        player.getEntityData()
            .getCompoundTag("PlayerPersisted")
            .getCompoundTag("WITCSpiritWorld")
            .setTag("OverworldInventory", inventoryList);;
    }

    private static NBTTagList getSpiritWorldInventory(EntityPlayer player) {
        return player.getEntityData()
            .getCompoundTag("PlayerPersisted")
            .getCompoundTag("WITCSpiritWorld")
            .getTagList("SpiritInventory", 10);
    }

    private static void setSpiritWorldInventory(EntityPlayer player, NBTTagList inventoryList) {
        player.getEntityData()
            .getCompoundTag("PlayerPersisted")
            .getCompoundTag("WITCSpiritWorld")
            .setTag("SpiritInventory", inventoryList);;
    }
}
