package cz.muni.fi.agentproject;

/**
 * Created by Wallecnik on 23.03.15.
 */
public class Constants {

    private Constants() {
        throw new AssertionError("Do not instantiate");
    }

    public static final String NAME_REGEX = "[\\p{L}\\s]*\\p{L}[\\p{L}\\s]*";

    public static final int NAME_MAX_LENGTH = 255;


}
