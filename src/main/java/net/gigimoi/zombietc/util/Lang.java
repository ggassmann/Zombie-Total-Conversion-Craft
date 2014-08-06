package net.gigimoi.zombietc.util;

import net.minecraft.client.resources.I18n;

/**
 * Created by gigimoi on 7/28/2014.
 */
public class Lang {
    public static String get(String in) {
        return I18n.format(in, new Object[0]);
    }
}
