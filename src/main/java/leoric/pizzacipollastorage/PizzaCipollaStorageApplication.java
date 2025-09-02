package leoric.pizzacipollastorage;

import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class PizzaCipollaStorageApplication {

	private final static String ADMIN = "ADMIN";
	private final static String MANAGER = "MANAGER";
	private final static String EMPLOYEE = "EMPLOYEE";
	private final static String BRANCH_MANAGER = "BRANCH_MANAGER";
	private final static String BRANCH_EMPLOYEE = "BRANCH_EMPLOYEE";

	public static void main(String[] args) {
		SpringApplication.run(PizzaCipollaStorageApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(ADMIN).isEmpty()) {
				roleRepository.save(Role.builder().name(ADMIN).build());
			}
			if (roleRepository.findByName(MANAGER).isEmpty()) {
				roleRepository.save(Role.builder().name(MANAGER).build());
            }
			if (roleRepository.findByName(EMPLOYEE).isEmpty()) {
				roleRepository.save(Role.builder().name(EMPLOYEE).build());
			}
			if (roleRepository.findByName(BRANCH_MANAGER).isEmpty()) {
				roleRepository.save(Role.builder().name(BRANCH_MANAGER).build());
			}
			if (roleRepository.findByName(BRANCH_EMPLOYEE).isEmpty()) {
				roleRepository.save(Role.builder().name(BRANCH_EMPLOYEE).build());
			}
		};
	}
}