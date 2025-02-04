package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerificationCodePinRequest {
    private String email;

    @JsonProperty("code_pin")
    private String codePin;
}
