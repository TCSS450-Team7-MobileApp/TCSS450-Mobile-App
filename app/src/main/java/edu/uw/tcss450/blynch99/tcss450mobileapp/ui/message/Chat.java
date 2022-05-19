package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;

public class Chat implements Serializable {

    private final String mChatId, mDate, mTitle, mTeaser;
    private final List<String> mMembers;

    public Chat(List<String> members, String chatId, String date, String teaser) {
        mMembers = members;
        mChatId = chatId;
        mDate = date;
        mTeaser = teaser;

        // Process members for title
        //List<String> memberNames = members.stream().map(Contact::getNickname).collect(Collectors.toList());
        mTitle = join(", ", members);
    }

    public String getChatId() { return mChatId; }

    public String getDate() { return mDate; }

    public String getFormattedDate() {
        String formattedDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date msgDate = dateFormat.parse(mDate.split(" ")[0]);
            formattedDate += msgDate.toString().split(" ")[0] + " ";
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ERROR", "Error with date: " + mDate);
        }
        String time = mDate.split(" ")[1];
        String[] times = time.split(":");
        Log.d("TIME", Arrays.toString(times));
        return formattedDate + times[0] + ":" + times[1];
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
        return this.mChatId.equals(other.getChatId());
    }
}
