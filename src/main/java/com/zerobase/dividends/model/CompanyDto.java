package com.zerobase.dividends.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyDto {
    private String ticker;
    private String name;
}
