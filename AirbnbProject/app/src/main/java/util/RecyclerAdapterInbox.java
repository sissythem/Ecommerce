package util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Conversations;
import gr.uoa.di.airbnbproject.InboxActivity;
import gr.uoa.di.airbnbproject.R;

import static util.Utils.DELETE_ACTION;
import static util.Utils.OPEN_MESSAGES_ACTION;
import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
import static util.Utils.VIEW_RESIDENCE_ACTION;
/** RecyclerView and CardView for Reservations used in InboxActivity **/
public class RecyclerAdapterInbox extends RecyclerView.Adapter<RecyclerAdapterInbox.ConversationsCardHolder> {
    Context context;
    Boolean user;
    ArrayList<Conversations> conversations = new ArrayList<>();

    public ArrayList<Conversations> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversations> conversations) {
        this.conversations = conversations;
    }

    int currentUserId;
    public RecyclerAdapterInbox(Context context, Boolean user, ArrayList<Conversations> conversations, Integer currentUserId) {
        this.context            = context;
        this.user               = user;
        this.conversations      = conversations;
        this.currentUserId      = currentUserId;
    }

    @Override
    public ConversationsCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inbox, parent, false);
        return new ConversationsCardHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationsCardHolder holder, final int position) {
        String userType, userPhoto, msgName;
        if (currentUserId == conversations.get(position).getSenderId().getId()) {
            userType    = USER_SENDER;
            userPhoto   = conversations.get(position).getReceiverId().getPhoto();
            msgName     = conversations.get(position).getReceiverId().getFirstName() + " " + conversations.get(position).getReceiverId().getLastName();
        } else {
            userType    = USER_RECEIVER;
            userPhoto   = conversations.get(position).getSenderId().getPhoto();
            msgName     = conversations.get(position).getSenderId().getFirstName() + " " + conversations.get(position).getSenderId().getLastName();
        }

        if ((userType == USER_SENDER && conversations.get(position).getDeletedFromSender() == 1) || (userType == USER_RECEIVER && conversations.get(position).getDeletedFromReceiver() == 1)) {
            holder.rCardView.setVisibility(View.GONE);
        }

        if ((userType == USER_SENDER && conversations.get(position).getReadFromSender() != 1) || (userType == USER_RECEIVER && conversations.get(position).getReadFromReceiver() != 1)) {
            holder.rName.setTypeface(null, Typeface.BOLD);
        }

        Utils.loadProfileImage(context, holder.rPhoto, userPhoto);
        holder.rName.setText(msgName);
        holder.rSubject.setText(conversations.get(position).getSubject());

        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Conversation Options");
                menu.add(0, position, 0, OPEN_MESSAGES_ACTION);
                menu.add(0, position, 1, VIEW_RESIDENCE_ACTION);
                menu.add(0, position, 2, DELETE_ACTION);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InboxActivity)context).openMessages(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationsCardHolder extends RecyclerView.ViewHolder {
        CardView rCardView;
        ImageView rPhoto;
        TextView rName, rSubject;

        public ConversationsCardHolder(final View itemView) {
            super(itemView);

            rCardView   = (CardView) itemView.findViewById(R.id.iCardView);
            rPhoto      = (ImageView) itemView.findViewById(R.id.profilepic);
            rName       = (TextView) itemView.findViewById(R.id.name);
            rSubject    = (TextView) itemView.findViewById(R.id.subject);
        }

        public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
