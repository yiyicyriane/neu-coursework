package com.chat.util;

public class CurrentViewContext {
    private static CurrentViewContext instance;
    private Object lastView, currentView;

    private CurrentViewContext() {}

    public static synchronized CurrentViewContext getInstance() {
        if (instance == null) {
            instance = new CurrentViewContext();
        }
        return instance;
    }

    public Object getCurrentView() {
        return currentView;
    }

    public void setCurrentView(Object currentView) {
        if (this.currentView != null) lastView = this.currentView;
        this.currentView = currentView;
    }

    public void closeCurrentView() {
        currentView = lastView;
    }
}
