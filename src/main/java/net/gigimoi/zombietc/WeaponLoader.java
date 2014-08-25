package net.gigimoi.zombietc;

import net.gigimoi.zombietc.item.weapon.BulletType;
import net.gigimoi.zombietc.item.weapon.FireMechanism;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.gigimoi.zombietc.item.weapon.WeaponData;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gigimoi on 8/15/2014.
 */
public class WeaponLoader {
    private static ArrayList<WeaponData> weaponDatas;
    public static HashMap<String, BulletType> bulletTypes = new HashMap<String, BulletType>();
    public static HashMap<String, FireMechanism> fireMechanisms = new HashMap<String, FireMechanism>();

    public static void init() {
        String weaponInfo = "-";
        try {
            BufferedInputStream stream = (BufferedInputStream)ZombieTC.class.getResource("/assets/zombietc/weapons.json").getContent();
            weaponInfo = IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(weaponInfo.equals("-")) {
            System.out.println("Error loading weaponDatas");
            return;
        }
        Yaml yaml = new Yaml();
        weaponDatas = (ArrayList<WeaponData>)yaml.load(weaponInfo);
    }
    public static void registerWeapon(String name) {
        WeaponData weaponData = null;
        for(int i = 0; i < weaponDatas.size(); i++) {
            WeaponData data = weaponDatas.get(i);
            if(data.name.equals(name)) {
                weaponData = data;
            }
        }
        if(weaponData == null) {
            System.out.println("Could not find weapon with name " + name);
            return;
        }
        BulletType bulletType = bulletTypes.get(weaponData.bulletType);
        FireMechanism fireMechanism = fireMechanisms.get(weaponData.fireMechanism);

        ItemWeapon weapon = new ItemWeapon(
                name,
                fireMechanism,
                weaponData.inventoryScale,
                weaponData.adsLift,
                weaponData.clipSize,
                weaponData.initialAmmo,
                weaponData.reloadTime,
                weaponData.fireDelay
        ).barrelLength(weaponData.barrelLength
        ).sightHeight(weaponData.sightHeight
        );
        weapon.inaccuracy = weaponData.inaccuracy;
        weapon.bulletsFired = weaponData.bulletsFired;
        weapon.bulletType = bulletType;
        ZombieTC.instance.registerItem(weapon, false);
    }
    public static void register() {
        BulletType.init();
        FireMechanism.init();
        init();
        for(int i = 0; i < weaponDatas.size(); i++) {
            registerWeapon(weaponDatas.get(i).name);
        }
    }
}
