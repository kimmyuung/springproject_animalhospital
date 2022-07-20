package animalhospital.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {


    @Query( value = "select * from message where toentity = :mno and msgtype = :type order by msgno desc", nativeQuery = true)
    List<MessageEntity> gettomsglist (int mno,@Param("type") int type);

    @Query( value = "select * from message where fromentity = :mno and msgtype = :type order by msgno desc", nativeQuery = true)
    List<MessageEntity> getfrommsglist(int mno, int type);

    @Query(value = "select count(*) from message where tomno = :mno and isread = 0", nativeQuery = true)
    Integer getisread(@Param("mno") int mno);
}
