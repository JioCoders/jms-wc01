package com.jiocoders.empservice.service.api;

import com.jiocoders.empservice.entity.Tutorial;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ITutorialService {

    public abstract Flux<Tutorial> findAll();

    public abstract Flux<Tutorial> findByTitleContaining(String title);

    public abstract Mono<Tutorial> findById(String id);

    public abstract Mono<Tutorial> save(Tutorial tutorial);

    public abstract Mono<Tutorial> update(String id, Tutorial tutorial);

    public abstract Mono<Void> deleteById(String id);

    public abstract Mono<Void> deleteAll();

    public abstract Flux<Tutorial> findByPublished(boolean isPublished);
}
