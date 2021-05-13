package dev.milosmilanovic.gymms.data.repository;

import dev.milosmilanovic.gymms.model.GiftCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GiftCodeRepository extends CrudRepository<GiftCode, Long> {

    public Optional<GiftCode> findByCode(Long id);

}
