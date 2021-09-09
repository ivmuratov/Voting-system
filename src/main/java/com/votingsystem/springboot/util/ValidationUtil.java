package com.votingsystem.springboot.util;

import com.votingsystem.springboot.model.BaseEntity;
import com.votingsystem.springboot.web.exception.IllegalRequestDataException;
import com.votingsystem.springboot.web.exception.NotFoundException;

public class ValidationUtil {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        if (!found) {
            throw new NotFoundException("Not found entity with id=" + id);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }
}