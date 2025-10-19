package com.skndan.repo;

import com.skndan.entity.Paged;
import com.skndan.entity.Settings;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SettingsRepo extends BaseRepo<Settings, Long> implements PanacheRepository<Settings> {

    /**
     * Find workspaces owned by a specific user
     *
     * @param profileId UUID of the workspace owner
     * @param page    Page number for pagination
     * @param size    Number of items per page
     * @return Paged list of workspaces
     */
    public Paged<Settings> findByOwnerId(UUID profileId, int page, int size) {
        PanacheQuery<Settings> query = find("profileId", profileId);
        long total = query.count();
        List<Settings> results = query.page(Page.of(page, size)).list();
        return new Paged<>(results, total, page, size);
    }
}