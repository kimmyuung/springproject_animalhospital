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

    @ManyToOne // 메시지 받는 사람
    @JoinColumn(name = "frommno")
    MemberEntity toentity;

    // 보내는 사람은 운영자로 고정되어 있으므로 굳이 컬럼으로 잡지 않음

}
