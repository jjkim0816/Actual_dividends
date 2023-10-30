package com.zerobase.dividends.persist.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DIVIDEND")
@Builder
public class DividendEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long companyId;
	
	private LocalDateTime date;
	
	private String dividend;
}
