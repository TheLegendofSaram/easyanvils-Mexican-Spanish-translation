package fuzs.easyanvils.world.inventory;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public interface AnvilMenuState {

    void init(ItemStack leftInput, ItemStack rightInput, String itemName);

    void createResult();

    ItemStack getLeftInput();

    ItemStack getRightInput();

    ItemStack getResult();

    int getRepairItemCountCost();

    @Nullable
    String getItemName();

    int getCost();

    static boolean equals(AnvilMenuState o1, AnvilMenuState o2) {
        if (o1.getCost() != o2.getCost()) return false;
        if (o1.getRepairItemCountCost() != o2.getRepairItemCountCost()) return false;
        if (!StringUtils.equals(o1.getItemName(), o2.getItemName())) return false;
        if (!ItemStack.tagMatches(o1.getResult(), o2.getResult())) return false;
        if (!ItemStack.tagMatches(o1.getLeftInput(), o2.getLeftInput())) return false;
        return ItemStack.tagMatches(o1.getRightInput(), o2.getRightInput());
    }
}
