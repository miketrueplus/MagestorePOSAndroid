package com.magestore.app.lib.observ;

import com.magestore.app.lib.controller.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SubjectObserv {
    public class Observe {
        Controller controllerObserve;
        String stateCode;
        Class stateClazz;
        Class controllerStateClazz;
        Controller controllerState;
        String methodName;

        public Observe setStateCode(String stateCode) {
            this.stateCode = stateCode;
            return this;
        }

        public Observe setStateClazz(Class stateClazz) {
            this.stateClazz = stateClazz;
            return this;
        }

        public Observe setControllerStateClazz(Class controllerStateClazz) {
            this.controllerStateClazz = controllerStateClazz;
            return this;
        }

        public Observe setControllerState(Controller controllerState) {
            this.controllerState = controllerState;
            return this;
        }

        public Observe setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }
    }
    private List<Observe> observerList = new ArrayList<Observe>();
//    private Map<String, Controller> stateCodeMap = new HashMap<String, Controller>();
//    private Map<State, Controller> stateMap = new HashMap<State, Controller>();

    /**
     * Cập nhật trạng thái
     * @param state
     */
    public void setState(State state) {
        notifyAllObservers(state);
    }

    /**
     * Thông báo cho các observs
     * State đã được cập nhật
     */
    private void notifyAllObservers(State state) {
        for (Observe observer : observerList) {
            // điều kiện để nhận đuwọc thông báo là
            // state code của observ đã đăng ký bằng với state code của State
            // và stateClazz của observer
            if (observer.stateCode != null && !observer.stateCode.equals(state.getStateCode())) continue;
            if (observer.stateClazz != null && !observer.stateClazz.isInstance(state)) continue;
            if (observer.controllerStateClazz != null && !observer.controllerStateClazz.isInstance(state.getController().getClass())) continue;
            if (observer.controllerState != null && !(observer.controllerState == state.getController())) continue;
            if (observer.methodName == null) observer.controllerObserve.notifyState(state);
            else {
                try {
                    Method method = observer.controllerObserve.getClass().getMethod(observer.methodName, State.class);
                    method.invoke(observer.controllerObserve, state);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Attach observer vào cuối danh sách đăng ký nhật thông báo
     * Khi cập nhật state
     */
    public void attach(Controller controllerObserve, String methodName, Class<State> stateClazz, String stateCode, Class<Controller> controllerClazz, Controller controllerState) {
        Observe observer = new Observe();
        observer.controllerObserve = controllerObserve;
        observer.stateCode = stateCode;
        observer.stateClazz = stateClazz;
        observer.controllerStateClazz = controllerClazz;
        observer.controllerState = controllerState;
        observer.methodName = methodName;
        observerList.add(observer);
    }

    public Observe attach(Controller controllerObserve) {
        Observe observer = new Observe();
        observer.controllerObserve = controllerObserve;
        observerList.add(observer);
        return observer;
    }

    /**
     * Gỡ, detach observe
     *
     */
    public void detach(Class<State> stateClazz, String stateCode, Controller controller) {
        for (Observe observer : observerList) {
            if (controller != observer.controllerObserve) continue;
            if (observer.stateCode != null && observer.stateCode.equals(stateCode)) continue;
            if (observer.stateClazz != null && observer.stateClazz.isInstance(stateClazz)) continue;

            observerList.remove(observer);
        }
    }

    /**
     * Gỡ, clear tất cả observ
     */
    public void release() {
        observerList.clear();
    }

//    /**
//     * Attach observer vào đầu danh sách
//     *
//     * @param observer
//     */
//    public void attachFirst(Class<State> fromClazz, String stateCode, Controller observer) {
//        observerList.add(0, observer);
//    }
//
//    /**
//     * Attach observer vào cuối danh sách
//     *
//     * @param observer
//     */
//    public void attachLast(Class<State> fromClazz, String stateCode, Controller observer) {
//        observerList.add(observer);
//    }
//
//    /**
//     * Attach oserber ngay trước 1 observe khác
//     *
//     * @param behindObserver
//     * @param observer
//     */
//    public void attachFront(Class<State> fromClazz, String stateCode, Controller observer, Controller behindObserver) {
//        int index = observerList.indexOf(behindObserver);
//        if (index < 0 || index >= observerList.size()) return;
//        observerList.add(index, observer);
//    }
//
//    /**
//     * Attach oserber ngay sau 1 observe khác
//     *
//     * @param frontObserver
//     * @param observer
//     */
//    public void attachBehind(Class<State> fromClazz, String stateCode, Controller frontObserver, Controller observer) {
//        int index = observerList.indexOf(frontObserver);
//        if (index < 0 || index >= observerList.size()) return;
//        observerList.add(index + 1, observer);
//    }
}