package dev.milosmilanovic.gymms.data.repository;

import dev.milosmilanovic.gymms.model.GiftCode;
import dev.milosmilanovic.gymms.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public class InsertRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberRepository memberRepository;

    public void insertMember(Member m) {
        // Checking if email is in use already
        Optional<Member> findByEmail = memberRepository.findByEmail(m.getEmail());
        if (findByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail address is already in use");
        }

        String sql = "INSERT INTO member (name, gender, email, date_of_birth, phone, expires) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, m.getName(), m.getGender().name(), m.getEmail(), m.getDateOfBirth(), m.getPhone(), m.getExpires());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred. Member not added");
        }
    }


    public void insertGiftCode(GiftCode g) {
        String sql = "INSERT INTO gift_code (code, expires, employee_id) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, g.getCode(), g.getExpires(), g.getEmployee().getId());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred.");
        }
    }
}
