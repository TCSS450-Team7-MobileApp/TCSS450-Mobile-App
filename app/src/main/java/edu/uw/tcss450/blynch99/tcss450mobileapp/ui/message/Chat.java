package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.util.Log;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;

/**
 * Represents a Chat with multiple members
 */
public class Chat implements Serializable {

    private final int mChatId;
    private String mDate;
    private final String mTitle;
    private String mTeaser;
    private List<String> mMembers;

    /**
     *
     * @param members List of usernames of members in the chat.
     * @param title The title/name of the chat.
     * @param chatId The ID of the chat.
     * @param date The date of the most recently sent message in the chat.
     * @param teaser The teaser/preview of the most recently sent message in the chat.
     */
    public Chat(List<String> members, String title, int chatId, String date, String teaser) {
        mMembers = members;
        mChatId = chatId;
        mDate = date;
        mTeaser = teaser;

        // Process members for title
        //List<String> memberNames = members.stream().map(Contact::getNickname).collect(Collectors.toList());
        //mTitle = join(", ", members);
        mTitle = title;
    }

    public void setMembers(List<String> members) {
        mMembers = members;
    }

    public void setTeaser(String teaser) {
        mTeaser = teaser;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getChatId() { return mChatId; }

    public String getDate() { return mDate; }

    /**
     * Returns the date of the most recent message in the chat in the format:
     *  - [DAY OF THE WEEK] [TIME]
     * @return String representation of formatted date.
     */
    public String getFormattedDate() {
        String formattedDate = "";
        Date today = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date msgDate = dateFormat.parse(mDate.split(" ")[0]);
            formattedDate += msgDate.toString().split(" ")[0] + " ";
            long msPassed = today.getTime() - msgDate.getTime();
            long daysPassed = TimeUnit.DAYS.convert(msPassed, TimeUnit.MILLISECONDS);

            //Log.d("TIME", mDate);
            String time = mDate.substring(11);
            String[] times = time.split(":");
            //Log.d("TIME", Arrays.toString(times));
            formattedDate += times[0] + ":" + times[1];
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ERROR", "Error with date: " + mDate);
        }
        return formattedDate;
    }

    public String getTeaser() { return mTeaser; }

    public String getTitle() { return mTitle; }

    public List<String> getMembers() { return mMembers; }

    private static String join(String delimiter, List<String> input) {
        if (input == null || input.size() < 1) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));
            if (i != input.size()-1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) return false;

        Chat other = (Chat) o;
        return this.mChatId == other.getChatId();
    }
}
