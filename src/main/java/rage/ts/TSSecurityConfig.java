package rage.ts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebMvcSecurity
public class TSSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("configuring ts security config");
        System.out.println("user details service: " + userDetailsService);

        SecurityContextHolder.setStrategyName("MODE_INHERITABLETHREADLOCAL");

        http.userDetailsService(userDetailsService).authorizeRequests()
                .antMatchers("/", "/index.html", "/static/**", "/app/login", "/app/auth*",
                        "/app/form*", "/app/test", "/app/login").permitAll()
                .anyRequest().authenticated()
                .and().rememberMe()
                .and().csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
        return userDetailsService;
    }
}
