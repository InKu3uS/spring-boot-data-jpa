package com.neftali.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.neftali.springboot.app.auth.handler.LoginSuccessHandler;

@Configuration
public class SpringSecurityConfig {
	
	@Autowired
	//Se importa el loginSuccessHandler 
	private LoginSuccessHandler successHandler;

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("User").password(passwordEncoder().encode("user")).roles("USER").build());

		manager.createUser(
				User.withUsername("Admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());

		return manager;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                		//URL's a las que tienen acceso los diferentes tipos de roles
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                        .requestMatchers("/ver/**", "/uploads/**").hasRole("USER")
                        .requestMatchers("/form/**", "/eliminar/**", "/factura/**").hasAnyRole("ADMIN")                        
                        .anyRequest().authenticated()
                        
        )
                .formLogin(formLogin ->
                        formLogin
                        		//Se carga el successHandler para que el formulario de login envie mensaje al iniciar sesion 
                        		.successHandler(successHandler)
                        		.loginPage("/login")
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .exceptionHandling((exception ->
                exception.accessDeniedPage("/error/403"))
                );

        return http.build();
    }

}
