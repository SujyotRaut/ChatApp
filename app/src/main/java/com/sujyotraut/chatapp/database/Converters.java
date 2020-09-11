package com.sujyotraut.chatapp.database;

import android.net.Uri;
import androidx.room.TypeConverter;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.utils.MsgType;

import java.util.Calendar;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static Calendar timeToCalendar(Long value) {
        if (value != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }else {
            return null;
        }
    }

    @TypeConverter
    public static Long calendarToTime(Calendar calendar) {
        if (calendar != null){
            return calendar.getTimeInMillis();
        }else {
            return null;
        }
    }

    @TypeConverter
    public static Timestamp timeToTimestamp(Long value) {
        if (value != null){
            Date date = new Date();
            date.setTime(value);
            return new Timestamp(date);
        }else {
            return null;
        }
    }

    @TypeConverter
    public static Long timestampToTime(Timestamp timestamp) {
        if (timestamp != null){
            return timestamp.toDate().getTime();
        }else {
            return null;
        }
    }

    @TypeConverter
    public static Uri stringToUri(String value){
        if (value != null){
            return Uri.parse(value);
        }else {
            return null;
        }
    }

    @TypeConverter
    public static String uriToString(Uri uri){
        if (uri != null){
            return uri.toString();
        }else {
            return null;
        }
    }

    @TypeConverter
    public static MsgType stringToMsgType(String value){
        if (value == null){
            return null;
        }else if (value.equals("TEXT")){
            return MsgType.TEXT;
        }else if (value.equals("IMAGE")){
            return MsgType.IMAGE;
        }else if (value.equals("VIDEO")){
            return MsgType.VIDEO;
        }else if (value.equals("AUDIO")){
            return MsgType.AUDIO;
        }else {
            return MsgType.FILE;
        }
    }

    @TypeConverter
    public static String msgTypeToString(MsgType msgType){
        if (msgType == null){
            return null;
        }else if (msgType == MsgType.TEXT){
            return "TEXT";
        }else if (msgType == MsgType.IMAGE){
            return "IMAGE";
        }else if (msgType == MsgType.VIDEO){
            return "VIDEO";
        }else if (msgType == MsgType.AUDIO){
            return "AUDIO";
        }else {
            return "FILE";
        }
    }
}