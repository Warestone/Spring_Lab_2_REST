package lab2.repository;

import lab2.model.PurchaseRest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRestRepository extends JpaRepository<PurchaseRest, Integer> { }
