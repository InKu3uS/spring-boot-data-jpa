package com.neftali.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Archivo para configuracion: Se añade un directorio externo para guardar las imagenes que suba el usuario
@Configuration
public class MvcConfig implements WebMvcConfigurer{
	

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	}*/
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error/403").setViewName("error/403");
	}
}
