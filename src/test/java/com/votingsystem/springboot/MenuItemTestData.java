package com.votingsystem.springboot;

import com.votingsystem.springboot.model.MenuItem;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "created", "restaurant");

    public static int MENU_ITEM_ID1 = 1;
    public static int MENU_ITEM_ID2 = 2;
    public static int MENU_ITEM_ID3 = 3;
    public static int MENU_ITEM_ID4 = 4;
    public static int MENU_ITEM_ID5 = 5;
    public static int MENU_ITEM_ID6 = 6;
    public static int MENU_ITEM_ID7 = 7;
    public static MenuItem menuItem1 = new MenuItem(MENU_ITEM_ID1, "Menu Item 1 for Restaurant 1", 10000L);
    public static MenuItem menuItem2 = new MenuItem(MENU_ITEM_ID2, "Menu Item 2 for Restaurant 1", 10000L);
    public static MenuItem menuItem3 = new MenuItem(MENU_ITEM_ID3, "Menu Item 1 for Restaurant 2", 20000L);
    public static MenuItem menuItem4 = new MenuItem(MENU_ITEM_ID4, "Menu Item 2 for Restaurant 2", 20000L);
    public static MenuItem menuItem5 = new MenuItem(MENU_ITEM_ID5, "Menu Item 1 for Restaurant 3", 30000L);
    public static MenuItem menuItem6 = new MenuItem(MENU_ITEM_ID6, "Menu Item 2 for Restaurant 3", 30000L);
    public static MenuItem menuItem7 = new MenuItem(MENU_ITEM_ID7, "Menu Item 2021-09-20 for Restaurant 1", 10000L);

    public static MenuItem getNew() {
        return new MenuItem(null, "New Menu Item for Restaurant 1", 40000L);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID1, "Update Menu Item for Restaurant 1", 50000L);
    }
}