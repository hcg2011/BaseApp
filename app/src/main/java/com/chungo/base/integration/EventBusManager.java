package com.chungo.base.integration;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;

import static com.chungo.base.base.Platform.DEPENDENCY_EVENTBUS;

public final class EventBusManager {
    private static volatile EventBusManager sInstance;

    public static EventBusManager getInstance() {
        if (sInstance == null) {
            synchronized (EventBusManager.class) {
                if (sInstance == null) {
                    sInstance = new EventBusManager();
                }
            }
        }
        return sInstance;
    }

    private EventBusManager() {
    }

    /**
     * 注册订阅者
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        if (DEPENDENCY_EVENTBUS && haveAnnotation(subscriber)) {
            org.greenrobot.eventbus.EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 注销订阅者
     *
     * @param subscriber 订阅者
     */
    public void unregister(Object subscriber) {
        if (DEPENDENCY_EVENTBUS && haveAnnotation(subscriber)) {
            org.greenrobot.eventbus.EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 发送事件, 如果您在项目中同时依赖了两个 EventBus, 请自己使用想使用的 EventBus 的 Api 发送事件
     *
     * @param event 事件
     */
    public void post(Object event) {
        if (DEPENDENCY_EVENTBUS) {
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 发送黏性事件
     *
     * @param event 事件
     */
    public void postSticky(Object event) {
        if (DEPENDENCY_EVENTBUS) {
            EventBus.getDefault().postSticky(event);
        }
    }

    /**
     * 注销黏性事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        if (DEPENDENCY_EVENTBUS) {
            return EventBus.getDefault().removeStickyEvent(eventType);
        }
        return null;
    }

    /**
     * 清除订阅者和事件的缓存
     */
    public void clear() {
        if (DEPENDENCY_EVENTBUS) {
            EventBus.clearCaches();
        }
    }

    /**
     * {@link org.greenrobot.eventbus.EventBus} 要求注册之前, 订阅者必须含有一个或以上声明 {@link org.greenrobot.eventbus.Subscribe}
     * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     *
     * @param subscriber 订阅者
     * @return 返回 {@code true} 则表示含有 {@link org.greenrobot.eventbus.Subscribe} 注解, {@code false} 为不含有
     */
    private boolean haveAnnotation(Object subscriber) {
        boolean skipSuperClasses = false;
        Class<?> clazz = subscriber.getClass();
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemCalss(clazz.getName()) && !skipSuperClasses) {
            Method[] allMethods;
            try {
                allMethods = clazz.getDeclaredMethods();
            } catch (Throwable th) {
                try {
                    allMethods = clazz.getMethods();
                } catch (Throwable th2) {
                    continue;
                } finally {
                    skipSuperClasses = true;
                }
            }
            for (int i = 0; i < allMethods.length; i++) {
                Method method = allMethods[i];
                Class<?>[] parameterTypes = method.getParameterTypes();
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe.class) && parameterTypes.length == 1) {
                    return true;
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }
}
