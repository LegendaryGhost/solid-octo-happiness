package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerificationCodePinRequest(
    String email,
    @JsonProperty("code_pin") String codePin
) { }
