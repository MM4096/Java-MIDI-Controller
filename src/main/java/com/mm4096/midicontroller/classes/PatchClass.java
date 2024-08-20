package com.mm4096.midicontroller.classes;

public class PatchClass {
    private String sound;
    private String comments;

    public PatchClass(String sound, String comments) {
        this.sound = sound;
        this.comments = comments;
    }

    public PatchClass() {
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "PatchClass{" +
                "sound='" + sound + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
