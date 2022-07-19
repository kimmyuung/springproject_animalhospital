package animalhospital.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="count")
public class CountEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cnum;
    private int count ;

    private LocalDate createdate;

}
