package com.example.chatti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponse {
    private String to;
    private String from;
    private String txt;
    private String subject;
}
