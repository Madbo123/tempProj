module com.mapper.map.bfst_map {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports com.mapper.map.bfst_map.Model;
    opens com.mapper.map.bfst_map.Model to javafx.fxml;
    exports com.mapper.map.bfst_map.Controller.AddressSearcher;
    exports com.mapper.map.bfst_map.Controller.Dijkstra;
    exports com.mapper.map.bfst_map.Controller.GUI;
    opens com.mapper.map.bfst_map.Controller.GUI to javafx.fxml;
    exports com.mapper.map.bfst_map.Controller.Demos;
    opens com.mapper.map.bfst_map.Controller.Demos to javafx.fxml;
    opens com.mapper.map.bfst_map.Controller.Dijkstra to javafx.fxml;
    exports com.mapper.map.bfst_map.Model.Dijkstra;
    opens com.mapper.map.bfst_map.Model.Dijkstra to javafx.fxml;
    exports com.mapper.map.bfst_map.Model.RTree;
    opens com.mapper.map.bfst_map.Model.RTree to javafx.fxml;
    exports com.mapper.map.bfst_map.Model.Elements;
    opens com.mapper.map.bfst_map.Model.Elements to javafx.fxml;
    exports com.mapper.map.bfst_map.Controller;
    opens com.mapper.map.bfst_map.Controller to javafx.fxml;

}