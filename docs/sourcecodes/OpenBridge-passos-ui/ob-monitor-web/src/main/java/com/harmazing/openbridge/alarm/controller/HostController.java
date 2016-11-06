package com.harmazing.openbridge.alarm.controller;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.vo.HostDTO;
import com.harmazing.openbridge.alarm.service.IHostService;
import com.harmazing.openbridge.monitor.model.vo.GraphVo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by liyang on 2016/7/28.
 */
@RestController
@RequestMapping("/hosts")
public class HostController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IHostService hostService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        try{
            Host dto = hostService.findById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("获取Host出错", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody HostDTO dto, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try{
            Host entity = hostService.insert(dto);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("添加Host出错", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
