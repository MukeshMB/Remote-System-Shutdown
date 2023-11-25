module org.nitj.systemkillerfx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens org.nitj.systemkillerfx to javafx.fxml;
    exports org.nitj.systemkillerfx;
    exports org.nitj.systemkiller;
}