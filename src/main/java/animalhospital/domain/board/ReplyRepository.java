package animalhospital.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    ReplyEntity findByboardEntity(BoardEntity boardEntity);

    @Query(value = "select * from reply where bno = :bno", nativeQuery = true)
    List<ReplyEntity> findbybno(int bno);
}
