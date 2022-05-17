package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;

public class Chat implements Serializable {

    private final String mChatId, mDate, mTitle, mTeaser;
    private final List<Contact> mMembers;

    public Chat(List<Contact> members, String chatId, String date, String teaser) {
        mMembers = members;
        mChatId = chatId;
        mDate = date;
        mTeaser = teaser;

        // Process members for title
        //List<String> memberNames = members.stream().map(Contact::getNickname).collect(Collectors.toList());
        List<String> memberNames = new ArrayList<>();
        for (Contact c : members)
            memberNames.add(c.getNickname());
        mTitle = join(", ", memberNames);
    }

    public String getChatId() { return mChatId; }

    public String getDate() { return mDate; }

    public String getTeaser() { return mTeaser; }

    public List<Contact> getMembers() { return mMembers; }

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
}
