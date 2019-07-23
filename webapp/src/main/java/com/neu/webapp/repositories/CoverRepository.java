package com.neu.webapp.repositories;

import com.neu.webapp.models.Cover;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoverRepository extends CrudRepository<Cover, UUID> {
}
