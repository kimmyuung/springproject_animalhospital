package animalhospital.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

    RequestEntity findBymno (int mno);
    @Query( value = "select mid from request where hospital = :to" , nativeQuery = true )
    String findByhospital(String to);
}
