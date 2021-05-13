package dev.milosmilanovic.gymms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class GiftCode {

    @Id
    private Long code;

    @NotNull
    private LocalDate expires;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // code redeemed to

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public GiftCode(Long code, LocalDate expires, Employee employee) {
        this.code = code;
        this.expires = expires;
        this.employee = employee;
    }
}
