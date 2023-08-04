package ru.pyrinoff.chatjobparser.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import ru.pyrinoff.chatjobparser.service.PropertyService;

@EnableWebMvc
@ComponentScan(basePackages = {"ru.pyrinoff.chatjobparser.controller"})
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@DependsOn("propertyService")
public class WebMvcConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PropertyService propertyService;

    //@Autowired
    //private UserDetailsService userDetailsService;

    @Bean
    public InternalResourceViewResolver resolver() {
        @NotNull final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/includes/**").addResourceLocations("/includes/");
        registry.addResourceHandler("/includes/**").addResourceLocations("/includes/");
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }*/

  /*  @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/

    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        try {
            auth
                    .inMemoryAuthentication()
                    .withUser(propertyService.getUserLogin()).password(passwordEncoder().encode(propertyService.getUserPassword())).roles("USER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/dataset").permitAll()
                .antMatchers("/includes/*").permitAll()
                //.antMatchers("/upload").hasAnyRole("USER")
                //.and().formLogin()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/login").permitAll()
                //.and().exceptionHandling().authenticationEntryPoint(new ServiceAuthenticationEntryPoint())
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/auth")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
                .and().logout().permitAll().logoutSuccessUrl("/login")
                .and().csrf().disable()
        ;
    }

}
