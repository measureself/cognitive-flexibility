package rage.ts.service;

import java.util.Arrays;
import javax.servlet.ServletContext;
import rage.ts.domain.Participant;
import rage.ts.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String contextPath = servletContext.getContextPath();

        Participant participant = participantRepository.findByUsernameAndContextPath(username, contextPath);
        if (participant == null) {
            participant = new Participant();
            participant.setUsername(username);
            participant.setContextPath(contextPath);
            participant.setPassword("");
            participant = participantRepository.save(participant);
            participantRepository.flush();
        }

        return new org.springframework.security.core.userdetails.User(
                participant.getUsername(),
                participant.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
