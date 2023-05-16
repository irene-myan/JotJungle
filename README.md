Irene Martirosyan - i2martirosyan@uwaterloo.ca
Patrick Telfer – patrick.telfer@uwaterloo.ca
Stephen Cao - stephen.cao@uwaterloo.ca
Tam Nguyen - tam.nguyen1@uwaterloo.ca

## Preview
<img width="833" alt="Screenshot 2023-05-16 at 3 03 32 AM" src="https://github.com/irene-myan/JotJungle/assets/48896721/a313dc1d-1671-4540-b37a-627a8316a16b">
<img width="833" alt="Screenshot 2023-05-16 at 3 07 08 AM" src="https://github.com/irene-myan/JotJungle/assets/48896721/83e1a382-8345-4b63-a4e6-cf77bed1a6df">


### How to run the project:
Run the server first (see below) then open the ``application`` exec in releases/JotJungle-1.3/lib/bin

### To run the server in Docker:
    docker build -t docker-spring-boot:docker-server .
    docker run -p 8080:8080 docker-spring-boot:docker-server .

## Release Notes

### v1.0
- Menu bar + Shortcuts working for Quit, Minimize, Maximize
- Window resizing + saving size
- Console launcher + installer
- Note List View
- Basic Note View (HTMLEditor)
- Create Note

### v1.1
- Added rich text support (Gluon Rich Text Library Integration, replaced HTMLEditor)
- Added Context menu and implemented:
  - Open Note
  - Rename Note
  - Delete Note
  - Note Properties
- Added Icon + new CSS styling
- Fix Error when clicking on empty space in List View
- Refactored UI / Model code

### v1.2
- Added Jot Jungle Green Themex
- Add Tags to front end (Create, Edit, Delete)
- Display tag + date in note list view
- Fix bug where note list view / note would not stretch
- Add save button + save pop up
- Notes now do not autosave
- Add web service infrastructure
- Notes now persist on app closure (plain text)
- Add search bar / sort UI
- Add Date and note id to note class
- Fix bug when clicking on empty note in NoteListView

### v1.3
- Bulleted lists and numbered lists now save in Note
- Fix bug where hitting cancel on the manage tag dialog would remove all tags
- Add tooltips for buttons
- Add margin to note editor
- Add the ability to filter by tag
- Fix bug where dates were not saving propely 
- Add keyboard shortcut for deleting and opening a note
- Add persistence to tags
- Add docker container for server + running instructions
- Support exporting to pdf (except lists and tab after a new line)
- Streamline save note flow
- Note persistence (including rich text)
- Fix error when clicking the x button on tag dialogs
- Change default font to arial in Note editor
- Add custom theme support (persists on app close)
- Add searching for note by title
- Add sorting notes by create date, edit date, and title
- Fix error with serializing dates
- Created column types for each datatype used in storing Notes 
- Notes now autosave

## External Sources / References:
- https://git.uwaterloo.ca/cs346/public/sample-code/-/tree/master/applications/desktop/contacts 
- https://fxdocs.github.io/docs/html5/#_event_handling 
- https://docs.oracle.com/javase/8/javafx/api/toc.htm 
- rich text: https://github.com/gluonhq/rich-text-area 
- circle path animation: https://stackoverflow.com/questions/14171856/javafx-2-circle-path-for-animation

icons and images:
- https://github.com/kordamp/ikonli
- https://www.flaticon.com
- https://edencoding.com/search-bar-dynamic-filtering/
- https://freeicons.io
- https://www.vecteezy.com

sound effects:
- https://pixabay.com/sound-effects/monkey-scream-6407/
