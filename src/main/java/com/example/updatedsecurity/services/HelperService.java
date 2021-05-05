package com.example.updatedsecurity.services;

import com.example.updatedsecurity.Dto.UserAuthDTO;
import com.example.updatedsecurity.Dto.UserAuthDTOResultTransformer;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.UUID;

@Service
public class HelperService {
    private final EntityManagerFactory entityManagerFactory;

    public HelperService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<UserAuthDTO> authDetailsById(String idStr){
        try {
            var id = UUID.fromString(idStr);
            var entityManager = entityManagerFactory.createEntityManager();
            var users =  entityManager.createNativeQuery(" " +
                    "select  " +
                    "u.email as u_email," +
                    "u.name as u_name, " +
                    "g.code as g_code " +
                    "from users u " +
                    "join user_groups ug on u.id = ug.user_id " +
                    "join principle_groups g on ug.group_id = g.id " +
                    "where u.id = :userId")
                    .setParameter("userId", id)
                    .unwrap(Query.class)
                    .setResultTransformer(new UserAuthDTOResultTransformer())
                    .getResultList();

            entityManager.close();
            return users;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

    }
}
