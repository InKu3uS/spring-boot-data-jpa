package com.neftali.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.neftali.springboot.app.auth.handler.LoginSuccessHandler;
import com.neftali.springboot.app.service.JpaUserDetailsService;

//Anotacion que activa la seguridad para usar la anotación @Secured en otras clases
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {
	
	@Autowired
	//Se importa el loginSuccessHandler 
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}

	

	/**
	@Bean
	//Creacion de usuarios en memoria para pruebas
	public UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("Nuria").password(passwordEncoder.encode("nuria")).roles("USER").build());

		manager.createUser(
				User.withUsername("Nefta").password(passwordEncoder.encode("nefta")).roles("ADMIN", "USER").build());

		return manager;
	}
	*/
	
	/*
	@Autowired
	public void ConfigureGlobal(AuthenticationManagerBuilder build) throws Exception {
		//Usando jdbc
		build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
		
		
	}
	*/
	

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                		//URL's a las que tienen acceso los diferentes tipos de roles
                
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale").permitAll()
                        //Manera sin usar anotaciones
                        //.requestMatchers("/ver/**", "/uploads/**").hasRole("USER")
                        //.requestMatchers("/form/**", "/eliminar/**", "/factura/**").hasAnyRole("ADMIN")                        
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
