package fromRESTful;

import java.io.Serializable;
import java.util.Collection;

/** Conversations class to map the object from RESTful services **/
public class Conversations implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String subject;
    private Collection<Messages> messagesCollection;
    private Users senderId;
    private Users receiverId;
    private Residences residenceId;
    private short readFromSender;
    private short readFromReceiver;
    private short deletedFromSender;
    private short deletedFromReceiver;

    public Conversations() {}

    public Conversations(Integer id) {
        this.id = id;
    }

    public Conversations(Integer id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public Conversations(Users senderId, Users receiverId, Residences residenceId, String subject, short readFromSender, short readFromReceiver, short deletedFromSender, short deletedFromReceiver) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.residenceId = residenceId;
        this.readFromSender = readFromSender;
        this.readFromReceiver = readFromReceiver;
        this.deletedFromSender = deletedFromSender;
        this.deletedFromReceiver = deletedFromReceiver;
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

    public short getDeletedFromSender() {
        return deletedFromSender;
    }

    public void setDeletedFromSender(short deletedFromSender) {
        this.deletedFromSender = deletedFromSender;
    }

    public short getDeletedFromReceiver() {
        return deletedFromReceiver;
    }

    public void setDeletedFromReceiver(short deletedFromReceiver) {
        this.deletedFromReceiver = deletedFromReceiver;
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
        return "domain.Conversations[ id=" + id + " ]";
    }
}