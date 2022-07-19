package animalhospital.domain;

import org.springframework.data.jpa.repository.JpaRepository;



public interface CountingRepository extends JpaRepository<CountEntity,Integer> {

}
