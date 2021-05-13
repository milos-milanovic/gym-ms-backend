package dev.milosmilanovic.gymms.controller;

import dev.milosmilanovic.gymms.data.service.MemberService;
import dev.milosmilanovic.gymms.model.Member;
import dev.milosmilanovic.gymms.model.response.MemberBasicInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;


    @GetMapping("/api/members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/api/members/{id}")
    public Member findById(@PathVariable("id") String inputId) {
        try {
            UUID id = UUID.fromString(inputId);
            return memberService.findById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID (UUID) format is not valid");
        }
    }

    @PostMapping("/api/members")
    public void insertMember(@RequestBody Member member) {
        member.setId(null);
        memberService.insertMember(member);
    }

    @PutMapping("/api/members")
    public void updateMember(@RequestBody Member member) {
        memberService.updateMember(member);
    }

    @GetMapping("/api/members/email/{email}")
    public MemberBasicInfoResponse findByEmail(@PathVariable("email") String emailInput) {
        return memberService.findByEmail(emailInput);
    }

    // Find member by: ID, Name, E-mail or Phone number
    @GetMapping("/api/members/search")
    public List<MemberBasicInfoResponse> searchMember(@RequestParam("q") String searchInput) {
        return memberService.searchMember(searchInput);
    }

}
