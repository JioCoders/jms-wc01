package com.harry.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harry.userservice.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{

    Company findByCompanyId(int companyId);
    
}
