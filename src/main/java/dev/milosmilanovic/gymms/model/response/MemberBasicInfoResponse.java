package dev.milosmilanovic.gymms.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Member info response without gift code
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberBasicInfoResponse {

    private UUID id;
    private String name;
    private String gender;
    private String email;
    private String dateOfBirth;
    private String phone;
    private String expires;

}
