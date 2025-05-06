# File Studio

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e14b3990d3da49748268bb8215e156c0)](https://app.codacy.com/gh/abummoja/File-Studio/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)  
[![Download File-Studio](https://a.fsdn.com/con/app/sf-download-button)](https://sourceforge.net/projects/filestudio/files/latest/download)  
[![Download File-Studio](https://img.shields.io/sourceforge/dt/filestudio.svg)](https://sourceforge.net/projects/filestudio/files/latest/download)

---

## Overview

File Studio is a powerful Java-based desktop application designed to handle advanced file operations with a custom graphical user interface. It supports a variety of file management tasks such as bulk renaming, organizing folders, duplicate file detection, file compression, archiving, disk scanning, and media upscaling. The application offers both a classic and a modern Metro-style UI with theme support, drag-and-drop functionality, and automatic update checking.

---

## Features

- Bulk renaming of files by replacing specified substrings in filenames.
- Organizing folders by filtering and moving files based on extensions.
- Duplicate file finder with options for automatic selection and deletion.
- Image and video upscaling (feature under development).
- File compression and archiving directories.
- Disk compression and disk scanning utilities.
- Disk formatting capabilities.
- Customizable GUI with dark and light themes.
- Drag-and-drop support for easy file and folder input.
- Automatic update checking with notifications.
- Cross-platform support (Java 8 and JavaFX based).

---

## Libraries Used

- gson 2.8.6
- zstd jni 1.5.6-4
- img scalr (4.2)
- xz (tukaani.xz) 1.10
- apache commons compress 1.25 (with javadoc jar)
- json (org.json) [optional]
- json-smart 2.x
- json-path 2.x
- JMetro (8.6.14)
- jFoenix (8.0.10)

---

## Test Libraries

- testng-6.14.3
- jcommander-1.78

---

## Installation

1. Ensure you have Java 8 or higher installed on your system.
2. Download the latest release from the [SourceForge File Studio project page](https://sourceforge.net/projects/filestudio/files/latest/download).
3. Extract the downloaded archive if necessary.
4. Run the executable or launch the application via command line using the provided scripts or jar files.

---

## Usage

- Launch the application to open the GUI.
- Use the drag-and-drop feature to add files or folders for processing.
- Navigate through the modules for specific tasks such as renaming, organizing, or finding duplicates.
- Configure settings and preferences via the settings UI.
- Check for updates automatically or manually through the application interface.

---

## Development

- Developed using Java 8 and JavaFX.
- Built with Apache Ant.
- Recommended IDE: NetBeans (project files included).
- Optional: Scene Builder for editing FXML UI files.
- Source code is organized under the `src/filestudio` directory with modular controllers and utilities.

---

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Make your changes with clear commit messages.
4. Test your changes thoroughly.
5. Submit a pull request describing your changes.

Please adhere to the existing code style and conventions.

---

## Screenshots

![File Studio Banner](fs-1-2.png)  
![New UI Coming Up](newFS.PNG)

---

## License

This project is licensed under the terms specified in the LICENSE file.

---

## Contact

For support or inquiries, please open an issue on the GitHub repository or contact the maintainer directly.
