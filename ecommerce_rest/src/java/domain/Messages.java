package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "messages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m"),
    @NamedQuery(name = "Messages.findById", query = "SELECT m FROM Messages m WHERE m.id = :id"),
    @NamedQuery(name = "Messages.findByBody", query = "SELECT m FROM Messages m WHERE m.body = :body"),
    @NamedQuery(name = "Messages.findByTimestamp", query = "SELECT m FROM Messages m WHERE m.timestamp = :timestamp"),
    
    /** CUSTOM **/
    @NamedQuery(name = "Messages.findByUserId", query = "SELECT m FROM Messages m WHERE m.userId.id = :id"),
    @NamedQuery(name = "findByConversation", query = "SELECT m FROM Messages m WHERE m.conversationId.id =:conversationId order by m.timestamp ASC")
})
public class Messages implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted_from_sender")
    private short deletedFromSender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted_from_receiver")
    private short deletedFromReceiver;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Conversations conversationId;

    public Messages() {
    }

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

    public void setConversationId(Conversations conversationId) {
        this.conversationId = conversationId;
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
        return "domain.Messages[ id=" + id + " ]";
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
}