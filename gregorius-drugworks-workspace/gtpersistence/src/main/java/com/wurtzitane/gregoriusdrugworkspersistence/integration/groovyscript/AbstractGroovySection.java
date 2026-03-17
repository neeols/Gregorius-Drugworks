package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.cleanroommc.groovyscript.api.IScriptReloadable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class AbstractGroovySection implements IScriptReloadable {

    private final String name;
    private final Collection<String> aliases;

    protected AbstractGroovySection(String name, String... aliases) {
        this.name = name;

        List<String> values = new ArrayList<>();
        values.add(name);
        if (aliases != null) {
            Collections.addAll(values, aliases);
        }
        this.aliases = Collections.unmodifiableList(values);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public void onReload() {
    }

    @Override
    public void afterScriptLoad() {
    }
}
