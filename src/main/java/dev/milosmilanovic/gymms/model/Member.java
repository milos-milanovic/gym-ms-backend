package dev.milosmilanovic.gymms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.milosmilanovic.gymms.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Member {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(nullable = false)
    @NotBlank
    private String name; // full name
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    // email - not required
    private String email;
    @NotNull
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    @NotBlank
    private String phone; // phone number
    @NotNull
    private LocalDate expires; // membership expires

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private Set<GiftCode> giftCodes;
}
