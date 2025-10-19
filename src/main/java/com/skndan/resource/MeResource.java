package com.skndan.resource;


import com.skndan.entity.Settings;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.skndan.entity.Profile;
import com.skndan.repo.ProfileRepo;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/v1/me")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
public class MeResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    ProfileRepo repo;

    @ConfigProperty(name = "chimera.default.model", defaultValue = "openai/gpt-4o-mini")
    String defaultModel;

    @GET
    @Transactional
    public Response getOrCreateProfile() {
        String uid = jwt.getSubject();
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        Profile profile = repo.findByUid(uid);
        if (profile == null) {
            profile = new Profile();
            profile.email = email;
            profile.name = name;
            profile.id = uid;
            profile.persistAndFlush();

            Settings settings = new Settings();
            settings.setProfileId(UUID.fromString(profile.id));
            settings.setModelName(defaultModel);
            settings.persistAndFlush();
        }

        return Response.ok(profile).build();
    }
}