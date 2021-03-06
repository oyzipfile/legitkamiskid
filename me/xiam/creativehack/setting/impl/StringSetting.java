// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.converter.StringConverter;
import me.xiam.creativehack.setting.Setting;

public class StringSetting extends Setting<String>
{
    private static final StringConverter converter;
    
    public StringSetting(final String value, final Predicate<String> restriction, final BiConsumer<String, String> consumer, final String name, final Predicate<String> visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }
    
    @Override
    public StringConverter converter() {
        return StringSetting.converter;
    }
    
    @Override
    public String getValueAsString() {
        return this.getValue();
    }
    
    @Override
    public void setValueFromString(final String s) {
        this.setValue(s);
    }
    
    static {
        converter = new StringConverter();
    }
}
