package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;

public class ArchiveChatDialogFragment extends DialogFragment {

    public static String TAG = "ArchiveChatDialog";
    private ArchiveChatDialogViewModel mModel;
    private Chat mChat;

    public ArchiveChatDialogFragment(Chat chat) {
        mChat = chat;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mModel = new ViewModelProvider(getActivity()).get(ArchiveChatDialogViewModel.class);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_archive_chat)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("ARCHIVE", "Archive chat");
                        //archiveChat();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void archiveChat() {
        UserInfoViewModel userInfoViewModel =
                new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel.connectDelete(
                mChat.getChatId(),
                userInfoViewModel.getEmail(),
                userInfoViewModel.getJwt());
    }
}
