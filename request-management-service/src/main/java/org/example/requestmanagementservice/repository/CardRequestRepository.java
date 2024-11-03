package org.example.requestmanagementservice.repository;

import org.example.requestmanagementservice.entity.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
}
