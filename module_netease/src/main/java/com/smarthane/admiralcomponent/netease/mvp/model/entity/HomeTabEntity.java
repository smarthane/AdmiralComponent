package com.smarthane.admiralcomponent.netease.mvp.model.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * @author smarthane
 * @time 2019/11/10 18:08
 * @describe
 */
public class HomeTabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public HomeTabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
