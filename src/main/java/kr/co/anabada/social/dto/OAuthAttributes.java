package kr.co.anabada.social.dto;

import java.util.Map;

public class OAuthAttributes {

    private final String name;
    private final String email;
    private final String provider;
    private final Map<String, Object> attributes;

    public OAuthAttributes(String name, String email, String provider, Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.attributes = attributes;
    }

    public static OAuthAttributes ofGoogle(String registrationId, Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        return new OAuthAttributes(name, email, registrationId, attributes);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
