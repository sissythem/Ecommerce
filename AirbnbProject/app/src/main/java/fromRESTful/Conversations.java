package fromRESTful;

/**
 * Created by sissy on 8/5/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import util.Utils;

public class Conversations implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String subject;
    private Collection<Messages> messagesCollection;
    private Users senderId;
    private Users receiverId;

    public Conversations() {
    }

    public Conversations(Integer id) {
        this.id = id;
    }

    public Conversations(Integer id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Collection<Messages> getMessagesCollection() {
        return messagesCollection;
    }

    public void setMessagesCollection(Collection<Messages> messagesCollection) {
        this.messagesCollection = messagesCollection;
    }

    public Users getSenderId() {
        return senderId;
    }

    public void setSenderId(Users senderId) {
        this.senderId = senderId;
    }

    public Users getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Users receiverId) {
        this.receiverId = receiverId;
    }

    public static Conversations fromJSON(JSONObject obj){
        Conversations c = new Conversations();
        try {
            c.id = (Integer) obj.get("id");
            c.subject="";
            if(Utils.isFieldOK(obj, "subject"))
                c.subject = (String) obj.get("subject");

            JSONObject senderObj = (JSONObject)obj.get("senderId");
            JSONObject receiverObj = (JSONObject)obj.get("receiverId") ;
            Users sender = Users.fromJSON(senderObj);
            Users receiver = Users.fromJSON(receiverObj);

            c.senderId = sender;
            c.receiverId = receiver;

            ArrayList<Messages> messagesList = new ArrayList<>();
            JSONArray jsonArrayMessages = (JSONArray)obj.get("Messages");

            for(int i=0;i<jsonArrayMessages.length();i++){
                JSONObject object = (JSONObject)jsonArrayMessages.get(i);
                Messages message = Messages.fromJSON(object);
                messagesList.add(message);
            }
            c.messagesCollection = messagesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversations)) {
            return false;
        }
        Conversations other = (Conversations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbpackage.Conversations[ id=" + id + " ]";
    }

}

