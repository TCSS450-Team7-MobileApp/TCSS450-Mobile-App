package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

/**
 * A simple {@link RecyclerView.Adapter<ContactRecyclerViewAdapter.myViewHolder>} subclass.
 */
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.myViewHolder> {

    protected final HashMap<Integer,Contact> mContacts;
    protected final Context mContext;
    protected ManagerFriendViewModel mManage;


    /**
     * Constructor
     * @param context current context
     * @param contacts current contacts
     */
    public ContactRecyclerViewAdapter(Context context, HashMap<Integer,Contact> contacts){


        mContacts = contacts;
        mContext = context;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_card,parent,false);

        mManage = new ViewModelProvider(
                (ViewModelStoreOwner) MainActivity.getActivity()).get(ManagerFriendViewModel.class);

        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if (mContacts.get(position) == null)
            return;
        holder.nickname.setText(mContacts.get(position).getNickname());
        holder.fullName.setText(mContacts.get(position).getFirstname() + " " + mContacts.get(position).getLastname());

        switch (mContacts.get(position).getStatus()){
            case FRIENDS:
                holder.manager.setText("Message");
                break;
            case RECEIVED_REQUEST:
                holder.manager.setText("Accept Request");
                holder.manager.setOnClickListener(button-> acceptRequest(mContacts.get(position), position));
                break;
            case NOT_FRIENDS:
                holder.manager.setText("Send Request");
                holder.manager.setOnClickListener(button-> sendRequest(mContacts.get(position), position));
                break;
        }

        holder.view.setOnClickListener(button -> showButtons(holder));

        holder.remove.setOnClickListener(button ->
                showRemoveDialog(mContacts.get(position),holder.view, position));
    }

    /**
     * show buttons
     * @param holder holder that hold the buttons
     */
    private void showButtons(myViewHolder holder){
        if (holder.remove.getVisibility() == View.VISIBLE || holder.manager.getVisibility() == View.VISIBLE) {
            holder.remove.setVisibility(View.GONE);
            holder.manager.setVisibility(View.GONE);
        } else {
            if (holder.manager.getText() != "Message")
                holder.manager.setVisibility(View.VISIBLE);
            if (holder.manager.getText() != "Send Request")
                holder.remove.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show Remove Dialog for removing friends
     * @param contact the contact
     * @param view the view for it to appear
     * @param position position of the contact
     */
    void showRemoveDialog(Contact contact, View view, int position) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_remove);
        dialog.findViewById(R.id.button_ok).setOnClickListener(button -> {
            dialog.dismiss();
            mManage.connectRemoveFriend(contact.getId());

            removeFromView(position);
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(button -> dialog.dismiss());
        dialog.show();
    }

    /**
     * Accept Friend Request
     * @param contact Contact of the request to accept
     * @param position position of the contact
     */
    private void acceptRequest(Contact contact, int position){
        mManage.connectAcceptRequest(contact.getId());

        removeFromView(position);
    }

    /**
     * Send a friend request
     * @param contact contact to send the request
     * @param position position of the contact
     */
    private void sendRequest(Contact contact, int position){
        mManage.connectSendRequest(contact.getId());
        removeFromView(position);
    }

    /**
     * Remove contact from list
     * @param position position of the contact
     */
    private void removeFromView(int position){
        mContacts.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, mContacts.size());
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
}