package com.scribassu.scribabot.services.startup;

import com.scribassu.scribabot.keyboard.Keyboard;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.services.CustomFileReader;
import com.scribassu.scribabot.services.SymbolConverter;
import com.scribassu.scribabot.util.DepartmentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupEvents implements ApplicationListener<ContextRefreshedEvent> {

    private final SymbolConverter symbolConverter;
    private final CustomFileReader customFileReader;

    @Value("${scriba-bot.vk.keyboards-folder}")
    private String keyboardsFolder;

    @Autowired
    public StartupEvents(SymbolConverter symbolConverter,
                         CustomFileReader customFileReader) {
        this.symbolConverter = symbolConverter;
        this.customFileReader = customFileReader;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        for (KeyboardType k : KeyboardType.values()) {
            KeyboardMap.keyboards.put(
                    k,
                    new Keyboard(
                            k.toString(),
                            symbolConverter.toOneLine(customFileReader.readAsString(keyboardsFolder + k.getFilename()))
                    )
            );
        }

        DepartmentConverter.add("биолог", "bf");
        DepartmentConverter.add("географ", "gf");
        DepartmentConverter.add("геолог", "gl");
        DepartmentConverter.add("идпо", "idpo");
        DepartmentConverter.add("и-т искусств", "ii");
        DepartmentConverter.add("ииимо", "imo");
        DepartmentConverter.add("и-т физики", "ff");
        DepartmentConverter.add("и-т физ кул", "ifk");
        DepartmentConverter.add("ифиж", "ifg");
        DepartmentConverter.add("и-т химии", "ih");
        DepartmentConverter.add("мехмат", "mm");
        DepartmentConverter.add("фияил", "fi");
        DepartmentConverter.add("книит", "knt");
        DepartmentConverter.add("фнбмт", "fn");
        DepartmentConverter.add("фнп", "fnp");
        DepartmentConverter.add("психолог", "fps");
        DepartmentConverter.add("пписо", "fppso");
        DepartmentConverter.add("философ", "fp");
        DepartmentConverter.add("эконом", "ef");
        DepartmentConverter.add("юрфак", "uf");
        DepartmentConverter.add("соцфак", "sf");
        DepartmentConverter.add("геолог к-ж", "kgl");
        DepartmentConverter.add("к-ж яблочкова", "cre");
    }
}
