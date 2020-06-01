package es.uned.foederis;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("redirect:/eventos");
		registry.addViewController("/").setViewName("redirect:/eventos");
		registry.addViewController("/Evento/listarFiltro").setViewName("redirect:/eventos");
		registry.addViewController("/eventos").setViewName("forward:/Evento/listarFiltro");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/chat").setViewName("chat");
		registry.addViewController("/upload").setViewName("upload");
		registry.addViewController("/administracion").setViewName("home");
		registry.addViewController("/administracion/usuario").setViewName("home");
		registry.addViewController("/administracion/sala").setViewName("home");
	}
}
