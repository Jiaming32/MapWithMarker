package com.example.mapwithmarker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

public class Mark implements Parcelable {
   Marker marker;

   String description;
   String author;

   int likes;
   int dislikes;
   Uri imageUri;


   public Mark(Marker marker, String description, String author, int likes, int dislikes, Uri imageUri) {
      this.marker = marker;
      this.description = description;
      this.author = author;
      this.likes = likes;
      this.dislikes = dislikes;
      this.imageUri = imageUri;
   }
   public String output() {
      return this.author;
   }

   protected Mark(Parcel in) {
      description = in.readString();
      author = in.readString();
      likes = in.readInt();
      dislikes = in.readInt();
      imageUri = in.readParcelable(Uri.class.getClassLoader());
   }

   public static final Creator<Mark> CREATOR = new Creator<Mark>() {
      @Override
      public Mark createFromParcel(Parcel in) {
         return new Mark(in);
      }

      @Override
      public Mark[] newArray(int size) {
         return new Mark[size];
      }
   };

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(description);
      parcel.writeString(author);
      parcel.writeInt(likes);
      parcel.writeInt(dislikes);
      parcel.writeParcelable(imageUri, i);
   }
}
