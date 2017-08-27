package util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Messages;
import gr.uoa.di.airbnbproject.R;
import me.himanshusoni.chatmessageview.ChatMessageView;

import static util.Utils.COPY_ACTION;
import static util.Utils.DELETE_ACTION;

/** RecyclerAdapter for MessageActivity**/

public class RecyclerAdapterMessages extends RecyclerView.Adapter<RecyclerAdapterMessages.MessageHolder>
{
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
        Context mContext;
        Boolean user;
        Integer currentUserId;
        ArrayList<Messages> mMessages = new ArrayList<>();

        public RecyclerAdapterMessages(Context context, ArrayList<Messages> messages, Boolean user, Integer currentUserId)
        {
            this.mContext = context;
            this.user=user;
            this.mMessages = messages;
            this.currentUserId=currentUserId;
        }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages item = mMessages.get(position);

        if (item.isMine(currentUserId)) return MY_MESSAGE;
        else return OTHER_MESSAGE;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mine_message, parent, false));
        } else {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final MessageHolder holder, final int position) {
        Messages chatMessage = mMessages.get(position);
        holder.tvName.setText(chatMessage.getUserId().getUsername());
        holder.tvMessage.setText(chatMessage.getBody());

        String date = Utils.ConvertDateToString(mMessages.get(position).getTimestamp(), Utils.FORMAT_DATE_DMY);
        holder.tvTime.setText("Sent: " + date);

        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Message Options");
                menu.add(0, position, 0, DELETE_ACTION);
                menu.add(0, position, 1, COPY_ACTION);
            }
        });
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvName;
        ChatMessageView chatMessageView;

        MessageHolder(View itemView) {
            super(itemView);
            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvName = (TextView)itemView.findViewById(R.id.msglist_name);
            tvMessage = (TextView) itemView.findViewById(R.id.msglist_body);
            tvTime = (TextView) itemView.findViewById(R.id.msglist_timestamp);
        }

        public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
