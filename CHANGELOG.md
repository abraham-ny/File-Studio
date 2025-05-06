# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

## [v2.0] - Date TBD
### Added
- New Metro-style UI (work in progress).
- Duplicate Finder module with scanning, selection, and deletion features.
- Bulk Renamer functionality for batch file renaming.
- Organizer module for filtering and moving files by extension.
- Logging added in GlobalVars via `fslog`.
- Update checker with notifications.
- Various UI improvements and bug fixes.

### Fixed
- Fixed issues in Duplicate Finder including deselection feature and checkbox options.
- Fixed bulk renamer bugs.
- Fixed module paths for gridview selection.
- Resolved runtime issues in OrganizerController related to loading items to tree.

### Changed
- Major refactoring of OrganizerController class.
- Improved alert and browse functions in GlobalVars.
- Enhanced logging and error handling.

### Removed
- Removed duplicate alert method in favor of existing implementation.

---

*This changelog is based on commit history and may be updated with future releases.*
