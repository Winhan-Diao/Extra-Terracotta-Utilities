package com.github.winhan.diao.compat;


import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.List;

public class HauntingDisplay extends BasicDisplay {

    public HauntingDisplay(Item input, Item output) {
        this(Collections.singletonList(EntryIngredients.of(input)), Collections.singletonList(EntryIngredients.of(output)));
    }
    public HauntingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }



    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HauntingCategory.HAUNTING;
    }
}
