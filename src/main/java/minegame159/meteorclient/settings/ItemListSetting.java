package minegame159.meteorclient.settings;

import minegame159.meteorclient.gui.screens.ItemListSettingScreen;
import minegame159.meteorclient.gui.widgets.WButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemListSetting extends Setting<List<Item>> {
    public ItemListSetting(String name, String description, String group, List<Item> defaultValue, Consumer<List<Item>> onChanged, Consumer<Setting<List<Item>>> onModuleActivated, boolean visible) {
        super(name, description, group, defaultValue, onChanged, onModuleActivated, visible);

        widget = new WButton("Select");
        ((WButton) widget).action = button -> MinecraftClient.getInstance().openScreen(new ItemListSettingScreen(this));
    }

    @Override
    protected List<Item> parseImpl(String str) {
        String[] values = str.split(",");
        List<Item> items = new ArrayList<>(1);

        for (String value : values) {
            String val = value.trim();
            Identifier id;
            if (val.contains(":")) id = new Identifier(val);
            else id = new Identifier("minecraft", val);
            items.add(Registry.ITEM.get(id));
        }

        return items;
    }

    @Override
    public void reset(boolean callbacks) {
        value = new ArrayList<>(defaultValue);
        if (callbacks) {
            resetWidget();
            changed();
        }
    }

    @Override
    protected void resetWidget() {

    }

    @Override
    protected boolean isValueValid(List<Item> value) {
        return true;
    }

    @Override
    protected String generateUsage() {
        return "#blueitem id #gray(dirt, minecraft:stone, etc)";
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = saveGeneral();

        ListTag valueTag = new ListTag();
        for (Item item : get()) {
            valueTag.add(new StringTag(Registry.ITEM.getId(item).toString()));
        }
        tag.put("value", valueTag);

        return tag;
    }

    @Override
    public List<Item> fromTag(CompoundTag tag) {
        get().clear();

        ListTag valueTag = tag.getList("value", 8);
        for (Tag tagI : valueTag) {
            get().add(Registry.ITEM.get(new Identifier(tagI.asString())));
        }

        changed();
        return get();
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private String group;
        private List<Item> defaultValue;
        private Consumer<List<Item>> onChanged;
        private Consumer<Setting<List<Item>>> onModuleActivated;
        private boolean visible = true;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder defaultValue(List<Item> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<List<Item>> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder onModuleActivated(Consumer<Setting<List<Item>>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public ItemListSetting build() {
            return new ItemListSetting(name, description, group, defaultValue, onChanged, onModuleActivated, visible);
        }
    }
}