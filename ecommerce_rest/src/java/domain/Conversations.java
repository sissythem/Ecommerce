package domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "conversations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conversations.findAll", query = "SELECT c FROM Conversations c"),
    @NamedQuery(name = "Conversations.findById", query = "SELECT c FROM Conversations c WHERE c.id = :id"),
    @NamedQuery(name = "Conversations.findBySubject", query = "SELECT c FROM Conversations c WHERE c.subject = :subject"),
})
public class Conversations implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted_from_sender")
    private short deletedFromSender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted_from_receiver")
    private short deletedFromReceiver;

    @Basic(optional = false)
    @NotNull
    @Column(name = "read_from_sender")
    private short readFromSender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "read_from_receiver")
    private short readFromReceiver;

    @JoinColumn(name = "residence_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Residences residenceId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "subject")
    private String subject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversationId")
    private Collection<Messages> messagesCollection;
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users senderId;
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
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

    @XmlTransient
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
        return "domain.Conversations[ id=" + id + " ]";
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
}