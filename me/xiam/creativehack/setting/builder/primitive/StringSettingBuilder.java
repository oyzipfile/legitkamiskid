// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.setting.builder.primitive;

import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.setting.impl.StringSetting;
import me.xiam.creativehack.setting.builder.SettingBuilder;

public class StringSettingBuilder extends SettingBuilder<String>
{
    @Override
    public StringSetting build() {
        return new StringSetting((String)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
}
