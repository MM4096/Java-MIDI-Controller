package com.mm4096.midicontroller.midi;

import javax.sound.midi.*;
import java.util.regex.Pattern;

public class MidiKeyboard {
    private final MidiDevice midiDevice;
    private int channel = 0;

    public MidiKeyboard(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
    }

    public MidiKeyboard(MidiDevice midiDevice, int channel) {
        this.midiDevice = midiDevice;
        this.channel = channel;
    }

    public MidiDevice getMidiDevice() {
        return midiDevice;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void open() throws MidiUnavailableException {
        midiDevice.open();
    }

    public void close() {
        midiDevice.close();
    }

    public void changeInstrument(int programNumber) throws InvalidMidiDataException, MidiUnavailableException {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.PROGRAM_CHANGE, channel, programNumber, 0);
        midiDevice.getReceiver().send(message, -1);
    }

    public void changeInstrument(int programNumber, int bankNumber) throws InvalidMidiDataException, MidiUnavailableException {
        ShortMessage bankSelectMSB = new ShortMessage();
        ShortMessage bankSelectLSB = new ShortMessage();


        int bankMSB = 0;
        bankSelectMSB.setMessage(ShortMessage.CONTROL_CHANGE, channel, 0, bankMSB);
        bankSelectLSB.setMessage(ShortMessage.CONTROL_CHANGE, channel, 32, bankNumber);

        midiDevice.getReceiver().send(bankSelectMSB, -1);
        midiDevice.getReceiver().send(bankSelectLSB, -1);

        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.PROGRAM_CHANGE, channel, programNumber, 0);
        midiDevice.getReceiver().send(message, -1);
    }

    /**
     * Changes the instrument based on a program number and bank number.
     *
     *
     * @param programNumber The program number to change to. This should match the format [a-h][0-127]
     * @param useProgramMode Whether to use program mode or not. If false, bank (favourites) mode will be used.
     *
     * @throws InvalidMidiDataException If the MIDI data is invalid
     * @throws MidiUnavailableException If the MIDI device is unavailable
     * @throws IllegalArgumentException If the program number is invalid
     */
    public void changeInstrument(String programNumber, boolean useProgramMode) throws InvalidMidiDataException, MidiUnavailableException {
        Pattern pattern = Pattern.compile("[abcdefgh]\\d+", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(programNumber).matches()) {
            int program = Integer.parseInt(programNumber.substring(1));
            int bank = programNumber.toLowerCase().charAt(0) - 'a';
            if (useProgramMode) {
                changeInstrument(program, bank);
            }
            else {
                changeInstrument(bank * 16 + program - 1);
            }
        }
        else if (programNumber.matches("\\d+")) {
            changeInstrument(Integer.parseInt(programNumber));
        }
        else {
            throw new IllegalArgumentException("Invalid program number: " + programNumber);
        }
    }

    public static MidiDevice getMidiDevice(String info) throws MidiUnavailableException {
        for (MidiDevice.Info thisInfo : MidiSystem.getMidiDeviceInfo()) {
            if (thisInfo.getName().contains(info)) {
                return MidiSystem.getMidiDevice(thisInfo);
            }
        }
        throw new MidiUnavailableException("Couldn't find device: " + info);
    }

    public static MidiKeyboard getMidiKeyboard(String info) throws MidiUnavailableException {
        return new MidiKeyboard(getMidiDevice(info));
    }

    public static String[] getMidiDevices() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        String[] devices = new String[infos.length];
        for (int i = 0; i < infos.length; i++) {
            devices[i] = infos[i].getName();
        }
        return devices;
    }

    public static String getInfoFromDevice(String deviceName) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            if (info.getName().equals(deviceName)) {
                return info.toString();
            }
        }
        throw new MidiUnavailableException("Couldn't find device: " + deviceName);
    }

    public static boolean isValidProgramNumber(String programNumber) {
        Pattern pattern = Pattern.compile("[abcdefgh]\\d+", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(programNumber).matches() || programNumber.matches("\\d+");
    }
}
