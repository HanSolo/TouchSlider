module touchslider {

    // Java
    requires java.base;
    requires java.logging;

    // Java-FX
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;

    exports eu.hansolo.fx.touchslider;
}