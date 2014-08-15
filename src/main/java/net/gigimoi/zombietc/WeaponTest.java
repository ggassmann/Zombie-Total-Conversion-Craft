package net.gigimoi.zombietc;

import com.google.gson.Gson;
import net.gigimoi.zombietc.item.weapon.WeaponData;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by gigimoi on 8/15/2014.
 */
public class WeaponTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(new WeaponData[]{new WeaponData()}));
        try {
            BufferedInputStream stream = (BufferedInputStream)ZombieTC.class.getResource("/assets/zombietc/weapons.json").getContent();
            String weaponInfo = IOUtils.toString(stream, "UTF-8");
            System.out.println(weaponInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
