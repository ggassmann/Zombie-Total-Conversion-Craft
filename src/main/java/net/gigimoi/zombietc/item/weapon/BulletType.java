package net.gigimoi.zombietc.item.weapon;

import net.gigimoi.zombietc.WeaponLoader;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

/**
 * Created by gigimoi on 8/15/2014.
 */
public class BulletType {
    public static void init() {
        new BulletTypeDefault("smallLead", 1);
        new BulletTypeDefault("mediumLead", 2);
        new BulletTypeDefault("largeLead", 3);
        new BulletTypeDefault("mediumBolt", 4); //TODO: Bolts should pierce through zombies, losing 1/2 damage a piece
        new BulletTypeDefault("mediumBolt", 5);
        new BulletType("goorand") {
            @Override
            public void onHit(EntityPlayer attacker, EntityZZombie attacked) {
                attacked.attackEntityFrom(DamageSource.generic, 3);
                PotionEffect effect = new PotionEffect(Potion.moveSlowdown.getId(), 10, 3, false); //TODO: Zombies should pay attention to slowing effects when walking
                if(attacked.isPotionApplicable(effect)) {
                    attacked.addPotionEffect(effect);
                }
            }
        };
    }

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
