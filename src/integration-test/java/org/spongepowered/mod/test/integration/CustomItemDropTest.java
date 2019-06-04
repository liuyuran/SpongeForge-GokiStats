package org.spongepowered.mod.test.integration;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.mctester.api.junit.MinecraftRunner;
import org.spongepowered.mctester.internal.BaseTest;
import org.spongepowered.mctester.internal.McTesterDummy;
import org.spongepowered.mctester.internal.event.StandaloneEventListener;
import org.spongepowered.mctester.junit.TestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

@RunWith(MinecraftRunner.class)
public class CustomItemDropTest extends BaseTest {

    private ItemType EGG_TOSS;

    public CustomItemDropTest(TestUtils testUtils) {
        super(testUtils);
    }

    @BeforeClass
    public static void prepare() {
        // TODO Is this even allowed?
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        final EggItem item = new EggItem();
        item.setCreativeTab(CreativeTabs.FOOD);
        item.setRegistryName(new ResourceLocation(McTesterDummy.INSTANCE.getModId(), "dropitem"));
        event.getRegistry().register(item);
        final Optional<ItemType> type = Sponge.getRegistry().getType(ItemType.class, this.container.getModId() + ":" + "dropitem");
        type.ifPresent(itemType -> this.EGG_TOSS = itemType);
    }

    @Test
    public void test() throws Throwable {
        this.testUtils.listen(new StandaloneEventListener<>(DropItemEvent.class, event -> {
            if (event instanceof DropItemEvent.Pre) {
                return;
            }

            event.setCancelled(true);
        }));

        this.testUtils.listen(new StandaloneEventListener<>(DestructEntityEvent.Death.class, event -> {
            event.setCancelled(true);
            final Living targetEntity = event.getTargetEntity();
            targetEntity.offer(Keys.HEALTH, targetEntity.require(Keys.MAX_HEALTH));
        }));

        // TODO This needs to run before modifications. How?
        this.testUtils.listen(new StandaloneEventListener<>(DropItemEvent.Dispense.class, event -> {
            if (!(event.getCause().root() instanceof Player)) {
                return;
            }

            final Player player = (Player) event.getCause().root();

            final List<org.spongepowered.api.entity.Item> collections = event.getEntities()
                .stream()
                .filter(entity -> entity instanceof org.spongepowered.api.entity.Item)
                .map(entity -> (org.spongepowered.api.entity.Item) entity)
                .filter(itemEntity -> itemEntity.getItemType().equals(this.EGG_TOSS))
                .collect(Collectors.toList());
            if (!collections.isEmpty()) {
                final Entity entity = player.getWorld().createEntity(EntityTypes.CREEPER, player.getPosition());
                event.getEntities().add(entity);
            }
        }));
    }

    public static class EggItem extends ItemEgg {

        @Override
        @Nullable
        public net.minecraft.entity.Entity createEntity(World world, net.minecraft.entity.Entity location, ItemStack itemstack) {
            EntityChicken entity = new EntityChicken(world);
            entity.copyLocationAndAnglesFrom(location);
            return entity;
        }

        @Override
        public boolean hasCustomEntity(ItemStack stack) {
            return true;
        }
    }
}
