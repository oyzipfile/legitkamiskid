// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.setting.builder.primitive;

import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.setting.impl.BooleanSetting;
import me.xiam.creativehack.setting.builder.SettingBuilder;

public class BooleanSettingBuilder extends SettingBuilder<Boolean>
{
    @Override
    public BooleanSetting build() {
        return new BooleanSetting((Boolean)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
    
    @Override
    public BooleanSettingBuilder withName(final String name) {
        return (BooleanSettingBuilder)super.withName(name);
    }
}
