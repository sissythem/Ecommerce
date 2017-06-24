package fromRESTful;

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
    private Residences residenceId;
    private short readFromSender;
    private short readFromReceiver;

    public Conversations() {}

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

//    @XmlTransient
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

    public Residences getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Residences residenceId) {
        this.residenceId = residenceId;
    }

    public short getReadFromSender() {
        return readFromSender;
    }

    public void setReadFromSender(short readFromSender) {
        this.readFromSender = readFromSender;
    }

    public short getReadFromReceiver() {
        return readFromReceiver;
    }

    public void setReadFromReceiver(short readFromReceiver) {
        this.readFromReceiver = readFromReceiver;
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

    public static Conversations fromJSON(JSONObject obj){
        System.out.println(obj);
        Conversations c = new Conversations();
        System.out.println(c);
        try {
            c.id = (Integer) obj.get("id");
            c.subject="";
            if(Utils.isFieldOK(obj, "subject")) c.subject = (String) obj.get("subject");

            c.senderId = Users.fromJSON((JSONObject)obj.get("senderId"));
            c.receiverId = Users.fromJSON((JSONObject)obj.get("receiverId"));

            if(Utils.isFieldOK(obj, "readFromSender")) c.readFromSender = Short.parseShort(obj.get("readFromSender").toString());
            if(Utils.isFieldOK(obj, "readFromReceiver")) c.readFromReceiver = Short.parseShort(obj.get("readFromReceiver").toString());

            if (Utils.isFieldOK(obj, "Messages")) {
                ArrayList<Messages> messagesList = new ArrayList<>();
                JSONArray jsonArrayMessages = (JSONArray)obj.get("Messages");

                for(int i=0;i<jsonArrayMessages.length();i++){
                    JSONObject object = (JSONObject)jsonArrayMessages.get(i);
                    Messages message = Messages.fromJSON(object);
                    messagesList.add(message);
                }
                c.messagesCollection = messagesList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

    public JSONObject toJSON () {
        JSONObject jsonConversation = new JSONObject();
        try {
            jsonConversation.put("id", id.toString());
            jsonConversation.put("senderId", senderId.toJSON());
            jsonConversation.put("receiverId", receiverId.toJSON());
            jsonConversation.put("subject", subject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonConversation;
    }
}