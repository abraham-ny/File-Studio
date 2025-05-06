# Developer Documentation

## Project Structure

The source code is organized under the `src/filestudio` directory with the following key components:

- **Main Application**
  - `FileStudio.java`: The main entry point of the application. Initializes the JavaFX UI, handles drag-and-drop, theme selection, and update checking.

- **Controllers**
  - Various controller classes handle UI logic for different modules and views, e.g.:
    - `BulkRenamerController.java`: Handles bulk renaming UI and logic.
    - `DuplicateFinderController.java`: Manages duplicate file scanning, selection, and deletion.
    - `OrganizerController.java`: Manages file organization features.
    - `MetroPanelController.java`, `FXMLDocumentController.java`: Controllers for different UI panels.

- **Modules**
  - Located in `src/filestudio/modules/`, these include specialized features such as Archiver, Duplicate Finder, Organizer, and more.

- **Utilities and Helpers**
  - `FileRenamer.java`: Implements bulk file renaming logic.
  - `Organizer.java`: Provides file filtering and moving utilities.
  - `Finder.java`: Utility for scanning files and finding duplicates.
  - `FLogger.java`: Logging utility.
  - `JsonHandler.java`: JSON processing helper.
  - `UserSettings.java`: Manages user preferences and settings.
  - `Util.java`: General utility functions.

- **UI Resources**
  - FXML files define the UI layouts.
  - CSS files for styling.
  - Image assets for icons and banners.

## Build and Run

- The project uses **Java 8** and **JavaFX**.
- Build system: **Apache Ant**.
- Recommended IDE: **NetBeans** (project files included).
- Optional: **Scene Builder** for editing FXML UI files.

To build and run the project:

1. Import the project into NetBeans or your preferred IDE.
2. Ensure Java 8 and JavaFX are configured.
3. Use Ant build scripts or IDE build/run commands.
4. Launch the main class `filestudio.FileStudio`.

## Coding Conventions

- Follow Java naming conventions and best practices.
- Use Javadoc comments for classes and methods.
- Maintain modular and reusable code.
- Log important events and errors using `FLogger`.
- Handle exceptions gracefully with user-friendly alerts.

## Contribution Guidelines

- Fork the repository and create feature or bugfix branches.
- Write clear and descriptive commit messages.
- Test changes thoroughly before submitting pull requests.
- Adhere to existing code style and project structure.

## Additional Notes

- The project supports two UI modes: classic and Metro style.
- Features are modularized for ease of maintenance and extension.
- Update checking is integrated to notify users of new releases.

For questions or support, refer to the main README or open an issue on the repository.
