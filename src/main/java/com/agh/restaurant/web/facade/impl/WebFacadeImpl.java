/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.web.facade.impl;

import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.facade.WebFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WebFacadeImpl implements WebFacade {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void registerUser(RegisterUserInit registrationUnit){
        userService.registerUser(registrationUnit);
    }

    @Transactional
    @Override
    public void updateUser(RegisterUserInit registrationUnit) {
        userService.updateUser(registrationUnit);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userService.deleteUser(id);
    }

    @Transactional
    @Override
    public void deleteUserByUsername(String username) {
        userService.deleteUser(username);
    }

    @Override public List<UserEntity> getUsers() {
        return userService.getUsers();
    }
}
