package animalhospital.domain.shop;

import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.shop.ShopEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name= "shoplike")
public class ShopLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shoplikeid;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberlike;

    @ManyToOne
    @JoinColumn(name = "sno")
    private ShopEntity shoplike;

}