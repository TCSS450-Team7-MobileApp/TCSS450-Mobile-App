package edu.uw.tcss450.blynch99.tcss450mobileapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Map<Integer, Integer>> mNewMessageCount;

    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(new HashMap<>());
    }

    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Map<Integer, Integer>> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    public void increment(int chatId) {
        mNewMessageCount.getValue().put(
                chatId,
                mNewMessageCount.getValue().containsKey(chatId) ?
                        mNewMessageCount.getValue().get(chatId) + 1 : 1
        );
        mNewMessageCount.setValue(mNewMessageCount.getValue());
    }

    public void reset(int chatId) {
        if (mNewMessageCount.getValue().containsKey(chatId)) {
            mNewMessageCount.getValue().put(chatId, 0);
        }
        mNewMessageCount.setValue(mNewMessageCount.getValue());
    }
}