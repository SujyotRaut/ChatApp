package com.sujyotraut.chatapp.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.models.Message;
import com.sujyotraut.chatapp.utils.MsgType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT = 19;
    private static final int IMAGE = 20;
    private static final int VIDEO = 21;
    private static final int AUDIO = 22;
    private static final int FILE = 23;
    private List<Message> messages;

    public MessagesRecyclerAdapter() {
        this.messages = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        MsgType msgType = messages.get(position).getMsgType();
        switch (msgType){
            case TEXT:
                return TEXT;
            case IMAGE:
                return IMAGE;
            case VIDEO:
                return VIDEO;
            case AUDIO:
                return AUDIO;
            default:
                return FILE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case TEXT:
                View textMsgView = inflater.inflate(R.layout.item_msg_text, parent, false);
                return new TextMsgViewHolder(textMsgView);
            case IMAGE:
                View imageMsgView = inflater.inflate(R.layout.item_msg_image, parent, false);
                return new ImageMsgViewHolder(imageMsgView);
            case VIDEO:
                View videoMsgView = inflater.inflate(R.layout.item_msg_video, parent, false);
                return new TextMsgViewHolder(videoMsgView);
            case AUDIO:
                View audioMsgView = inflater.inflate(R.layout.item_msg_audio, parent, false);
                return new TextMsgViewHolder(audioMsgView);
            default:
                View fileMsgView = inflater.inflate(R.layout.item_msg_file, parent, false);
                return new TextMsgViewHolder(fileMsgView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Message message = messages.get(position);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String date = dateFormat.format(message.getTimestamp().toDate());
        int msgMargin = 200;

        switch (holder.getItemViewType()){
            case TEXT:
                TextMsgViewHolder textMsgVh = ((TextMsgViewHolder) holder);
                textMsgVh.tvMsg.setText(message.getMsgText());
                textMsgVh.tvTime.setText(date);

                if (message.getSenderId().equals(user.getUid())) {
                    LinearLayout linearLayout = textMsgVh.llRoot;
                    linearLayout.setGravity(Gravity.END);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
                    params.setMarginStart(msgMargin);
                    linearLayout.setLayoutParams(params);

                    if (message.getSeen()){
                        textMsgVh.ivSent.setImageResource(R.drawable.ic_double_tick_16px);
                    }else if (message.getSent()){
                        textMsgVh.ivSent.setImageResource(R.drawable.ic_tick_16px);
                    }else {
                        textMsgVh.ivSent.setImageResource(R.drawable.ic_schedule_16px);
                    }
                }else {
                    LinearLayout linearLayout = textMsgVh.llRoot;
                    linearLayout.setGravity(Gravity.START);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
                    params.setMarginEnd(msgMargin);
                    linearLayout.setLayoutParams(params);

                    textMsgVh.ivSent.setImageResource(0);

                }
                break;
            case IMAGE:
                ImageMsgViewHolder imageMsgVh = ((ImageMsgViewHolder) holder);
                imageMsgVh.tvMsg.setText(message.getMsgText());
                imageMsgVh.tvTime.setText(date);

                if (message.getSenderId().equals(user.getUid())) {
                    LinearLayout linearLayout = imageMsgVh.llRoot;
                    linearLayout.setGravity(Gravity.END);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
                    params.setMarginStart(msgMargin);
                    linearLayout.setLayoutParams(params);

                    if (message.getSeen()){
                        imageMsgVh.ivSent.setImageResource(R.drawable.ic_double_tick_16px);
                    }else if (message.getSent()){
                        imageMsgVh.ivSent.setImageResource(R.drawable.ic_tick_16px);
                    }else {
                        imageMsgVh.ivSent.setImageResource(R.drawable.ic_schedule_16px);
                    }
                }else {
                    LinearLayout linearLayout = imageMsgVh.llRoot;
                    linearLayout.setGravity(Gravity.START);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
                    params.setMarginEnd(msgMargin);
                    linearLayout.setLayoutParams(params);

                    imageMsgVh.ivSent.setImageResource(0);

                }
                break;
            case VIDEO:
                VideoMsgViewHolder videoMsgVh = ((VideoMsgViewHolder) holder);
                break;
            case AUDIO:
                AudioMsgViewHolder audioMsgVh = ((AudioMsgViewHolder) holder);
                break;
            default:
                FileMsgViewHolder fileMsgVh = ((FileMsgViewHolder) holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class TextMsgViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llRoot;
        private TextView tvMsg, tvTime;
        private ImageView ivSent;
        public TextMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_msg_text_root);
            tvMsg = itemView.findViewById(R.id.tv_msg_text_msg);
            tvTime = itemView.findViewById(R.id.tv_msg_text_time);
            ivSent = itemView.findViewById(R.id.iv_msg_text_sent);
        }
    }

    public static class ImageMsgViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llRoot;
        private TextView tvMsg, tvTime;
        private ImageView ivImage, ivSent;
        public ImageMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_msg_image_root);
            tvMsg = itemView.findViewById(R.id.tv_msg_image_msg);
            tvTime = itemView.findViewById(R.id.tv_msg_image_time);
            ivSent = itemView.findViewById(R.id.iv_msg_image_sent);
            ivImage = itemView.findViewById(R.id.iv_msg_image_img);
        }
    }

    public static class VideoMsgViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llRoot;
        private TextView tvMsg, tvTime;
        private ImageView ivSent;
        public VideoMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_msg_video_root);
            tvMsg = itemView.findViewById(R.id.tv_msg_video_msg);
            tvTime = itemView.findViewById(R.id.tv_msg_video_time);
            ivSent = itemView.findViewById(R.id.iv_msg_video_sent);
        }
    }

    public static class AudioMsgViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llRoot;
        private TextView tvTime;
        private ImageView ivSent;
        private Button btnPlay;
        private SeekBar sbAudio;
        public AudioMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_msg_audio_root);
            tvTime = itemView.findViewById(R.id.tv_msg_audio_time);
            ivSent = itemView.findViewById(R.id.iv_msg_audio_sent);
            btnPlay = itemView.findViewById(R.id.btn_msg_audio_play);
            sbAudio = itemView.findViewById(R.id.sb_msg_audio);
        }
    }

    public static class FileMsgViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llRoot;
        private TextView tvFileName, tvTime;
        private ImageView ivIcon, ivSent;
        public FileMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_msg_file_root);
            tvFileName = itemView.findViewById(R.id.tv_msg_file_name);
            tvTime = itemView.findViewById(R.id.tv_msg_file_time);
            ivSent = itemView.findViewById(R.id.iv_msg_file_sent);
            ivIcon = itemView.findViewById(R.id.iv_msg_file_icon);
        }
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void scrollToBottom(RecyclerView recyclerView){
        recyclerView.scrollToPosition(messages.size()-1);
    }

    public void smoothScrollToBottom(RecyclerView recyclerView){
        recyclerView.smoothScrollToPosition(messages.size()-1);
    }
}
