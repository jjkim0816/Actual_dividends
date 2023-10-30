package com.zerobase.dividends.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DividendDto {
	private LocalDateTime date;
	private String devidend;
}
