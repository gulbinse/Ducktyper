/**
 * Module definition for the TypeRacer client application. This module requires various JavaFX
 * modules for the user interface, including controls, graphics, FXML, media, and web modules. It
 * exports the typeracer.client.view package for use by other modules and opens it for deep
 * reflection by the JavaFX FXML module.
 */
module sep.group.project.client.main {
  requires javafx.controls;
  requires javafx.graphics;
  requires javafx.fxml;
  requires javafx.media;
  requires javafx.web;

  /**
   * Exports the typeracer.client.view package, allowing other modules to access the public types in
   * this package.
   */
  exports typeracer.client.view;

  /**
   * Opens the typeracer.client.view package to the javafx.fxml module, allowing it to use
   * reflection to access non-public types and members.
   */
  opens typeracer.client.view to
      javafx.fxml;
}
