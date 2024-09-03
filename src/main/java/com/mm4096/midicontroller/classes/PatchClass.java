package com.mm4096.midicontroller.classes;

/**
 * A singular patch format (on disk)
 * For a performance patch, see {@link com.mm4096.midicontroller.parser.PerformanceItem}
 */
public class PatchClass {
    /**
     * The sound that the keyboard will play, either a direct code or an alias
     */
    private String sound;
    /**
     * Comments for the patch
     */
    private String comments;

    /**
     * Constructor
     * @param sound The sound that the keyboard will play, either a direct code or an alias
     * @param comments Comments for the patch
     */
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
