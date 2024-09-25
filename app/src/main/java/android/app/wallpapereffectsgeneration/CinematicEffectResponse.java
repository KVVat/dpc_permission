package android.app.wallpapereffectsgeneration;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
//Dummy
public final class CinematicEffectResponse implements Parcelable {
    protected CinematicEffectResponse(Parcel in) {
    }

    public static final Creator<CinematicEffectResponse> CREATOR = new Creator<CinematicEffectResponse>() {
        @Override
        public CinematicEffectResponse createFromParcel(Parcel in) {
            return new CinematicEffectResponse(in);
        }

        @Override
        public CinematicEffectResponse[] newArray(int size) {
            return new CinematicEffectResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
