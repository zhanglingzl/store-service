package com.rxr.store.web.service;

import com.rxr.store.common.entities.Guest;

import java.util.List;

/**
 * @author zero
 */
public interface IGuestService {

    List<Guest> findSearch(Guest guest);

}
