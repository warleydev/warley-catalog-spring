package com.warleydev.warleycatalog.services.utils;

import java.util.List;

public class ListUtils {

    public static boolean listEmptyOrNull(List<?> list){
        return list == null || list.isEmpty();
    }

}
