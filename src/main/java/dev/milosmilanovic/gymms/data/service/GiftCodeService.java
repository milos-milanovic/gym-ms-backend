package dev.milosmilanovic.gymms.data.service;

import dev.milosmilanovic.gymms.data.repository.EmployeeRepository;
import dev.milosmilanovic.gymms.data.repository.GiftCodeRepository;
import dev.milosmilanovic.gymms.data.repository.InsertRepository;
import dev.milosmilanovic.gymms.data.repository.MemberRepository;
import dev.milosmilanovic.gymms.model.Employee;
import dev.milosmilanovic.gymms.model.GiftCode;
import dev.milosmilanovic.gymms.model.Member;
import dev.milosmilanovic.gymms.model.response.CheckGiftCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class GiftCodeService {

    public final Predicate<String> VALIDATE_EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).asPredicate();

    @Autowired
    private InsertRepository insertRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GiftCodeRepository giftCodeRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Random random;


    public CheckGiftCodeResponse getGiftCode(long inputCode) {
        GiftCode code = giftCodeRepository.findByCode(inputCode).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gift code is not valid"));

        CheckGiftCodeResponse res = new CheckGiftCodeResponse();
        res.setCode(String.valueOf(code.getCode()));
        res.setCodeExpires(code.getExpires());

        Employee codeEmployee = code.getEmployee();
        Map<String, String> emp = new HashMap<>();
        emp.put("id", codeEmployee.getId().toString());
        emp.put("name", codeEmployee.getName());
        emp.put("email", codeEmployee.getEmail());
        emp.put("phone", codeEmployee.getPhone());

        res.setEmployee(emp);


        Member codeMember = code.getMember();
        // Since gift code can be unused it is checked
        if (codeMember != null) {
            // gift code custom member data - not all data
            Map<String, String> mem = new HashMap<>();
            mem.put("id", codeMember.getId().toString());
            mem.put("name", codeMember.getName());
            mem.put("email", codeMember.getEmail());
            mem.put("phone", codeMember.getPhone());

            res.setMember(mem);
        }

        return res;
    }

    public Map<String, String> generateGiftCode(String inputEmail) {
        String email = null; // trimmed email

        if (inputEmail != null) {
            email = inputEmail.trim();
            if (!VALIDATE_EMAIL.test(email)) {
                // ***** FOR TESTING ONLY *****
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail is not valid");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sending an email in test environment is disabled. Leave the input field empty to generate a gift code.");
            }
        }

        // TODO: Example data until Spring Security is implemented and throw exc. if employee not valid...
        // TEST Employee data - Employee who generated the gift code
        Optional<Employee> optionalEmployee = employeeRepository.findById(UUID.fromString("306fd134-533e-412d-ba5e-dde044658dbe"));
        Employee employee = optionalEmployee.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not valid"));

        long code = Math.abs(random.nextLong());
        LocalDate expires = LocalDate.now().plusMonths(3);
        GiftCode giftCode = new GiftCode(code, expires, employee);

        if (email != null) {
            emailService.send(email, emailService.constructGiftCodeMessage(code, expires));
        }

        insertRepository.insertGiftCode(giftCode);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", String.valueOf(giftCode.getCode()));

        return responseMap;
    }

    public void redeemGiftCode(String code, UUID memberId) {
        // NOTE: for now there is no check if both parameters are passed so if they are member id is used
        if (memberId != null) {
            // checking if member is valid
            Optional<Member> optionalMember = memberRepository.findById(UUID.fromString(memberId.toString().trim()));
            Member member = optionalMember.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Member with ID: '%s' does not exist", memberId)));
            // checking if gift code is valid
            GiftCode giftCode = getGiftCodeAndCheck(Long.parseLong(code));

            applyGiftCode(member, giftCode);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "In order to redeem the code a member ID or email must be provided");
        }

    }


    /***** HELPER METHODS *****/

    private void applyGiftCode(Member member, GiftCode giftCode) {
        // TODO: plusMonths() shouldn't be hard coded '1'
        member.setExpires(member.getExpires().plusMonths(1));
        giftCode.setMember(member);

        memberRepository.save(member);
        giftCodeRepository.save(giftCode);
    }

    // checking if gift code is valid
    private GiftCode getGiftCodeAndCheck(long code) {
        Optional<GiftCode> optionalGiftCode = giftCodeRepository.findByCode(code);
        GiftCode giftCode = optionalGiftCode.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gift code is not valid"));
        if (giftCode.getMember() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("This code is already applied to member: %s (%s)", giftCode.getMember().getName(), giftCode.getMember().getId()));
        }
        if (giftCode.getExpires().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gift code expired");
        }
        return giftCode;
    }


}
