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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.myViewHolder> {

    private final HashMap<Integer,Contact> mContacts;
    private final Context mContext;
    private ManagerFriendViewModel mManage;

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
        holder.manager.setText(mContacts.get(position).getStatus().toString());
        holder.view.setOnClickListener(button -> showButtons(mContacts.get(position).getStatus(), holder));

        holder.manager.setOnClickListener(button-> {
            if (holder.manager.getText() == "Accept Request")
                acceptRequest(mContacts.get(position), position);
        });

        holder.remove.setOnClickListener(button ->
            showRemoveDialog(mContacts.get(position),holder.view, position));
    }

    private void showButtons(FriendStatus status, myViewHolder holder){
        if (status == FriendStatus.FRIENDS) {
            if (holder.manager.getVisibility() == View.VISIBLE) {
                holder.remove.setVisibility(View.GONE);
                holder.manager.setVisibility(View.GONE);
            } else {
                holder.manager.setText("Message");
                holder.manager.setVisibility(View.VISIBLE);
                holder.remove.setVisibility(View.VISIBLE);
            }
        }
        else if (status == FriendStatus.RECEIVED_REQUEST){
            if (holder.manager.getVisibility() == View.VISIBLE) {
                holder.manager.setVisibility(View.GONE);
                holder.remove.setVisibility(View.GONE);
            }
            else {
                holder.manager.setText("Accept Request");
                holder.manager.setVisibility(View.VISIBLE);
                holder.remove.setVisibility(View.VISIBLE);
            }
        }
    }

    void showRemoveDialog(Contact contact, View view, int position) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_remove);
        dialog.findViewById(R.id.button_ok).setOnClickListener(button -> {
            Log.d("ARCHIVE", "CLICK OK");
            dialog.dismiss();
            mManage.connectRemoveFriend(contact.getId());

            removeFromView(position);
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(button -> dialog.dismiss());
        dialog.show();
    }

    private void acceptRequest(Contact contact, int position){
        mManage.connectAcceptRequest(contact.getId());



        removeFromView(position);
    }

    private void removeFromView(int position){
        mContacts.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, mContacts.size());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
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
