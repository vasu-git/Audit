package test.service.Audit;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AuditEntries, Long>{

}
