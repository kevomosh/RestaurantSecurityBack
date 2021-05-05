package com.example.updatedsecurity.Dto;

import org.hibernate.transform.ResultTransformer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserAuthDTOResultTransformer implements ResultTransformer {
    private Map<String, UserAuthDTO> userAuthDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

       String userEmail = String.valueOf(tuple[aliasToIndexMap.get(UserAuthDTO.EMAIL_ALIAS)]);

        UserAuthDTO userAuthDTO = userAuthDTOMap.computeIfAbsent(
                userEmail,
                id -> new UserAuthDTO(tuple, aliasToIndexMap)
        );

        userAuthDTO.getGroups().add(
                new GroupDTO(tuple, aliasToIndexMap)
        );
        return userAuthDTO;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(userAuthDTOMap.values());
    }

    public  Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }
}
