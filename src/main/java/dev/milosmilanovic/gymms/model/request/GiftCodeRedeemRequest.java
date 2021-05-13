package dev.milosmilanovic.gymms.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCodeRedeemRequest {

    private String code;
    private UUID memberId;
    private String email;

}
