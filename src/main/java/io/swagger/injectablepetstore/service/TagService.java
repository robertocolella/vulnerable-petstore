package io.swagger.injectablepetstore.service;

import io.swagger.injectablepetstore.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService implements TagServiceInterface {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void deleteAll() {
        tagRepository.reset();
    }
}
