package nl.ictrek.ictrix.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nl.ictrek.ictrix.Chat;
import nl.ictrek.ictrix.R;

/**
 * Created by Koen Bolhuis on 11-Apr-17.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<Chat> mChatList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle, textViewSummary, textViewTime;
        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.imageView_chatImage);
            textViewTitle = (TextView)view.findViewById(R.id.textView_chatTitle);
            textViewSummary = (TextView)view.findViewById(R.id.textView_chatSummary);
            textViewTime = (TextView)view.findViewById(R.id.textView_chatTime);
        }
    }

    public ChatListAdapter(List<Chat> chatList) {
        mChatList = chatList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.chat_list_chat, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = mChatList.get(position);
        // TODO: handle image
        holder.textViewTitle.setText(chat.getTitle());
        holder.textViewSummary.setText(chat.getSummary());
        holder.textViewTime.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
