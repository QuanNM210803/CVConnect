package com.cvconnect.config.oauth2;

import com.cvconnect.entity.User;
import com.cvconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    String currentProvider = existingUser.getProvider();
                    if (currentProvider == null || currentProvider.isEmpty()) {
                        existingUser.setProvider("GOOGLE");
                    } else {
                        List<String> providers = new ArrayList<>(Arrays.asList(currentProvider.split(",")));
                        if (!providers.contains("GOOGLE")) {
                            providers.add("GOOGLE");
                            existingUser.setProvider(String.join(",", providers));
                        }
                    }
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider("GOOGLE");
                    return userRepository.save(newUser);
                });
        return user;
    }
}
