# Changelog

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