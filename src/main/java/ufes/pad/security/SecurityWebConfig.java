package ufes.pad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityWebConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().authorizeRequests()		
		.antMatchers("/css/**", "/img/**", "/javax.faces.resource/**", "/index.xhtml",
		"/cadastro_usuario.xhtml", "/ajuda.xhtml", "/esqueci_senha.xhtml").permitAll()		
		.anyRequest().authenticated();
		
		http.formLogin().loginPage("/index.xhtml").permitAll()
		.defaultSuccessUrl("/pacientes/painel_de_controle.xhtml")
		.failureUrl("/index.xhtml?error=true");
		
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		//auth.inMemoryAuthentication()
		//.withUser("Andre").password("123").roles("ADMIN");
		
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
