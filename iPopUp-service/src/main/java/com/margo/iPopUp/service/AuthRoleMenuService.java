package com.margo.iPopUp.service;

import com.margo.iPopUp.dao.AuthRoleMenuDao;
import com.margo.iPopUp.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIdSet);
    }
}
