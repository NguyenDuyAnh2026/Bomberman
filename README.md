# Bomberman JavaFX

## Open in IntelliJ IDEA
1. Extract this ZIP.
2. Open the `Bomberman-main` folder (the folder containing `pom.xml`).
3. If IntelliJ asks to load/import Maven, choose **Load Maven Project**.
4. Set Project SDK to **JDK 27** in `File > Project Structure > Project`.
5. In `Settings > Build Tools > Maven > Importing`, set JDK for importer to JDK 27 (or Project JDK).
6. Reload Maven using the Maven tool window's **Reload All Maven Projects** button.
7. Open `src/BombermanApp.java` and click the green Run icon next to `main()`.

The project uses Java 17 language/source level for compatibility. JDK 27 is the runtime/compiler JDK; JavaFX 21.0.5 is downloaded by Maven.

## Alternative
Run Maven goal `javafx:run` from IntelliJ's Maven tool window.
