module JRemoteControl {
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swt;
    requires javafx.swing;
    requires javafx.controls;
    requires opencv;
    requires thumbnailator;

    opens server;
}