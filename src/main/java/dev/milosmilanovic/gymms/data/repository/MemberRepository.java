package dev.milosmilanovic.gymms.data.repository;

import dev.milosmilanovic.gymms.model.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends CrudRepository<Member, UUID> {

    public List<Member> findAll();

    public Optional<Member> findById(UUID id);

    public List<Member> findAllById(UUID id);

    public Optional<Member> findByEmail(String email);

    @Query("FROM Member m WHERE lower(m.name) LIKE ?1% OR lower(m.email) LIKE ?1%")
    public List<Member> searchMemberByNameOrEmail(String input);

    @Query("FROM Member m WHERE m.phone LIKE ?1%")
    public List<Member> searchMemberByPhone(String input);


}
