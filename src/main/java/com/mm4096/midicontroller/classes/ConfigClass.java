package com.mm4096.midicontroller.classes;

public class ConfigClass {
    private String alias;
    private String bankId;

    public ConfigClass(String alias, String bankId) {
        this.alias = alias;
        this.bankId = bankId;
    }

    public ConfigClass() {
    }

    public String getAlias() {
        return alias;
    }

    public String getBankId() {
        return bankId;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Override
    public String toString() {
        return "ConfigClass{" +
                "alias='" + alias + '\'' +
                ", bankId=" + bankId +
                '}';
    }
}
