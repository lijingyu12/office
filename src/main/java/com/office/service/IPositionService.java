package com.office.service;

import com.office.common.ServerResponse;
import com.office.pojo.Position;

import java.util.List;

public interface IPositionService {
    ServerResponse add(Position position);
    ServerResponse del(Integer id);
    ServerResponse update(Position position);
    ServerResponse<List<Position>> selectBydepart(Integer departmentId);


}
