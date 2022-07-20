package animalhospital.domain.message;

import animalhospital.domain.BaseTime;
import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="message")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int msgno;
    private String msg;
    private boolean isread;
    private int msgtype;

    @ManyToOne
    @JoinColumn(name="fromentity")
    MemberEntity fromentity;

    @ManyToOne
    @JoinColumn(name="toentity")
    MemberEntity toentity;

}
