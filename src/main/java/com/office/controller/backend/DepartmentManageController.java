package com.office.controller.backend;

import com.office.common.ServerResponse;
import com.office.service.IDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/depart")
public class DepartmentManageController {
    @Autowired
    IDepartService iDepartService;

    @RequestMapping("/add")
    @ResponseBody
    public ServerResponse add(String name){
        return iDepartService.addDepartment(name);
    }

    @RequestMapping("/del")
    @ResponseBody
    public ServerResponse del(Integer id){
        return iDepartService.deleteDepart(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    public ServerResponse update(Integer id,String name){
        return iDepartService.updateName(id,name);
    }

    @RequestMapping("/select")
    @ResponseBody
    public ServerResponse select(){
        return iDepartService.selectAllDepart();
    }






}
