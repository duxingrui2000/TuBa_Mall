package com.tuba.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tuba.mall.dao")
public class TubaMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubaMallApplication.class, args);
	}

}
