package com.example.demo.repo;

import com.example.demo.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationsRepo extends JpaRepository<Locations,String> {
}
