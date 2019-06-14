package com.neu.webapp.repositories;

import com.neu.webapp.models.Cover;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CoverRepository extends CrudRepository<Cover, UUID> {
}
