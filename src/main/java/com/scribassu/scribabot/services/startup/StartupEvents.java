package com.scribassu.scribabot.services.startup;

import com.scribassu.scribabot.util.DepartmentConverter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupEvents implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
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
