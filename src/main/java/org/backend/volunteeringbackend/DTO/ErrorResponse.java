package org.backend.volunteeringbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public static ErrorResponse of(int status, String message) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}