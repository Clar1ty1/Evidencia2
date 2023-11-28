module com.jose.evidencia2 {
    requires javafx.controls;
    exports com.jose.evidencia2;
    requires com.google.gson;
    requires java.desktop;
    opens com.jose.evidencia2 to com.google.gson;

}
