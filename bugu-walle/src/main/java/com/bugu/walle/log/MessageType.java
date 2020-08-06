package com.bugu.walle.log;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@IntDef({MessageType.NORMAL, MessageType.ERROR, MessageType.HTTP})
@Retention(RetentionPolicy.SOURCE)
public @interface MessageType {
    int NORMAL = 0;
    int ERROR = 1;
    int HTTP = 2;
}
