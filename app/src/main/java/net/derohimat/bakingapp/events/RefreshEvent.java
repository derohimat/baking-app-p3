package net.derohimat.bakingapp.events;

import android.support.annotation.Nullable;

public class RefreshEvent {
    @Nullable
    private final boolean mSuccess;
    @Nullable
    private final String message;

    public RefreshEvent(boolean mSuccess, @Nullable String message) {
        this.mSuccess = mSuccess;
        this.message = message;
    }

    @Nullable
    public boolean ismSuccess() {
        return mSuccess;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
