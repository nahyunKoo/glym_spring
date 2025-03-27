package gllim.gllim_spring.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private final String message;
    private final String username;
}
