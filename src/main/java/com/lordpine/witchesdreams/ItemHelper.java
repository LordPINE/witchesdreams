package com.lordpine.witchesdreams;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemHelper {

    private ItemHelper() {}

    public static boolean isStackInArray(ItemStack stack, String[] itemStrings) {
        for (String itemString : itemStrings) {
            if (matchesStack(stack, itemString)) return true;
        }
        return false;
    }

    public static boolean matchesStack(ItemStack stack, String stackString) {
        stackString = stackString.replace("\"", "");
        String[] splitString = stackString.split(":");
        if (splitString.length < 2 || splitString.length > 3) {
            WitchesDreams.LOG.warn(stackString + " is not a valid item ID, ignoring!");
            return false;
        }
        // ore dictionary handling
        if (splitString[0] == "ore" && OreDictionary.doesOreNameExist(splitString[1])) {
            for (int ID : OreDictionary.getOreIDs(stack)) {
                if (OreDictionary.getOreID(splitString[1]) == ID) {
                    return true;
                }
            }
        } else {
            Item item = GameRegistry.findItem(splitString[0], splitString[1]);
            if (stack.getItem()
                .equals(item)) {
                if (splitString.length == 3) {
                    if (splitString[2].equals("*")) return true;
                    try {
                        if (Integer.parseInt(splitString[2]) == stack.getItemDamage()) {
                            return true;
                        }
                    } catch (Exception ignored) {}
                } else if (stack.getItemDamage() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack createStackFromString(String stackString) {
        String[] splitString = stackString.split(":");
        if (splitString.length < 2 || splitString.length > 3) {
            WitchesDreams.LOG.warn(stackString + " is not a valid item ID, ignoring!");
            return null;
        }
        Item item = GameRegistry.findItem(splitString[0], splitString[1]);
        ItemStack stack = new ItemStack(item);
        if (splitString.length == 3) {
            try {
                stack.setItemDamage(Integer.parseInt(splitString[2]));
            } catch (Exception ignored) {}
        }
        return stack;
    }
}
