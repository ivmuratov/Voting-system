package com.votingsystem.springboot.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class UserTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Name user", example = "New user")
    @NotBlank
    @Size(max = 40)
    private String name;

    @Schema(description = "Email user", example = "new.email@email.com")
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Schema(description = "Password user", example = "123456")
    @NotBlank
    @Size(min = 5, max = 32)
    private String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }
}