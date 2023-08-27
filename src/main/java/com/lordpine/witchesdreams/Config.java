package com.lordpine.witchesdreams;

import java.io.File;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class Config {

    public static String[] movesBetweenWorlds = new String[] {};
    public static String[] allowedIntoSpiritWorld = new String[] {};
    public static String[] allowedFromSpiritWorld = new String[] {};

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        movesBetweenWorlds = configuration.getStringList(
            "movesBetweenWorlds",
            Configuration.CATEGORY_GENERAL,
            new String[] {},
            "What items can move freely between the normal and spirit world?");
        allowedIntoSpiritWorld = configuration.getStringList(
            "allowedIntoSpiritWorld",
            Configuration.CATEGORY_GENERAL,
            new String[] {},
            "What items are allowed into the spirit world?");
        allowedFromSpiritWorld = configuration.getStringList(
            "allowedFromSpiritWorld",
            Configuration.CATEGORY_GENERAL,
            new String[] {},
            "What items are allowed to return from the spirit world?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isAllowedIntoSpiritWorld(ItemStack stack) {
        return isStackInArray(stack, movesBetweenWorlds) || isStackInArray(stack, allowedIntoSpiritWorld);
    }

    public static boolean isAllowedFromSpiritWorld(ItemStack stack) {
        return isStackInArray(stack, movesBetweenWorlds) || isStackInArray(stack, allowedFromSpiritWorld);
    }

    private static boolean isStackInArray(ItemStack stack, String[] itemStrings) {
        for (String itemString : itemStrings) {
            itemString = itemString.replace("\"", "");
            String[] splitString = itemString.split(":");
            if (splitString.length < 2 || splitString.length > 3) {
                WitchesDreams.LOG.warn(itemString + " is not a valid item ID, ignoring!");
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
                        if (splitString[2] == "*") return true;
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
        }
        return false;
    }
}
