package com.finallion.villagersplus.blockentities;

import com.finallion.villagersplus.blocks.AlchemistTableBlock;
import com.finallion.villagersplus.client.screen.AlchemistTableScreenHandler;
import com.finallion.villagersplus.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AlchemistTableBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    private static final int INPUT_SLOT_INDEX = 3;
    private static final int FUEL_SLOT_INDEX = 4;
    private static final int[] TOP_SLOTS = new int[]{3};
    private static final int[] BOTTOM_SLOTS = new int[]{0, 1, 2, 3};
    private static final int[] SIDE_SLOTS = new int[]{0, 1, 2, 4};
    public static final int MAX_FUEL_USES = 20;
    public static final int BREW_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_PROPERTY_INDEX = 1;
    public static final int PROPERTY_COUNT = 2;
    private DefaultedList<ItemStack> inventory;
    private boolean[] slotsEmptyLastTick;
    private Item itemBrewing;
    int brewTime;
    int fuel;
    protected final PropertyDelegate propertyDelegate;
    public static List<ItemStack> potions = new ArrayList<>(){
        {
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HARMING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HEALING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.TURTLE_MASTER));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_FIRE_RESISTANCE));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_TURTLE_MASTER));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LONG_WEAKNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_TURTLE_MASTER));

            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WATER));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.MUNDANE));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.FIRE_RESISTANCE));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WATER_BREATHING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HARMING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HEALING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.INVISIBILITY));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.NIGHT_VISION));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SLOW_FALLING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WEAKNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.TURTLE_MASTER));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_FIRE_RESISTANCE));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_INVISIBILITY));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_NIGHT_VISION));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_SLOW_FALLING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_TURTLE_MASTER));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_WATER_BREATHING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_WEAKNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_HARMING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_HEALING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_LEAPING));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_POISON));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_REGENERATION));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_SLOWNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_STRENGTH));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_SWIFTNESS));
            add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_TURTLE_MASTER));
        }
    };


    public AlchemistTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ALCHEMIST_TABLE_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AlchemistTableBlockEntity.this.brewTime;
                    case 1:
                        return AlchemistTableBlockEntity.this.fuel;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AlchemistTableBlockEntity.this.brewTime = value;
                        break;
                    case 1:
                        AlchemistTableBlockEntity.this.fuel = value;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    protected Text getContainerName() {
        return new TranslatableText("container.alchemist_table");
    }

    public int size() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }


    public static void tick(World world, BlockPos pos, BlockState state, AlchemistTableBlockEntity blockEntity) {
        ItemStack itemStack = (ItemStack)blockEntity.inventory.get(4);
        if (blockEntity.fuel <= 0 && itemStack.isOf(Items.GUNPOWDER)) {
            blockEntity.fuel = 20;
            itemStack.decrement(1);
            markDirty(world, pos, state);
        }

        if (blockEntity.fuel <= 0) {
            state = (BlockState)state.with(AlchemistTableBlock.HAS_FUEL, false);
        } else {
            state = (BlockState)state.with(AlchemistTableBlock.HAS_FUEL, true);
        }

        boolean bl = canCraft(blockEntity.inventory);
        boolean bl2 = blockEntity.brewTime > 0;
        ItemStack itemStack2 = (ItemStack)blockEntity.inventory.get(3);

        if (!itemStack2.isEmpty()) {
            state = (BlockState)state.with(AlchemistTableBlock.BOTTLE_PROPERTIES[3], true);
        } else {
            state = (BlockState)state.with(AlchemistTableBlock.BOTTLE_PROPERTIES[3], false);
        }


        if (bl2) {
            --blockEntity.brewTime;
            boolean bl3 = blockEntity.brewTime == 0;
            state = (BlockState)state.with(AlchemistTableBlock.IS_BREWING, true);
            if (world.random.nextInt(8) == 0) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.5F);
            }

            if (bl3 && bl) {
                craft(world, pos, blockEntity.inventory);
                markDirty(world, pos, state);
            } else if (!bl || !itemStack2.isOf(blockEntity.itemBrewing)) {
                blockEntity.brewTime = 0;
                markDirty(world, pos, state);
            }
        } else if (bl && blockEntity.fuel > 0) {
            blockEntity.fuel -= 20;
            blockEntity.brewTime = 400;
            blockEntity.itemBrewing = itemStack2.getItem();
            markDirty(world, pos, state);
        }

        if (blockEntity.brewTime <= 0) {
            state = (BlockState)state.with(AlchemistTableBlock.IS_BREWING, false);
        }

        world.setBlockState(pos, state, 2);

        boolean[] bls = blockEntity.getSlotsEmpty();
        if (!Arrays.equals(bls, blockEntity.slotsEmptyLastTick)) {
            blockEntity.slotsEmptyLastTick = bls;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof AlchemistTableBlock)) {
                return;
            }

            for(int i = 0; i < AlchemistTableBlock.BOTTLE_PROPERTIES.length - 1; ++i) {
                blockState = (BlockState)blockState.with(AlchemistTableBlock.BOTTLE_PROPERTIES[i], bls[i]);
            }

            world.setBlockState(pos, blockState, 2);
        }

    }

    private boolean[] getSlotsEmpty() {
        boolean[] bls = new boolean[3];

        for(int i = 0; i < 3; ++i) {
            if (!((ItemStack)this.inventory.get(i)).isEmpty()) {
                bls[i] = true;
            }
        }

        return bls;
    }

    private static boolean canCraft(DefaultedList<ItemStack> slots) {
        ItemStack itemStack = (ItemStack)slots.get(3);
        if (itemStack.isEmpty()) {
            return false;
        } else if (!(itemStack.isOf(Items.LINGERING_POTION) || itemStack.isOf(Items.SPLASH_POTION) || itemStack.isOf(Items.POTION))) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                ItemStack itemStack2 = (ItemStack)slots.get(i);
                if (!itemStack2.isEmpty() /*&& BrewingRecipeRegistry.hasRecipe(itemStack2, itemStack)*/) {
                    return true;
                }
            }

            return false;
        }
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        // ingredient slot
        ItemStack itemStack = (ItemStack)slots.get(3);

        if (world.getRandom().nextInt(3) == 0) {
            for(int i = 0; i < 3; ++i) {
                if (!slots.get(i).isEmpty()) {
                    slots.set(i, potions.get(world.random.nextInt(potions.size())));
                }
            }
        } else {
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3.0F, Explosion.DestructionType.NONE);
            slots.set(0, ItemStack.EMPTY);
            slots.set(1, ItemStack.EMPTY);
            slots.set(2, ItemStack.EMPTY);
            slots.set(4, ItemStack.EMPTY);
        }

        itemStack.decrement(1);

        slots.set(3, itemStack);
        world.syncWorldEvent(1035, pos, 0);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.brewTime = nbt.getShort("BrewTime");
        this.fuel = nbt.getByte("Fuel");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BrewTime", (short)this.brewTime);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putByte("Fuel", (byte)this.fuel);
    }

    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? (ItemStack)this.inventory.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }

    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 3) {
            return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.GLASS_BOTTLE);
        } else if (slot == 4) { // fuel
            return stack.isOf(Items.GUNPOWDER);
        } else {
            return stack.isOf(Items.GLASS_BOTTLE) && this.getStack(slot).isEmpty();
        }
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot != 3 || stack.isOf(Items.GLASS_BOTTLE);
    }

    public void clear() {
        this.inventory.clear();
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AlchemistTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
