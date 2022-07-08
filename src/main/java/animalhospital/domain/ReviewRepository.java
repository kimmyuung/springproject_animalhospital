package animalhospital.domain;

import animalhospital.domain.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {
    @Query( value = "select * from review" , nativeQuery = true )
    Page<ReviewEntity> findByrlist(Pageable pageable);
}