package com.example.projektandroid;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadProgress implements Parcelable {

    public int bytesDownloaded;
    public int size;
    public int finished;

    public DownloadProgress() {
        bytesDownloaded = 0;
        size = 0;
        finished = 0;
    }

    /**
     * Metoda wczytująca dane do swojej instancji z obiektu Parcelable podanego w argumencie
     * @param in
     */
    protected DownloadProgress(Parcel in) {
        bytesDownloaded = in.readInt();
        size = in.readInt();
        finished = in.readInt();
    }

    public static final Creator<DownloadProgress> CREATOR = new Creator<DownloadProgress>() {
        @Override
        public DownloadProgress createFromParcel(Parcel in) {
            return new DownloadProgress(in);
        }

        @Override
        public DownloadProgress[] newArray(int size) {
            return new DownloadProgress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Metoda zapisująca ze swojej instancji dane do podanego w argumencie obiektu Parcelable
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(bytesDownloaded);
        dest.writeInt(size);
        dest.writeInt(finished);
    }
}
