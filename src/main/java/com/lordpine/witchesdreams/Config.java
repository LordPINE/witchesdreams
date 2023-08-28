package com.lordpine.witchesdreams;

import java.io.File;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String[] movesBetweenWorlds = new String[] {};
    public static String[] allowedIntoSpiritWorld = new String[] {};
    public static String[] allowedFromSpiritWorld = new String[] {};
    public static String[] spiritToNormalConversions = new String[] {};
    public static String[] normalToSpiritConversions = new String[] {};

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
        spiritToNormalConversions = configuration.getStringList(
            "spiritToNormalConversions",
            Configuration.CATEGORY_GENERAL,
            new String[] {},
            "Items in this list get converted to a different item when going from the spirit world to some other place. Format:\n"
                + "modid:itemid>modid2:itemid2\n"
                + "The second of these items is the output of the conversion.");
        normalToSpiritConversions = configuration.getStringList(
            "normalToSpiritConversions",
            Configuration.CATEGORY_GENERAL,
            new String[] {},
            "Items in this list get converted to a different item when going from the spirit world to some other place. Format:\n"
                + "modid:itemid>modid2:itemid2\n"
                + "The second of these items is the output of the conversion.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isAllowedIntoSpiritWorld(ItemStack stack) {
        return ItemHelper.isStackInArray(stack, movesBetweenWorlds)
            || ItemHelper.isStackInArray(stack, allowedIntoSpiritWorld);
    }

    public static boolean isAllowedFromSpiritWorld(ItemStack stack) {
        return ItemHelper.isStackInArray(stack, movesBetweenWorlds)
            || ItemHelper.isStackInArray(stack, allowedFromSpiritWorld);
    }

    public static ItemStack spiritWorldConversion(ItemStack stack) {
        for (String conversion : normalToSpiritConversions) {
            String[] splitConversion = conversion.split(">");
            if (ItemHelper.matchesStack(stack, splitConversion[0])) {
                ItemStack out = ItemHelper.createStackFromString(splitConversion[1]);
                out.stackSize = stack.stackSize;
                return out;
            }
        }
        return null;
    }

    public static ItemStack normalWorldConversion(ItemStack stack) {
        for (String conversion : spiritToNormalConversions) {
            String[] splitConversion = conversion.split(">");
            if (ItemHelper.matchesStack(stack, splitConversion[0])) {
                ItemStack out = ItemHelper.createStackFromString(splitConversion[1]);
                out.stackSize = stack.stackSize;
                return out;
            }
        }
        return null;
    }
}
