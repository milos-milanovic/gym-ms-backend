package dev.milosmilanovic.gymms.data.service;

import dev.milosmilanovic.gymms.data.repository.InsertRepository;
import dev.milosmilanovic.gymms.data.repository.MemberRepository;
import dev.milosmilanovic.gymms.model.Member;
import dev.milosmilanovic.gymms.model.response.MemberBasicInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Service
public class MemberService {

    public final Predicate<String> VALIDATE_EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).asPredicate();
    public final Predicate<String> VALIDATE_UUID =
            Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE).asPredicate();
    public final Predicate<String> VALIDATE_STARTS_AS_PHONE_NUMBER =
            Pattern.compile("^\\+\\d+$", Pattern.CASE_INSENSITIVE).asPredicate();

    private final MemberRepository memberRepository;
    private final InsertRepository memberInsertRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, InsertRepository memberInsertRepository) {
        this.memberRepository = memberRepository;
        this.memberInsertRepository = memberInsertRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member findById(UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member with ID: '%s' does not exist", id.toString())));
    }

    public void insertMember(Member member) {
        if (!VALIDATE_EMAIL.test(member.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail is not valid");
        }

        memberInsertRepository.insertMember(member);
    }

    public void updateMember(Member inputMember) {
        Optional<Member> optionalMember = memberRepository.findById(inputMember.getId());
        Member foundMember = optionalMember.orElseThrow(notFoundExceptionSupplier(inputMember));

        // ID is not be editable
        foundMember.setName(inputMember.getName());
        foundMember.setGender(inputMember.getGender());
        foundMember.setEmail(inputMember.getEmail());
        foundMember.setDateOfBirth(inputMember.getDateOfBirth());
        foundMember.setPhone(inputMember.getPhone());
        foundMember.setExpires(inputMember.getExpires());

        memberRepository.save(foundMember);
    }

    private Supplier<ResponseStatusException> notFoundExceptionSupplier(Member m) {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member with ID: '%s' does not exist", m.getId()));
    }

    public MemberBasicInfoResponse findByEmail(String emailInput) {
        String email = emailInput.trim();
        Member m = memberRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member with e-mail %s does not exist", email)));
        return new MemberBasicInfoResponse(
                m.getId(),
                m.getName(),
                m.getGender().name(),
                m.getEmail(),
                m.getDateOfBirth().toString(),
                m.getPhone(),
                m.getExpires().toString()
        );
    }

    public List<MemberBasicInfoResponse> searchMember(String input) {
        String inputTrimmed = input.trim();

        List<Member> databaseResult;

        if (VALIDATE_STARTS_AS_PHONE_NUMBER.test(inputTrimmed)) {
            databaseResult = memberRepository.searchMemberByPhone(inputTrimmed);
        } else if (VALIDATE_UUID.test(inputTrimmed)) {
            databaseResult = memberRepository.findAllById(UUID.fromString(inputTrimmed));
        } else {
            databaseResult = memberRepository.searchMemberByNameOrEmail(inputTrimmed.toLowerCase());
        }

        List<MemberBasicInfoResponse> searchResult = new ArrayList<>();
        databaseResult.stream().forEach(m -> searchResult.add(new MemberBasicInfoResponse(m.getId(), m.getName(), m.getGender().name(), m.getEmail(), m.getDateOfBirth().toString(), m.getPhone(), m.getExpires().toString())));

        return searchResult;
    }
}
