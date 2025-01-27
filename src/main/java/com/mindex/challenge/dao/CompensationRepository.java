package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

//the repository containing the compensation for employees
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String>{
    Compensation findBycompensationID(String compensationID);
    Compensation[] findByemployeeId(String employeeId);
}
