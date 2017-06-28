package fromRESTful;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;

import util.Utils;

public class Messages implements Serializable {

    private Users userId;
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String body;
    private Date timestamp;
    private Conversations conversationId;

    public Messages() {}

    public Messages(Integer id) {
        this.id = id;
    }

    public Messages(Integer id, String body, Date timestamp) {
        this.id = id;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Conversations getConversationId() {
        return conversationId;
    }

    public void setConversationId(Conversations conversationId) { this.conversationId = conversationId; }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbpackage.Messages[ id=" + id + " ]";
    }



    public static Messages fromJSON (JSONObject obj){
        Messages m = new Messages();

        try {
            m.id = (Integer) obj.get("id");

            JSONObject userobject = (JSONObject)obj.get("userId");
            Users senderUser = Users.fromJSON(userobject);
            m.userId = senderUser;

            m.body="";
            if(Utils.isFieldOK(obj, "body")) m.body = (String) obj.get("body");

            String dateString;
            java.util.Date date;
            if(Utils.isFieldOK(obj,"timestamp")) {
                dateString = (String) obj.get("timestamp");
                date = Utils.ConvertStringToDate(dateString, Utils.DATABASE_DATETIME);
                m.timestamp = new java.sql.Date(date.getTime());
            }
            JSONObject conversationObj = (JSONObject)obj.get("conversationId");
            Conversations conversation = Conversations.fromJSON(conversationObj);
            m.conversationId = conversation;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }

}