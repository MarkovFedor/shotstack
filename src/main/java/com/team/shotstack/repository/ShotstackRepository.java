package com.team.shotstack.repository;

import com.team.shotstack.entity.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShotstackRepository extends CrudRepository<Video, Long> {
}
