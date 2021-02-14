package lab2.repository;

import lab2.model.PurchaseDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDbRepository extends JpaRepository<PurchaseDb, Integer> { }
