package nl.ictrek.ananas.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.chat_list_chat.view.*

import nl.ictrek.ananas.Chat
import nl.ictrek.ananas.R

/**
 * Created by Koen Bolhuis on 11-Apr-17.
 *
 * listadapter for the chats on the main activity.
 *
 * @param mChatList List with the chats.
 */
class ChatListAdapter(private val mChatList: List<Chat>) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_list_chat, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = mChatList[position]
        // TODO: handle image
        var title = chat.title
        if (chat.type === Chat.Type.GROUP) {
            holder.view.textView_chatTitle.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_people_black_18dp, 0, 0, 0
            )
            title = " $title"
        }
        holder.view.textView_chatTitle.text = title
        holder.view.textView_chatSummary.text = chat.summary
        holder.view.textView_chatTime.text = chat.time
    }

    override fun getItemCount(): Int = mChatList.size
}
