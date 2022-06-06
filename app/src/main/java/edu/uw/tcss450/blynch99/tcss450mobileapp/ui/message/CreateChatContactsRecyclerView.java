package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactRecyclerViewAdapter;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ManagerFriendViewModel;

public class CreateChatContactsRecyclerView extends RecyclerView.Adapter<CreateChatContactsRecyclerView.myViewHolder>  {
    protected final HashMap<Integer,Contact> mContacts;
    protected final Context mContext;
    private final List<Contact> mGroupList;

    public CreateChatContactsRecyclerView(HashMap<Integer, Contact> mContacts, Context mContext) {
        this.mContacts = mContacts;
        this.mContext = mContext;
        mGroupList = new ArrayList<>();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_card,parent,false);

        return new CreateChatContactsRecyclerView.myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if (mContacts.get(position) == null)
            return;
        holder.nickname.setText(mContacts.get(position).getNickname());
        holder.fullName.setText(mContacts.get(position).getFirstname() + " " + mContacts.get(position).getLastname());

        holder.view.setOnClickListener(button -> addToContactList(mContacts.get(position), holder));

    }

    /**
     * Adds the given Contact to the list of Contacts.
     * @param contact Contact to add.
     * @param holder ViewHolder for the Contacts in the CreateChatRecyclerView.
     */
    private void addToContactList(Contact contact,  myViewHolder holder){
        if (mGroupList.contains(contact)){
            holder.nickname.setTextColor(MainActivity.getActivity().getColor(R.color.black));
            mGroupList.remove(contact);
            return;
        }

        mGroupList.add(contact);
        holder.nickname.setTextColor(MainActivity.getActivity().getColor(R.color.coral));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView nickname, fullName;
        ConstraintLayout cardLayout;
        Button manager, remove;
        View view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.text_nickname);
            fullName = itemView.findViewById(R.id.text_full_name);
            cardLayout = itemView.findViewById(R.id.layout_card);
            manager = itemView.findViewById(R.id.button_friend_manager);
            remove = itemView.findViewById(R.id.button_friend_remove);
            view = itemView.getRootView();
        }
    }

    /**
     * Gets the Contacts that are selected in the ViewHolder.
     * @return List of Contacts
     */
    public List<Contact> getGroupList(){
        return mGroupList;
    }
}
