package com.skndan.resource;

import com.skndan.entity.Settings;
import com.skndan.repo.SettingsRepo;
import com.skndan.utils.EntityCopyUtils;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/v1/settings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Settings", description = "Operations on Settings resource.")
public class SettingsResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    SettingsRepo repo;

    @Inject
    EntityCopyUtils entityCopyUtils;

    @POST
    @Transactional
    public Response create(@Valid Settings setting) {
        System.out.println("JWT Subject: " + jwt.getSubject());
        setting.setProfileId(UUID.fromString(jwt.getSubject()));
        setting.persist();
        return Response.status(Response.Status.CREATED).entity(setting).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        Settings setting = repo.findById(id);
        if (setting == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure the user can only access their own workspaces
        UUID ownerId = UUID.fromString(jwt.getSubject());
        if (!setting.getProfileId().equals(ownerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(setting).build();
    }

    @GET
    public Response getByProfileId() {
        UUID profileId = UUID.fromString(jwt.getSubject());
        Settings workspaces = repo.find("profileId", profileId).firstResult();
        return Response.ok(workspaces).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(
            @PathParam("id") long id,
            @Valid Settings setting) {
        Settings existingGroup = repo.findById(id);

        if (existingGroup == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure the user can only update their own workspaces
        UUID ownerId = UUID.fromString(jwt.getSubject());
        if (!existingGroup.getProfileId().equals(ownerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        entityCopyUtils.copyProperties(existingGroup, setting);
        existingGroup.persist();

        return Response.ok(existingGroup).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") long id) {
        Settings setting = repo.findById(id);
        if (setting == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure the user can only delete their own workspaces
        UUID ownerId = UUID.fromString(jwt.getSubject());
        if (!setting.getProfileId().equals(ownerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        repo.delete(setting);
        return Response.noContent().build();
    }
}
