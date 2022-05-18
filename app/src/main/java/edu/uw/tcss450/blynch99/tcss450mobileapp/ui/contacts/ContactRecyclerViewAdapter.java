package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.myViewHolder> {

    Contact[] mContacts;
    Context mContext;

    public ContactRecyclerViewAdapter(Context context, Contact[] contacts){

        mContacts = contacts;
        mContext = context;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_contact_card,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.nickname.setText(mContacts[position].getNickname());
        holder.fullName.setText(mContacts[position].getFirstname() + " " + mContacts[position].getLastname());

        holder.cardLayout.setOnClickListener(button -> navigateToFriend( holder.view, mContacts[position]));


    }

    private void navigateToFriend(View view, Contact contact){
        Navigation.findNavController(view)
                .navigate(ContactsFragmentDirections.
                        actionContactsFragmentToNavigationFriend(contact));
    }

    @Override
    public int getItemCount() {
        return mContacts.length;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView nickname, fullName;
        ConstraintLayout cardLayout;
        View view;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.text_nickname);
            fullName = itemView.findViewById(R.id.text_full_name);
            cardLayout = itemView.findViewById(R.id.layout_card);
            view = itemView.getRootView();
        }
    }
}
