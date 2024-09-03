package com.mm4096.midicontroller.classes;

import java.util.Arrays;

/**
 * Class to store a list of patches and configs, mainly for writing to disk
 */
public class PatchListClass {
    /**
     * Name of the patch list
     */
    private String name;
    /**
     * List of patches
     */
    private PatchClass[] patchList;
    /**
     * List of configs, unparsed
     */
    private ConfigClass[] configList;

    /**
     * Constructor
     * @param name Name of the patch list
     * @param patchList List of patches
     * @param configList List of configs, unparsed
     */
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
