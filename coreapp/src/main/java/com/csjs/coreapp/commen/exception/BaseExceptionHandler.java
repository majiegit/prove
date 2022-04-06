package com.csjs.coreapp.commen.exception;

import com.csjs.coreapp.commen.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public R baseExceptionHandler(BaseException e) {
        return R.error(e.getMessage());
    }

//    /**
//     * 处理空指针异常
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = NullPointerException.class)
//    public R nullPointerExceptionHandler(NullPointerException e) {
//        return R.error(" 空指针异常" + e.getMessage());
//    }
}
