package animalhospital.domain.shop;

import animalhospital.domain.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {

    @Query( value = "select * from shop where sno = :sno" , nativeQuery = true )
    Page<ShopEntity> findByblist(int sno, Pageable pageable);

    @Query( value = "select * from shop where sno = :sno" , nativeQuery = true )
    List<ShopEntity> findbynoticelist(int sno, Pageable pageable);

  //  ShopEntity findBybno(int bno );

}
