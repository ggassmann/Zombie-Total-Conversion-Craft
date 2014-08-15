package net.gigimoi.zombietc.item.weapon;

import net.gigimoi.zombietc.WeaponLoader;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

/**
 * Created by gigimoi on 8/15/2014.
 */
public class BulletType {
    public static void init() {
        smallLead = new BulletTypeDefault("smallLead", 1);
        mediumLead = new BulletTypeDefault("mediumLead", 2);
        largeLead = new BulletTypeDefault("largeLead", 3);
        smallBolt = new BulletTypeDefault("mediumBolt", 4); //TODO: Bolts should pierce through zombies, losing 1/2 damage a piece
        mediumBolt = new BulletTypeDefault("mediumBolt", 5);
    }
    public static BulletType smallLead;
    public static BulletType mediumLead;
    public static BulletType largeLead;
    public static BulletType smallBolt;
    public static BulletType mediumBolt;

    public BulletType(String id) {
        WeaponLoader.bulletTypes.put(id, this);
    }

    public void onHit(EntityPlayer attacker, EntityZZombie attacked) { }

    public static class BulletTypeDefault extends BulletType {
        public int damage;
        public BulletTypeDefault(String id, int damage) {
            super(id);
            this.damage = damage;
        }
        @Override
        public void onHit(EntityPlayer attacker, EntityZZombie attacked) {
            attacked.attackEntityFrom(DamageSource.generic, damage);
        }
    }
}
