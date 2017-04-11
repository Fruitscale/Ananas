package nl.ictrek.ictrix;

/**
 * Created by Koen Bolhuis on 11-Apr-17.
 */

public class Chat {
    private String mTitle;
    private String mSummary;

    public Chat(String title, String summary) {
        mTitle = title;
        mSummary = summary;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary() {
        return mSummary;
    }
}
