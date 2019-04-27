package com.office.service.impl;

import com.office.common.ServerResponse;
import com.office.dao.PositionMapper;
import com.office.dao.UserMapper;
import com.office.pojo.Position;
import com.office.service.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iPostionService")
public class PositionServiceImpl  implements IPositionService {

    @Autowired
    PositionMapper positionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse add(Position position) {
        int rowCount = positionMapper.insert(position);
        if(rowCount>0) {
            return ServerResponse.createBySuccessMessage("新增职位成功");

        }
        return ServerResponse.createBySuccessMessage("新增职位失败");
    }

    @Override
    public ServerResponse del(Integer id) {

        int rowCount = positionMapper.deleteByDepartId(id);

        return null;
    }


    @Override
    public ServerResponse update(Position position) {
        return null;
    }

    @Override
    public ServerResponse<List<Position>> selectBydepart(Integer departmentId) {
        return null;
    }
}
