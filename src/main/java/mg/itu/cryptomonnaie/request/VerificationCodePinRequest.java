package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VerificationCodePinRequest {
    private String email;

    @JsonProperty("code_pin")
    private String codePin;
}
