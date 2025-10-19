# Changelog

### Version 1.1.0  

**GitHub Username:** balajimalathi  
**Date:** Sun Oct 19, 2025  

#### Added  
- Introduced a new `Settings` entity to manage user-specific configuration settings (`src/main/java/com/skndan/entity/Settings.java`).
- Implemented `SettingsRepo` to handle database interactions for `Settings` (`src/main/java/com/skndan/repo/SettingsRepo.java`).
- Created `SettingsResource` to provide RESTful API operations for `Settings`, including creating, retrieving, updating, and deleting settings (`src/main/java/com/skndan/resource/SettingsResource.java`).
- Added `EntityCopyUtils`, a utility class to copy properties from DTOs to entities (`src/main/java/com/skndan/utils/EntityCopyUtils.java`).
- Integrated a default model configuration for user profiles in `MeResource` with the `chimera.default.model` property set to `openai/gpt-4o-mini` (`src/main/java/com/skndan/resource/MeResource.java`).

#### Changed  
- Upgraded Quarkus platform version from `3.26.2` to `3.28.2` in `pom.xml` to incorporate the latest features and fixes.

#### Fixed  
- Persisted and flushed user profiles and their default settings immediately after creation to ensure consistency (`src/main/java/com/skndan/resource/MeResource.java`).

This release follows Semantic Versioning, indicating the addition of significant new features and enhancements.

### Version 1.2.0

**GitHub Username:** balajimalathi  
**Date:** September 12, 2025  

#### Changed
- **Logging Enhancement:**  
  - Added detailed logging within the `ChatService` class to capture and log key information about chat requests. The following fields are now logged:
    - `prompt`
    - `sheet`
    - `cell`
    - `roomName`
    - `selectionType`
  - These logs are enclosed between clear start and end markers for improved readability and debugging.

This update improves the traceability of chat requests within the Chimera API, aiding in debugging and monitoring interactions with the AI-enabled Excel add-in.