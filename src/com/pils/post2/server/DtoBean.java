package com.pils.post2.server;

import com.pils.post2.shared.dto.*;

import java.util.List;

public interface DtoBean {

	SessionUser login(String name, String password);

	void addEntity(Entity entity);

	List<Section> fetchSections(User user);

	List<User> fetchUsers(String query);

	List<Tag> fetchTags(String query);

	List<? extends Entity> lightSearch(String query);

	EntitiesList search(String query, int from, int number);

	EntitiesList fetchEntities(Entity parent, int from, int number);

	Entry fetchEntry(long entryId);
}
