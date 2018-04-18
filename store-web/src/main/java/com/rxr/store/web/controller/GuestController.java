package com.rxr.store.web.controller;

import com.rxr.store.common.entities.Guest;
import com.rxr.store.web.service.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zero
 */

@RestController
@RequestMapping(value = "/guest")
public class GuestController {

    @Autowired
    private IGuestService guestService;

    @GetMapping(value = "/findAll")
    public List<Guest> findAll(Guest guest){
        System.out.println(guestService.findSearch(guest).size());
        return guestService.findSearch(guest);
    }

}
