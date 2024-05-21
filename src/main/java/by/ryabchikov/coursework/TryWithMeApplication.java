package by.ryabchikov.coursework;

import by.ryabchikov.coursework.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class TryWithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TryWithMeApplication.class, args);
	}

}
