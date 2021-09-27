package com.votingsystem.springboot.util;

import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.util.exception.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuItemUtil {
    public static void checkOwn(MenuItem menuItem, MenuItemRepository r, int restaurantId, int menuItemId) {
        MenuItem chooseMenuItem = r.findById(menuItemId)
                .filter(item -> item.getRestaurant().getId() == restaurantId)
                .orElse(null);
        if (!menuItem.isNew() && chooseMenuItem == null) {
            throw new IllegalRequestDataException("Restaurant with id=" + restaurantId + " is not the owner");
        }
    }
}