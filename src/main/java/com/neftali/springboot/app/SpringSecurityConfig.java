package com.neftali.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());

		manager.createUser(
				User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());

		return manager;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
						.requestMatchers("/ver/**").hasRole("USER").requestMatchers("/uploads/**").hasRole("USER")
						.requestMatchers("/form/**").hasAnyRole("ADMIN").requestMatchers("/eliminar/**")
						.hasAnyRole("ADMIN").requestMatchers("/factura/**").hasAnyRole("ADMIN").anyRequest()
						.authenticated().and().formLogin(login -> {
							try {
								login.loginPage("/login").permitAll().and().logout(logout -> logout.permitAll());
							} catch (Exception e) {

								e.getMessage();
							}
						}

						);
			} catch (Exception e) {
				e.getMessage();
			}
		});
		return http.build();
	}

}
