module simpleRadarSimulation {
    exports etf.gui.main;
    opens etf.gui.controller.mainController;
    exports etf.gui.controller.crashesViewController;
    opens etf.gui.controller.crashesViewController;
    exports etf.gui.controller.detailsController;
    opens etf.gui.controller.detailsController;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.logging;
}