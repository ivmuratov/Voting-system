package com.votingsystem.springboot;

import com.votingsystem.springboot.model.MenuItem;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "registered", "restaurant");

    public static int MENU_ITEM_ID1 = 1;
    public static int MENU_ITEM_ID2 = 2;
    public static int MENU_ITEM_ID3 = 3;
    public static MenuItem menuItem1 = new MenuItem(MENU_ITEM_ID1, "Menu Item for Restaurant 1", 50000L);
    public static MenuItem menuItem2 = new MenuItem(MENU_ITEM_ID2, "Menu Item for Restaurant 2", 60000L);
    public static MenuItem menuItem3 = new MenuItem(MENU_ITEM_ID3, "Menu Item for Restaurant 3", 70000L);

    public static MenuItem getNew() {
        return new MenuItem(null, "New Menu Item for Restaurant 1", 40000L);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID1, "Update Menu Item for Restaurant 1", 80000L);
    }
}