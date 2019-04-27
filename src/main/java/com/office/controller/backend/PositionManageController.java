package com.office.controller.backend;

import com.office.common.ServerResponse;
import com.office.service.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/position")
public class PositionManageController {
    @Autowired
    IPositionService iPositionService;

    @RequestMapping("add")
    @ResponseBody
    public ServerResponse add(String positionName,String departmentName){
        return

    }

}
