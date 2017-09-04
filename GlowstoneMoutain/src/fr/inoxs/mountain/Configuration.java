package fr.inoxs.mountain;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
/*
    @Author : CTRL
 */
    private GlowstoneMountain mountain;

    public Configuration(GlowstoneMountain mountain) {
        this.mountain = mountain;
    }

    public String MOUNTAIN_SPAWN_MESSAGE = "";
    public String FORCESTART_MESSAGE = "";
    public String INFO_MESSAGE = "";
    public void setupConfig(FileConfiguration config){
        MOUNTAIN_SPAWN_MESSAGE = config.getString("MOUNTAIN_SPAWN_MESSAGE").replace('&','§');
        FORCESTART_MESSAGE = config.getString("FORCESTART_MESSAGE").replace('&','§');
        INFO_MESSAGE = config.getString("INFO_MESSAGE").replace('&','§');
    }

}
