package nl.ictrek.ictrix;

import android.util.TimeUtils;

/**
 * Created by Koen Bolhuis on 11-Apr-17.
 */

public class Chat {
    public enum Type {
        PERSONAL,
        GROUP
    }

    private String mTitle;
    private String mSummary;
    private String mTime;
    private Type mType;


    public Chat(String title, String summary, String time, Type type) {
        mTitle = title;
        mSummary = summary;
        mTime = time;
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getTime() {
        return mTime;
    }

    public Type getType() {
        return mType;
    }
}
