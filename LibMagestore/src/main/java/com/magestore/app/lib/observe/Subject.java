package com.magestore.app.lib.observe;

import com.magestore.app.lib.controller.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class Subject {
    class Observe {
        Controller controller;
        String stateCode;
        Class stateClazz;
    }
    private List<Observe> observerList = new ArrayList<Observe>();
    private Map<String, Controller> stateCodeMap = new HashMap<String, Controller>();
    private Map<State, Controller> stateMap = new HashMap<State, Controller>();
    /**
     * Cập nhật trạng thái
     * @param state
     */
    public void setState(State state) {
        notifyAllObservers(state);
    }

    /**
     * Attach observer vào cuối danh sách
     *
     */
    public void attach(Class<State> StateClazz, String stateCode, Controller controller) {
        Observe observer = new Observe();
        observer.controller = controller;
        observer.stateCode = stateCode;
        observer.stateClazz = StateClazz;
        observerList.add(observer);
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

    /**
     * Gỡ, detach observe
     *
     */
    public void detach(Class<State> stateClazz, String stateCode, Controller controller) {
        for (Observe observer : observerList) {
            if (controller != observer.controller) continue;
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

    /**
     * Thông báo cho các observs
     */
    public void notifyAllObservers(State state) {
        for (Observe observer : observerList) {
            if (observer.stateCode != null && !observer.stateCode.equals(state.getStateCode())) continue;
            if (observer.stateClazz != null && !observer.stateClazz.isInstance(state)) continue;
            observer.controller.notifyState(state);
        }
    }
}