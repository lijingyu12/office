package com.office.service.impl;

import com.office.common.ServerResponse;
import com.office.dao.DepartmentMapper;
import com.office.dao.PositionMapper;
import com.office.dao.UserMapper;
import com.office.pojo.Department;
import com.office.service.IDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iDepartService")
public class DepartServiceImpl implements IDepartService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PositionMapper positionMapper;

    @Override
    public ServerResponse addDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        int resultCount = departmentMapper.insert(department);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse updateName(Integer id,String name) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        int resultCount = departmentMapper.updateByPrimaryKey(department);
        if(resultCount>0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createBySuccessMessage("更新失败");
    }

    @Override
    public ServerResponse selectAllDepart() {
        List<Department> departments = departmentMapper.selectAllDeparts();
        return ServerResponse.createBySuccess(departments);

    }

    @Override
    public ServerResponse deleteDepart(Integer id) {
        //先判断这个部门是否有员工 有则不能删除
        int result = userMapper.selectbyDepart(id);

        if(result == 0){
            int rowCount = departmentMapper.deleteByPrimaryKey(id);
            if(rowCount>0){
                    return ServerResponse.createBySuccessMessage("删除成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("该部门还有人员不能删除");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }
}
