package dev.milosmilanovic.gymms.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CheckGiftCodeResponse {

    // Code
    private String code;
    private LocalDate codeExpires;

    // Employee
    private Map<String, String> employee;

    // Member
    private Map<String, String> member;

}
