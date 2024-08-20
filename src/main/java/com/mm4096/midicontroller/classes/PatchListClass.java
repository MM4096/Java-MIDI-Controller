package com.mm4096.midicontroller.classes;

import java.util.Arrays;

public class PatchListClass {
    private String name;
    private PatchClass[] patchList;
    private ConfigClass[] configList;

    public PatchListClass(String name, PatchClass[] patchList, ConfigClass[] configList) {
        this.name = name;
        this.patchList = patchList;
        this.configList = configList;
    }

    public PatchListClass() {
    }

    public PatchClass[] getPatchList() {
        return patchList;
    }

    public ConfigClass[] getConfigList() {
        return configList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatchList(PatchClass[] patchList) {
        this.patchList = patchList;
    }

    public void setConfigList(ConfigClass[] configList) {
        this.configList = configList;
    }

    @Override
    public String toString() {
        return "PatchListClass{" +
                "name='" + name + '\'' +
                ", patchList=" + Arrays.toString(patchList) +
                ", configList=" + Arrays.toString(configList) +
                '}';
    }
}
