package com.mm4096.midicontroller.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mm4096.midicontroller.classes.ConfigClass;
import com.mm4096.midicontroller.classes.PatchClass;
import com.mm4096.midicontroller.classes.PatchListClass;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Compiler {

    public enum CompilerType {
        PATCH,
        CONFIG,
        PATCHLIST,
    }


    private ConfigClass[] configs;
    private PatchClass[] patchList;

    private final CompilerType type;

    public Compiler(ConfigClass[] configs) {
        this.configs = configs;
        this.type = CompilerType.CONFIG;
    }

    public Compiler(ConfigClass[] configs, PatchClass[] patchList) {
        this.configs = configs;
        this.patchList = patchList;
        this.type = CompilerType.PATCH;
    }

    public Compiler(PatchListClass list) {
        this.configs = list.getConfigList();
        this.patchList = list.getPatchList();
        this.type = CompilerType.PATCH;
    }

    public Compiler(PatchClass[] patchList) {
        this.type = CompilerType.PATCHLIST;
        this.patchList = patchList;
    }

    public String compile() throws CompileException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ConfigClass.class, new ConfigClassSerializer());
        objectMapper.registerModule(module);

        switch (type) {
            case CONFIG -> {
                try {
                    return objectMapper.writeValueAsString(configs);
                }
                catch (JsonProcessingException e) {
                    throw new CompileException("Error parsing config: JSON Parse Error", 0);
                }
            }
            case PATCH -> {
                System.out.println("This isn't a thing yet?");
                return "";
            }
            case PATCHLIST -> {
                Map<String, PatchClass[]> map = new HashMap<>();
                map.put("list", patchList);
                try {
                    return objectMapper.writeValueAsString(map);
                }
                catch (JsonProcessingException e) {
                    throw new CompileException("Error parsing patch list: JSON Parse Error", 0);
                }
            }
            default -> {
                System.out.println("Something wasn't supported!");
                return "";
            }
        }
    }
}
