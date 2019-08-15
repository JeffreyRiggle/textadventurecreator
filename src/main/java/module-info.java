module textadventurecreator {
    requires java.base;
    requires java.desktop;
    requires java.logging;
    requires java.mail;
    requires java.activation;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires spring.core;

    requires ilusr.core;
    requires ilusr.gamestatemanager;
    requires ilusr.playerlib;
    requires ilusr.iroshell;
    requires ilusr.logrunner;
    requires ilusr.textadventurelib;
    requires ilusr.persistencelib;
}