package ru.pyrinoff.chatjobparser.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.pyrinoff.chatjobparser.service.PropertyService;

@Configuration
//@Import(SpringApplicationConfiguration.class) //pre-initialization
//@ComponentScan(basePackages = {"ru.pyrinoff.chatjobparser"})
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    PropertyService propertyService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //admin zone
                                .requestMatchers("/admin/**", "/swagger-ui/**")
                                    .hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                                formLogin
                                        .loginPage("/login")
                                        .loginProcessingUrl("/login")
                                        .defaultSuccessUrl("/admin", true)
                                        .failureUrl("/login?error=true")
                        //.permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername(propertyService.getUserLogin())
                .password(passwordEncoder().encode(propertyService.getUserPassword()))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


