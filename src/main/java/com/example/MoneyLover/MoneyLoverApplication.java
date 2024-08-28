package com.example.MoneyLover;

import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Icon.Entity.Icon;
import com.example.MoneyLover.infra.Icon.Repository.IconRepo;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map.Entry;
import java.util.Map;


@SpringBootApplication
@EnableScheduling
public class MoneyLoverApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private IconRepo iconRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	public static void main(String[] args) {
		SpringApplication.run(MoneyLoverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(userRepo.count()==0)
		{
			User user = new User();
			user.set_enable(true);
			user.setPassword(new BCryptPasswordEncoder().encode("123456"));
			user.setEmail("gmailtest@gmail.com");
			user.setUsername("ngoc");
			userRepo.save(user);
		}else{
			System.out.println("User already exist");
		}

		Map<String, String> icons = Map.ofEntries(
				Map.entry("Tea & milk tea", "https://img.icons8.com/?size=100&id=6CJuMCbc084c&format=png&color=000000"),
				Map.entry("Bills", "https://img.icons8.com/?size=100&id=31106&format=png&color=000000"),
				Map.entry("Other", "https://img.icons8.com/?size=100&id=31127&format=png&color=000000"),
				Map.entry("Cloth", "https://img.icons8.com/?size=100&id=31111&format=png&color=000000"),
				Map.entry("jewelry", "https://img.icons8.com/?size=100&id=31139&format=png&color=000000"),
				Map.entry("Home service", "https://img.icons8.com/?size=100&id=21240&format=png&color=000000"),
				Map.entry("School", "https://img.icons8.com/?size=100&id=31183&format=png&color=000000"),
				Map.entry("Kitchen", "https://img.icons8.com/?size=100&id=31141&format=png&color=000000"),
				Map.entry("Business", "https://img.icons8.com/?size=100&id=31188&format=png&color=000000"),
				Map.entry("Gaming", "https://img.icons8.com/?size=100&id=Ig90y3QoWr6s&format=png&color=000000"),
				Map.entry("Travel", "https://img.icons8.com/?size=100&id=24373&format=png&color=000000"),
				Map.entry("entertainment", "https://img.icons8.com/?size=100&id=56537&format=png&color=000000"),
				Map.entry("Health & fitness", "https://img.icons8.com/?size=100&id=23107&format=png&color=000000"),
				Map.entry("Health care", "https://img.icons8.com/?size=100&id=51823&format=png&color=000000"),
				Map.entry("Coffee", "https://img.icons8.com/?size=100&id=35901&format=png&color=000000"),
				Map.entry("Bills water", "https://img.icons8.com/?size=100&id=30162&format=png&color=000000"),
				Map.entry("Bills electric", "https://img.icons8.com/?size=100&id=30103&format=png&color=000000"),
				Map.entry("Education", "https://img.icons8.com/?size=100&id=1tfwd1tt4Nmd&format=png&color=000000"),
				Map.entry("Pets", "https://img.icons8.com/?size=100&id=30921&format=png&color=000000"),
				Map.entry("Contact", "https://img.icons8.com/?size=100&id=11904&format=png&color=000000")
		);

		Map<String, String> iconsIncome = Map.ofEntries(
				Map.entry("Salary", "https://img.icons8.com/?size=100&id=40585&format=png&color=000000"),
				Map.entry("Other Income", "https://img.icons8.com/?size=100&id=vfv1AbUEfpHl&format=png&color=000000"),
				Map.entry("Incoming transfer", "https://img.icons8.com/?size=100&id=71R8z5qsbvOU&format=png&color=000000")
		);

		Map<String, String> iconsDebLoan = Map.ofEntries(
				Map.entry("Loan", "https://img.icons8.com/?size=100&id=CtlpOMb8eXWe&format=png&color=000000"),
				Map.entry("Debt", "https://img.icons8.com/?size=100&id=cLb09JdeKCFr&format=png&color=000000")
				);

		// Check if the table is empty
		if (iconRepo.count() == 0) {
			// Loop through the map and save each icon with name and path
			for (Entry<String, String> entry : icons.entrySet()) {
				Icon icon = new Icon(null, entry.getKey(), entry.getValue());
				iconRepo.save(icon);
			}



			System.out.println("Default icons inserted");
		} else {
			System.out.println("Icons table already contains data");
		}

		if(categoryRepo.count()==0){
			for (Entry<String, String> entry : icons.entrySet()) {
				Category Category = new Category( entry.getKey(), CategoryType.Expense,entry.getValue(),false,0);
				categoryRepo.save(Category);
			}

			for (Entry<String, String> entry : iconsIncome.entrySet()) {
				Category Category = new Category( entry.getKey(), CategoryType.Expense,entry.getValue(),true,0);
				categoryRepo.save(Category);
			}

			for (Entry<String, String> entry : iconsDebLoan.entrySet()) {
				Category Category = new Category( entry.getKey(), CategoryType.Expense,entry.getValue(),true,0);
				categoryRepo.save(Category);
			}

			System.out.println("Default category inserted");

		}else{
			System.out.println("Category table already contains data");
		}
	}

}
