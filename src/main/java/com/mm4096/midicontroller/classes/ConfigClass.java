package com.mm4096.midicontroller.classes;


/**
 * Class to turn strings (alias) into bank ids (both favourites and program)
 */
public class ConfigClass {
    /**
     * Alias of the bank
     */
    private String alias;
    /**
     * Bank id
     * (See {@link com.mm4096.midicontroller.midi.MidiKeyboard} for more information)
     */
    private String bankId;

    /**
     * Constructor
     * @param alias Alias of the bank
     * @param bankId Bank id
     */
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
