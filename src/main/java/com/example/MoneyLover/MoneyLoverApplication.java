package com.example.MoneyLover;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableScheduling
public class MoneyLoverApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepo;

	public static void main(String[] args) {
		SpringApplication.run(MoneyLoverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User userFound = userRepo.findTopByEmail("nguyentienngoc7166@gmail.com");
		if(userFound==null)
		{
			User user = new User();
			user.set_enable(true);
			user.setPassword(new BCryptPasswordEncoder().encode("123456"));
			user.setEmail("nguyentienngoc7166@gmail.com");
			user.setUsername("ngoc");
			userRepo.save(user);
		}

	}
}
