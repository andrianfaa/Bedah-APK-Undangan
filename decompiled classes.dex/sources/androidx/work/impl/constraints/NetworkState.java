package androidx.work.impl.constraints;

import mt.Log1F380D;

/* compiled from: 00C5 */
public class NetworkState {
    private boolean mIsConnected;
    private boolean mIsMetered;
    private boolean mIsNotRoaming;
    private boolean mIsValidated;

    public NetworkState(boolean isConnected, boolean isValidated, boolean isMetered, boolean isNotRoaming) {
        this.mIsConnected = isConnected;
        this.mIsValidated = isValidated;
        this.mIsMetered = isMetered;
        this.mIsNotRoaming = isNotRoaming;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkState)) {
            return false;
        }
        NetworkState networkState = (NetworkState) o;
        return this.mIsConnected == networkState.mIsConnected && this.mIsValidated == networkState.mIsValidated && this.mIsMetered == networkState.mIsMetered && this.mIsNotRoaming == networkState.mIsNotRoaming;
    }

    public int hashCode() {
        int i = 0;
        if (this.mIsConnected) {
            i = 0 + 1;
        }
        if (this.mIsValidated) {
            i += 16;
        }
        if (this.mIsMetered) {
            i += 256;
        }
        return this.mIsNotRoaming ? i + 4096 : i;
    }

    public boolean isConnected() {
        return this.mIsConnected;
    }

    public boolean isMetered() {
        return this.mIsMetered;
    }

    public boolean isNotRoaming() {
        return this.mIsNotRoaming;
    }

    public boolean isValidated() {
        return this.mIsValidated;
    }

    public String toString() {
        String format = String.format("[ Connected=%b Validated=%b Metered=%b NotRoaming=%b ]", new Object[]{Boolean.valueOf(this.mIsConnected), Boolean.valueOf(this.mIsValidated), Boolean.valueOf(this.mIsMetered), Boolean.valueOf(this.mIsNotRoaming)});
        Log1F380D.a((Object) format);
        return format;
    }
}
