package animalhospital.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

    RequestEntity findBymno (int mno);
}
