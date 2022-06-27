package animalhospital.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder@ToString
@Entity
@Table(name="review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    private int rno;
    private int mno;
    private String rcontent;
    private String rimg;
    private String hinfo1;
    private String hinfo2;
    private String hinfo3;
    private String rdate;
    private int rstar;
}
