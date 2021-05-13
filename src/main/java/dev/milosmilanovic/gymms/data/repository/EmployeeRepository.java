package dev.milosmilanovic.gymms.data.repository;

import dev.milosmilanovic.gymms.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {

    public Optional<Employee> findById(UUID id);

}
