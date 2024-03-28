module be.kuleuven.candycrush {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires com.google.common;


    opens be.kuleuven.candycrush to javafx.fxml;
    exports be.kuleuven.candycrush;
}