package com.disepi.moonlight.utils;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import com.disepi.moonlight.anticheat.Moonlight;

public class FakePlayer extends EntityHuman {

    // Used inside checks to determine how long the fake player has been active for
    public int ticks = 0;

    // Sets up the fake players compound tag (position, skin, rotations etc.)
    public FakePlayer(Position position, byte[] skinData) {
        super(position.getLevel().getChunk((int) position.getX() >> 4, (int) position.getZ() >> 4), new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", position.x)).add(new DoubleTag("", position.y)).add(new DoubleTag("", position.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0))).putCompound("Skin", new CompoundTag().putByteArray("Data", skinData).putString("ModelId", "Standard_Steve")).putString("NameTag", Util.generateRandomString(6 + Util.rnd.nextInt(6))));
        this.setNameTagAlwaysVisible(false);
        this.setNameTagVisible(false);
    }

    // Spawns the fake player if it is not spawned in already
    public void spawn(Player player) {
        if (!this.hasSpawned.containsValue(player)) {
            Moonlight.fakePlayers.add(this);
            this.spawnTo(player);
        }
    }
}