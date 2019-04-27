package com.office.service;

import com.office.common.ServerResponse;

public interface IDepartService {
    ServerResponse addDepartment(String name);
    ServerResponse updateName(Integer id,String name);
    ServerResponse selectAllDepart();
    ServerResponse deleteDepart(Integer id);
}
